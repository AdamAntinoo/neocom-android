package org.dimensinfin.eveonline.neocom.planetary;

import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseTypesTypeIdOkDogmaAttributes;

import org.joda.time.DateTime;

public interface IPlanetaryFacility {
	String getName();

	PlanetaryFacilityType getFacilityType();

	int getIconReferenceId();

	int getStructureColorReference();

	int getBackgroundColor();

	int getIconColorReference();

	DateTime getLastCycleStart();

	int getCpuUsage();

	int getPowerUsage();

	GetUniverseTypesTypeIdOkDogmaAttributes getDogmaAttributeById( final int attributeId );
}
