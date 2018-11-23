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
import android.support.annotation.WorkerThread;
import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dimensinfin.android.mvc.core.AbstractPart;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.interfaces.ICredentialActivity;
import org.dimensinfin.eveonline.neocom.market.MarketDataEntry;
import org.dimensinfin.eveonline.neocom.market.MarketDataSet;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.model.RefiningDetailReport;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;

import java.util.concurrent.ExecutionException;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class RefiningDetailPart extends NeoComAbstractPart {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger = LoggerFactory.getLogger("RefiningDetailPart");

	// - F I E L D - S E C T I O N ............................................................................
	private Credential credential = null;
	private EveItem resource = null;
	private int jumps = -1;
	private MarketDataEntry closestHub = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public RefiningDetailPart( final RefiningDetailReport node ) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public RefiningDetailReport getCastedModel() {
		return (RefiningDetailReport) this.getModel();
	}

	@WorkerThread
	@Override
	public boolean runDependencies() {
		super.runDependencies();
		AndroidGlobalDataManager.submitJob2ui(() -> {
			try {
				credential = ((ICredentialActivity) this.getActivity()).getCredential();
				resource = new AndroidGlobalDataManager().searchItem4Id(this.getCastedModel().getTypeId());
				// Get the buyer market data for the input resource and search for the closest hub market data.
				final MarketDataSet data = resource.getBuyerMarketData();
				int minJumps = 999;
				//			MarketDataEntry closestHub = null;
				for (MarketDataEntry entry : data.getDataOnMarketHub()) {
					// Get the jump distance from the hub and the resource location.
					final int hubSystemId = entry.getLocation().getSystemId();
//					int jumps = AndroidGlobalDataManager.calculateRouteJumps(credential,
//							this.getCastedModel().getResourceLocation(),
//							hubSystemId
//					);
					// TODO set jumpt to a single value because the resource has not location.
					int jumps = 1;
					if ( jumps < minJumps ) {
						minJumps = jumps;
						closestHub = entry;
					}
				}
				this.jumps = minJumps;
			} catch ( ExecutionException ee ) {
				ee.printStackTrace();
			} catch ( InterruptedException ie ) {
				ie.printStackTrace();
			}
		});
		return true;
	}

	// --- M O D E L   D A T A   T R A N S F O R M A T I O N
	public Spanned getTClosestMarketLocation() {
		//		try {
		//			// Get the buyer market data for the input resource and search for the closest hub market data.
		//			final MarketDataSet data = resource.getBuyerMarketData();
		//			int minJumps = 999;
		//			MarketDataEntry closestHub = null;
		//			for (MarketDataEntry entry : data.getDataOnMarketHub()) {
		//				// Get the jump distance from the hub and the resource location.
		//				final int hubSystemId = entry.getSystemId();
		//				int jumps = AndroidGlobalDataManager.calculateRouteJumps(credential,
		//						this.getCastedModel().getResourceLocation(),
		//						hubSystemId
		//				);
		//				if ( jumps < minJumps ) {
		//					minJumps = jumps;
		//					closestHub = entry;
		//				}
		//			}
		// Return the market data.
		StringBuffer htmlLocation = new StringBuffer();
		if ( null != closestHub ) {
			//				try {
			double security = Double.valueOf(closestHub.getSecurity());
			htmlLocation.append(this.generateSecurityColor(security, NeoComAbstractPart.securityFormatter.format(security)))
					.append("[").append(closestHub.getPrice()).append(" ISK] ")
					.append(" ").append(closestHub.getRegion())
					.append(" - ").append(closestHub.getLocation().getSystem())
					.append(" (").append(this.jumps).append(" jumps)");
			//				} catch (ExecutionException e) {
			//					return Html.fromHtml("0.9 The Forge - Jita");
			//				} catch (InterruptedException e) {
			//					return Html.fromHtml("0.9 The Forge - Jita");
			//				}
			return Html.fromHtml(htmlLocation.toString());
		} else return Html.fromHtml("-");
		//		} catch (ExecutionException ee) {
		//			ee.printStackTrace();
		//			return Html.fromHtml("-");
		//		} catch (InterruptedException ie) {
		//			ie.printStackTrace();
		//			return Html.fromHtml("-");
		//		}
	}

	public Spanned getTBestMarketLocation() {
		// Get the market information form the best buyer market data records.
		StringBuffer htmlLocation = new StringBuffer();
		try {
			double security = Double.valueOf(this.getCastedModel().getHighestBuyerPrice().getSecurity());
			htmlLocation.append(this.generateSecurityColor(security, NeoComAbstractPart.securityFormatter.format(security)));
			htmlLocation.append(" ").append(this.getCastedModel().getHighestBuyerPrice().getRegion());
			htmlLocation.append(" - ").append(this.getCastedModel().getHighestBuyerPrice().getLocation().getSystem());
		} catch ( ExecutionException e ) {
			return Html.fromHtml("0.9 The Forge - Jita");
		} catch ( InterruptedException e ) {
			return Html.fromHtml("0.9 The Forge - Jita");
		}
		return Html.fromHtml(htmlLocation.toString());
	}

	public String getTMarketPrice() {
		try {
			return NeoComAbstractPart.generatePriceString(this.getCastedModel().getHighestBuyerPrice().getPrice(), false, true);
		} catch ( ExecutionException e ) {
			return "- ISK";
		} catch ( InterruptedException e ) {
			return "- ISK";
		}
	}

//	public String getRefiningFacilityName() {
//		return this.getCastedModel().getClosestRefiningLocation().getName();
//	}
//
//	public String getTRefiningSystem() {
//		return this.getCastedModel().getClosestRefiningLocation().getName();
//	}
//
//	public String getTRefiningStation() {
//		return this.getCastedModel().getClosestRefiningLocation().getStation();
//	}

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
		if ( null != this.getCastedModel() ) buffer.append("model: ").append(this.getCastedModel().toString()).append(" ");
		buffer.append("]");
		//		buffer.append("->").append(super.toString());
		return buffer.toString();
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	public static class RefiningDetailRender extends AbstractRender {
		// - F I E L D - S E C T I O N ............................................................................
		// - U I   F I E L D S
		private ImageView inputResourceIconA = null;
		private ImageView inputResourceIconB = null;
		private TextView inputClosestMarketSystem = null;
		private TextView inputBestMarketSystem = null;

		private TextView refiningSystemName = null;
		private TextView refiningStationName = null;

		//		private TextView inputResourceMarketSystem = null;
		private TextView inputResourceName = null;
		private TextView inputResourceMarketPrice = null;
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
			inputClosestMarketSystem = (TextView) _convertView.findViewById(R.id.inputClosestMarketSystem);
			inputBestMarketSystem = (TextView) _convertView.findViewById(R.id.inputBestMarketSystem);

			refiningSystemName = (TextView) _convertView.findViewById(R.id.refiningSystemName);
			refiningStationName = (TextView) _convertView.findViewById(R.id.refiningStationName);

			//			inputResourceMarketSystem = (TextView) _convertView.findViewById(R.id.inputResourceMarketSystem);
			inputResourceName = (TextView) _convertView.findViewById(R.id.inputResourceName);
			inputResourceMarketPrice = (TextView) _convertView.findViewById(R.id.inputResourceMarketPrice);
			refiningRate = (TextView) _convertView.findViewById(R.id.refiningRatio);
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
			// Set the icon for the input resource.
			String link = AndroidGlobalDataManager.getURLForItem(this.getPart().getCastedModel().getTypeId());
			Picasso.get().load(link).into(inputResourceIconA);
			Picasso.get().load(link).into(inputResourceIconB);

			// Set the data for the input market sources.
			inputClosestMarketSystem.setText(this.getPart().getTClosestMarketLocation());
			inputBestMarketSystem.setText(this.getPart().getTBestMarketLocation());
//			refiningSystemName.setText(this.getPart().getTRefiningSystem());
//			refiningStationName.setText(this.getPart().getTRefiningStation());


			//			inputClosestMarketSystem.setText("-");
			//			inputClosestMarketSystem.invalidate();

			inputResourceName.setText(this.getPart().getCastedModel().getName());
			inputResourceMarketPrice.setText(this.getPart().getTMarketPrice());
//			refiningStationName.setText(this.getPart().getRefiningFacilityName());
//			refiningRate.setText(NeoComAbstractPart.qtyFormatter.format(this.getPart().getCastedModel().getRefiningRatio()));
			refiningQuantity.setText("100");
			refiningPacks.setText("1");
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
