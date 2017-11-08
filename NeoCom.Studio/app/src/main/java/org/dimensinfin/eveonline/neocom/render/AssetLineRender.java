//	PROJECT:        EVEIndustrialist (EVEI)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2014 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API11.
//	DESCRIPTION:		Application helper for Eve Online Industrialists. Will help on Industry and Manufacture.

package org.dimensinfin.eveonline.neocom.render;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.eveonline.neocom.parts.AssetPart;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;

// - CLASS IMPLEMENTATION ...................................................................................
public class AssetLineRender extends AbstractRender {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - L A Y O U T   F I E L D S
	@Bind(R.id.assetIcon)
	private ImageView assetIcon = null;
	@Bind(R.id.assetName)
	private TextView assetName = null;
	@Bind(R.id.assetCategory)
	private TextView assetCategory = null;
	@Bind(R.id.itemPrice)
	private TextView itemPrice = null;
	@Bind(R.id.stackValue)
	private TextView stackValue = null;
	@Bind(R.id.count)
	public TextView count = null;

	// - L A Y O U T   L A B E L S
	@Bind(R.id.valueLabel)
	public TextView valueLabel = null;
	@Bind(R.id.countLabel)
	public TextView countLabel = null;


	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AssetLineRender (final AssetPart target, final Activity context) {
		super(target, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public AssetPart getPart () {
		return (AssetPart) super.getPart();
	}

	@Override
	public void initializeViews () {
		//	super.initializeViews();
		// Get the view of the rightArrow if found.
		_rightArrow = (ImageView) _convertView.findViewById(R.id.rightArrow);

		assetName = (TextView) _convertView.findViewById(R.id.assetName);
		assetCategory = (TextView) _convertView.findViewById(R.id.assetCategory);
		itemPrice = (TextView) _convertView.findViewById(R.id.itemPrice);
		stackValue = (TextView) _convertView.findViewById(R.id.stackValue);
	}

	public void updateContent () {
		super.updateContent();
		assetName.setText(getPart().getName());
		assetCategory.setText("-CATEGORY-");
		itemPrice.setText("123 ISK");
		stackValue.setText("0.00 ISK");
		count.setText(getPart().getCastedModel().getQuantity());
	}

	@Override
	protected void createView () {
		final LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		// The view is a new view. We have to fill all the items
		_convertView = mInflater.inflate(R.layout.asset4location, null);
		_convertView.setTag(this);
	}
}

// - UNUSED CODE ............................................................................................
