package org.dimensinfin.eveonline.neocom.appvisualtest.datasources;

import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;
import org.dimensinfin.eveonline.neocom.appvisualtest.activity.VisualTestsActivity;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkPins;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniversePlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseTypesTypeIdOk;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.planetary.ColonyPack;
import org.dimensinfin.eveonline.neocom.planetary.CommandCenterFacility;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.PlanetType;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryStorage;

public class VisualTestsDataSource extends AMVCDataSource {
	private ESIDataAdapter esiDataAdapter = NeoComComponentFactory.getSingleton().getEsiDataAdapter();
	private ColonyPack colonyPack;

	@Override
	public void prepareModel() {
		// - S P L A S H
		// - P L A N E T A R Y
		final GetCharactersCharacterIdPlanets200Ok colony = new GetCharactersCharacterIdPlanets200Ok();
		colony.setOwnerId(92223647);
		colony.setPlanetId(40208073);
		colony.setNumPins(1);
		colony.setUpgradeLevel(2);
		colony.setSolarSystemId(30003280);
		colony.setPlanetType(GetCharactersCharacterIdPlanets200Ok.PlanetTypeEnum.GAS);

		final GetUniversePlanetsPlanetIdOk planetData = new GetUniversePlanetsPlanetIdOk();
		planetData.setPlanetId(40208073);
		planetData.setName("Planet Name IV");
		planetData.setSystemId(30003280);
		planetData.setTypeId(2016);

		final GetUniverseTypesTypeIdOk facilityItemEsi = new GetUniverseTypesTypeIdOk();
		facilityItemEsi.name("Barren Command Center");
		facilityItemEsi.typeId(2524);
		facilityItemEsi.groupId(1027);
		final EveItem facilityItem = new EveItem(facilityItemEsi);
		final EveItem facilityItem2 = this.esiDataAdapter.searchEsiItem4Id(2524);

		final GetCharactersCharacterIdPlanetsPlanetIdOkPins pin = new GetCharactersCharacterIdPlanetsPlanetIdOkPins();
		pin.setPinId(1012696414582L);
		pin.setTypeId(2524);
		pin.setLatitude(0.0F);
		pin.setLongitude(0.0F);

		GetCharactersCharacterIdPlanetsPlanetIdOkContents content = new GetCharactersCharacterIdPlanetsPlanetIdOkContents();
		content.setTypeId(3689);
		content.setAmount(1000L);
		pin.addContentsItem(content);

		final List<IPlanetaryFacility> facilities = new ArrayList<>();
		final IPlanetaryFacility baseFacility = new PlanetaryFacility.Builder()
				                                        .withEsiDataAdapter(esiDataAdapter)
				                                        .withPlanetType(PlanetType.GAS)
				                                        .withFacitityItem(facilityItem2)
				                                        .withPin(pin)
				                                        .build();
		final CommandCenterFacility commandCenter = new CommandCenterFacility.Builder()
				                                            .withLevel(colony.getUpgradeLevel())
				                                            .withPlanetaryFacility(baseFacility)
				                                            .withPlanetaryStorage(new PlanetaryStorage(baseFacility.getContents(), this.esiDataAdapter))
				                                            .build();
		facilities.add(commandCenter);
		colony.setNumPins(facilities.size());
		colonyPack = new ColonyPack.Builder()
				             .withCapsuleerIdentifier(123456)
				             .withColony(colony)
				             .withPlanetData(planetData)
				             .withFacilities(facilities)
				             .build();
	}

	@Override
	public void collaborate2Model() {
		if (this.getVariant().equalsIgnoreCase(VisualTestsActivity.VisualTestPages.SPLASH.name())) {
			// - S P L A S H
			this.addModelContents(new AppVersion());
			this.addModelContents(new AppVersion().setName("Visual Test Name"));
			this.addModelContents(new AppVersion().setVersion("Visual Test Version"));
		}
		if (this.getVariant().equalsIgnoreCase(VisualTestsActivity.VisualTestPages.PLANETARY_COMMANDCENTER.name())) {
			// -  P L A N E T A R Y
			this.addModelContents(colonyPack);
		}
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
