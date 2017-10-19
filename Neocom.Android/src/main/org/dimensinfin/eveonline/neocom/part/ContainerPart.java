//	PROJECT:        EveIndustrialAssistant (EIA)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2014 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API11.
//	DESCRIPTION:		Application helper for Eve Online Industrialists. Will help on Minery and mainly on Manufacture.

package org.dimensinfin.eveonline.neocom.part;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import org.dimensinfin.android.model.Separator;
import org.dimensinfin.android.model.Separator.ESeparatorType;
import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.android.mvc.part.SeparatorPart;
import org.dimensinfin.eveonline.neocom.NeoComApp;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.core.EveAbstractPart;
import org.dimensinfin.eveonline.neocom.holder.ContainerHolder;
import org.dimensinfin.eveonline.neocom.model.NeoComAsset;
import org.dimensinfin.eveonline.neocom.model.SpaceContainer;

import android.view.View;
import android.view.View.OnClickListener;

// - CLASS IMPLEMENTATION ...................................................................................
public class ContainerPart extends AssetPart implements OnClickListener {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = -2462731579059844711L;

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public ContainerPart(final NeoComAsset node) {
		super(node);
		newImplementation = true;
	}

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
				result.add(this);
			}
		}
		return result;
	}

	public String get_containerCategory() {
		return this.getCastedModel().getItem().getName();
	}

	@Override
	public SpaceContainer getCastedModel() {
		return (SpaceContainer) this.getModel();
	}

	@Override
	public String getName() {
		return this.getTAssetName();
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * Container may be user named so check if the user label is defined and then return that value.
	 */
	@Override
	public String getTAssetName() {
		String userName = this.getCastedModel().getUserLabel();
		if (null == userName)
			return "#" + this.getCastedModel().getAssetID();
		else
			return userName;
	}

	public String getTContentCount() {
		return EveAbstractPart.qtyFormatter.format(this.getCastedModel().getContents().size());
	}

	public int getTypeID() {
		return this.getCastedModel().getTypeID();
	}

	@Override
	public void onClick(final View view) {
		// Toggle location to show its contents.
		this.toggleExpanded();
		this.fireStructureChange(SystemWideConstants.events.EVENTSTRUCTURE_ACTIONEXPANDCOLLAPSE.name(), this, this);
	}

	public boolean onLongClick(final View target) {
		return false;
	}

	//	@Override
	//	public ArrayList<AbstractAndroidPart> getPartChildren() {
	//		ArrayList<AbstractAndroidPart> result = new ArrayList<AbstractAndroidPart>();
	//		Vector<AbstractPropertyChanger> ch = this.getChildren();
	//
	//		for (AbstractPropertyChanger node : ch) {
	//			// Convert the node to a part.
	//			AbstractAndroidPart part = (AbstractAndroidPart) node;
	//			result.add(part);
	//			// Check if the node is expanded. Then add its children.
	//			if (part.isExpanded()) {
	//				ArrayList<AbstractAndroidPart> grand = part.getPartChildren();
	//				result.addAll(grand);
	//			}
	//		}
	//		return result;
	//	}
	/**
	 * The default actions inside this method usually are the sorting of the children nodes. Sort the container
	 * contents by name.
	 */
	@Override
	public Vector<IPart> runPolicies(final Vector<IPart> targets) {
		// Order the contents by alphabetical name.
		Collections.sort(targets, NeoComApp.createPartComparator(AppWideConstants.comparators.COMPARATOR_NAME));
		return targets;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("ContainerPart [");
		buffer.append(this.getTAssetName());
		buffer.append(" ]");
		return buffer.toString();
	}

	@Override
	protected AbstractRender selectRenderer() {
		// Get the proper holder from the render mode.
		if (this.getRenderMode() == AppWideConstants.fragment.FRAGMENT_ASSETSBYLOCATION)
			return new ContainerHolder(this, _activity);
		return new ContainerHolder(this, _activity);
	}

}

// - UNUSED CODE ............................................................................................
