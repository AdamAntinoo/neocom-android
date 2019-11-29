package org.dimensinfin.eveonline.neocom.test.support;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.app.controller.PilotController;
import org.dimensinfin.eveonline.neocom.assets.controller.RegionController;
import org.dimensinfin.eveonline.neocom.assets.controller.StationController;

public enum PanelTypes {
	NOT_SET( "-UNDEFINED-" ),
	PILOTCONTROLLER( "Pilot" ),
	REGIONCONTROLLER( "Region" ),
	STATIONCONTROLLER( "Station" );

	public String panelName;

	PanelTypes( final String panelName ) {
		this.panelName = panelName;
	}

	public static PanelTypes decodeName( final String name ) {
		for (PanelTypes b : PanelTypes.values())
			if (b.panelName.equalsIgnoreCase( name )) return b;
		return PanelTypes.NOT_SET;
	}

	public boolean matches( final IAndroidController item ) {
		switch (this){
			case PILOTCONTROLLER:
				return item instanceof PilotController;
			case REGIONCONTROLLER:
				return item instanceof RegionController;
			case STATIONCONTROLLER:
				return item instanceof StationController;
		}
		return false;
	}
}
