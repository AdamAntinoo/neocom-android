package org.dimensinfin.eveonline.neocom.planetary.datasource;

import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.datamngmt.ESIGlobalAdapter;
import org.dimensinfin.eveonline.neocom.domain.EsiItemV2;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkPins;
import org.dimensinfin.eveonline.neocom.planetary.CommandCenterFacility;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.PlanetType;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryFacilityType;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryStorage;
import org.dimensinfin.eveonline.neocom.planetary.StorageFacility;

public abstract class PlanetaryCommonDataSource extends AMVCDataSource {
	protected Credential credential;
	protected ESIGlobalAdapter esiAdapter;
	protected ESIDataAdapter esiDataAdapter;

	protected ESIDataAdapter getEsiAdapter() {
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
			final EsiItemV2 facilityItem = new EsiItemV2(this.getEsiAdapter().getUniverseTypeById(facility.getTypeId())); // The item for this facility.
			// Create the right facility class depending on the type.
			final PlanetaryFacilityType facilityType = PlanetaryFacilityType.getTypeByStructureGroup(facilityItem.getGroupId());
			final IPlanetaryFacility baseFacility = new PlanetaryFacility.Builder()
					                                        .withPin(facility)
					                                        .withPlanetType(planetType)
					                                        .withPlanetItem(facilityItem)
					                                        .withEsiDataAdapter(this.getEsiAdapter())
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
				default:
					facilities.add(baseFacility);
			}
		}
		return facilities;
	}
}
