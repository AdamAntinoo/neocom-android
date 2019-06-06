package org.dimensinfin.eveonline.neocom.appvisualtest.datasources;

import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;
import org.dimensinfin.eveonline.neocom.datamngmt.ESIGlobalAdapter;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkPins;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniversePlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseTypesTypeIdOk;
import org.dimensinfin.eveonline.neocom.planetary.ColonyPack;
import org.dimensinfin.eveonline.neocom.planetary.CommandCenterFacility;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.PlanetType;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryFacility;

public class VisualTestsDataSource extends AMVCDataSource {
	private static ESIGlobalAdapter esiAdapter = NeoComComponentFactory.getSingleton().getEsiAdapter();
	private ColonyPack colonyPack;

	@Override
	public void prepareModel() {
		// - S P L A S H
		// - P L A N E T A R Y
		final GetCharactersCharacterIdPlanets200Ok colony = new GetCharactersCharacterIdPlanets200Ok();
		colony.setOwnerId(92223647);
		colony.setPlanetId(40208073);
		colony.setNumPins(1);
		colony.setUpgradeLevel(1);
		colony.setSolarSystemId(30003280);
		colony.setPlanetType(GetCharactersCharacterIdPlanets200Ok.PlanetTypeEnum.GAS);

		final GetUniversePlanetsPlanetIdOk planetData = new GetUniversePlanetsPlanetIdOk();
		planetData.setPlanetId(40208073);
		planetData.setName("Planet Name IV");
		planetData.setSystemId(30003280);
		planetData.setTypeId(2016);

		final GetUniverseTypesTypeIdOk planetItem = new GetUniverseTypesTypeIdOk();
		planetItem.name("Planet (Barren)");
		planetItem.typeId(2524);
		planetItem.groupId(1027);
		final GetUniverseTypesTypeIdOk facilityItem = new GetUniverseTypesTypeIdOk();
		facilityItem.name("Barren Command Center");
		facilityItem.typeId(2524);
		facilityItem.groupId(1027);

		final GetCharactersCharacterIdPlanetsPlanetIdOkPins pin = new GetCharactersCharacterIdPlanetsPlanetIdOkPins();
		pin.setPinId(1012696414582L);
		pin.setTypeId(2524);
		//		pin.setG

		final List<IPlanetaryFacility> facilities = new ArrayList<>();
		final CommandCenterFacility commandCenter = new CommandCenterFacility.Builder()
															.withLevel(colony.getUpgradeLevel())
				                                            .withPlanetaryFacility(new PlanetaryFacility.Builder()
						                                                                   .withEsiAdapter(esiAdapter)
						                                                                   .withPlanetType(PlanetType.GAS)
						                                                                   .withPlanetItem(planetItem)
						                                                                   .withFacitityItem(facilityItem)
						                                                                   .withPin(pin)
						                                                                   .build())
				                                            .build();
		facilities.add(commandCenter);
		colonyPack = new ColonyPack.Builder()
				             .withCapsuleerIdentifier(123456)
				             .withColony(colony)
				             .withPlanetData(planetData)
				             .withFacilities(facilities)
				             .build();

	}

	@Override
	public void collaborate2Model() {
		// - S P L A S H
		this.addModelContents(new AppVersion());
		this.addModelContents(new AppVersion().setName("Visual Test Name"));
		this.addModelContents(new AppVersion().setVersion("Visual Test Version"));

		// -  P L A N E T A R Y
		this.addModelContents(colonyPack);
	}

	// - B U I L D E R
	public static class Builder extends AMVCDataSource.BaseBuilder<VisualTestsDataSource, Builder> {
		private VisualTestsDataSource onConstruction;

		@Override
		protected VisualTestsDataSource getActual() {
			if (null == this.onConstruction) this.onConstruction = new VisualTestsDataSource();
			return this.onConstruction;
		}

		@Override
		protected Builder getActualBuilder() {
			return this;
		}
	}
}
