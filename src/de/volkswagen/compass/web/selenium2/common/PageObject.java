package de.volkswagen.compass.web.selenium2.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PageObject {
	
	public interface Identifier {
		
	}
	
	
	private PageObject() {
		// empty
	}
	
	private PageObject(String name) {
		this.name = name;
	}
	
	List<PageObject> children = new ArrayList<PageObject>();
	private PageObject parent;
	private String name;
	
	public PageObject addChild(PageObject child) {
		children.add(child);
		child.setParent(this);
		return this;
	}

	public List<PageObject> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public PageObject getParent() {
		return parent;
	}
	
	public static final PageObject createInstance(String id) {
		return new PageObject();
	}
	
	public static final PageObject createInstance(String id, String name) {
		return new PageObject(name);
	}

	public static final PageObject createInstance(String selector, int position) {
		return new PageObject();
	}
	
	public static final PageObject createInstance(String selector, int position, String name) {
		return new PageObject(name);
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("name", name).toString();
	}

	public void setParent(PageObject parent) {
		this.parent = parent;
	}


}
