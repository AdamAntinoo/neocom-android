package org.dimensinfin.eveonline.neocom.planetary;

import java.util.List;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.domain.IItemFacet;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseTypesTypeIdOkDogmaAttributes;

import org.joda.time.DateTime;

public interface IPlanetaryFacility extends ICollaboration, IItemFacet {
	String getName();

	int getGroupId();

	PlanetaryFacilityType getFacilityType();

	int getBackgroundColor();

	int getIconReferenceId();

	int getIconColorReference();

	int getStructureColorReference();

	FacilityGeoPosition getGeoPosition();

	Integer getSchematicId();

	DateTime getLastCycleStart();

	List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> getContents();

	int getCpuUsage();

	int getPowerUsage();

	FacilityGeoPosition getCommandCenterPosition();

	void setCommandCenterPosition( final FacilityGeoPosition commandCenterPosition );

	GetUniverseTypesTypeIdOkDogmaAttributes getDogmaAttributeById( final int attributeId );

	//	double getContentsVolume();

	//	Float getStorageCapacity();
}
