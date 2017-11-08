//	PROJECT:        corebase.model (CORE.M)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2016 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Java 1.6.
//	DESCRIPTION:		Library that defines the model classes to implement the core for a GEF based
//									Model-View-Controller. Code is as neutral as possible and made to be reused
//									on all Java development projects.
package org.dimensinfin.core.model;

//- IMPORT SECTION .........................................................................................
import java.util.Vector;

import org.dimensinfin.core.model.CEventModel.ECoreModelEvents;

import com.fasterxml.jackson.annotation.JsonIgnore;

// - CLASS IMPLEMENTATION ...................................................................................
public abstract class AbstractGEFNode extends AbstractPropertyChanger implements IGEFNode {

	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long		serialVersionUID	= 4649865295860939719L;

	// - F I E L D - S E C T I O N ............................................................................
	@JsonIgnore
	private transient IGEFNode	parent						= null;
	@JsonIgnore
	private Vector<IGEFNode>		children					= new Vector<IGEFNode>();
	protected String						jsonClass					= "AbstractGEFNode";

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AbstractGEFNode() {
		super();
		jsonClass = "AbstractGEFNode";
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@JsonIgnore
	public IGEFNode accessParent() {
		return parent;
	}

	public void addChild(final IGEFNode child) {
		if (null != child) {
			getChildren().add(child);
			child.setParent(this);
			firePropertyChange(ECoreModelEvents.EVENT_CHILD_ADDEDPROP.name(), null, child);
		}
	}

	public void clean() {
		getChildren().removeAllElements();
	}

	/**
	 * Check for pointer transient fields that can be loaded from object read operations.
	 */
	@JsonIgnore
	public Vector<IGEFNode> getChildren() {
		if (null == children) children = new Vector<IGEFNode>();
		return children;
	}

	public String getJsonClass() {
		return jsonClass;
	}

	public String quote(final double value) {
		return '"' + new Double(value).toString() + '"';
	}

	public String quote(final int value) {
		return '"' + new Integer(value).toString() + '"';
	}

	public String quote(final String value) {
		if (null == value) return '"' + "" + '"';
		return '"' + value + '"';
	}

	public void removeChild(final IGEFNode child) {
		if (null != child) {
			getChildren().remove(child);
			firePropertyChange(ECoreModelEvents.EVENT_CHILD_REMOVEDPROP.name(), child, null);
		}
	}

	public void setDirty(final boolean dirtyState) {
		if (null != parent) parent.setDirty(dirtyState);
	}

	/**
	 * Sets the parent node for this instance and also the shaded field of the parent changer so the listener
	 * chain can also make use of the parent to fire the events.
	 */
	public void setParent(final IGEFNode newParent) {
		parent = newParent;
		// Set also the parent of the changer behind.
		super.setParentChanger((AbstractPropertyChanger) newParent);
	}
}
// - UNUSED CODE ............................................................................................
