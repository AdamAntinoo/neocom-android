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

import android.content.Intent;
import android.view.View;
import android.view.View.OnLongClickListener;

import org.dimensinfin.eveonline.neocom.activity.PlanetaryOptimizationActivity;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

// - CLASS IMPLEMENTATION ...................................................................................
public class Location4PlanetaryPart extends Location4AssetsPart implements OnLongClickListener {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = -1226553463320336724L;


	//	private static String _contextMenuTitle = "Select Location Role";

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public Location4PlanetaryPart (final EveLocation location) {
		super(location);
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * This part only implements the actions for the special Activity of the Planetary Intractions. This is a simple
	 * long click to fire the Planetary Optimization Activity.
	 * @param view
	 * @return
	 */
	@Override
	public boolean onLongClick (final View view) {
		logger.info(">> [Location4PlanetaryPart.onClick]");
		final Object targetPart = view.getTag();
		if ( targetPart instanceof Location4PlanetaryPart ) {
//			final NeoComCharacter pilot = NeoComModelStore.getSingleton().getActiveCharacter();
			final Intent intent = new Intent(this.getActivity(), PlanetaryOptimizationActivity.class);
			// Add all the identification chain as in the backend Angular calls. This is Login/Pilot/Manager
//			intent.putExtra(AppWideConstants.EExtras.EXTRA_LOGINID.name(), pilot.getLoginRef().getName());
			intent.putExtra(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), NeoComModelStore.getActiveCredential().getAccountId());
			intent.putExtra(AppWideConstants.EExtras.EXTRA_LOCATIONID.name(), getCastedModel().getID());
			getActivity().startActivity(intent);
		}
		logger.info("<< [Location4PlanetaryPart.onClick]");
		return true;
	}
}
// - UNUSED CODE ............................................................................................
