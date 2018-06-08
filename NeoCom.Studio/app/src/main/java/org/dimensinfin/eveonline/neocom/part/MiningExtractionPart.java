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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dimensinfin.android.mvc.core.AbstractPart;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.NeoComApplication;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.database.entity.MiningExtraction;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class MiningExtractionPart extends NeoComAbstractPart {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("MiningExtractionPart");

	// - F I E L D - S E C T I O N ............................................................................
	private EveItem item = new EveItem();

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public MiningExtractionPart( final ICollaboration model ) {
		super(model);
		item = new AndroidGlobalDataManager().searchItem4Id(getCastedModel().getTypeId());
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public MiningExtraction getCastedModel() {
		return (MiningExtraction) this.getModel();
	}

	public String getTExtractionValue() {
		if (null != item)
			try {
				return generatePriceString(item.getHighestBuyerPrice().getPrice() * getCastedModel().getQuantity(), true, true);
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
				return generatePriceString(item.getHighestBuyerPrice().getPrice(), false, true);
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
		return qtyFormatter.format(getCastedModel().getQuantity());
	}

	public String getTStackVolume() {
		if (null != item) {
			return qtyFormatter.format(getCastedModel().getQuantity() * item.getVolume()) + "m3";
		} else return "00m3";
	}

	@Override
	public long getModelId() {
		return 0;
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
		private TextView extractionDate = null;
		private TextView quantity = null;
		private TextView value = null;
		private TextView unitPrice = null;
		private TextView valueIndex = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public MiningExtractionRender( final AbstractPart newPart, final Activity context ) {
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
			extractionDate = (TextView) _convertView.findViewById(R.id.extractionDate);
			unitPrice = (TextView) _convertView.findViewById(R.id.unitPrice);
			valueIndex = (TextView) _convertView.findViewById(R.id.valueIndex);
		}

		@Override
		public void updateContent() {
			super.updateContent();
			extractionSystemName.setText(this.getPart().getCastedModel().getSystemName());
			extractionDate.setText(this.getPart().getCastedModel().getExtractionDate());
			unitPrice.setText(this.getPart().getTResourcePrice());
			valueIndex.setText(this.getPart().getTValueIndex());
		}

		@Override
		public int accessLayoutReference() {
			return R.layout.miningextractions4list;
		}
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	public static class MiningExtractionPackedRender extends AbstractRender {
		// - F I E L D - S E C T I O N ............................................................................
		private ImageView targetIcon = null;

		private TextView resourceName = null;
		private TextView resourceTypeId = null;
		private TextView quantity = null;
		private TextView value = null;
		private TextView volume = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public MiningExtractionPackedRender( final AbstractPart newPart, final Activity context ) {
			super(newPart, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public MiningExtractionPart getPart() {
			return (MiningExtractionPart) super.getPart();
		}

		@Override
		public void initializeViews() {
			targetIcon = (ImageView) _convertView.findViewById(R.id.targetIcon);

			resourceName = (TextView) _convertView.findViewById(R.id.resourceName);
			resourceTypeId = (TextView) _convertView.findViewById(R.id.resourceTypeId);
			quantity = (TextView) _convertView.findViewById(R.id.quantity);
			value = (TextView) _convertView.findViewById(R.id.value);
			volume = (TextView) _convertView.findViewById(R.id.volume);
		}

		@Override
		public void updateContent() {
			String link = AndroidGlobalDataManager.getURLForItem(this.getPart().getCastedModel().getTypeId());
			Picasso.get().load(link).into(targetIcon);
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
