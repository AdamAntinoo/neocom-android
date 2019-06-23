package org.dimensinfin.eveonline.neocom.planetary.datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.PlanetaryRepository;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniversePlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.planetary.ColonyPack;
import org.dimensinfin.eveonline.neocom.planetary.PlanetType;

public class PlanetaryDataSource extends PlanetaryCommonDataSource {
	protected List<ColonyPack> colonies = new ArrayList<>();

	// - C O N S T R U C T O R S
	protected PlanetaryDataSource() {}

	// - I D A T A S O U R C E   I N T E R F A C E
	@Override
	public void prepareModel() {
		logger.info(">> [PlanetaryDataSource.prepareModel]");
		// Read the colony data and generate the support structureContainer, both for the header and for the main.
		final List<GetCharactersCharacterIdPlanets200Ok> esiColonies = this.getEsiDataAdapter().getCharactersCharacterIdPlanets(
				credential.getAccountId()
				, credential.getRefreshToken()
				, credential.getDataSource());
		for (GetCharactersCharacterIdPlanets200Ok colonyEsi : esiColonies) {
			final GetUniversePlanetsPlanetIdOk planetData = this.getEsiDataAdapter().getUniversePlanetsPlanetId(colonyEsi.getPlanetId());
			final GetCharactersCharacterIdPlanetsPlanetIdOk structureContainer = this.getEsiDataAdapter().getCharactersCharacterIdPlanetsPlanetId(
					credential.getAccountId()
					, colonyEsi.getPlanetId()
					, credential.getRefreshToken()
					, credential.getDataSource());
			if (null != structureContainer) {
				this.colonies.add(new ColonyPack.Builder()
						                  .withColony(colonyEsi)
						                  .withPlanetData(planetData)
						                  .withFacilities(this.convertPin2Facility(structureContainer.getPins()
								                  , PlanetType.valueOf(colonyEsi.getPlanetType().name())))
						                  .withCapsuleerIdentifier(this.credential.getAccountId())
						                  .build());
			}
		}
		logger.info("<< [PlanetaryDataSource.prepareModel]");
	}

	// - I C O L L A B O R A T I O N
	@Override
	public void collaborate2Model() {
		for (ColonyPack colony : this.colonies)
			this.addModelContents(colony);
	}

	// - B U I L D E R
	public static class Builder extends AMVCDataSource.BaseBuilder<PlanetaryDataSource, Builder> {
		private PlanetaryDataSource onConstruction;

		@Override
		protected PlanetaryDataSource getActual() {
			if (null == this.onConstruction)
				this.onConstruction = new PlanetaryDataSource();
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
		public PlanetaryDataSource build() {
			// Check additional mandatory fields.
			final PlanetaryDataSource target = super.build();
			Objects.requireNonNull(target.credential);
			Objects.requireNonNull(target.esiDataAdapter);
			return target;
		}
	}
}
