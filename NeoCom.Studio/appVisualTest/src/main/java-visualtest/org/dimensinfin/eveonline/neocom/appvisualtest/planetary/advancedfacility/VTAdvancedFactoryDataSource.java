package org.dimensinfin.eveonline.neocom.appvisualtest.planetary.advancedfacility;

import com.annimon.stream.Stream;

import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.adapters.NeoComRetrofitFactory;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.PlanetaryRepository;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniversePlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.planetary.ColonyFactory;
import org.dimensinfin.eveonline.neocom.planetary.ColonyPack;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryFacilityType;
import org.dimensinfin.eveonline.neocom.planetary.datasource.PlanetaryCommonDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VTAdvancedFactoryDataSource extends PlanetaryCommonDataSource {
	private VTAdvancedFacilityFragment fragment;
	private GetCharactersCharacterIdPlanets200Ok.PlanetTypeEnum planetType;
	private GetUniversePlanetsPlanetIdOk planetData;
	private List<IPlanetaryFacility> advancedFactories = new ArrayList<>();
	private int planetIdentifier;

	/**
	 * To test the factories I should read a configuration file that describes the facilities. I should use the same code as in the
	 * production data source so the test code can be used back on Production.
	 * So the first step is to replace the GetCharactersCharacterIdPlanets200Ok list bu a mock response with only the right colony
	 * data. It should also be the same with the list of facilities deployed on the colony.
	 *
	 * The Mock ESI interceptor will then send the requests to another server (for example a Mountebank server) that will return
	 * the mock data to be used on the testing. This is not possible !!.
	 *
	 * We run on an Android device so we do not have access to the local server. We need an Android mock server available and accessible
	 * from the device.
	 */
	@Override
	public void prepareModel() {
		NeoComRetrofitFactory.add2MockList("getCharactersCharacterIdPlanets");
		NeoComRetrofitFactory.add2MockList("getCharactersCharacterIdPlanetsPlanetId");
		planetIdentifier = 40237824;
		final List<GetCharactersCharacterIdPlanets200Ok> esiColonies = this.getEsiDataAdapter().getCharactersCharacterIdPlanets(
				credential.getAccountId(),
				credential.getRefreshToken(),
				credential.getDataSource());
		for (GetCharactersCharacterIdPlanets200Ok colonyEsi : esiColonies) {
			if (colonyEsi.getPlanetId() == this.planetIdentifier) { // Only process the colony for the selected planet
				final ColonyPack colony = new ColonyFactory.Builder()
						.withEsiDataAdapter(this.getEsiDataAdapter())
						.withCredential(this.getCredential())
						.withPlanetaryRepository(this.planetaryRepository)
						.build()
						.accessColony(colonyEsi);
				Stream.of(colony.getFacilities())
				      .filter(facility -> (facility.getFacilityType() == PlanetaryFacilityType.PLANETARY_FACTORY
						      || facility.getFacilityType() == PlanetaryFacilityType.COMMAND_CENTER))
				      .forEach(advancedFactories::add);
			}
		}

//		for (final GetCharactersCharacterIdPlanets200Ok colonyEsi : esiColonies) {
//			if (planetIdentifier == colonyEsi.getPlanetId()) {
//				this.planetType = colonyEsi.getPlanetType();
//				this.planetData = this.getEsiDataAdapter().getUniversePlanetsPlanetId(colonyEsi.getPlanetId());
//				final GetCharactersCharacterIdPlanetsPlanetIdOk structureContainer = this.getEsiDataAdapter().getCharactersCharacterIdPlanetsPlanetId(
//						credential.getAccountId(),
//						colonyEsi.getPlanetId(),
//						credential.getRefreshToken(),
//						credential.getDataSource());
//				if (null != structureContainer) {
//					final List<IPlanetaryFacility> facilities = this.convertPin2Facility(structureContainer.getPins()
//							, PlanetType.valueOf(colonyEsi.getPlanetType().name()));
//					Stream.of(facilities)
//					      .filter(facility -> (facility.getFacilityType() == PlanetaryFacilityType.PLANETARY_FACTORY
//							      || facility.getFacilityType() == PlanetaryFacilityType.COMMAND_CENTER))
//					      .forEach(advancedFactories::add);
//					new ColonyPack.Builder()
//							.withColony(colonyEsi)
//							.withPlanetData(planetData)
//							.withFacilities(advancedFactories)
//							.withPilotIdentifier(this.credential.getAccountId())
//							.build();
//				}
//				break;
//			}
//		}
	}

	@Override
	public void collaborate2Model() {
		for (final IPlanetaryFacility facility : this.advancedFactories) {
			this.addModelContents(facility);
		}
//		this.fragment.setBackground(this.planetType);
	}

	// - B U I L D E R
	public static class Builder extends BaseBuilder<VTAdvancedFactoryDataSource, VTAdvancedFactoryDataSource.Builder> {
		private VTAdvancedFactoryDataSource onConstruction;

		@Override
		protected VTAdvancedFactoryDataSource getActual() {
			if (null == this.onConstruction) this.onConstruction = new VTAdvancedFactoryDataSource();
			return this.onConstruction;
		}

		@Override
		protected VTAdvancedFactoryDataSource.Builder getActualBuilder() {
			return this;
		}

		@Override
		public VTAdvancedFactoryDataSource build() {
			final VTAdvancedFactoryDataSource ds = super.build();
			Objects.requireNonNull(this.onConstruction.credential);
			Objects.requireNonNull(this.onConstruction.esiDataAdapter);
			Objects.requireNonNull(this.onConstruction.planetaryRepository);
			return ds;
		}

		public VTAdvancedFactoryDataSource.Builder withCredential( final Credential credential ) {
			Objects.requireNonNull(credential);
			this.onConstruction.credential = credential;
			return this;
		}

		public VTAdvancedFactoryDataSource.Builder withEsiDataAdapter( final ESIDataAdapter esiDataAdapter ) {
			Objects.requireNonNull(esiDataAdapter);
			this.onConstruction.esiDataAdapter = esiDataAdapter;
			return this;
		}

		public VTAdvancedFactoryDataSource.Builder withPlanetaryRepository( final PlanetaryRepository planetaryRepository ) {
			Objects.requireNonNull(planetaryRepository);
			this.onConstruction.planetaryRepository = planetaryRepository;
			return this;
		}

		public VTAdvancedFactoryDataSource.Builder withFragment( final VTAdvancedFacilityFragment fragment ) {
			Objects.requireNonNull(fragment);
			this.onConstruction.fragment = fragment;
			return this;
		}
	}
}
