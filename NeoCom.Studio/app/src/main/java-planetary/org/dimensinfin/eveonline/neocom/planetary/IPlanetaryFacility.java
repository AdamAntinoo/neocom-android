package org.dimensinfin.eveonline.neocom.planetary;

import java.util.List;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.domain.IItemFacet;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseTypesTypeIdOkDogmaAttributes;

import org.joda.time.DateTime;

public interface IPlanetaryFacility extends ICollaboration, IItemFacet {
	String getName();

	PlanetaryFacilityType getFacilityType();

	int getIconReferenceId();

	int getStructureColorReference();

	int getBackgroundColor();

	int getIconColorReference();

	DateTime getLastCycleStart();

	Integer getSchematicId();

	List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> getContents();

	double getContentsVolume();

	Float getStorageCapacity();

	CommandCenterFacility getCommandCenter();

	FacilityGeoPosition getGeoPosition();

	PlanetaryFacility setCommandCenter( final IPlanetaryFacility commandCenter );

	int getCpuUsage();

	int getPowerUsage();

	int getGroupId();

	GetUniverseTypesTypeIdOkDogmaAttributes getDogmaAttributeById( final int attributeId );
}
