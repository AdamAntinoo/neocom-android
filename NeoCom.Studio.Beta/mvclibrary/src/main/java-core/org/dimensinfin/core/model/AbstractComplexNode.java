//	PROJECT:        corebase.model (CORE.M)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2016 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Java 1.6.
//	DESCRIPTION:		Library that defines the model classes to implement the core for a GEF based
//									Model-View-Controller. Code is as neutral as possible and made to be reused
//									on all Java development projects.
package org.dimensinfin.core.model;

// - CLASS IMPLEMENTATION ...................................................................................
/**
 * This class extends the GEFNode with more properties that extend the base wireframe model exposed by GEF to
 * a more wide hierarchical model where the collaboration to the model does not come only from the contents of
 * the parentship fields <code>children</code> and <code>parent</code>. <br>
 * Most of this fields are exported to the Part for easy interaction and the changes should fire the
 * corresponding events to the connected listeners.
 * 
 * @author Adam Antinoo
 */
public abstract class AbstractComplexNode extends AbstractGEFNode {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = -7147051299063576257L;
	static {
	}
	// - F I E L D - S E C T I O N ............................................................................
	//	protected String	jsonClass				= "AbstractComplexNode";
	/** Stores the expand/collapse visual state to be used on the UI. */
	private boolean		expanded				= false;
	/**
	 * If the node has its children available (by default) then this shows a <code>true</code>. If the hierarchy
	 * may be calculated on a deferred instant like the moment the user expands or selects the node then stores
	 * the downloaded state of the children data.
	 */
	private boolean		downloaded			= false;
	/** If true then the node is ever on the contents list. If false then it will depend on the hierarchy. */
	protected boolean	renderWhenEmpty	= true;
	/** Allows to control the specific visibility of a single part. Defaults to visible when not empty. */
	private boolean		visible					= true;

	/** Java class name to be used on the JSON serialization. */

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AbstractComplexNode() {
		super();
		jsonClass = "AbstractComplexNode";
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * Collapses a node from any state if had. If will collapse the node without checking the current state.
	 * 
	 * @return the final extend state.
	 */
	public boolean collapse() {
		boolean oldValue = expanded;
		expanded = false;
		//		firePropertyChange(ECoreModelEvents.EVENT_EXPANDCOLLAPSENODE.name(), new Boolean(oldValue), new Boolean(expanded));
		return expanded;
	}

	/**
	 * Only expands nodes that have children. This action also has to keep synchronized the recording of actions
	 * that are used to reproduce the state of a hierarchy.<br>
	 * With the new model there is no need to check if an element has children to filter the expansion. This is
	 * something that is used and calculated when the model element collaborates to the viewable model. Remove
	 * that check because now children hierarchies are not guaranteed.
	 * 
	 * @return the final extend state.
	 */
	public boolean expand() {
		boolean oldValue = expanded;
		expanded = true;
		//		firePropertyChange(ECoreModelEvents.EVENT_EXPANDCOLLAPSENODE.name(), new Boolean(oldValue), new Boolean(expanded));
		return expanded;
	}

	public boolean isDownloaded() {
		return downloaded;
	}

	public boolean isExpanded() {
		return expanded;
	}

	/**
	 * Checks of the groups should be rendered depending of some configuration values. By default all groups are
	 * rendered only when they have contents. If they are empty they may not be rendered. Only they can be
	 * forced to be rendered if the right flag value is set to true. <br>
	 * With the new design the use of the children to control the emptiness of a node is no longer valid until I
	 * implement the children list as a copy of the model representation. So the abstract nodes are empty by
	 * definition under the new design.
	 * 
	 * @return
	 */
	public boolean isRenderWhenEmpty() {
		return renderWhenEmpty;
	}

	public boolean isVisible() {
		return visible;
	}

	public AbstractComplexNode setDownloaded(final boolean downloadedstate) {
		boolean oldValue = downloaded;
		this.downloaded = downloadedstate;
		//		firePropertyChange(ECoreModelEvents.EVENT_DOWNLOADNODE.name(), new Boolean(oldValue), new Boolean(downloaded));
		return this;
	}

	public AbstractComplexNode setExpanded(final boolean newState) {
		boolean oldValue = expanded;
		expanded = newState;
		//		firePropertyChange(ECoreModelEvents.EVENT_EXPANDCOLLAPSENODE.name(), new Boolean(oldValue), new Boolean(expanded));
		return this;
	}

	public AbstractComplexNode setRenderWhenEmpty(final boolean renderWhenEmpty) {
		this.renderWhenEmpty = renderWhenEmpty;
		return this;
	}

	public AbstractComplexNode setVisible(final boolean visiblestate) {
		boolean oldValue = visible;
		this.visible = visiblestate;
		//		firePropertyChange(ECoreModelEvents.EVENT_ALTERVISIBLESTATENODE.name(), new Boolean(oldValue),new Boolean(visible));
		return this;
	}

	/**
	 * Tries to invert the expansion state of the node. Only expands nodes that have children and is not
	 * operative on the other nodes.<br>
	 * Since the addition of the view cache every change on the part content or model should trigger a change on
	 * the view.
	 */
	public boolean toggleExpanded() {
		if (!expanded)
			return this.expand();
		else
			return this.collapse();
	}

	public boolean toggleVisible() {
		setVisible(!visible);
		return visible;
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer("AbstractComplexNode [");
		buffer.append("ex/dow/eren/vis: [").append(expanded).append("/").append(downloaded).append("/")
				.append(renderWhenEmpty);
		buffer.append("/").append(visible).append("]");
		buffer.append("]");
		return buffer.toString();
	}
}

// - UNUSED CODE ............................................................................................
