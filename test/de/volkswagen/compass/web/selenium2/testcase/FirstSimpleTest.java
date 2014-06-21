package de.volkswagen.compass.web.selenium2.testcase;

import org.junit.Before;
import org.junit.Test;

import de.volkswagen.compass.web.selenium2.common.PageObject;

public class FirstSimpleTest {
	
	private PageObject myBtn;
	
	
	@Test
	public void justTesst() {
		
	}
	
	
	@Before
	public void setup() {
		myBtn = PageObject.createInstance(0, "selector1", "MenuPanel");
		PageObject.createInstance("form", "Meine Form").addChild(PageObject.createInstance("panel", "Mein Panel").addChild(myBtn));
	}
}
