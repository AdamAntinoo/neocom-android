//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API22.
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
package org.dimensinfin.eveonline.neocom.render;

import android.app.Activity;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dimensinfin.android.mvc.core.ANodeBase4ExpandRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.part.AssetPart;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public abstract class AAssetIdentifierRender extends ANodeBase4ExpandRender {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger = LoggerFactory.getLogger("AAssetIdentifierRender");

	// - F I E L D - S E C T I O N ............................................................................
	protected TextView assetName = null;
	protected TextView typeId = null;
	protected TextView assetCategory = null;
	protected TextView assetIdentifier = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AAssetIdentifierRender( final AssetPart target, final Activity context ) {
		super(target, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public AssetPart getPart() {
		return (AssetPart) super.getPart();
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		assetName = (TextView) _convertView.findViewById(R.id.assetName);
		typeId = (TextView) _convertView.findViewById(R.id.typeId);
		assetCategory = (TextView) _convertView.findViewById(R.id.assetCategory);
		assetIdentifier = (TextView) _convertView.findViewById(R.id.assetIdentifier);
	}

	@Override
	public void updateContent() {
		super.updateContent();
		String link = AndroidGlobalDataManager.getURLForItem(this.getPart().getCastedModel().getTypeId());
		Picasso.get().load(link).into(nodeIcon);

		this.assetName.setText(this.getPart().getTAssetName());
		this.typeId.setText("[#" + this.getPart().getCastedModel().getTypeId() + "]");
		this.assetCategory.setText(this.getPart().getTItemCategory());
		this.assetIdentifier.setText("[" + this.getPart().getCastedModel().getAssetId() + "]");
	}
}
// - UNUSED CODE ............................................................................................
//[01]
