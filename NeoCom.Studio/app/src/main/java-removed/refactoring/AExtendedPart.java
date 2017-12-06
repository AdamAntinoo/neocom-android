//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.refactoring;

import java.util.ArrayList;
import java.util.Vector;

import org.dimensinfin.android.model.AbstractViewableNode;
import org.dimensinfin.android.model.Separator;
import org.dimensinfin.android.model.Separator.ESeparatorType;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.android.mvc.part.SeparatorPart;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.model.AbstractComplexNode;
import org.dimensinfin.eveonline.neocom.core.EveAbstractPart;

// - CLASS IMPLEMENTATION ...................................................................................
public abstract class AExtendedPart extends EveAbstractPart {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AExtendedPart(final ICollaboration node) {
		super(node);
		newImplementation = true;
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * The goal of this method is to return the list of Parts on the children list that should be visible and
	 * that collaborates to the ListView population. The default behavior for this method is to check if the
	 * Model behind the Part is expanded, in that case the children have the opportunity to be added to the
	 * visible list.<br>
	 * If the Region is not expanded the list of elements collaborated should be:
	 * <ul>
	 * <li>Region</li>
	 * </ul>
	 * If the Region is expanded and has a number of contents the collaboration changes to add the Separators:
	 * <ul>
	 * <li>Separator Red</li>
	 * <li>Region</li>
	 * <li>Location...</li>
	 * <li>Separator Red</li>
	 * </ul>
	 */
	@Override
	public ArrayList<IPart> collaborate2View() {
		ArrayList<IPart> result = new ArrayList<IPart>();
		// Check if the Part is visible and collaborates to the LiastView.
		if (this.isVisible()) {
			// First we have to check if we have contents or if the part should be shown altogether.
			//			int contentCount = this.getCastedModel().getContentCount();
			// If the node is expanded then give the children the opportunity to also be added.
			if (this.isExpanded()) {
				if (this.getCastedModel().isEmpty()) {
					if (this.isRenderWhenEmpty()) {
						// The region is surrounded with the Red Separator at the beginning and at the end.
						result.add(new SeparatorPart(new Separator().setType(ESeparatorType.LINE_RED))
								.setRenderMode(this.getRenderMode()));
						result.add(this);
						// This region is empty of contents. Add the Empty Separator and complete.
						result.add(new SeparatorPart(new Separator().setType(ESeparatorType.EMPTY_SIGNAL))
								.setRenderMode(this.getRenderMode()));
						result.add(new SeparatorPart(new Separator().setType(ESeparatorType.LINE_RED))
								.setRenderMode(this.getRenderMode()));
					}
				} else {
					// Generate the list of Parts that should be collaborated to the ListView.
					// The region is surrounded with the Red Separator at the beginning and at the end.
					result.add(
							new SeparatorPart(new Separator().setType(ESeparatorType.LINE_RED)).setRenderMode(this.getRenderMode()));
					result.add(this);
					// --- This is the section where we get the Contents and then we apply the customized Part policies.
					Vector<IPart> childParts = this.runPolicies(this.getChildren());
					// --- End of policies
					for (IPart part : childParts) {
						// For each of the children that are registered to this part and that have a model recursively search for collaborations.
						ArrayList<IPart> collaboration = part.collaborate2View();
						if (!part.isNewImplemented()) {
							result.add(part);
						}
						result.addAll(collaboration);
					}
					result.add(
							new SeparatorPart(new Separator().setType(ESeparatorType.LINE_RED)).setRenderMode(this.getRenderMode()));
				}
			} else {
				// Check for items that will not shown when empty.
				if (this.isRenderWhenEmpty()) {
					result.add(this);
				}
			}
		}
		return result;
	}

	public AbstractViewableNode getCastedModel() {
		return (AbstractViewableNode) this.getModel();
	}
}

// - UNUSED CODE ............................................................................................
