package org.dimensinfin.eveonline.neocom.planetary;

import java.util.List;

import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseTypesTypeIdOkDogmaAttributes;
import org.dimensinfin.eveonline.neocom.model.NeoComNode;

import org.joda.time.DateTime;

public abstract class PlanetaryFacilityWrapper extends NeoComNode implements IPlanetaryFacility {
	protected IPlanetaryFacility planetaryFacility;

	// - I P L A N E T A R Y F A C I L I T Y
	@Override
	public String getName() {
		return this.planetaryFacility.getName();
	}

	@Override
	public int getGroupId() {
		return planetaryFacility.getGroupId();
	}

	@Override
	public PlanetaryFacilityType getFacilityType() {
		return this.planetaryFacility.getFacilityType();
	}

	@Override
	public int getBackgroundColor() {
		return planetaryFacility.getBackgroundColor();
	}

	@Override
	public int getIconReferenceId() {
		return planetaryFacility.getIconReferenceId();
	}

	@Override
	public int getIconColorReference() {
		return planetaryFacility.getIconColorReference();
	}

	@Override
	public int getStructureColorReference() {
		return planetaryFacility.getStructureColorReference();
	}

	@Override
	public FacilityGeoPosition getGeoPosition() {
		return planetaryFacility.getGeoPosition();
	}

	@Override
	public Integer getSchematicId() {
		return planetaryFacility.getSchematicId();
	}

	@Override
	public DateTime getLastCycleStart() {
		return this.planetaryFacility.getLastCycleStart();
	}

	@Override
	public List<GetCharactersCharacterIdPlanetsPlanetIdOkContents> getContents() {
		return this.planetaryFacility.getContents();
	}

	@Override
	public GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails getExtractorDetails() {
		return this.planetaryFacility.getExtractorDetails();
	}

	@Override
	public PlanetType getPlanetType() {
		return this.planetaryFacility.getPlanetType();
	}

	@Override
	public int getCpuUsage() {
		return this.planetaryFacility.getCpuUsage();
	}

	@Override
	public int getPowerUsage() {
		return this.planetaryFacility.getPowerUsage();
	}

	@Override
	public FacilityGeoPosition getCommandCenterPosition() {
		return planetaryFacility.getCommandCenterPosition();
	}

	@Override
	public void setCommandCenterPosition( final FacilityGeoPosition commandCenterPosition ) {
		planetaryFacility.setCommandCenterPosition(commandCenterPosition);
	}

	@Override
	public GetUniverseTypesTypeIdOkDogmaAttributes getDogmaAttributeById( final int attributeId ) {
		return this.planetaryFacility.getDogmaAttributeById(attributeId);
	}

	// -  I I T E M F A C E T
	@Override
	public String getURLForItem() {
		return planetaryFacility.getURLForItem();
	}
}
