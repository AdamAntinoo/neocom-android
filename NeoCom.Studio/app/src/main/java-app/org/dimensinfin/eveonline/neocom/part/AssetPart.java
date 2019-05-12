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

import com.squareup.picasso.Picasso;

import org.dimensinfin.android.mvc.core.ANodeBase4ExpandRender;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.core.util.Chrono;
import org.dimensinfin.eveonline.neocom.R;
//import org.dimensinfin.eveonline.neocom.database.entity.NeoComAsset;
//import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.AndroidGlobalDataManager;
//import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.entities.NeoComAsset;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.render.NeoComRender;

import java.util.Comparator;
import java.util.concurrent.ExecutionException;

// - CLASS IMPLEMENTATION ...................................................................................
public class AssetPart extends NeoComAbstractPart {
//	public static Comparator<IPart> AssetNameComparator = new Comparator<IPart>() {
//		public int compare( final IPart left, final IPart right ) {
//			String leftField = null;
//			String rightField = null;
//			if (left instanceof INamedPart) {
//				leftField = ((INamedPart) left).getName();
//			}
//			if (right instanceof INamedPart) {
//				rightField = ((INamedPart) right).getName();
//			}
//			if (null == leftField) return 1;
//			if (null == rightField) return -1;
//			if ("" == leftField) return 1;
//			if ("" == rightField) return -1;
//			return leftField.compareTo(rightField);
//		}
//	};

	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = 8910031270706432316L;

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AssetPart( final NeoComAsset node ) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	public void onClick( final View target ) {
		Log.i("EVEI", ">> AssetPart.onClick");
		// REFACTOR Call to ItemDetails Activity disabled because that activity was not supported.
		//		Intent intent = new Intent(this.getActivity(), ItemDetailsActivity.class);
		//		intent.putExtra(AppWideConstants.extras.EXTRA_EVECHARACTERID, this.getPilot().getCharacterID());
		//		intent.putExtra(AppWideConstants.extras.EXTRA_EVEITEMID, this.getCastedModel().getTypeID());
		//		this.getActivity().startActivity(intent);
		Log.i("EVEI", "<< AssetPart.onClick");
	}

	// --- M O D E L   D A T A   T R A N S F O R M A T I O N
	public String getTAssetName() {
		return this.getCastedModel().getName();
	}

	public String getTItemCategory() {
		final String assetIdentifier = this.getCastedModel().getGroupName() + "/" + this.getCastedModel().getCategoryName();
		return assetIdentifier;
	}

	public String getTItemPrice() {
		try {
			return generatePriceString(getCastedModel().getItem().getHighestBuyerPrice().getPrice(), false, true);
		} catch (ExecutionException ee) {
			return "- ISK";
		} catch (InterruptedException ie) {
			return "- ISK";
		}
	}
	public String getTStackValue() {
			try {
				return generatePriceString(getCastedModel().getItem().getHighestBuyerPrice().getPrice() * getCastedModel().getQuantity()
						, true, true);
			} catch (ExecutionException ee) {
				return "- ISK";
			} catch (InterruptedException ie) {
				return "- ISK";
			}
	}
	public String getTQuantity() {
		return qtyFormatter.format(getCastedModel().getQuantity());
	}

//	/**
//	 * This method does two things. It will get the current price (even obsolete or inexact) from the Item
//	 * database and at the same time fire an update of the item information (mainly on prices and locations) to
//	 * the background manager.
//	 */
//	@Deprecated
//	public Spanned getTAssetInformation() {
//		StringBuffer assetInfo = new StringBuffer();
//		assetInfo.append("<font color='#FFFFFF'>").append("id: ").append("</font>");
//		assetInfo.append("<font color='#A0A0A0'>").append(getCastedModel().getAssetId()).append("</font>").append
//				(FLOW_ARROW_RIGHT);
//		assetInfo.append("<font color='#FFFFFF'>").append("Cat.: ").append("</font>");
//		assetInfo.append("<font color='#0000FF'>").append(getCastedModel().getCategory()).append("</font>").append(FLOW_ARROW_RIGHT);
//		assetInfo.append("<font color='#FFFFFF'>").append("Type: ").append("</font>");
//		assetInfo.append("<font color='#F2540C'>").append(getCastedModel().getItem().getTypeID()).append("</font>");
//		return Html.fromHtml(assetInfo.toString());
//	}
	//[01]

	//	public long getAssetID() {
	//		return this.getCastedModel().getAssetId();
	//	}

	//	public String getTItemPrice () {
	//		return this.generatePriceString(this.getBuyerPrice(), false, true);
	//	}
	//	@Deprecated
	//	public String getTStackValue () {
	//		long quantity = this.getCastedModel().getQuantity();
	//		//		double price = searchMarketData(getCastedModel().getTypeID()).getBestMarket().getPrice();
	//		return this.generatePriceString(this.getBuyerPrice() * quantity, true, true);
	//	}
	//
	//	@Deprecated
	//	public String getTCount () {
	//		long quantity = this.getCastedModel().getQuantity();
	//		DecimalFormat formatter = new DecimalFormat("###,###");
	//		String qtyString = formatter.format(quantity);
	//		return qtyString;
	//	}


	//	@Deprecated
	//	public Spanned get_assetLocation() {
	//		return this.colorFormatLocation(this.getCastedModel().getLocation());
	//	}


	//	public int get_assetTypeID () {
	//		return this.getCastedModel().getTypeID();
	//	}


