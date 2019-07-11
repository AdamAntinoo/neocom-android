package org.dimensinfin.eveonline.neocom.planetary.datasource;

import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.PlanetaryRepository;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkPins;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.PlanetType;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryFacilityType;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryStorage;
import org.dimensinfin.eveonline.neocom.planetary.facilities.CommandCenterFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.ExtractorFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.FactoryFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.PlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.StorageFacility;

import java.util.ArrayList;
import java.util.List;

public abstract class PlanetaryCommonDataSource extends AMVCDataSource {
	protected Credential credential;
	protected ESIDataAdapter esiDataAdapter;
	protected PlanetaryRepository planetaryRepository;

	protected ESIDataAdapter getEsiDataAdapter() {
		return this.esiDataAdapter;
	}

	/**
	 * Convert the list of facilities received from ESI to out own model structures. Facilities should be interconnected in another way
	 * to allow the access to some other functions. Also there are special characteristics only available on some facilities depending
	 * on the facility type.
	 *
	 * The planet item record is required to identify the pin group and from it the facility type.
	 *
	 * The colony is required by a command center to be able to request the used power and cpu. The colony is the only able to scan all
	 * the facilities and get such information.
	 */
	protected List<IPlanetaryFacility> convertPin2Facility( final List<GetCharactersCharacterIdPlanetsPlanetIdOkPins> pins
			, final PlanetType planetType ) {
		final List<IPlanetaryFacility> facilities = new ArrayList<>();
		for (GetCharactersCharacterIdPlanetsPlanetIdOkPins facility : pins) {
			final EveItem facilityItem = new EveItem(facility.getTypeId()); // The item for this facility.
			// Create the right facility class depending on the type.
			final PlanetaryFacilityType facilityType = PlanetaryFacilityType.getTypeByStructureGroup(facilityItem.getGroupId());
			final PlanetaryFacility baseFacility = new PlanetaryFacility.Builder()
					                                       .withPin(facility)
					                                       .withPlanetType(planetType)
					                                       .withFacilityItem(facilityItem)
					                                       .withEsiDataAdapter(this.getEsiDataAdapter())
					                                       .build();
			switch (facilityType) {
				case COMMAND_CENTER:
					facilities.add(new CommandCenterFacility.Builder()
							               .withPlanetaryFacility(baseFacility)
							               .withPlanetaryStorage(new PlanetaryStorage(baseFacility.getContents(), this.esiDataAdapter))
							               .build());
					break;
				case STORAGE:
					facilities.add(new StorageFacility.Builder()
							               .withPlanetaryFacility(baseFacility)
							               .withPlanetaryStorage(new PlanetaryStorage(baseFacility.getContents(), this.esiDataAdapter))
							               .build());
					break;
				case LAUNCHPAD:
					facilities.add(new StorageFacility.Builder()
							               .withPlanetaryFacility(baseFacility)
							               .withPlanetaryStorage(new PlanetaryStorage(baseFacility.getContents(), this.esiDataAdapter))
							               .build());
					break;
				case EXTRACTOR_CONTROL_UNIT:
					facilities.add(new ExtractorFacility.Builder()
							               .withPlanetaryFacility(baseFacility)
							               .withExtractorDetails(facility)
							               .build());
					break;
				case PLANETARY_FACTORY:
					facilities.add(new FactoryFacility.Builder()
							               .withPlanetaryFacility(baseFacility)
							               .withSchematics(baseFacility.getSchematicId())
							               .withPlanetaryRepository(this.planetaryRepository)
							               .build());
					break;
				default:
					facilities.add(baseFacility);
			}
		}
		return facilities;
	}
}
