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

// - IMPORT SECTION .........................................................................................

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.model.NeoComSimpleAsset;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.render.NeoComRender;

import java.text.DecimalFormat;

// - CLASS IMPLEMENTATION ...................................................................................
public class NeoComSimpleAssetPart extends NeoComAbstractPart implements INamedPart/*, OnClickListener*/ {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long																	serialVersionUID	= 8910031270706432316L;
//	private static Logger																			logger						= Logger.getLogger("AssetPart");

	// - F I E L D - S E C T I O N ............................................................................
	@SuppressWarnings("rawtypes")
//	private final HashMap<Long, HashMap<Integer, AssetPart>>	stackList					= new HashMap();

	//	private MarketDataSet																			mdata							= null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeoComSimpleAssetPart(final NeoComSimpleAsset node) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public String getTAssetCategory() {
		final String assetIdentifier = this.getCastedModel().getAssetID() + "-" + this.getCastedModel().getCategoryName();
		return assetIdentifier;
	}

	public String getTAssetLocation() {
		return Long.valueOf(getCastedModel().getLocationID()).toString();
//		return this.colorFormatLocation(this.getCastedModel().getLocation());
	}

	public String getTAssetName() {
		return this.getCastedModel().getName();
	}

//	public int getTAssetTypeID() {
//		return this.getCastedModel().getTypeID();
//	}

	public String getTItemCount() {
		long quantity = this.getCastedModel().getQuantity();
		DecimalFormat formatter = new DecimalFormat("###,###");
		String qtyString = formatter.format(quantity);
		return qtyString;
	}

	/**
	 * This method does two things. It will get the current price (even obsolete or inexact) from the Item
	 * database and at the same time fire an update of the item information (mainly on prices and locations) to
	 * the background manager.
	 *
	 * @return
	 */
	public String getTItemPrice() {
		return this.generatePriceString(this.getCastedModel().getPrice(), false, true);
	}

	public String getTStackValue() {
		long quantity = this.getCastedModel().getQuantity();
		//		double price = searchMarketData(getCastedModel().getTypeID()).getBestMarket().getPrice();
		return this.generatePriceString(this.getCastedModel().getPrice() * quantity, true, true);
	}

	public long getAssetID() {
		return this.getCastedModel().getAssetID();
	}

	public NeoComSimpleAsset getCastedModel() {
		return (NeoComSimpleAsset) this.getModel();
	}

	@Override
	public long getModelID() {
		return this.getCastedModel().getAssetID();
	}

	public String getName() {
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

//	protected void checkAssetStacking(final AssetPart apart) {
//		// Locate the stack if exists.
//		HashMap<Integer, AssetPart> container = stackList.get(this.getCastedModel().getDAOID());
//		int type = apart.getCastedModel().getTypeID();
//		if (null != container) {
//			AssetPart stack = container.get(type);
//			if (null != stack) {
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

//	@Override
//	protected void initialize() {
//		item = this.getCastedModel().getItem();
//	}

	@Override
	protected AbstractRender selectRenderer() {
		// Get the proper holder from the render mode.
//		if (this.getRenderMode() == AppWideConstants.fragment.FRAGMENT_ASSETSBYLOCATION)
//			return new AssetHolder(this, _activity);
//		if (this.getRenderMode() == AppWideConstants.rendermodes.RENDER_LOCATIONMODE)
//			return new AssetLineRender(this, _activity);
//		if (this.getRenderMode() == AppWideConstants.fragment.FRAGMENT_ITEMMODULESTACKS)
//			return new Asset4CategoryHolder(this, _activity);
		return new Asset4CategoryRender(this, _activity);
	}

	//	private MarketDataSet searchMarketData(final int itemid) {
	//		if (null == mdata)
	//			mdata = NeoComAppConnector.getSingleton().getDBConnector().searchMarketData(getCastedModel().getItem().getItemID(),
	//					ModelWideConstants.marketSide.BUYER);
	//		return mdata;
	//	}
}
// - CLASS IMPLEMENTATION ...................................................................................
final class Asset4CategoryRender extends NeoComRender {
	// - S T A T I C - S E C T I O N ..........................................................................
//	private static Logger logger				= LoggerFactory.getLogger(Asset4CategoryRender.class);

	// - F I E L D - S E C T I O N ............................................................................
	public TextView						assetName				= null;
	public TextView						assetLocation				= null;
	public TextView						assetCategory		= null;
	public TextView						itemPrice				= null;
	public TextView						stackValue			= null;
	public TextView						count						= null;

	public TextView						countLabel			= null;
	public TextView locationHtml	= null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public Asset4CategoryRender(final NeoComSimpleAssetPart target, final Activity context) {
		super(target, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public NeoComSimpleAssetPart getPart() {
		return (NeoComSimpleAssetPart) super.getPart();
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		this.assetName = (TextView) this._convertView.findViewById(R.id.assetName);
		this.assetLocation = (TextView) this._convertView.findViewById(R.id.assetLocation);
		this.assetCategory = (TextView) this._convertView.findViewById(R.id.assetCategory);
		this.itemPrice = (TextView) this._convertView.findViewById(R.id.itemPrice);
		this.stackValue = (TextView) this._convertView.findViewById(R.id.stackValue);
		this.count = (TextView) this._convertView.findViewById(R.id.count);
	}

	@Override
	public void updateContent() {
		super.updateContent();
		this.assetName.setText(getPart().getTAssetName());
		assetLocation.setText(getPart().getTAssetLocation());
		this.assetCategory.setText(getPart().getTAssetCategory());
		this.itemPrice.setText(getPart().getTItemPrice());
		this.stackValue.setText(getPart().getTStackValue());
		this.count.setText(getPart().getTItemCount());
		loadEveIcon((ImageView) this._convertView.findViewById(R.id.nodeIcon), getPart().getCastedModel().getTypeID());
	}

	@Override
	protected void createView() {
		_convertView = inflateView(R.layout.asset4category);
		_convertView.setTag(this);
	}
}

// - UNUSED CODE ............................................................................................
