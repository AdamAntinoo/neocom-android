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
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.model.NeoComAsset;
import org.dimensinfin.eveonline.neocom.render.NeoComRender;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.logging.Logger;

// - CLASS IMPLEMENTATION ...................................................................................
public class AssetPart extends MarketDataPart implements INamedPart/*, OnClickListener*/ {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = 8910031270706432316L;
	private static Logger logger = Logger.getLogger("AssetPart");

	// - F I E L D - S E C T I O N ............................................................................
	@SuppressWarnings("rawtypes")
	private final HashMap<Long, HashMap<Integer, AssetPart>> stackList = new HashMap();

	//	private MarketDataSet																			mdata							= null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AssetPart (final NeoComAsset node) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public String getTAssetName () {
		return this.getCastedModel().getName();
	}

	public String getTAssetCategory () {
		final String assetIdentifier = this.getCastedModel().getAssetID() + "-" + this.getCastedModel().getCategory();
		return assetIdentifier;
	}

	public Spanned getTAssetInformation () {
		StringBuffer assetInfo = new StringBuffer();
		assetInfo.append("<font color='#FFFFFF'>").append("id: ").append("</font>");
		assetInfo.append("<font color='#A0A0A0'>").append(getCastedModel().getAssetID()).append("</font>").append(AppWideConstants.FLOW_ARROW_RIGHT);
		assetInfo.append("<font color='#FFFFFF'>").append("Cat.: ").append("</font>");
		assetInfo.append("<font color='#0000FF'>").append(getCastedModel().getCategory()).append("</font>").append(AppWideConstants.FLOW_ARROW_RIGHT);
		assetInfo.append("<font color='#FFFFFF'>").append("Type: ").append("</font>");
		assetInfo.append("<font color='#F2540C'>").append(getCastedModel().getItem().getTypeID()).append("</font>");
		return Html.fromHtml(assetInfo.toString());
	}

	/**
	 * This method does two things. It will get the current price (even obsolete or inexact) from the Item
	 * database and at the same time fire an update of the item information (mainly on prices and locations) to
	 * the background manager.
	 */
	public String getTItemPrice () {
		return this.generatePriceString(this.getBuyerPrice(), false, true);
	}

	public String getTStackValue () {
		long quantity = this.getCastedModel().getQuantity();
		//		double price = searchMarketData(getCastedModel().getTypeID()).getBestMarket().getPrice();
		return this.generatePriceString(this.getBuyerPrice() * quantity, true, true);
	}

	public String getTCount () {
		long quantity = this.getCastedModel().getQuantity();
		DecimalFormat formatter = new DecimalFormat("###,###");
		String qtyString = formatter.format(quantity);
		return qtyString;
	}


	public Spanned get_assetLocation () {
		return this.colorFormatLocation(this.getCastedModel().getLocation());
	}


	//	public int get_assetTypeID () {
	//		return this.getCastedModel().getTypeID();
	//	}


	public long getAssetID () {
		return this.getCastedModel().getAssetID();
	}

	public NeoComAsset getCastedModel () {
		return (NeoComAsset) this.getModel();
	}

	@Override
	public long getModelID () {
		return this.getCastedModel().getDAOID();
	}

	public String getName () {
		return this.getCastedModel().getName();
	}

	public void onClick (final View target) {
		Log.i("EVEI", ">> AssetPart.onClick");
		// REFACTOR Call to ItemDetails Activity disabled because that activity was not supported.
		//		Intent intent = new Intent(this.getActivity(), ItemDetailsActivity.class);
		//		intent.putExtra(AppWideConstants.extras.EXTRA_EVECHARACTERID, this.getPilot().getCharacterID());
		//		intent.putExtra(AppWideConstants.extras.EXTRA_EVEITEMID, this.getCastedModel().getTypeID());
		//		this.getActivity().startActivity(intent);
		Log.i("EVEI", "<< AssetPart.onClick");
	}

	protected void checkAssetStacking (final AssetPart apart) {
		// Locate the stack if exists.
		HashMap<Integer, AssetPart> container = stackList.get(this.getCastedModel().getDAOID());
		int type = apart.getCastedModel().getTypeID();
		if ( null != container ) {
			AssetPart stack = container.get(type);
			if ( null != stack ) {
				//	if (stack instanceof AssetPart) {
				int count = stack.getCastedModel().getQuantity();
				stack.getCastedModel().setQuantity(count + 1);
				return;
				//	}
				// Do nothing because we do not know how to add to Assets.
			} else {
				// Add a new stack for this type to the current container.
				container.put(type, apart);
				this.addChild(apart);
			}
		} else {
			// There is no container also with this stack.
			container = new HashMap<Integer, AssetPart>();
			container.put(type, apart);
			this.addChild(apart);
			stackList.put(this.getCastedModel().getDAOID(), container);
		}
	}

	@Override
	protected void initialize () {
		item = this.getCastedModel().getItem();
	}

	@Override
	protected AbstractRender selectRenderer () {
		//		// Get the proper holder from the render mode.
		//		if ( this.getRenderMode() == AppWideConstants.fragment.FRAGMENT_ASSETSBYLOCATION )
		//			return new AssetHolder(this, _activity);
		//		if ( this.getRenderMode() == AppWideConstants.rendermodes.RENDER_LOCATIONMODE )
		//			return new AssetLineRender(this, _activity);
		//		if ( this.getRenderMode() == AppWideConstants.fragment.FRAGMENT_ITEMMODULESTACKS )
		//			return new Asset4CategoryHolder(this, _activity);
		return new AssetHolder(this, _activity);
	}

	//	private MarketDataSet searchMarketData(final int itemid) {
	//		if (null == mdata)
	//			mdata = NeoComAppConnector.getSingleton().getDBConnector().searchMarketData(getCastedModel().getItem().getItemID(),
	//					ModelWideConstants.marketSide.BUYER);
	//		return mdata;
	//	}
}

