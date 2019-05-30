package org.dimensinfin.eveonline.neocom.planetary.ui;

import org.dimensinfin.eveonline.neocom.planetary.FacilityGeoPosition;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryFacility;

public interface IPlanetaryImageView {
	void setDeltaPosition( final FacilityGeoPosition deltaPosition );

	void setStructure( PlanetaryFacility planetaryFacility );
}
