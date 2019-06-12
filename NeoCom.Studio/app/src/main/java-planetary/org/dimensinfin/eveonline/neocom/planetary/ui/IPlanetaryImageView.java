package org.dimensinfin.eveonline.neocom.planetary.ui;

import org.dimensinfin.eveonline.neocom.planetary.FacilityGeoPosition;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryFacility;

public interface IPlanetaryImageView {
	void setDeltaPosition( final FacilityGeoPosition deltaPosition );

	void setPlanetaryFacility( final IPlanetaryFacility planetaryFacility );
}
