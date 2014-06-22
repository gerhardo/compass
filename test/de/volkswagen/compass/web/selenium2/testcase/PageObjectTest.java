package de.volkswagen.compass.web.selenium2.testcase;

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
		Assert.assertTrue(po.getIdPath().equals("po1:po2:po3"));
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
