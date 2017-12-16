//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.part;

import android.app.Activity;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.core.PartComparatorFactory;
import org.dimensinfin.eveonline.neocom.core.PartComparatorFactory.ComparatorType;
import org.dimensinfin.eveonline.neocom.model.AssetCategory;
import org.dimensinfin.eveonline.neocom.render.BaseExpandableRender;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

// - CLASS IMPLEMENTATION ...................................................................................
public class AssetCategoryPart extends NeoComExpandablePart implements INamedPart/*, OnClickListener*/ {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static DecimalFormat itemCountFormatter = new DecimalFormat("###,##0");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AssetCategoryPart (final AssetCategory node) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public AssetCategory getCastedModel () {
		return (AssetCategory) this.getModel();
	}

	public long getModelID () {
		return this.getCastedModel().getCategoryId();
	}

	public String getName () {
		return this.getCastedModel().getCategoryName();
	}

	public String getTContentCount () {
		return itemCountFormatter.format(this.getCastedModel().getContentSize());
	}

	//	public void onClick (final View view) {
	//		logger.info(">> [AssetCategoryPart.onClick]");
	//		// Signal the action may take some time and launch it on the background.
	//		activateClick();
	//		// Force the update of the listview.
	//		invalidate();
	//		notifyDataSetChanged();
	//		// launch the actions on a new thread.
	//		new Handler().postDelayed(new OneShotTask<AssetCategoryPart>(this) {
	//			public void run () {
	//				logger.info(">> [AssetCategoryPart.onClick.OneShotTask.run]");
	//				Chrono chrono = new Chrono();
	//				getTargetPart().toggleExpanded();
	//				getTargetPart().invalidate();
	//				getTargetPart().fireStructureChange(SystemWideConstants.events.EVENTSTRUCTURE_ACTIONEXPANDCOLLAPSE.name(), this, this);
	//				completeClick();
	//				logger.info("<< [AssetCategoryPart.onClick.OneShotTask.run]> Time Elapsed: " + chrono.printElapsed(Chrono.ChonoOptions.SHOWMILLIS));
	//			}
	//		}, CoreConstants.HUNDRETH);
	//		logger.info("<< [AssetCategoryPart.onClick]");
	//	}

	/**
	 * Sort the Contents by their names.
	 */
	@Override
	public List<IPart> runPolicies (final List<IPart> targets) {
		// Order the characters by alphabetical name.
		Collections.sort(targets, PartComparatorFactory.createPartComparator(ComparatorType.COMPARATOR_NAME));
		return targets;
	}

	@Override
	public String toString () {
		StringBuffer buffer = new StringBuffer("AssetCategoryPart [");
		buffer.append(this.getCastedModel().getCategoryName());
		buffer.append(" ]");
		return buffer.toString();
	}

	@Override
	protected AbstractRender selectRenderer () {
		return new AssetCategoryRender(this, _activity);
	}
}

//- CLASS IMPLEMENTATION ...................................................................................
final class AssetCategoryRender extends BaseExpandableRender {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - L A Y O U T   F I E L D S

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AssetCategoryRender (final AssetCategoryPart target, final Activity context) {
		super(target, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public AssetCategoryPart getPart () {
		return (AssetCategoryPart) super.getPart();
	}

	@Override
	public void updateContent () {
		logger.info(">> [AssetCategoryRender.updateContent]");
		super.updateContent();
		nodeIcon.setImageDrawable(getDrawable(R.drawable.nopilot));
		name.setText(this.getPart().getName());
		childCount.setText(this.getPart().getTContentCount());

		// Set this variant labels
		classLabel.setText("CATEGORY");
		categoryLabel.setText("LOCATIONS");
		_convertView.setBackground(getContext().getResources().getDrawable(R.drawable.panelborderred));
		logger.info("<< [AssetCategoryRender.updateContent]");
	}
}

// - UNUSED CODE ............................................................................................
