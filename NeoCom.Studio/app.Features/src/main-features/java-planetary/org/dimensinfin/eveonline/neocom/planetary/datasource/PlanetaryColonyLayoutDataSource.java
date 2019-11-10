package org.dimensinfin.eveonline.neocom.planetary.datasource;

import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.PlanetaryRepository;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.planetary.ColonyFactory;
import org.dimensinfin.eveonline.neocom.planetary.ColonyPack;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryFacilitySummary;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryFacilityType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlanetaryColonyLayoutDataSource extends PlanetaryCommonDataSource {
	private int planetIdentifier;
	private ColonyPack colony;
	/**
	 * Aggregation map for the selected planet structures. Each entry on the map aggregates the instances and the number of the
	 * map entry type planet planetary interaction facilities.
	 */
	private Map<PlanetaryFacilityType, List<IPlanetaryFacility>> facilityMap = new HashMap<>();

	// - C O N S T R U C T O R S
	protected PlanetaryColonyLayoutDataSource() {}

	// - I D A T A S O U R C E   I N T E R F A C E

	/**
	 * To prepare the model for a colony we only should have to read the planet installations for the selected colony. This information should be
	 * present on the extras bundle received and processed by the Activity.
	 * But using the same code to gel the colony information allows to connect the facilities and share information.
	 */
	@Override
	public void prepareModel() {
		logger.info(">> [PlanetaryColonyLayoutDataSource.prepareModel]");
		this.planetIdentifier = this.getExtras().getInt(EExtras.PLANET_IDENTIFIER.name());
		final List<GetCharactersCharacterIdPlanets200Ok> esiColonies = this.getEsiDataAdapter().getCharactersCharacterIdPlanets(
				credential.getAccountId(),
				credential.getRefreshToken(),
				credential.getDataSource());
		for (GetCharactersCharacterIdPlanets200Ok colonyEsi : esiColonies) {
			if (colonyEsi.getPlanetId() == this.planetIdentifier) { // Only process the colony for the selected planet
				this.colony = new ColonyFactory.Builder()
						.withEsiDataAdapter(this.getEsiDataAdapter())
						.withCredential(this.getCredential())
						.withPlanetaryRepository(this.planetaryRepository)
						.build()
						.accessColony(colonyEsi);
			}
		}
//
//		// Read the colony data and generate the support structureContainer, both for the header and for the main.
////		final List<GetCharactersCharacterIdPlanets200Ok> esiColonies = this.getEsiDataAdapter().getCharactersCharacterIdPlanets(
////				credential.getAccountId()
////				, credential.getRefreshToken()
////				, credential.getDataSource());
//		// Search for the selected planet and then construct the facilities.
////		final int planetIdentifier = this.getExtras().getInt(EExtras.PLANET_IDENTIFIER.name());
//		for (GetCharactersCharacterIdPlanets200Ok colonyEsi : esiColonies) {
//			if (planetIdentifier == colonyEsi.getPlanetId()) {
//				final GetUniversePlanetsPlanetIdOk planetData = this.getEsiDataAdapter().getUniversePlanetsPlanetId(colonyEsi.getPlanetId());
//				final GetCharactersCharacterIdPlanetsPlanetIdOk structureContainer = this.getEsiDataAdapter().getCharactersCharacterIdPlanetsPlanetId(
//						credential.getAccountId()
//						, colonyEsi.getPlanetId()
//						, credential.getRefreshToken()
//						, credential.getDataSource());
//				if (null != structureContainer) {
//					final List<IPlanetaryFacility> facilities = this.convertPin2Facility(structureContainer.getPins()
//							, PlanetType.valueOf(colonyEsi.getPlanetType().name()));
//					Stream.of(facilities)
//							.map(facility -> {
//								// Aggregate pins of identical type into a pin map.
//								List<IPlanetaryFacility> found = this.facilityMap.get(facility.getFacilityType());
//								if (null == found) {
//									found = new ArrayList<>();
//									found.add(facility);
//									this.facilityMap.put(facility.getFacilityType(), found);
//								} else found.add(facility);
//								return facility;
//							});
//					colony = new ColonyPack.Builder()
//							         .withColony(colonyEsi)
//							         .withPlanetData(planetData)
//							         .withFacilities(facilities)
//							         .withPilotIdentifier(this.credential.getAccountId())
//							         .build();
//				}
//				break;
//			}
//		}
	}

	@Override
	public void collaborate2Model() {
		for (Map.Entry<PlanetaryFacilityType, List<IPlanetaryFacility>> structures : this.facilityMap.entrySet()) {
			final IPlanetaryFacility facility = structures.getValue().get(0);
			this.addHeaderContents(new PlanetaryFacilitySummary.Builder(facility.getName())
					                       .withGroupId(facility.getGroupId())
					                       .withIconReference(facility.getIconReferenceId())
					                       .withGroupCount(structures.getValue().size())
					                       .build());
		}
		// - S E C T I O N   D A T A
		for (IPlanetaryFacility planetaryFacility : this.colony.getFacilities())
			this.addModelContents(planetaryFacility);
	}

	// - B U I L D E R
	public static class Builder extends PlanetaryCommonDataSource.Builder<PlanetaryColonyLayoutDataSource,
			                                                                     PlanetaryColonyLayoutDataSource.Builder> {
		private PlanetaryColonyLayoutDataSource onConstruction;

		@Override
		protected PlanetaryColonyLayoutDataSource getActual() {
			if (null == this.onConstruction)
				this.onConstruction = new PlanetaryColonyLayoutDataSource();
			return this.onConstruction;
		}

		@Override
		protected Builder getActualBuilder() {
			return this;
		}

		public Builder withCredential( final Credential credential ) {
			this.onConstruction.credential = credential;
			return this;
		}

		public Builder withEsiAdapter( final ESIDataAdapter esiDataAdapter ) {
			this.onConstruction.esiDataAdapter = esiDataAdapter;
			return this;
		}
		public Builder withPlanetaryRepository( final PlanetaryRepository planetaryRepository ) {
			Objects.requireNonNull(planetaryRepository);
			this.onConstruction.planetaryRepository = planetaryRepository;
			return this;
		}

		@Override
		public PlanetaryColonyLayoutDataSource build() {
			// Check additional mandatory fields.
			final PlanetaryColonyLayoutDataSource target = super.build();
			Objects.requireNonNull(target.credential);
			Objects.requireNonNull(target.esiDataAdapter);
			Objects.requireNonNull(target.planetaryRepository);
			return target;
		}
	}
}
