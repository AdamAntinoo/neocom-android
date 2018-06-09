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
import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dimensinfin.android.mvc.core.AbstractPart;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.model.RefiningDetailReport;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPartOld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class RefiningDetailPart extends NeoComAbstractPart {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("RefiningDetailPart");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public RefiningDetailPart( final RefiningDetailReport node ) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public RefiningDetailReport getCastedModel() {
		return (RefiningDetailReport) this.getModel();
	}

	// --- M O D E L   D A T A   T R A N S F O R M A T I O N
	public Spanned getTMarketLocation() {
		// Get the market information form the best buyer market data records.
		StringBuffer htmlLocation = new StringBuffer();
		try {
			double security = Double.valueOf(this.getCastedModel().getHighestBuyerPrice().getSecurity());
			htmlLocation.append(this.generateSecurityColor(security, NeoComAbstractPart.securityFormatter.format(security)));
			htmlLocation.append(" ").append(this.getCastedModel().getHighestBuyerPrice().getRegion());
			htmlLocation.append(" - ").append(this.getCastedModel().getHighestBuyerPrice().getSystem());
		} catch (ExecutionException e) {
			return Html.fromHtml("0.9 The Forge - Jita");
		} catch (InterruptedException e) {
			return Html.fromHtml("0.9 The Forge - Jita");
		}
		return Html.fromHtml(htmlLocation.toString());
	}
public String getTMarketPrice(){
	try {
		return NeoComAbstractPart.generatePriceString(this.getCastedModel().getHighestBuyerPrice().getPrice(),false,true);
	} catch (ExecutionException e) {
		return "- ISK";
	} catch (InterruptedException e) {
		return "- ISK";
	}
}
	// --- I A N D R O I D P A R T   I N T E R F A C E
	@Override
	public long getModelId() {
		return getCastedModel().getTypeId();
	}

	@Override
	public AbstractRender selectRenderer() {
		return new RefiningDetailRender(this, getActivity());
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("RefiningDetailPart [ ");
		buffer.append("name: ").append(0);
		buffer.append("]");
		buffer.append("->").append(super.toString());
		return buffer.toString();
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	public static class RefiningDetailRender extends AbstractRender {
		// - F I E L D - S E C T I O N ............................................................................
		private ImageView inputResourceIconA = null;
		private ImageView inputResourceIconB = null;

		private TextView inputResourceMarketSystem = null;
		private TextView inputResourceName = null;
		private TextView inputResourceMarketPrice = null;
		private TextView refiningStationName = null;
		private TextView refiningRate = null;
		private TextView refiningQuantity = null;
		private TextView refiningPacks = null;
		private TextView mineralPrice1 = null;
		private TextView mineralName1 = null;
		private TextView mineralPrice2 = null;
		private TextView mineralName2 = null;
		private TextView mineralPrice3 = null;
		private TextView mineralName3 = null;
		private TextView processingInputResourceName = null;
		private TextView processingInputValue = null;
		private TextView processingOutputValue = null;
		private TextView processingOutputResourceName = null;


		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public RefiningDetailRender( AbstractPart newPart, Activity context ) {
			super(newPart, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public RefiningDetailPart getPart() {
			return (RefiningDetailPart) super.getPart();
		}

		// --- A B S T R A C T R E N D E R   O V E R R I D E S
		@Override
		public void initializeViews() {
			inputResourceIconA = (ImageView) _convertView.findViewById(R.id.inputResourceIconA);
			inputResourceIconB = (ImageView) _convertView.findViewById(R.id.inputResourceIcon);

			inputResourceMarketSystem = (TextView) _convertView.findViewById(R.id.inputResourceMarketSystem);
			inputResourceName = (TextView) _convertView.findViewById(R.id.inputResourceName);
			inputResourceMarketPrice = (TextView) _convertView.findViewById(R.id.inputResourceMarketPrice);
			refiningStationName = (TextView) _convertView.findViewById(R.id.refiningStationName);
			refiningRate = (TextView) _convertView.findViewById(R.id.refiningRate);
			refiningQuantity = (TextView) _convertView.findViewById(R.id.refiningQuantity);
			refiningPacks = (TextView) _convertView.findViewById(R.id.refiningPacks);
			mineralPrice1 = (TextView) _convertView.findViewById(R.id.mineralPrice1);
			mineralName1 = (TextView) _convertView.findViewById(R.id.mineralName1);
			mineralPrice2 = (TextView) _convertView.findViewById(R.id.mineralPrice2);
			mineralName2 = (TextView) _convertView.findViewById(R.id.mineralName2);
			mineralPrice3 = (TextView) _convertView.findViewById(R.id.mineralPrice3);
			mineralName3 = (TextView) _convertView.findViewById(R.id.mineralName3);
			processingInputResourceName = (TextView) _convertView.findViewById(R.id.processingInputResourceName);
			processingInputValue = (TextView) _convertView.findViewById(R.id.processingInputValue);
			processingOutputValue = (TextView) _convertView.findViewById(R.id.processingOutputValue);
			processingOutputResourceName = (TextView) _convertView.findViewById(R.id.processingOutputResourceName);
		}

		@Override
		public void updateContent() {
			String link = AndroidGlobalDataManager.getURLForItem(this.getPart().getCastedModel().getTypeId());
			Picasso.get().load(link).into(inputResourceIconA);
			Picasso.get().load(link).into(inputResourceIconB);
			inputResourceMarketSystem.setText(this.getPart().getTMarketLocation());
			inputResourceName.setText(this.getPart().getCastedModel().getName());
			inputResourceMarketPrice.setText(this.getPart().getTMarketPrice());
			refiningStationName.setText(this.getPart().getTMarketLocation());
			refiningRate.setText(this.getPart().getTMarketLocation());
			refiningQuantity.setText(this.getPart().getTMarketLocation());
			refiningPacks.setText(this.getPart().getTMarketLocation());
//			resourceMarketSystem.setText(this.getPart().getTMarketLocation());
//			resourceMarketSystem.setText(this.getPart().getTMarketLocation());
//			resourceMarketSystem.setText(this.getPart().getTMarketLocation());
//			resourceMarketSystem.setText(this.getPart().getTMarketLocation());
//			resourceMarketSystem.setText(this.getPart().getTMarketLocation());
//			resourceMarketSystem.setText(this.getPart().getTMarketLocation());
//			resourceMarketSystem.setText(this.getPart().getTMarketLocation());
//			resourceMarketSystem.setText(this.getPart().getTMarketLocation());
//			resourceMarketSystem.setText(this.getPart().getTMarketLocation());
		}

		@Override
		public int accessLayoutReference() {
			return R.layout.refiningdetail4header;
		}
	}
}
// - UNUSED CODE ............................................................................................
//[01]
