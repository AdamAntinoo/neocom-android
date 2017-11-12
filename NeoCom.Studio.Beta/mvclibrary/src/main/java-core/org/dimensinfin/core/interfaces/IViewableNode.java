//	PROJECT:        corebase.model (CORE.M)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2016 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Java 1.6.
//	DESCRIPTION:		Library that defines the model classes to implement the core for a GEF based
//									Model-View-Controller. Code is as neutral as possible and made to be reused
//									on all Java development projects.
package org.dimensinfin.core.interfaces;

import java.util.ArrayList;

import org.dimensinfin.core.model.AbstractComplexNode;

// - CLASS IMPLEMENTATION ...................................................................................
public interface IViewableNode {
	// - M E T H O D - S E C T I O N ..........................................................................
	public ArrayList<AbstractComplexNode> collaborate2Model(final String variant);

	public boolean collapse();

	public boolean expand();

	public boolean isDownloaded();

	//	public int getContentCount();
	public boolean isEmpty();

	public boolean isExpanded();

	public boolean isRenderWhenEmpty();

	@Deprecated
	public boolean isVisible();

	public AbstractComplexNode setDownloaded(final boolean downloadedstate);

	public AbstractComplexNode setExpanded(final boolean newState);

	public AbstractComplexNode setRenderWhenEmpty(final boolean renderWhenEmpty);

	public AbstractComplexNode setVisible(final boolean visiblestate);

	public boolean toggleExpanded();

	public boolean toggleVisible();
}
// - UNUSED CODE ............................................................................................
