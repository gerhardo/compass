package de.volkswagen.compass.web.selenium2.testcase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.volkswagen.compass.web.selenium2.common.PageObject;

public class FirstSimpleTest {
	
	private PageObject myBtn;
	
	
	@Test
	public void justTest() {
		Assert.assertTrue(myBtn.getIdentifier().getIdPath().equals("form:panel:[0/selector1]"));
	}
	
	
	@Before
	public void setup() {
		myBtn = PageObject.createInstance(0, "selector1", "MenuPanel");
		PageObject panel = PageObject.createInstance("panel", "Panel");
		PageObject form = PageObject.createInstance("form", "Form");
		form.addChild(panel);
		panel.addChild(myBtn);
	}
}
