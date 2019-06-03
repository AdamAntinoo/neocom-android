package org.dimensinfin.eveonline.neocom.planetary;

import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseTypesTypeIdOkDogmaAttributes;

public interface IPlanetaryFacility {
	GetUniverseTypesTypeIdOkDogmaAttributes getDogmaAttributeById( final int attributeId );
}
