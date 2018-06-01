//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API16.
//  DESCRIPTION: Android Application related to the Eve Online game. The purpose is to download and organize
//               the game data to help capsuleers organize and prioritize activities. The strong points are
//               help at the Industry level tracking and calculating costs and benefits. Also the market
//               information update service will help to identify best prices and locations.
//               Planetary Interaction and Ship fittings are point under development.
//               ESI authorization is a new addition that will give continuity and allow download game data
//               from the new CCP data services.
//               This is the Android application version but shares libraries and code with other application
//               designed for Spring Boot Angular 4 platform.
//               The model management is shown using a generic Model View Controller that allows make the
//               rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.part;

import android.app.Activity;

import org.dimensinfin.android.mvc.core.AbstractExpandableRender;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.model.Region;
import org.dimensinfin.eveonline.neocom.part.core.NeoComExpandablePart;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

// - CLASS IMPLEMENTATION ...................................................................................
public class RegionPart extends NeoComExpandablePart /*implements INamedPart/*, OnClickListener */ {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = -7696249781992547826L;

	public static Comparator<IPart> regionNameComparator = new Comparator<IPart>() {
		public int compare (IPart part1, IPart part2) {
			String partName1 = ((RegionPart) part1).getTName().toUpperCase();
			String partName2 = ((RegionPart) part2).getTName().toUpperCase();

			//ascending order
			return partName1.compareTo(partName2);

			//descending order
			//return fruitName2.compareTo(fruitName1);
		}
	};

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public RegionPart (final Region region) {
		super(region);
		// Set the part color theme.
		setThemeColor(Separator.ESeparatorType.LINE_RED);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	// --- I A N D R O I D P A R T   I N T E R F A C E
	@Override
	public long getModelId () {
		return this.getCastedModel().getRegionId();
	}

	@Override
	public AbstractRender selectRenderer () {
		// Get the proper holder from the render mode.
		return new RegionRender(this, _activity);
	}

	/**
	 * Sort the Regions by their names.
	 */
	public Vector<IPart> runPolicies (final Vector<IPart> targets) {
		// Order the characters by alphabetical name.
		Collections.sort(targets, regionNameComparator);
		return targets;
	}

	// --- N E O C O M   P A R T   R E Q U I R E M E N T S
	public Region getCastedModel () {
		return (Region) this.getModel();
	}

	// --- M O D E L   D A T A   T R A N S F O R M A T I O N
	public String getTName () {
		return this.getCastedModel().getName();
	}

	public String getTContentCount () {
		return RegionPart.itemCountFormatter.format(this.getCastedModel().getLocationCount());
	}

	@Override
	public String toString () {
		StringBuffer buffer = new StringBuffer("RegionPart [ ");
		buffer.append(this.getCastedModel());
		buffer.append(" ]");
		return buffer.toString();
	}
}

// - CLASS IMPLEMENTATION ...................................................................................

/**
 * Defines the contents for a Region node. It uses a generic Expandable node that already defines the right arrow,
 * the expand collapse functionality, the panel type, the expandable node content count and a label to identify the
 * content count meaning.
 * It also has a reference for the panel background so it can be changed to the Part theme selected.
 */
final class RegionRender extends AbstractExpandableRender {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public RegionRender (final RegionPart target, final Activity context) {
		super(target, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	// --- N E O C O M   R E N D E R   R E Q U I R E M E N T S
	@Override
	public RegionPart getPart () {
		return (RegionPart) super.getPart();
	}

	//	@Override
	//	public void initializeViews () {
	//	}

	@Override
	public void updateContent () {
		logger.info(">> [RegionRender.updateContent]");
		super.updateContent();
		nodeIcon.setImageDrawable(getDrawable(R.drawable.map));
		name.setText(this.getPart().getTName());
		childCount.setText(this.getPart().getTContentCount());

		// Set this variant labels
		classLabel.setText("REGION");
		categoryLabel.setText("LOCATIONS");
		setPanelBorderColor(getPart().getThemeColor());
		_convertView.invalidate();
		logger.info("<< [RegionRender.updateContent]");
	}
}

// - UNUSED CODE ............................................................................................
//[01]
//	public void onClick (final View view) {
//		// Toggle location to show its contents.
//		this.invalidate();
//		this.toggleExpanded();
//		this.fireStructureChange(SystemWideConstants.events.EVENTSTRUCTURE_ACTIONEXPANDCOLLAPSE.name(), this, this);
//	}

//	/**
//	 * Returns the list of parts that are available for this node. If the node it is expanded then the list will
//	 * include the children and any other grand children of this one. If the node is collapsed then the only
//	 * result will be the node itself.
//	 *
//	 * @return list of parts that are accessible for this node.
//	 */
//	@Override
//	public ArrayList<AbstractAndroidPart> getPartChildren() {
//		ArrayList<AbstractAndroidPart> result = new ArrayList<AbstractAndroidPart>();
//		Vector<AbstractPropertyChanger> ch = getChildren();
//		Collections.sort(ch, EVEDroidApp.createComparator(AppWideConstants.comparators.COMPARATOR_NAME));
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

//	@Override
//	public ArrayList<AbstractAndroidPart> collaborate2View() {
//		ArrayList<AbstractAndroidPart> result = new ArrayList<AbstractAndroidPart>();
//		Vector<AbstractPropertyChanger> ch = getChildren();
//		Collections.sort(ch, EVEDroidApp.createComparator(AppWideConstants.comparators.COMPARATOR_NAME));
//		for (AbstractPropertyChanger node : ch) {
//			// Convert the node to a part.
//			AbstractAndroidPart part = (AbstractAndroidPart) node;
//			// Add me to the output list because I am not empty
//			result.add(part);
//			// Check if the node is expanded. Then add its children.
//			if (part.isExpanded()) {
//				ArrayList<AbstractAndroidPart> grand = part.collaborate2View();
//				result.addAll(grand);
//			}
//		}
//		return result;
//	}

//[02]
//
//
//	@Override
//	protected AbstractRender selectRenderer () {
//	}
