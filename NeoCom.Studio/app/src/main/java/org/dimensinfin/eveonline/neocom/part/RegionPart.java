//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Android Application related to the Eve Online game. The purpose is to download and organize
//                  the game data to help capsuleers organize and prioritize activities. The strong points are
//                  help at the Industry level tracking and calculating costs and benefits. Also the market
//                  information update service will help to identify best prices and locations.
//                  Planetary Interaction and Ship fittings are point under development.
//                  ESI authorization is a new addition that will give continuity and allow download game data
//                  from the new CCP data services.
//                  This is the Android application version but shares libraries and code with other application
//                  designed for Spring Boot Angular 4 platform.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.part;

import android.app.Activity;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.core.PartComparatorFactory;
import org.dimensinfin.eveonline.neocom.core.PartComparatorFactory.ComparatorType;
import org.dimensinfin.eveonline.neocom.model.Region;
import org.dimensinfin.eveonline.neocom.part.core.NeoComExpandablePart;
import org.dimensinfin.eveonline.neocom.render.BaseExpandableRender;

import java.util.Collections;
import java.util.Vector;

// - CLASS IMPLEMENTATION ...................................................................................
public class RegionPart extends NeoComExpandablePart implements INamedPart/*, OnClickListener */ {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = -7696249781992547826L;
	//	private static Logger					logger							= Logger.getLogger("RegionPart");
	//	private static DecimalFormat itemCountFormatter = new DecimalFormat("###,##0");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public RegionPart (final Region region) {
		super(region);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public Region getCastedModel () {
		return (Region) this.getModel();
	}
	//	public String get_region () {
	//		return this.getCastedModel().getTitle();
	//	}
	//
	//	@Override

	@Override
	public long getModelID () {
		return this.getCastedModel().getTitle().hashCode();
	}

	public String getName () {
		return this.getCastedModel().getTitle();
	}

	public String getTContentCount () {
		return RegionPart.itemCountFormatter.format(this.getCastedModel().getLocationCount());
	}

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

	/**
	 * Sort the Regions by their names.
	 */
	public Vector<IPart> runPolicies (final Vector<IPart> targets) {
		// Order the characters by alphabetical name.
		Collections.sort(targets, PartComparatorFactory.createPartComparator(ComparatorType.COMPARATOR_NAME));
		return targets;
	}

	@Override
	public String toString () {
		StringBuffer buffer = new StringBuffer("RegionPart [");
		buffer.append(this.getCastedModel());
		buffer.append(" ]");
		return buffer.toString();
	}

	@Override
	protected AbstractRender selectRenderer () {
		// Get the proper holder from the render mode.
		return new RegionRender(this, _activity);
	}
}

// - CLASS IMPLEMENTATION ...................................................................................
final class RegionRender extends BaseExpandableRender {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger	logger			= Logger.getLogger("RegionHolder");

	// - F I E L D - S E C T I O N ............................................................................


	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public RegionRender (final RegionPart target, final Activity context) {
		super(target, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public RegionPart getPart () {
		return (RegionPart) super.getPart();
	}

	@Override
	public void updateContent () {
		logger.info(">> [RegionRender.updateContent]");
		super.updateContent();
		nodeIcon.setImageDrawable(getDrawable(R.drawable.map));
		name.setText(this.getPart().getName());
		childCount.setText(this.getPart().getTContentCount());

		// Set this variant labels
		classLabel.setText("REGION");
		categoryLabel.setText("LOCATIONS");
		if ( VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP ) {
			_convertView.setBackground(getContext().getResources().getDrawable(R.drawable.panelborderred, getContext().getTheme()));
		} else _convertView.setBackground(getContext().getResources().getDrawable(R.drawable.panelborderred));
		_convertView.invalidate();
		logger.info("<< [RegionRender.updateContent]");
	}
}

// - UNUSED CODE ............................................................................................
