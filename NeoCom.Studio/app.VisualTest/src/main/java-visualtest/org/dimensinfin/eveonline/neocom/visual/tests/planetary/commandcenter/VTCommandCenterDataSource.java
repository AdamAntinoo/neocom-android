package org.dimensinfin.eveonline.neocom.visual.tests.planetary.commandcenter;

import com.annimon.stream.Stream;

import org.dimensinfin.android.mvc.datasource.MVCDataSource;
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
import org.dimensinfin.eveonline.neocom.planetary.facilities.CommandCenterFacility;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Create a set of Command center with different characteristics to be check the drawing and the data displayed.
 */
public class VTCommandCenterDataSource extends PlanetaryCommonDataSource {
	//	private VTAdvancedFacilityFragment fragment;
	private GetCharactersCharacterIdPlanets200Ok.PlanetTypeEnum planetType;
	private GetUniversePlanetsPlanetIdOk planetData;
	private List<IPlanetaryFacility> commandCenterFactories = new ArrayList<>();
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
		planetIdentifier = 40208304;
//		final List<GetCharactersCharacterIdPlanets200Ok> esiColonies = this.getEsiDataAdapter().getCharactersCharacterIdPlanets(
//				credential.getAccountId(),
//				credential.getRefreshToken(),
//				credential.getDataSource());
//		for (GetCharactersCharacterIdPlanets200Ok colonyEsi : esiColonies) {
//			if (colonyEsi.getPlanetId() == this.planetIdentifier) { // Only process the colony for the selected planet
		final GetCharactersCharacterIdPlanets200Ok colonyEsi = new GetCharactersCharacterIdPlanets200Ok();
		colonyEsi.setPlanetId(planetIdentifier);
		colonyEsi.setNumPins(3);
		colonyEsi.setOwnerId(93813310);
		colonyEsi.setPlanetType(GetCharactersCharacterIdPlanets200Ok.PlanetTypeEnum.PLASMA);
		colonyEsi.setSolarSystemId(30003283);
		colonyEsi.setUpgradeLevel(2);
		final ColonyPack colony = new ColonyFactory.Builder()
				.withEsiDataAdapter(this.getEsiDataAdapter())
				.withCredential(this.getCredential())
				.withPlanetaryRepository(this.planetaryRepository)
				.build()
				.accessColony(colonyEsi);
		Stream.of(colony.getFacilities())
		      .filter(facility -> (facility.getFacilityType() == PlanetaryFacilityType.COMMAND_CENTER))
		      .forEach(commandCenterFactories::add);
	}

	@Override
	public void collaborate2Model() {
		for (int i = 0; i < this.commandCenterFactories.size(); i++) {
			// Change the data fr each of the command centers.
			final CommandCenterFacility commandCenter = (CommandCenterFacility) this.commandCenterFactories.get(i);
			if (0 == i) {
				commandCenter.setUpgradeLevel(2);
				commandCenter.setCpuInUse(5000);
				commandCenter.setPowerInUse(8000);
				this.addModelContents(commandCenter);
			}
//			if (1 == i) {
//				commandCenter.setUpgradeLevel(2);
//				commandCenter.setCpuInUse(12136);
//				commandCenter.setPowerInUse(12000);
//				commandCenter.setCommandCenterPosition(new FacilityGeoPosition()
//						.setLatitude(2.30938039038)
//						.setLongitude(1.99885095517));
//				this.addModelContents(commandCenter);
//			}
//			if (2 == i) {
//				commandCenter.setUpgradeLevel(2);
//				commandCenter.setCpuInUse(0);
//				commandCenter.setPowerInUse(0);
//				commandCenter.setCommandCenterPosition(new FacilityGeoPosition()
//						.setLatitude(2.30938039038)
//						.setLongitude(1.99885095517));
//				this.addModelContents(commandCenter);
//			}
		}
	}

	// - B U I L D E R
	public static class Builder extends MVCDataSource.Builder<VTCommandCenterDataSource, VTCommandCenterDataSource.Builder> {
		private VTCommandCenterDataSource onConstruction;

		@Override
		protected VTCommandCenterDataSource getActual() {
			if (null == this.onConstruction) this.onConstruction = new VTCommandCenterDataSource();
			return this.onConstruction;
		}

		@Override
		protected VTCommandCenterDataSource.Builder getActualBuilder() {
			return this;
		}

		@Override
		public VTCommandCenterDataSource build() {
			final VTCommandCenterDataSource ds = super.build();
			Objects.requireNonNull(this.onConstruction.credential);
			Objects.requireNonNull(this.onConstruction.esiDataAdapter);
			Objects.requireNonNull(this.onConstruction.planetaryRepository);
			return ds;
		}

		public VTCommandCenterDataSource.Builder withCredential( final Credential credential ) {
			Objects.requireNonNull(credential);
			this.onConstruction.credential = credential;
			return this;
		}

		public VTCommandCenterDataSource.Builder withEsiDataAdapter( final ESIDataAdapter esiDataAdapter ) {
			Objects.requireNonNull(esiDataAdapter);
			this.onConstruction.esiDataAdapter = esiDataAdapter;
			return this;
		}

		public VTCommandCenterDataSource.Builder withPlanetaryRepository( final PlanetaryRepository planetaryRepository ) {
			Objects.requireNonNull(planetaryRepository);
			this.onConstruction.planetaryRepository = planetaryRepository;
			return this;
		}

//		public VTCommandCenterDataSource.Builder withFragment( final VTAdvancedFacilityFragment fragment ) {
//			Objects.requireNonNull(fragment);
//			this.onConstruction.fragment = fragment;
//			return this;
//		}
	}
}
