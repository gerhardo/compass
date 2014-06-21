package de.volkswagen.compass.web.selenium2.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Bezeichnet ein Objekt (aka Komponente) einer Seite. Kann hierarchisch
 * aufgebaut werden. Durch das Einbinden in die Hierachie mit der Methode
 * {@link PageObject#addChild(PageObject)} wird die ID des Kindes mit dem
 * ID-Pfad des Elternknotens verknüpft, so dass sich über den ID-Pfad ein
 * Auffinden der zugehörigen Komponente realisieren lässt.
 * 
 * @author go
 * 
 */
public class PageObject {

	public static final String DEFAULT_ID_PART_SEPARATOR = ":";

	/**
	 * Identifizierung eines Elements, auf das das {@link PageObject} verweisen soll.
	 * 
	 */
	public interface Identifier {

		/**
		 * Manipulationen des eigenen {@link Identifier} ermöglichen, wenn das
		 * {@link PageObject} in eine Hierarchie eingebunden oder daraus
		 * entfernt wird.
		 * 
		 * @param parent
		 *            {@link PageObject}, zu dem dieses als Kind hinzugefügt
		 *            wurde
		 */
		public void connectParent(PageObject parent, String idPartSeparator);

		/**
		 * Liefert die verkettete ID aller Hierarchiestufen bis zu diesem
		 * Element.
		 * 
		 * @return {@link String} mit ID's getrennt durch eine Separator
		 */
		public String getIdPath();
	}

	/**
	 * Identifizierung eines Elements durch einen {@link String}.
	 * 
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
			this.idPath = parent.getIdentifier().getIdPath() + idPartSeparator
					+ id;
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
	 * Identifizierung über eine Position innerhalb einer Elementmenge des
	 * Parents, die durch einen Selector String ausgewählt wird.
	 * 
	 * @author go
	 * 	/**
	 * Factory Methode für ein neues {@link PageObject}.
	 * 
	 * @param position
	 *            Position des Objektes in der Liste der Kinder des Parent
	 *            Objekts, wenn nach dem selector gefiltert wird
	 * @param selector
	 *            Selector Ausdruck für die Ermittlung der möglichen Kinder des
	 *            Parents
	 * @return {@link PageObject}
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
			this.idPath = parent.getIdentifier().getIdPath() + idPartSeparator
					+ formatAsString();
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
			return "[" + String.valueOf(position) + "/"
					+ (selector == null ? "<NULL>" : selector) + "]";
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

	/**
	 * Fügt ein {@link PageObject} als Kind dem aktuellen {@link PageObject}
	 * hinzu.
	 * 
	 * @param child
	 *            untergeordnetes {@link PageObject}
	 * @return Referenz auf das aktuelle {@link PageObject}
	 */
	public PageObject addChild(PageObject child) {
		Validate.notNull(child);
		children.add(child);
		child.setParent(this);
		return this;
	}

	/**
	 * Liefert die Liste der Kindobjekte.
	 * 
	 * @return Eine nicht änderbare Liste der Kindobjekte vom Typ
	 *         {@link PageObject}.
	 */
	public List<PageObject> getChildren() {
		return Collections.unmodifiableList(children);
	}

	/**
	 * Liefert das übergeordnete Parent Objekt.
	 * 
	 * @return {@link PageObject}
	 */
	public PageObject getParent() {
		return parent;
	}

	/**
	 * Factory Methode für ein neues {@link PageObject}.
	 * 
	 * @param id
	 *            ID des Objekts
	 * @return {@link PageObject}
	 */
	public static final PageObject createInstance(String id) {
		return new PageObject(new StringIdentifier(id));
	}

	/**
	 * Factory Methode für ein neues {@link PageObject}.
	 * 
	 * @param id
	 *            ID des Objekts
	 * @param name
	 *            Name des Objekts.
	 * @return {@link PageObject}
	 */
	public static final PageObject createInstance(String id, String name) {
		return new PageObject(new StringIdentifier(id), name);
	}

	/**
	 * Factory Methode für ein neues {@link PageObject}.
	 * 
	 * @param position
	 *            Position des Objektes in der Liste der Kinder des Parent
	 *            Objekts, wenn nach dem selector gefiltert wird
	 * @param selector
	 *            Selector Ausdruck für die Ermittlung der möglichen Kinder des
	 *            Parents
	 * @return {@link PageObject}
	 */
	public static final PageObject createInstance(int position, String selector) {
		return new PageObject(
				new PositionSelectorIdentifier(position, selector));
	}

	/**
	 * Factory Methode für ein neues {@link PageObject}.
	 * 
	 * @param position
	 *            Position des Objektes in der Liste der Kinder des Parent
	 *            Objekts, wenn nach dem selector gefiltert wird
	 * @param selector
	 *            Selector Ausdruck für die Ermittlung der möglichen Kinder des
	 *            Parents
	 * @param name Name des Objektes
	 * @return {@link PageObject}
	 */
	public static final PageObject createInstance(int position,
			String selector, String name) {
		return new PageObject(
				new PositionSelectorIdentifier(position, selector), name);
	}

	/**
	 * Liefert den Namnes des Objektes.
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Liefert den Identifier des Objekts.
	 * @return {@link Identifier}
	 */
	public Identifier getIdentifier() {
		return id;
	}
	
	/**
	 * Liefert true, wenn das Objekt keine Kinder besitzt.
	 * @return 
	 */
	public boolean isLeaf() {
		return this.children.size() == 0;
	}
	
	/**
	 * Liefert die Anzahl der Kinder des Objektes.
	 * @return
	 */
	public int numChildren() {
		return this.children.size();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("name", name).append("idPath", id.getIdPath())
				.toString();
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
