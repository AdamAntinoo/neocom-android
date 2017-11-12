//	PROJECT:        corebase.model (CORE.M)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2016 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Java 1.6.
//	DESCRIPTION:		Library that defines the model classes to implement the core for a GEF based
//									Model-View-Controller. Code is as neutral as possible and made to be reused
//									on all Java development projects.
package org.dimensinfin.core.model;

import java.util.ArrayList;

import org.dimensinfin.core.interfaces.IViewableNode;

// - CLASS IMPLEMENTATION ...................................................................................
/**
 * This is an special implementation of an <code>AbstractComplexNode</code> that is used to contain the first
 * and root element of all Model hierarchies.
 * 
 * @author Adam Antinoo
 */
public class RootNode extends AbstractComplexNode implements IViewableNode {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = 297128593703416475L;

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public RootNode() {
		// Change the default expansion state to always expanded.
		setExpanded(true);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public void addChildren(final ArrayList<AbstractComplexNode> modelList) {
		for (AbstractComplexNode node : modelList)
			this.addChild(node);
	}

	/**
	 * This special node collaborates with their children but not itself.
	 */
	public ArrayList<AbstractComplexNode> collaborate2Model(final String variant) {
		final ArrayList<AbstractComplexNode> results = new ArrayList<AbstractComplexNode>();
		for (IGEFNode node : this.getChildren())
			results.add((AbstractComplexNode) node);
		return results;
	}

	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean isExpanded() {
		return true;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("RootNode [");
		buffer.append("count: ").append(this.getChildren().size()).append("\t,");
		buffer.append("[").append(this.getChildren()).append("]");
		buffer.append(" ").append(super.toString()).append(" ]");
		return buffer.toString();
	}
}

// - UNUSED CODE ............................................................................................
