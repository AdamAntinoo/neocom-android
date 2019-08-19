package org.dimensinfin.eveonline.neocom.planetary.datasource;

import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.PlanetaryRepository;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.planetary.ColonyFactory;
import org.dimensinfin.eveonline.neocom.planetary.ColonyPack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlanetaryColoniesDataSource extends PlanetaryCommonDataSource {
	protected List<ColonyPack> colonies = new ArrayList<>();

	// - C O N S T R U C T O R S
	protected PlanetaryColoniesDataSource() {}

	// - I D A T A S O U R C E   I N T E R F A C E
	@Override
	public void prepareModel() {
		logger.info(">> [PlanetaryColoniesDataSource.prepareModel]");
//		this.planetIdentifier = this.getExtras().getInt(EExtras.PLANET_IDENTIFIER.name());
		final List<GetCharactersCharacterIdPlanets200Ok> esiColonies = this.getEsiDataAdapter().getCharactersCharacterIdPlanets(
				credential.getAccountId(),
				credential.getRefreshToken(),
				credential.getDataSource());
		for (GetCharactersCharacterIdPlanets200Ok colonyEsi : esiColonies) {
//			if (colonyEsi.getPlanetId() == this.planetIdentifier) { // Only process the colony for the selected planet
			this.colonies.add( new ColonyFactory.Builder()
						.withEsiDataAdapter(this.getEsiDataAdapter())
						.withCredential(this.getCredential())
						.withPlanetaryRepository(this.planetaryRepository)
						.build()
						.accessColony(colonyEsi));
//			}
		}

		// Read the colony data and generate the support structureContainer, both for the header and for the main.
//		final List<GetCharactersCharacterIdPlanets200Ok> esiColonies = this.getEsiDataAdapter().getCharactersCharacterIdPlanets(
//				credential.getAccountId(),
//				credential.getRefreshToken(),
//				credential.getDataSource());
//		for (GetCharactersCharacterIdPlanets200Ok colonyEsi : esiColonies) {
//			final GetUniversePlanetsPlanetIdOk planetData = this.getEsiDataAdapter().getUniversePlanetsPlanetId(colonyEsi.getPlanetId());
//			final GetCharactersCharacterIdPlanetsPlanetIdOk structureContainer = this.getEsiDataAdapter().getCharactersCharacterIdPlanetsPlanetId(
//					credential.getAccountId(),
//					colonyEsi.getPlanetId(),
//					credential.getRefreshToken(),
//					credential.getDataSource());
//			if (null != structureContainer) {
//				this.colonies.add(new ColonyPack.Builder()
//						.withColony(colonyEsi)
//						.withPlanetData(planetData)
//						.withFacilities(this.convertPin2Facility(structureContainer.getPins(),
//								PlanetType.valueOf(colonyEsi.getPlanetType().name())))
//						.withPilotIdentifier(this.credential.getAccountId())
//						.build());
//			}
//		}
		logger.info("<< [PlanetaryColoniesDataSource.prepareModel]");
	}

	// - I C O L L A B O R A T I O N
	@Override
	public void collaborate2Model() {
		for (ColonyPack colony : this.colonies)
			this.addModelContents(colony);
	}

	// - B U I L D E R
	public static class Builder extends PlanetaryCommonDataSource.Builder<PlanetaryColoniesDataSource,
			                                                                     PlanetaryColoniesDataSource.Builder> {
		private PlanetaryColoniesDataSource onConstruction;

		@Override
		protected PlanetaryColoniesDataSource getActual() {
			if (null == this.onConstruction)
				this.onConstruction = new PlanetaryColoniesDataSource();
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
		public PlanetaryColoniesDataSource build() {
			// Check additional mandatory fields.
			final PlanetaryColoniesDataSource target = super.build();
			Objects.requireNonNull(target.credential);
			Objects.requireNonNull(target.esiDataAdapter);
			Objects.requireNonNull(target.planetaryRepository);
			return target;
		}
	}
}
