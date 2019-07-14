package org.dimensinfin.eveonline.neocom.planetary.datasource;

import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.PlanetaryRepository;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniversePlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.planetary.ColonyPack;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.PlanetType;

import java.util.List;
import java.util.Objects;

public class PlanetaryColoniesContentsDataSource extends PlanetaryCommonDataSource {
	protected int targetPlanetId;
	private List<IPlanetaryFacility> facilities;
	private ColonyPack colony;

	// - C O N S T R U C T O R S
	protected PlanetaryColoniesContentsDataSource() {}

	// - I D A T A S O U R C E   I N T E R F A C E
	@Override
	public void prepareModel() {
		logger.info(">> [PlanetaryColoniesDataSource.prepareModel]");
		this.targetPlanetId = this.getExtras().getInt(EExtras.PLANET_IDENTIFIER.name());
		// Read the colony data and generate the support structureContainer, both for the header and for the main.
		final List<GetCharactersCharacterIdPlanets200Ok> esiColonies = this.getEsiDataAdapter().getCharactersCharacterIdPlanets(
				credential.getAccountId(),
				credential.getRefreshToken(),
				credential.getDataSource());
		for (GetCharactersCharacterIdPlanets200Ok colonyEsi : esiColonies) {
			if (colonyEsi.getPlanetId() == this.targetPlanetId) {
				final GetUniversePlanetsPlanetIdOk planetData = this.getEsiDataAdapter().getUniversePlanetsPlanetId(colonyEsi.getPlanetId());
				final GetCharactersCharacterIdPlanetsPlanetIdOk structureContainer = this.getEsiDataAdapter().getCharactersCharacterIdPlanetsPlanetId(
						credential.getAccountId(),
						colonyEsi.getPlanetId(),
						credential.getRefreshToken(),
						credential.getDataSource());
				if (null != structureContainer) {
					this.facilities = this.convertPin2Facility(structureContainer.getPins(),
							PlanetType.valueOf(colonyEsi.getPlanetType().name()));
				}
				this.colony = new ColonyPack.Builder()
						.withColony(colonyEsi)
						.withPlanetData(planetData)
						.withFacilities(this.facilities)
						.withCapsuleerIdentifier(this.credential.getAccountId())
						.build();
			}
		}
		logger.info("<< [PlanetaryColoniesDataSource.prepareModel]");
	}

	// - I C O L L A B O R A T I O N
	@Override
	public void collaborate2Model() {
		this.addHeaderContents(this.colony);
		for (IPlanetaryFacility facility : this.facilities)
			this.addModelContents(facility);
	}

	// - B U I L D E R
	public static class Builder extends BaseBuilder<PlanetaryColoniesContentsDataSource, Builder> {
		private PlanetaryColoniesContentsDataSource onConstruction;

		@Override
		protected PlanetaryColoniesContentsDataSource getActual() {
			if (null == this.onConstruction)
				this.onConstruction = new PlanetaryColoniesContentsDataSource();
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
		public PlanetaryColoniesContentsDataSource build() {
			// Check additional mandatory fields.
			final PlanetaryColoniesContentsDataSource target = super.build();
			Objects.requireNonNull(target.credential);
			Objects.requireNonNull(target.esiDataAdapter);
			Objects.requireNonNull(target.planetaryRepository);
			return target;
		}
	}
}
