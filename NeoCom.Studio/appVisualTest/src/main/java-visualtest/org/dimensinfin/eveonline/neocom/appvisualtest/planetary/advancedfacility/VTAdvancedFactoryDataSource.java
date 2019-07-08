package org.dimensinfin.eveonline.neocom.appvisualtest.planetary.advancedfacility;

import java.util.List;
import java.util.Objects;

import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.adapters.NeoComRetrofitFactory;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.PlanetaryRepository;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;

public class VTAdvancedFactoryDataSource extends AMVCDataSource {
//	private List<Pilot> testPilots = new ArrayList<>();

	private Credential credential;
	private ESIDataAdapter esiDataAdapter;
	private PlanetaryRepository planetaryRepository;

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
		final List<GetCharactersCharacterIdPlanets200Ok> esiColonies = this.esiDataAdapter.getCharactersCharacterIdPlanets(
				credential.getAccountId()
				, credential.getRefreshToken()
				, credential.getDataSource());
//		final Pilot pilot = new Pilot.Builder()
//									.withPilotIdentifier(this.credential.getAccountId())
//				                    .withCharacterPublicData(characterPublicData)
//				                    .build();
//		this.testPilots.add(pilot);
	}

	@Override
	public void collaborate2Model() {
//		for (Pilot pilot : this.testPilots) {
//			this.addHeaderContents(pilot);
//		}
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
	}
}
