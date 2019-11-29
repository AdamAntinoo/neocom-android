package org.dimensinfin.eveonline.neocom.test.support;

import org.apache.commons.lang3.ObjectUtils;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.app.controller.PilotController;
import org.dimensinfin.eveonline.neocom.assets.controller.RegionController;
import org.dimensinfin.eveonline.neocom.assets.controller.StationController;

public enum PanelTypes {
	NOT_SET( "-UNDEFINED-", ObjectUtils.Null.class ),
	PILOTCONTROLLER( "Pilot", PilotController.class ),
	REGIONCONTROLLER( "Region", RegionController.class ),
	STATIONCONTROLLER( "Station", StationController.class );

	public String panelName;
	public Class panelClass;

	PanelTypes( final String panelName
			, final Class panelClass ) {
		this.panelName = panelName;
		this.panelClass = panelClass;
	}

	public Class panelClass() {
		return this.panelClass;
	}

	public static PanelTypes decodeName( final String name ) {
		for (PanelTypes b : PanelTypes.values())
			if (b.panelName.equalsIgnoreCase( name )) return b;
		return PanelTypes.NOT_SET;
	}

	public boolean matches( final IAndroidController item ) {
		switch (this) {
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
