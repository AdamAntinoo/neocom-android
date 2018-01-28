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
package org.dimensinfin.eveonline.neocom.part;

import android.app.Activity;
import android.widget.TextView;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.model.Ship;
import org.dimensinfin.eveonline.neocom.part.core.NeoComExpandablePart;
import org.dimensinfin.eveonline.neocom.render.NeoComRender;

// - CLASS IMPLEMENTATION ...................................................................................
public class AssetGroupPart extends NeoComExpandablePart {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = -7696249781992547826L;

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AssetGroupPart(final Ship.ShipAssetGroup node) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public Ship.ShipAssetGroup getCastedModel() {
		return (Ship.ShipAssetGroup) this.getModel();
	}
	public long getModelID() {
		return this.getCastedModel().getTitle().hashCode();
	}
	public String getTContentCount() {
		return itemCountFormatter.format(getCastedModel().getContentSize());
	}

//	public String get_itemCount() {
////		int qty = 0;
////		for (IPart node : this.getChildren())
////			if (node instanceof AssetPart) {
////				qty += ((AssetPart) node).getCastedModel().getQuantity();
////			}
//		return itemCountFormatter.format(getCastedModel().getContentSize());
//	}
//
////	public String get_name() {
////		return this.getCastedModel().getName();
////	}
//
//	public String get_sellValue() {
//		double value = 0.0;
//		for (IPart node : this.getChildren())
//			if (node instanceof AssetPart) {
//				NeoComAsset ass = ((AssetPart) node).getCastedModel();
//				long count = ass.getQuantity();
//				double price = ass.getItem().getHighestBuyerPrice().getPrice();
//				value += count * price;
//			}
//		return this.generatePriceString(value, true, false);
//	}
//
//	public String get_volume() {
//		double volume = 0.0;
//		for (IPart node : this.getChildren())
//			if (node instanceof AssetPart) {
//				NeoComAsset ass = ((AssetPart) node).getCastedModel();
//				long count = ass.getQuantity();
//				double vol = ass.getItem().getVolume();
//				volume += count * vol;
//			}
//		return NeoComAbstractPart.itemCountFormatter.format(volume);
//	}
//	public String getName() {
//		return this.getCastedModel().getName();
//	}

//	public void onClick(final View view) {
//		// Toggle location to show its contents.
//		this.toggleExpanded();
//		this.fireStructureChange(SystemWideConstants.events.EVENTSTRUCTURE_ACTIONEXPANDCOLLAPSE.name(), this, this);
//	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("AssetGroupPart [");
		buffer.append(this.getCastedModel().toString());
		buffer.append(" ]");
		return buffer.toString();
	}

	@Override
	protected AbstractRender selectRenderer() {
		// Get the proper holder from the render mode.
		return new AssetGroupRender(this, _activity);
	}
	// - CLASS IMPLEMENTATION ...................................................................................
	public static class AssetGroupRender extends NeoComRender {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................

		// - L A Y O U T   F I E L D S
		public TextView groupName		= null;
		public TextView	childCount	= null;
		public TextView	itemCount		= null;
		public TextView	volume			= null;
		public TextView	sellValue		= null;

		// - L A Y O U T   L A B E L S
		public TextView	iskLabel		= null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public AssetGroupRender(final AssetGroupPart target, final Activity context) {
			super(target, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public AssetGroupPart getPart() {
			return (AssetGroupPart) super.getPart();
		}

		@Override
		public void initializeViews() {
			super.initializeViews();
			groupName = (TextView) _convertView.findViewById(R.id.groupName);
			childCount = (TextView) _convertView.findViewById(R.id.childCount);
			itemCount = (TextView) _convertView.findViewById(R.id.itemCount);
			volume = (TextView) _convertView.findViewById(R.id.volume);
			sellValue = (TextView) _convertView.findViewById(R.id.sellValue);

			iskLabel = (TextView) _convertView.findViewById(R.id.iskLabel);
		}

		public void updateContent() {
			super.updateContent();
			groupName.setText(getPart().getCastedModel().getTitle());
			childCount.setText(getPart().getTContentCount());
//			itemCount.setText(getPart().get_itemCount());
//			volume.setText(getPart().get_volume());
//			sellValue.setText(getPart().get_sellValue());
//			loadEveIcon((ImageView) _convertView.findViewById(R.id.nodeIcon), getPart().getCastedModel().getTypeID());
		}

		@Override
		protected void createView () {
			_convertView = inflateView(R.layout.assetgroup4listbylocation);
			_convertView.setTag(this);
		}
	}
}

// - UNUSED CODE ............................................................................................
