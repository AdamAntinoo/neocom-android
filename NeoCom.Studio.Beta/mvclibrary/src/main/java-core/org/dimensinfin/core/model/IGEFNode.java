//  PROJECT:        net.sf.gef.core
//  FILE NAME:      $Id: ProcessorApp.java,v $
//  LAST UPDATE:    $Date: 2000/06/28 11:52:36 $
//  RELEASE:        $Revision: 1.4 $
//  AUTHORS:        Lima Delta Delta (LDD) - boneymen@netscape.com
//  COPYRIGHT:      (c) 2008 by LDD Game Development Spain, all rights reserved.

package org.dimensinfin.core.model;

// - IMPORT SECTION .........................................................................................
import java.util.Vector;

// - INTERFACE IMPLEMENTATION ...............................................................................
public interface IGEFNode extends INodeModel {
	public IGEFNode accessParent();

	// - M E T H O D - S E C T I O N ..........................................................................
	public void addChild(IGEFNode child);

	public Vector<IGEFNode> getChildren();

	public void setDirty(final boolean dirtyState);

	public void setParent(IGEFNode parent);
}
// - UNUSED CODE ............................................................................................
