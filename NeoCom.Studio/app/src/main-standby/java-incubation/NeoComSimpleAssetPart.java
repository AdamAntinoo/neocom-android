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

// - IMPORT SECTION .........................................................................................

import android.app.Activity;
import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.TextView;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.model.NeoComSimpleAsset;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.render.NeoComRender;

import java.text.DecimalFormat;

// - CLASS IMPLEMENTATION ...................................................................................
public class NeoComSimpleAssetPart extends NeoComAbstractPart implements INamedPart/*, OnClickListener*/ {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = 8910031270706432316L;
	//	private static Logger																			logger						= Logger.getLogger("AssetPart");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeoComSimpleAssetPart (final NeoComSimpleAsset node) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public String getTAssetCategory () {
		final String assetIdentifier = this.getCastedModel().getAssetID() + "-" + this.getCastedModel().getCategoryName();
		return assetIdentifier;
	}

	public String getTAssetLocation () {
		return Long.valueOf(getCastedModel().getLocationID()).toString();
	}

	public String getTAssetName () {
		return this.getCastedModel().getName();
	}

	public Spanned getTAssetInformation () {
		StringBuffer assetInfo = new StringBuffer();
		assetInfo.append("<font color='#FFFFFF'>").append("id: ").append("</font>");
		assetInfo.append("<font color='#A0A0A0'>").append(getCastedModel().getAssetID()).append("</font>").append(AppWideConstants.FLOW_ARROW_RIGHT);
		assetInfo.append("<font color='#FFFFFF'>").append("Cat.: ").append("</font>");
		assetInfo.append("<font color='#0000FF'>").append(getCastedModel().getCategoryName()).append("</font>").append(AppWideConstants.FLOW_ARROW_RIGHT);
		assetInfo.append("<font color='#FFFFFF'>").append("Type: ").append("</font>");
		assetInfo.append("<font color='#F2540C'>").append(getCastedModel().getItem().getTypeID()).append("</font>");
		return Html.fromHtml(assetInfo.toString());
	}

	public String getTItemCount () {
		long quantity = this.getCastedModel().getQuantity();
		DecimalFormat formatter = new DecimalFormat("###,###");
		String qtyString = formatter.format(quantity);
		return qtyString;
	}

	/**
	 * This method does two things. It will get the current price (even obsolete or inexact) from the Item
	 * database and at the same time fire an update of the item information (mainly on prices and locations) to
	 * the background manager.
	 */
	public String getTItemPrice () {
		return this.generatePriceString(this.getCastedModel().getPrice(), false, true);
	}

	public String getTStackValue () {
		long quantity = this.getCastedModel().getQuantity();
		//		double price = searchMarketData(getCastedModel().getTypeID()).getBestMarket().getPrice();
		return this.generatePriceString(this.getCastedModel().getPrice() * quantity, true, true);
	}

	public long getAssetID () {
		return this.getCastedModel().getAssetID();
	}

	public NeoComSimpleAsset getCastedModel () {
		return (NeoComSimpleAsset) this.getModel();
	}

	@Override
	public long getModelID () {
		return this.getCastedModel().getAssetID();
	}

	public String getName () {
		return this.getCastedModel().getName();
	}

	//	public void onClick(final View target) {
	//		Log.i("EVEI", ">> AssetPart.onClick");
	//		// REFACTOR Call to ItemDetails Activity disabled because that activity was not supported.
	//		//		Intent intent = new Intent(this.getActivity(), ItemDetailsActivity.class);
	//		//		intent.putExtra(AppWideConstants.extras.EXTRA_EVECHARACTERID, this.getPilot().getCharacterID());
	//		//		intent.putExtra(AppWideConstants.extras.EXTRA_EVEITEMID, this.getCastedModel().getTypeID());
	//		//		this.getActivity().startActivity(intent);
	//		Log.i("EVEI", "<< AssetPart.onClick");
	//	}

	@Override
	protected AbstractRender selectRenderer () {
		return new Asset4CategoryRender(this, _activity);
	}
}

// - CLASS IMPLEMENTATION ...................................................................................
final class Asset4CategoryRender extends NeoComRender {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger				= LoggerFactory.getLogger(Asset4CategoryRender.class);

	// - F I E L D - S E C T I O N ............................................................................
	public TextView assetName = null;
//	public TextView assetLocation = null;
	public TextView assetCategory = null;
	public TextView itemPrice = null;
	public TextView stackValue = null;
	public TextView count = null;

	public TextView countLabel = null;
	public TextView locationHtml = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public Asset4CategoryRender (final NeoComSimpleAssetPart target, final Activity context) {
		super(target, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public NeoComSimpleAssetPart getPart () {
		return (NeoComSimpleAssetPart) super.getPart();
	}

	@Override
	public void initializeViews () {
		super.initializeViews();
		this.assetName = (TextView) this._convertView.findViewById(R.id.assetName);
//		this.assetLocation = (TextView) this._convertView.findViewById(R.id.assetLocation);
		this.assetCategory = (TextView) this._convertView.findViewById(R.id.assetCategory);
		this.itemPrice = (TextView) this._convertView.findViewById(R.id.itemPrice);
		this.stackValue = (TextView) this._convertView.findViewById(R.id.stackValue);
		this.count = (TextView) this._convertView.findViewById(R.id.count);
	}

	@Override
	public void updateContent () {
		super.updateContent();
		this.assetName.setText(getPart().getTAssetName());
//		assetLocation.setText(getPart().getTAssetLocation());
		this.assetCategory.setText(getPart().getTAssetInformation());
		this.itemPrice.setText(getPart().getTItemPrice());
		this.stackValue.setText(getPart().getTStackValue());
		this.count.setText(getPart().getTItemCount());
		loadEveIcon((ImageView) this._convertView.findViewById(R.id.nodeIcon), getPart().getCastedModel().getTypeID());
	}

	@Override
	protected void createView () {
		_convertView = inflateView(R.layout.asset4list);
		_convertView.setTag(this);
	}
}

// - UNUSED CODE ............................................................................................
