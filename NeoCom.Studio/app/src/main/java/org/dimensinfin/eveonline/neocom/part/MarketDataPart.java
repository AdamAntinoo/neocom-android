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

import android.text.Spanned;

import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.model.NeoComAsset;

// - CLASS IMPLEMENTATION ...................................................................................
public abstract class MarketDataPart extends NeoComAbstractPart {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long	serialVersionUID	= -5642783343846527150L;

	// - F I E L D - S E C T I O N ............................................................................
	protected EveItem					item							= null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public MarketDataPart(final NeoComAsset node) {
		super(node);
		this.initialize();
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public Spanned display_BuyerLocation() {
		return this.colorFormatLocation(this.getItem().getHighestBuyerPrice().getLocation());
	}

	public Spanned display_SellerLocation() {
		return this.colorFormatLocation(this.getItem().getLowestSellerPrice().getLocation());
	}

	public String get_highestBuyerPrice() {
		return this.generatePriceString(this.getItem().getHighestBuyerPrice().getPrice(), false, false);
	}

	public String get_lowestSellerPrice() {
		return this.generatePriceString(this.getItem().getLowestSellerPrice().getPrice(), false, false);
	}

	public double getBuyerPrice() {
		return this.getItem().getHighestBuyerPrice().getPrice();
	}

	public EveItem getItem() {
		if (null == item) throw new RuntimeException("RT> Use of a MarketDataPart without setting the the item.");
		return item;
	}

	public double getSellerPrice() {
		return this.getItem().getLowestSellerPrice().getPrice();
	}

	protected abstract void initialize();
}
// - UNUSED CODE ............................................................................................
