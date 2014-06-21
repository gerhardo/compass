package de.volkswagen.compass.web.selenium2.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PageObject {
	
	/**
	 * Identifizierung von Elmenten.
	 *
	 */
	public interface Identifier {

		/**
		 * Manipulationen des eigenen {@link Identifier} ermöglichen, wenn das {@link PageObject} in eine Hierarchie eingebunden oder daraus entfernt wird.
		 * @param parent
		 */
		public void connectParent(PageObject parent);
	}
	
	/**
	 * Identifizierung eines Elements durch einen {@link String}.
	 * @author go
	 *
	 */
	public static final class StringIdentifier implements Identifier {
		
		public static final String ID_PART_SEPARATOR = "|";
		
		public StringIdentifier(String id) {
			super();
			Validate.notEmpty(id);
			this.id = new StringBuilder(id);
		}
		private StringBuilder id;

		
		public String getId() {
			return id.toString();
		}
		@Override
		public void connectParent(PageObject parent) {
			if (parent.getId() instanceof StringIdentifier) {
				this.id.insert(0, ID_PART_SEPARATOR);
				this.id.insert(0, parent.getId());
			}
		}
		@Override
		public String toString() {
			return id == null ? "<NULL>" : id.toString();
		}
	}
	
	/**
	 * Identifizierung über eine Position innerhalb einer Elementmenge des Parents, die durch einen Selector String ausgewählt wird.
	 * @author go
	 *
	 */
	public static final class PositionSelectorIdentifier implements Identifier {
		public PositionSelectorIdentifier(int position, String selector) {
			super();
			this.position = position;
			this.selector = selector;
		}
		private int position;
		private String selector;
		
		public int getPosition() {
			return position;
		}
		
		public String getSelector() {
			return selector;
		}
		@Override
		public void connectParent(PageObject parent) {
			// TODO Auto-generated method stub
		}
		@Override
		public String toString() {
			return String.valueOf(position) + "/" + (selector == null ? "<NULL>" : selector);
		}
	}
	
	private PageObject() {
		// empty
	}
	
	private PageObject(String name) {
		this.name = name;
	}
	
	private PageObject(Identifier id) {
		this.id = id;
	}
	
	private PageObject(Identifier id, String name) {
		this.id = id;
		this.name = name;
	}
	
	
	List<PageObject> children = new ArrayList<PageObject>();
	private Identifier id;
	private PageObject parent;
	private String name;
	
	public PageObject addChild(PageObject child) {
		Validate.notNull(child);
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
		return new PageObject(new StringIdentifier(id));
	}
	
	public static final PageObject createInstance(String id, String name) {
		return new PageObject(new StringIdentifier(id), name);
	}

	public static final PageObject createInstance(int position, String selector) {
		return new PageObject(new PositionSelectorIdentifier(position, selector));
	}
	
	public static final PageObject createInstance(int position, String selector, String name) {
		return new PageObject(new PositionSelectorIdentifier(position, selector), name);
	}
	
	public String getName() {
		return name;
	}
	
	public Identifier getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("name", name).append("id", id).toString();
	}

	public void setParent(PageObject parent) {
		this.parent = parent;
		if (this.id != null) {
			this.id.connectParent(parent);
		}
	}


}