	//	@Override
	//	public long getModelID () {
	//		return this.getCastedModel().getDAOID();
	//	}

	//	@Deprecated
	//	public String getName () {
	//		return this.getCastedModel().getName();
	//	}


	//	protected void checkAssetStacking (final AssetPart apart) {
	//		// Locate the stack if exists.
	//		HashMap<Integer, AssetPart> container = stackList.get(this.getCastedModel().getDAOID());
	//		int type = apart.getCastedModel().getTypeID();
	//		if ( null != container ) {
	//			AssetPart stack = container.get(type);
	//			if ( null != stack ) {
	//				//	if (stack instanceof AssetPart) {
	//				int count = stack.getCastedModel().getQuantity();
	//				stack.getCastedModel().setQuantity(count + 1);
	//				return;
	//				//	}
	//				// Do nothing because we do not know how to add to Assets.
	//			} else {
	//				// Add a new stack for this type to the current container.
	//				container.put(type, apart);
	//				this.addChild(apart);
	//			}
	//		} else {
	//			// There is no container also with this stack.
	//			container = new HashMap<Integer, AssetPart>();
	//			container.put(type, apart);
	//			this.addChild(apart);
	//			stackList.put(this.getCastedModel().getDAOID(), container);
	//		}
	//	}

	//	@Deprecated
	//	protected void initialize () {
	//		item = this.getCastedModel().getItem();
	//	}


	//	private MarketDataSet searchMarketData(final int itemid) {
	//		if (null == mdata)
	//			mdata = NeoComAppConnector.getSingleton().getDBConnector().searchMarketData(getCastedModel().getItem().getItemID(),
	//					ModelWideConstants.marketSide.BUYER);
	//		return mdata;
	//	}
	// --- I A N D R O I D P A R T   I N T E R F A C E

	/**
	 * Returns a numeric identifier for this part model item that should be unique from all other system wide
	 * parts to allow for easy management of the corresponding parts and views. The asset part return the unique asset
	 * identifier that is unique for each asset on the game, as most of the game data instances.
	 * @return <code>long</code> identifier with the model number.
	 */
	public long getModelId() {
		return this.getCastedModel().getAssetId();
	}

	@Override
	public AbstractRender selectRenderer() {
		return new AssetRender(this, _activity);
	}

	// --- N E O C O M   P A R T   R E Q U I R E M E N T S
	public NeoComAsset getCastedModel() {
		return (NeoComAsset) this.getModel();
	}
}

// - CLASS IMPLEMENTATION ...................................................................................
final class AssetRender extends ANodeBase4ExpandRender {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger			logger					= Logger.getLogger("AssetRender");

	// - F I E L D - S E C T I O N ............................................................................
//	private ImageView targetIcon = null;

	protected TextView assetName = null;
	protected TextView typeId = null;
	protected TextView assetCategory = null;
	protected TextView itemPrice = null;
	protected TextView stackValue = null;
	protected TextView nodeClass = null;
	protected TextView quantity = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AssetRender( final AssetPart target, final Activity context ) {
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
//		targetIcon = (ImageView) _convertView.findViewById(R.id.targetIcon);

		this.assetName = (TextView) this._convertView.findViewById(R.id.assetName);
		this.typeId = (TextView) this._convertView.findViewById(R.id.typeId);
		this.assetCategory = (TextView) this._convertView.findViewById(R.id.assetCategory);
		this.itemPrice = (TextView) this._convertView.findViewById(R.id.itemPrice);
		this.stackValue = (TextView) this._convertView.findViewById(R.id.stackValue);
		this.nodeClass = (TextView) this._convertView.findViewById(R.id.nodeClass);
		this.quantity = (TextView) this._convertView.findViewById(R.id.quantity);
	}

	@Override
	public void updateContent() {
		super.updateContent();
		Chrono chrono = new Chrono();
		this.assetName.setText(getPart().getTAssetName());
		this.typeId.setText("[#" + this.getPart().getCastedModel().getTypeId() + "]");
		this.assetCategory.setText(getPart().getTItemCategory());
		this.itemPrice.setText(getPart().getTItemPrice());
				this.stackValue.setText(getPart().getTStackValue());
				this.quantity.setText(getPart().getTQuantity());
//		String link = AndroidGlobalDataManager.getURLForItem(this.getPart().getCastedModel().getTypeId());
//		Picasso.get().load(link).into(targetIcon);

		// If asset inside a container or ship then set a white margin at the left.
//		if (getPart().getCastedModel().hasParent()) {
//			final ImageView image = (ImageView) this._convertView.findViewById(R.id.nodeIcon);
//			final RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(image.getLayoutParams());
//			lp.setMargins(15, 0, 0, 0);
//			image.setLayoutParams(lp);
//		}

//		loadEveIcon((ImageView) this._convertView.findViewById(R.id.nodeIcon), getPart().getCastedModel().getTypeId());
		logger.info("-- [AssetRender.updateContent]> Rendering time: " + chrono.printElapsed(Chrono.ChronoOptions.SHOWMILLIS));
	}

	@Override
	public int accessLayoutReference() {
		return R.layout.asset4miningledger;
	}
}

final class AssetGestureListener extends GestureDetector.SimpleOnGestureListener {
	@Override
	public boolean onDown( final MotionEvent event ) {
		return true;
	}
}

// - UNUSED CODE ............................................................................................
