package de.volkswagen.compass.web.selenium2.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PageObject {
	
	public static final String DEFAULT_ID_PART_SEPARATOR = ":";
	
	/**
	 * Identifizierung von Elmenten.
	 *
	 */
	public interface Identifier {

		/**
		 * Manipulationen des eigenen {@link Identifier} ermöglichen, wenn das {@link PageObject} in eine Hierarchie eingebunden oder daraus entfernt wird.
		 * @param parent {@link PageObject}, zu dem dieses als Kind hinzugefügt wurde
		 */
		public void connectParent(PageObject parent, String idPartSeparator);
		
		/**
		 * Liefert die verkettete ID aller Hierarchiestufen bis zu diesem Element. 
		 * @return {@link String} mit ID's getrennt durch eine Separator
		 */
		public String getIdPath();
	}
	
	/**
	 * Identifizierung eines Elements durch einen {@link String}.
	 * @author go
	 *
	 */
	public static final class StringIdentifier implements Identifier {
		
		
		public StringIdentifier(String id) {
			super();
			Validate.notEmpty(id);
			this.id = id;
			this.idPath = id;
		}
		private String id;
		private String idPath;

		
		public String getId() {
			return id;
		}
		@Override
		public void connectParent(PageObject parent, String idPartSeparator) {
			this.idPath = parent.getIdentifier().getIdPath() + idPartSeparator + id;
		}
		
		@Override
		public String getIdPath() {
			return idPath;
		}
		
		@Override
		public String toString() {
			return getIdPath();
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
			this.idPath = formatAsString();
		}
		private int position;
		private String selector;
		private String idPath;
		
		public int getPosition() {
			return position;
		}
		
		public String getSelector() {
			return selector;
		}
		@Override
		public void connectParent(PageObject parent, String idPartSeparator) {
			this.idPath = parent.getIdentifier().getIdPath() + idPartSeparator + formatAsString();
		}
		
		@Override
		public String getIdPath() {
			return idPath;
		}

		@Override
		public String toString() {
			return getIdPath();
		}
		
		private String formatAsString() {
			return "[" + String.valueOf(position) + "/" + (selector == null ? "<NULL>" : selector) + "]";
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
	private String idPartSeparator = DEFAULT_ID_PART_SEPARATOR;
	
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
	
	public Identifier getIdentifier() {
		return id;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("name", name).append("idPath", id.getIdPath()).toString();
	}

	public void setParent(PageObject parent) {
		this.parent = parent;
		if (this.id != null) {
			this.id.connectParent(parent, idPartSeparator);
		}
	}

	public void setIdPartSeparator(String idPartSeparator) {
		this.idPartSeparator = idPartSeparator;
	}


}
