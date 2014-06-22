package de.volkswagen.compass.web.selenium2.testcase;

import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.Validate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sun.org.apache.xpath.internal.axes.ChildIterator;

import de.volkswagen.compass.web.selenium2.common.PageObject;

public class PageObjectTest {
	
	private PageObject myBtn;
	
	
	@Test
	public void checkSimpleIdWithSelectorPart() {
		Assert.assertTrue(myBtn.getIdPath().equals("form:panel:[0/selector1]"));
	}
	
	/**
	 * Test prüft die Unabhängigkeit des Id Path von der Reihenfolge des Zusammenbaus.
	 */
	@Test 
	public void checkPageObjectHierarchyBuilding() {
		
		PageObject po = createPageObject("po1", "po2", "po3");
		
		PageObject po3 = PageObject.createInstance("po3");
		PageObject po2 = PageObject.createInstance("po2");
		PageObject po1 = PageObject.createInstance("po1");
		po2.addChild(po3);
		po1.addChild(po2);

		assertTrue(po.isLeaf());
		assertTrue(po3.isLeaf());
		assertTrue(po.getIdPath().equals(po3.getIdPath()));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void nullIdentifierNotAllowed() {
		PageObject.createInstance(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void emptyIdentifierNotAllowed() {
		PageObject.createInstance("");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void nullSelectorInIdentifierNotAllowed() {
		PageObject.createInstance(-1, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void positionMustBeGreaterThan0() {
		PageObject.createInstance(-1, "sel");
	}

	@Before
	public void setup() {
		myBtn = PageObject.createInstance(0, "selector1", "MenuPanel");
		PageObject panel = PageObject.createInstance("panel", "Panel");
		PageObject form = PageObject.createInstance("form", "Form");
		form.addChild(panel);
		panel.addChild(myBtn);
	}
	
	private PageObject createPageObject(String... ids) {
		Validate.notEmpty(ids);
		PageObject tmp = PageObject.createInstance(ids[0]);
		for (int i=1; i<ids.length; i++) {
			tmp.addChild(PageObject.createInstance(ids[i]));
			tmp = tmp.getChildren().get(0);
		}
		return tmp;
	}
}
