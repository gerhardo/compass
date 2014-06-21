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
		myBtn = PageObject.createInstance("selector1",0);
		PageObject.createInstance("form", "Meine Form").addChild(PageObject.createInstance("panel", "Mein Panel").addChild(myBtn));
	}
}
