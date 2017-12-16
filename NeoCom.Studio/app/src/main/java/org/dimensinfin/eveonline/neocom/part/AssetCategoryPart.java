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
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.core.OneShotTask;
import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.core.constant.CoreConstants;
import org.dimensinfin.core.util.Chrono;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.core.PartComparatorFactory;
import org.dimensinfin.eveonline.neocom.model.AssetCategory;
import org.dimensinfin.eveonline.neocom.render.NeoComRender;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

// - CLASS IMPLEMENTATION ...................................................................................
public class AssetCategoryPart extends NeoComAbstractPart implements INamedPart, OnClickListener {
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

	public String getTransformedContentCount () {
		return itemCountFormatter.format(this.getCastedModel().getContentSize());
	}

	public void onClick (final View view) {
		logger.info(">> [AssetCategoryPart.onClick]");
		// Signal the action may take some time and launch it on the background.
		activateClick();
		// Force the update of the listview.
		invalidate();
		notifyDataSetChanged();
		// launch the actions on a new thread.
		new Handler().postDelayed(new OneShotTask<AssetCategoryPart>(this) {
			public void run () {
				logger.info(">> [AssetCategoryPart.onClick.OneShotTask.run]");
				Chrono chrono = new Chrono();
				getTargetPart().toggleExpanded();
				getTargetPart().invalidate();
				getTargetPart().fireStructureChange(SystemWideConstants.events.EVENTSTRUCTURE_ACTIONEXPANDCOLLAPSE.name(), this, this);
				completeClick();
				logger.info("<< [AssetCategoryPart.onClick.OneShotTask.run]> Time Elapsed: " + chrono.printElapsed(Chrono.ChonoOptions.SHOWMILLIS));
			}
		}, CoreConstants.HUNDRETH);
		logger.info("<< [AssetCategoryPart.onClick]");
	}

	/**
	 * Sort the Contents by their names.
	 */
	@Override
	public List<IPart> runPolicies (final List<IPart> targets) {
		// Order the characters by alphabetical name.
		Collections.sort(targets, PartComparatorFactory.createPartComparator(AppWideConstants.comparators.COMPARATOR_NAME));
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
final class AssetCategoryRender extends NeoComRender {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - L A Y O U T   F I E L D S
	private ImageView nodeIcon = null;
	public TextView name = null;
	public TextView childCount = null;

	// - L A Y O U T   L A B E L S
	public TextView titleLabel = null;
	public TextView locationLabel = null;

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
	public void initializeViews () {
		super.initializeViews();
		nodeIcon = (ImageView) _convertView.findViewById(R.id.nodeIcon);
		name = (TextView) _convertView.findViewById(R.id.name);
		childCount = (TextView) _convertView.findViewById(R.id.childCount);

		titleLabel = (TextView) _convertView.findViewById(R.id.titleLabel);
		locationLabel = (TextView) _convertView.findViewById(R.id.locationLabel);
	}

	@Override
	public void updateContent () {
		super.updateContent();
		name.setText(this.getPart().getName());
		childCount.setText(this.getPart().getTransformedContentCount());
		//		_convertView.invalidate();
		titleLabel.setText("CATEGORY");
		locationLabel.setText("ASSETS");
		nodeIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.defaulticonplaceholder,getContext().getTheme()));
	}

	@Override
	protected void createView () {
		_convertView = inflateView(R.layout.login4list);
		_convertView.setTag(this);
	}
}

// - UNUSED CODE ............................................................................................
