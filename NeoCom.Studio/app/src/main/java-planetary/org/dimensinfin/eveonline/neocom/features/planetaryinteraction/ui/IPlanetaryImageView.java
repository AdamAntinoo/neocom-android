package org.dimensinfin.eveonline.neocom.features.planetaryinteraction.ui;

import org.dimensinfin.eveonline.neocom.features.planetaryinteraction.StructureGeoPosition;
import org.dimensinfin.eveonline.neocom.planetary.Structure;

public interface IPlanetaryImageView {
	void setDeltaPosition( final StructureGeoPosition deltaPosition );

	void setStructure( Structure structure );
}
