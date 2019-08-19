package org.dimensinfin.eveonline.neocom.visual.tests.planetary;

import com.annimon.stream.Stream;

import org.dimensinfin.eveonline.neocom.adapters.NeoComRetrofitFactory;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.planetary.ColonyFactory;
import org.dimensinfin.eveonline.neocom.planetary.ColonyPack;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.datasource.PlanetaryCommonDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VTPlanetaryDataSource extends PlanetaryCommonDataSource {
	private static final int EXTRACTOR_TEST_PLANET_IDENTIFIER = 40208303;
	private int planetIdentifier;
	private List<IPlanetaryFacility> factories = new ArrayList<>();

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
		planetIdentifier = EXTRACTOR_TEST_PLANET_IDENTIFIER;
		final GetCharactersCharacterIdPlanets200Ok colonyEsi = new GetCharactersCharacterIdPlanets200Ok();
		colonyEsi.setPlanetId(planetIdentifier);
		colonyEsi.setNumPins(3);
		colonyEsi.setOwnerId(93813310);
		colonyEsi.setPlanetType(GetCharactersCharacterIdPlanets200Ok.PlanetTypeEnum.PLASMA);
		colonyEsi.setSolarSystemId(30003283);
		colonyEsi.setUpgradeLevel(3);
		final ColonyPack colony = new ColonyFactory.Builder()
				.withEsiDataAdapter(this.getEsiDataAdapter())
				.withCredential(this.getCredential())
				.withPlanetaryRepository(this.planetaryRepository)
				.build()
				.accessColony(colonyEsi);
		Stream.of(colony.getFacilities())
//		      .filter(facility -> (facility.getFacilityType() == PlanetaryFacilityType.EXTRACTOR_CONTROL_UNIT
//				      || facility.getFacilityType() == PlanetaryFacilityType.COMMAND_CENTER))
		      .forEach(factories::add);
	}

	@Override
	public void collaborate2Model() {
		for ( IPlanetaryFacility extractor : this.factories)
			this.addModelContents(extractor);
	}

	// - B U I L D E R
	public static class Builder extends PlanetaryCommonDataSource.Builder<VTPlanetaryDataSource, VTPlanetaryDataSource.Builder> {
		private VTPlanetaryDataSource onConstruction;

		@Override
		protected VTPlanetaryDataSource getActual() {
			if (null == this.onConstruction) this.onConstruction = new VTPlanetaryDataSource();
			return this.onConstruction;
		}

		@Override
		protected VTPlanetaryDataSource.Builder getActualBuilder() {
			return this;
		}

		@Override
		public VTPlanetaryDataSource build() {
			final VTPlanetaryDataSource ds = super.build();
			Objects.requireNonNull(this.onConstruction.credential);
			Objects.requireNonNull(this.onConstruction.esiDataAdapter);
			Objects.requireNonNull(this.onConstruction.planetaryRepository);
			return ds;
		}
	}
}
