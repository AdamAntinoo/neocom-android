package org.dimensinfin.eveonline.neocom.planetary.datasource;

import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.PlanetaryRepository;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.planetary.ColonyFactory;
import org.dimensinfin.eveonline.neocom.planetary.ColonyPack;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryFacility;

import java.util.List;
import java.util.Objects;

public class PlanetaryColonyContentsDataSource extends PlanetaryCommonDataSource {
	private int planetIdentifier;
	private List<IPlanetaryFacility> facilities;
	private ColonyPack colony;

	// - C O N S T R U C T O R S
	protected PlanetaryColonyContentsDataSource() {}

	// - I D A T A S O U R C E   I N T E R F A C E
	@Override
	public void prepareModel() {
		logger.info(">> [PlanetaryColoniesDataSource.prepareModel]");
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
						.build()
						.accessColony(colonyEsi);
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
	public static class Builder extends BaseBuilder<PlanetaryColonyContentsDataSource, Builder> {
		private PlanetaryColonyContentsDataSource onConstruction;

		@Override
		protected PlanetaryColonyContentsDataSource getActual() {
			if (null == this.onConstruction)
				this.onConstruction = new PlanetaryColonyContentsDataSource();
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
		public PlanetaryColonyContentsDataSource build() {
			// Check additional mandatory fields.
			final PlanetaryColonyContentsDataSource target = super.build();
			Objects.requireNonNull(target.credential);
			Objects.requireNonNull(target.esiDataAdapter);
			Objects.requireNonNull(target.planetaryRepository);
			return target;
		}
	}
}