// - CLASS IMPLEMENTATION ...................................................................................
final class AssetHolder extends NeoComRender {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger			logger					= Logger.getLogger("AssetHolder");

	// - F I E L D - S E C T I O N ............................................................................
	//	protected GestureDetector	_gestureScanner	= null;
	protected TextView assetName = null;
	protected TextView assetCategory = null;
	protected TextView itemPrice = null;
	protected TextView stackValue = null;
	protected TextView count = null;

	public TextView countLabel = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AssetHolder (final AssetPart target, final Activity context) {
		super(target, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public AssetPart getPart () {
		return (AssetPart) super.getPart();
	}

	@Override
	public void initializeViews () {
		super.initializeViews();
		this.assetName = (TextView) this._convertView.findViewById(R.id.assetName);
		this.assetCategory = (TextView) this._convertView.findViewById(R.id.assetCategory);
		this.itemPrice = (TextView) this._convertView.findViewById(R.id.itemPrice);
		this.stackValue = (TextView) this._convertView.findViewById(R.id.stackValue);
		this.count = (TextView) this._convertView.findViewById(R.id.count);

		//		this.assetName.setTypeface(getThemeTextFont());
		//		this.count.setTypeface(getThemeTextFont());
		//		this.itemPrice.setTypeface(getThemeTextFont());
		//		this.stackValue.setTypeface(getThemeTextFont());
	}

	@Override
	public void updateContent () {
		super.updateContent();
		this.assetName.setText(getPart().getTAssetName());
		this.assetCategory.setText(getPart().getTAssetInformation());
		this.itemPrice.setText(getPart().getTItemPrice());
		this.stackValue.setText(getPart().getTStackValue());
		this.count.setText(getPart().getTCount());

		// If asset inside a container or ship then set a white margin at the left.
		if ( getPart().getCastedModel().hasParent() ) {
			final ImageView image = (ImageView) this._convertView.findViewById(R.id.nodeIcon);
			final RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(image.getLayoutParams());
			lp.setMargins(15, 0, 0, 0);
			image.setLayoutParams(lp);
		}

		loadEveIcon((ImageView) this._convertView.findViewById(R.id.nodeIcon), getPart().getCastedModel().getTypeID());
	}

	@Override
	protected void createView () {
		_convertView = inflateView(R.layout.asset4list);
		_convertView.setTag(this);
	}
}

final class AssetGestureListener extends GestureDetector.SimpleOnGestureListener {
	@Override
	public boolean onDown (final MotionEvent event) {
		return true;
	}
}

// - UNUSED CODE ............................................................................................
