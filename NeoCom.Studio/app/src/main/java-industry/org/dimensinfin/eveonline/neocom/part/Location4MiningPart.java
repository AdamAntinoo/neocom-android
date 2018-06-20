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
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.core.ANodeBase4ExpandRender;
import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.IMenuActionTarget;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.model.LocationFacetedAssetsContainer;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractExpandablePart;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class Location4MiningPart extends NeoComAbstractExpandablePart implements IMenuActionTarget {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("Location4MiningPart");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public Location4MiningPart( final LocationFacetedAssetsContainer model ) {
		super(model);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public LocationFacetedAssetsContainer getCastedModel() {
		return (LocationFacetedAssetsContainer) this.getModel();
	}

	// --- M O D E L   D A T A   T R A N S F O R M A T I O N
	public Spanned getTStationName() {
		StringBuffer htmlLocation = new StringBuffer();
		double security = this.getCastedModel().getSecurityValue();
		htmlLocation.append(NeoComAbstractPart.generateSecurityColor(security, NeoComAbstractPart.securityFormatter.format(security)));
		htmlLocation.append(" ").append(this.getCastedModel().getStation());
		return Html.fromHtml(htmlLocation.toString());
	}

	public String getTContentsCount() {
		int locationAssets = getCastedModel().getContentSize();
		String countString = null;
		if (locationAssets > 1) {
			countString = NeoComAbstractPart.qtyFormatter.format(locationAssets) + " items";
		} else {
			countString = NeoComAbstractPart.qtyFormatter.format(locationAssets) + " item";
		}
		return countString;
	}

	public String getTContentsValue() {
		return NeoComAbstractPart.generatePriceString(getCastedModel().getTotalValue(), true, true);
	}

	public String getTContentsVolume() {
		return NeoComAbstractPart.qtyFormatter.format(getCastedModel().getTotalVolume()) + " m3";
	}

	// --- I A N D R O I D P A R T   I N T E R F A C E
	@Override
	public long getModelId() {
		return this.getCastedModel().getID();
	}

	@Override
	public AbstractRender selectRenderer() {
		return new Location4AssetsPart.Location4MiningRender(this, getActivity());
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("Location4MiningPart [ ");
		buffer.append("name: ").append(0);
		buffer.append("]");
		buffer.append("->").append(super.toString());
		return buffer.toString();
	}

	// --- I M E N U A C T I O N T A R G E T   I N T E R F A C E
	@Override
	public boolean onContextItemSelected( MenuItem item ) {
		logger.info(">> [Location4MiningPart.onContextItemSelected]");
		try {
			final int menuItemIndex = item.getItemId();
			invalidate();
			fireStructureChange(SystemWideConstants.events.EVENTCONTENTS_ACTIONMODIFYDATA.name(), this, this);
		} finally {
			logger.info(">> [Location4MiningPart.onContextItemSelected]");
			return true;
		}
	}

	@Override
	public void onCreateContextMenu( ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo ) {
		logger.info(">> [Location4MiningPart.onCreateContextMenu]");
		// Initialize the contextual menu each time.
		MenuInflater inflater = new MenuInflater(AndroidGlobalDataManager.getAppContext());
		inflater.inflate(R.menu.context_location4assets, menu);
		logger.info("<< [Location4MiningPart.onCreateContextMenu]");
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	public static class Location4MiningRender extends ANodeBase4ExpandRender {
		// - F I E L D - S E C T I O N ............................................................................
		private TextView locationRegion = null;
		private TextView locationSystem = null;
		private TextView locationIdentifier = null;
		private TextView locationStation = null;
		private TextView contentsCount = null;
		private TextView locationItemsValue = null;
		private TextView locationItemsVolume = null;


		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public Location4MiningRender( AbstractAndroidPart newPart, Activity context ) {
			super(newPart, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public Location4MiningPart getPart() {
			return (Location4MiningPart) super.getPart();
		}

		// --- A B S T R A C T R E N D E R   O V E R R I D E S
		@Override
		public void initializeViews() {
			super.initializeViews();
			locationRegion = (TextView) _convertView.findViewById(R.id.locationRegion);
			locationSystem = (TextView) _convertView.findViewById(R.id.locationSystem);
			locationIdentifier = (TextView) _convertView.findViewById(R.id.locationIdentifier);
			locationStation = (TextView) _convertView.findViewById(R.id.locationStation);
			contentsCount = (TextView) _convertView.findViewById(R.id.contentsCount);
			locationItemsValue = (TextView) _convertView.findViewById(R.id.locationItemsValue);
			locationItemsVolume = (TextView) _convertView.findViewById(R.id.locationItemsVolume);
		}

		@Override
		public void updateContent() {
			super.updateContent();
			final String link = getPart().getCastedModel().getUrlLocationIcon();
			if( (null != link) && (null != nodeIcon))
				Picasso.get().load(link).into(nodeIcon);
			locationRegion.setText(this.getPart().getCastedModel().getRegion());
			locationSystem.setText(this.getPart().getCastedModel().getSystem());
			locationIdentifier.setText("[" + this.getPart().getCastedModel().getID() + "]");
			locationStation.setText(this.getPart().getTStationName());
			contentsCount.setText(this.getPart().getTContentsCount());
			locationItemsValue.setText(this.getPart().getTContentsValue());
			locationItemsVolume.setText(this.getPart().getTContentsVolume());
		}

		@Override
		public int accessLayoutReference() {
			return R.layout.location4mining;
		}
	}
}
// - UNUSED CODE ............................................................................................
//[01]
