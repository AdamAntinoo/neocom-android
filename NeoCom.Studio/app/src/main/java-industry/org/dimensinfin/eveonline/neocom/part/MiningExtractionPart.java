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
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.eveonline.neocom.NeoComApplication;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.activity.RefiningDetailActivity;
import org.dimensinfin.eveonline.neocom.database.entity.MiningExtraction;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.interfaces.ICredentialActivity;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.part.core.AColorThemedPart;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.render.ANodeBaseDecoratedRender;

import java.util.concurrent.ExecutionException;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class MiningExtractionPart extends AColorThemedPart<MiningExtraction> implements View.OnLongClickListener {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger = LoggerFactory.getLogger("MiningExtractionPart");

	// - F I E L D - S E C T I O N ............................................................................
	private EveItem item = new EveItem();

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public MiningExtractionPart( final MiningExtraction model ) {
		super(model);
		item = new AndroidGlobalDataManager().searchItem4Id(getCastedModel().getTypeId());
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public MiningExtraction getCastedModel() {
		return (MiningExtraction) this.getModel();
	}

	// --- M O D E L   D A T A   T R A N S F O R M A T I O N
	public String getTExtractionValue() {
		if (null != item)
			try {
				return NeoComAbstractPart.generatePriceString(item.getHighestBuyerPrice().getPrice() * getCastedModel().getQuantity(), true,
						true);
			} catch (ExecutionException ee) {
				return "- ISK";
			} catch (InterruptedException ie) {
				return "- ISK";
			}
		else return "- ISK";
	}

	public String getTResourcePrice() {
		if (null != item) {
			try {
				return NeoComAbstractPart.generatePriceString(item.getHighestBuyerPrice().getPrice(), false, true);
			} catch (ExecutionException ee) {
				return "- ISK";
			} catch (InterruptedException ie) {
				return "- ISK";
			}
		} else return "- ISK";
	}

	public String getTValueIndex() {
		if (null != item) {
			try {
				return Double.valueOf(Math.round((item.getHighestBuyerPrice().getPrice() / item.getVolume()) * 10.0)).toString();
			} catch (ExecutionException ee) {
				return "00";
			} catch (InterruptedException ie) {
				return "00";
			}
		} else return "00";
	}

	public String getTQuantity() {
		return NeoComAbstractPart.qtyFormatter.format(getCastedModel().getQuantity());
	}

	public String getTStackVolume() {
		if (null != item) {
			return NeoComAbstractPart.qtyFormatter.format(getCastedModel().getQuantity() * item.getVolume()) + "m3";
		} else return "00m3";
	}

	public String getTDelta() {
		return NeoComAbstractPart.qtyFormatter.format(getCastedModel().getDelta());
	}


	// --- I A N D R O I D P A R T   I N T E R F A C E
	@Override
	public long getModelId() {
		return getCastedModel().getRecordId().hashCode();
	}

	/**
	 * Return a rendered depending on where we are located. The actions under the DateFaceted containers should not display some of the
	 * fields so we can use a more compact panel.
	 * @return
	 */
	@Override
	public AbstractRender selectRenderer() {
		if (getRenderMode().equalsIgnoreCase(NeoComApplication.EVariants.MINING_EXTRACTIONS_ALL.name()))
			return new MiningExtractionPackedRender(this, this.getActivity());
		else return new MiningExtractionRender(this, this.getActivity());
	}

	// --- O N L O N G C L I C K L I S T E N E R   I N T E R F A C E
	@Override
	public boolean onLongClick( View view ) {
		logger.info(">> [CredentialPart.onClick]");
		// Set the pilot selected on the context and then go to the Pilot Dashboard.
		final Object resourcePart = view.getTag();
		if (resourcePart instanceof MiningExtractionPart) {
			// With the new implementation get just the identifier for the Credential.
			final int resourceTypeId = ((MiningExtractionPart) resourcePart).getCastedModel().getTypeId();
			final Intent intent = new Intent(this.getActivity(), RefiningDetailActivity.class);

			// Go to the Activity and get back the pilot identifier.
			if (this.getActivity() instanceof ICredentialActivity) {
				int pilotIdentifier = ((ICredentialActivity) this.getActivity()).getCredential().getAccountId();
				intent.putExtra(NeoComApplication.EExtras.CAPSULEER_IDENTIFIER.name(), pilotIdentifier);
			}
			intent.putExtra(NeoComApplication.EExtras.REFINETYPE_IDENTIFIER.name(), resourceTypeId);
			intent.putExtra(NeoComApplication.EExtras.REFININGLOCATION_IDENTIFIER.name()
					, ((MiningExtractionPart) resourcePart).getCastedModel().getSolarSystemId());
			getActivity().startActivity(intent);
		}
		logger.info("<< [CredentialPart.onClick]");
		return true;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("MiningExtractionPart [ ");
		buffer.append("name: ").append(0);
		buffer.append("]");
		buffer.append("->").append(super.toString());
		return buffer.toString();
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	public static class MiningExtractionRender extends MiningExtractionPackedRender {
		// - F I E L D - S E C T I O N ............................................................................
		private TextView extractionSystemName = null;
		private TextView extractionHour = null;
		private TextView quantity = null;
		private TextView value = null;
		private TextView unitPrice = null;
		private TextView valueIndex = null;
		private TextView delta = null;
		private TextView separatorLabel = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public MiningExtractionRender( final AColorThemedPart newPart, final Activity context ) {
			super(newPart, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public MiningExtractionPart getPart() {
			return (MiningExtractionPart) super.getPart();
		}

		@Override
		public void initializeViews() {
			super.initializeViews();
			extractionSystemName = (TextView) _convertView.findViewById(R.id.extractionSystemName);
			extractionHour = (TextView) _convertView.findViewById(R.id.extractionHour);
			unitPrice = (TextView) _convertView.findViewById(R.id.unitPrice);
			valueIndex = (TextView) _convertView.findViewById(R.id.valueIndex);
			delta = (TextView) _convertView.findViewById(R.id.delta);
			separatorLabel = (TextView) _convertView.findViewById(R.id.separatorLabel);
		}

		@Override
		public void updateContent() {
			super.updateContent();
			extractionSystemName.setText(this.getPart().getCastedModel().getSystemName());
			extractionHour.setText("HOUR: " + this.getPart().getCastedModel().getExtractionHour());
			unitPrice.setText(this.getPart().getTResourcePrice());
			valueIndex.setText(this.getPart().getTValueIndex());
			delta.setText(this.getPart().getTDelta());

			// Hide some elements if the delta os ZERO
			if (this.getPart().getCastedModel().getDelta() < 1) {
				delta.setVisibility(View.INVISIBLE);
				separatorLabel.setVisibility(View.INVISIBLE);
			}
		}

		@Override
		public int accessLayoutReference() {
			return R.layout.miningextractions4today;
		}
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	public static class MiningExtractionPackedRender extends ANodeBaseDecoratedRender {
		// - F I E L D - S E C T I O N ............................................................................
		private TextView resourceName = null;
		private TextView resourceTypeId = null;
		private TextView quantity = null;
		private TextView value = null;
		private TextView volume = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public MiningExtractionPackedRender( final AColorThemedPart newPart, final Activity context ) {
			super(newPart, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public MiningExtractionPart getPart() {
			return (MiningExtractionPart) super.getPart();
		}

		@Override
		public void initializeViews() {
			super.initializeViews();
			resourceName = (TextView) _convertView.findViewById(R.id.resourceName);
			resourceTypeId = (TextView) _convertView.findViewById(R.id.resourceTypeId);
			quantity = (TextView) _convertView.findViewById(R.id.quantity);
			value = (TextView) _convertView.findViewById(R.id.value);
			volume = (TextView) _convertView.findViewById(R.id.volume);
		}

		@Override
		public void updateContent() {
			super.updateContent();
			String link = AndroidGlobalDataManager.getURLForItem(this.getPart().getCastedModel().getTypeId());
			if ((null != link) && (null != nodeIcon))
				Picasso.get().load(link).into(nodeIcon);
			resourceName.setText(this.getPart().getCastedModel().getResourceName());
			resourceTypeId.setText("[#" + this.getPart().getCastedModel().getTypeId() + "]");
			quantity.setText(this.getPart().getTQuantity());
			value.setText(this.getPart().getTExtractionValue());
			volume.setText(this.getPart().getTStackVolume());
		}

		@Override
		public int accessLayoutReference() {
			return R.layout.miningextractions4datefacet;
		}
	}
}
// - UNUSED CODE ............................................................................................
//[01]
