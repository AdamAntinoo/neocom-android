package org.dimensinfin.eveonline.neocom.planetary;

import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.PlanetaryRepository;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkPins;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniversePlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.planetary.facilities.CommandCenterFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.ExtractorFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.FactoryFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.PlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.StorageFacility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Colony creation and data gathering is a complex and lengthy operation. This class will concentrate all the creation code on a
 * single place and also can give caching and validation among other functionality like initial data calculation
 * and setup.
 */
public class ColonyFactory {
	// - C O M P O N E N T S
	private ESIDataAdapter esiDataAdapter;
	private Credential credential;
	private PlanetaryRepository planetaryRepository;

	private Map<Integer, ColonyPack> colonyCache = new HashMap<>();
	private List<IPlanetaryFacility> facilities = new ArrayList<>();

	public ColonyPack accessColony( final GetCharactersCharacterIdPlanets200Ok esiColonyData ) {
		if (this.colonyCache.containsKey(esiColonyData.getPlanetId())) {
			final ColonyPack colony = this.colonyCache.get(esiColonyData.getPlanetId());
			if (colony.needsRefresh()) return this.createColony(esiColonyData);
			else return this.colonyCache.get(esiColonyData.getPlanetId());
		} else return this.createColony(esiColonyData);
	}

	protected ColonyPack createColony( final GetCharactersCharacterIdPlanets200Ok esiColonyData ) {
		final GetUniversePlanetsPlanetIdOk planetData = this.esiDataAdapter.getUniversePlanetsPlanetId(esiColonyData.getPlanetId());
		final GetCharactersCharacterIdPlanetsPlanetIdOk structureContainer = this.esiDataAdapter.getCharactersCharacterIdPlanetsPlanetId(
				credential.getAccountId(),
				esiColonyData.getPlanetId(),
				credential.getRefreshToken(),
				credential.getDataSource()
		);
		if (null != structureContainer) {
			final PlanetType planetType = PlanetType.valueOf(esiColonyData.getPlanetType().name());
			for (GetCharactersCharacterIdPlanetsPlanetIdOkPins pin : structureContainer.getPins()) {
				this.facilities.add(this.convertPin2Facility(pin, planetType));
			}
		}
		final ColonyPack colony = new ColonyPack.Builder()
				.withPilotIdentifier(this.credential.getAccountId())
				.withPlanetFacilities(structureContainer)
				.withColony(esiColonyData)
				.withPlanetData(planetData)
				.withFacilities(this.facilities)
				.build();
		colony.timeStamp();
		this.colonyCache.put(esiColonyData.getPlanetId(), colony);
		return colony;
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
	protected IPlanetaryFacility convertPin2Facility( final GetCharactersCharacterIdPlanetsPlanetIdOkPins pin,
	                                                  final PlanetType planetType ) {
		final EveItem facilityItem = new EveItem(pin.getTypeId()); // The item for this facility.
		// Create the right facility class depending on the type.
		final PlanetaryFacilityType facilityType = PlanetaryFacilityType.getTypeByStructureGroup(facilityItem.getGroupId());
		final PlanetaryFacility baseFacility = new PlanetaryFacility.Builder()
				.withPin(pin)
				.withPlanetType(planetType)
				.withFacilityItem(facilityItem)
				.build();
		switch (facilityType) {
			case COMMAND_CENTER:
				return new CommandCenterFacility.Builder()
						.withPlanetaryFacility(baseFacility)
						.withPlanetaryStorage(new PlanetaryStorage(baseFacility.getContents()))
						.build();
			case STORAGE:
				return new StorageFacility.Builder()
						.withPlanetaryFacility(baseFacility)
						.withPlanetaryStorage(new PlanetaryStorage(baseFacility.getContents()))
						.build();
			case LAUNCHPAD:
				facilities.add(new StorageFacility.Builder()
						.withPlanetaryFacility(baseFacility)
						.withPlanetaryStorage(new PlanetaryStorage(baseFacility.getContents()))
						.build());
				break;
			case EXTRACTOR_CONTROL_UNIT:
				return new ExtractorFacility.Builder()
						.withPlanetaryFacility(baseFacility)
						.withExtractorDetails(pin)
						.build();
			case PLANETARY_FACTORY:
				return new FactoryFacility.Builder()
						.withPlanetaryFacility(baseFacility)
						.withSchematics(baseFacility.getSchematicId())
						.withPlanetaryRepository(this.planetaryRepository)
						.build();
		}
		return baseFacility;
	}

	// - B U I L D E R
	public static class Builder {
		private ColonyFactory onConstruction;

		public Builder() {
			this.onConstruction = new ColonyFactory();
		}

		public Builder withEsiDataAdapter( final ESIDataAdapter esiDataAdapter ) {
			Objects.requireNonNull(esiDataAdapter);
			this.onConstruction.esiDataAdapter = esiDataAdapter;
			return this;
		}

		public Builder withCredential( final Credential credential ) {
			Objects.requireNonNull(credential);
			this.onConstruction.credential = credential;
			return this;
		}

		public Builder withPlanetaryRepository( final PlanetaryRepository planetaryRepository ) {
			Objects.requireNonNull(planetaryRepository);
			this.onConstruction.planetaryRepository = planetaryRepository;
			return this;
		}

		public ColonyFactory build() {
			Objects.requireNonNull(this.onConstruction.esiDataAdapter);
			Objects.requireNonNull(this.onConstruction.credential);
			Objects.requireNonNull(this.onConstruction.planetaryRepository);
			return this.onConstruction;
		}
	}
}
