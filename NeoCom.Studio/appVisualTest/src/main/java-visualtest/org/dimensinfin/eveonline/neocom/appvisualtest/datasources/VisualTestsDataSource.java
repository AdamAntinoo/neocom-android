package org.dimensinfin.eveonline.neocom.appvisualtest.datasources;

import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;
import org.dimensinfin.eveonline.neocom.appvisualtest.activity.VisualTestsActivity;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetailsHeads;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkPins;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniversePlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseTypesTypeIdOk;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.planetary.ColonyPack;
import org.dimensinfin.eveonline.neocom.planetary.CommandCenterFacility;
import org.dimensinfin.eveonline.neocom.planetary.ExtractorFacility;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.PlanetType;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryStorage;
import org.dimensinfin.eveonline.neocom.planetary.StorageFacility;

import org.joda.time.DateTime;

public class VisualTestsDataSource extends AMVCDataSource {
	private ESIDataAdapter esiDataAdapter /*= NeoComComponentFactory.getSingleton().getEsiDataAdapter()*/;
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

		final GetUniverseTypesTypeIdOk facilityCommandCenterItem = new GetUniverseTypesTypeIdOk();
		facilityCommandCenterItem.name("Barren Command Center");
		facilityCommandCenterItem.typeId(2136);
		facilityCommandCenterItem.groupId(1027);
		//		final EveItem facilityItem = new EveItem(2524);
		//		final GetUniverseTypesTypeIdOk facilityItem2 = this.esiDataAdapter.searchEsiItem4Id(2524);

		final GetCharactersCharacterIdPlanetsPlanetIdOkPins pin = new GetCharactersCharacterIdPlanetsPlanetIdOkPins();
		pin.setPinId(1012696414582L);
		pin.setTypeId(2524);
		pin.setLatitude(0.0F);
		pin.setLongitude(0.0F);

		GetCharactersCharacterIdPlanetsPlanetIdOkContents content = new GetCharactersCharacterIdPlanetsPlanetIdOkContents();
		content.setTypeId(3689);
		content.setAmount(100L);
		pin.addContentsItem(content);

		final List<IPlanetaryFacility> facilities = new ArrayList<>();
		PlanetaryFacility baseFacility = new PlanetaryFacility.Builder()
				                                 .withEsiDataAdapter(esiDataAdapter)
				                                 .withPlanetType(PlanetType.GAS)
				                                 .withFacilityItem(new EveItem(facilityCommandCenterItem.getTypeId()))
				                                 .withPin(pin)
				                                 .build();
		final CommandCenterFacility commandCenter = new CommandCenterFacility.Builder()
				                                            .withLevel(colony.getUpgradeLevel())
				                                            .withPlanetaryFacility(baseFacility)
				                                            .withPlanetaryStorage(new PlanetaryStorage(baseFacility.getContents(), this.esiDataAdapter))
				                                            .build();
		facilities.add(commandCenter);

		// - STORAGE FACILITY
		final GetCharactersCharacterIdPlanetsPlanetIdOkPins storagePin = new GetCharactersCharacterIdPlanetsPlanetIdOkPins();
		storagePin.setPinId(1024811499528L);
		storagePin.setTypeId(2536);
		storagePin.setLatitude(0.0F);
		storagePin.setLongitude(0.0F);
		content = new GetCharactersCharacterIdPlanetsPlanetIdOkContents();
		content.setTypeId(9836);
		content.setAmount(1000L);
		storagePin.addContentsItem(content);
		baseFacility = new PlanetaryFacility.Builder()
				               .withEsiDataAdapter(esiDataAdapter)
				               .withPlanetType(PlanetType.GAS)
				               .withFacilityItem(new EveItem(2536))
				               .withPin(storagePin)
				               .build();
		final StorageFacility storageFacility = new StorageFacility.Builder()
				                                        .withPlanetaryFacility(baseFacility)
				                                        .withPlanetaryStorage(new PlanetaryStorage(storagePin.getContents(), this.esiDataAdapter))
				                                        .build();
		facilities.add(storageFacility);

		// - EXTRACTOR FACILITY - INACTIVE
		final GetCharactersCharacterIdPlanetsPlanetIdOkPins extractorPinInactive = new GetCharactersCharacterIdPlanetsPlanetIdOkPins();
		extractorPinInactive.setPinId(1024811499533L);
		extractorPinInactive.setTypeId(2848);
		extractorPinInactive.setLastCycleStart(new DateTime("2019-06-14T23:07:15Z"));
		extractorPinInactive.setLatitude(0.0F);
		extractorPinInactive.setLongitude(0.0F);
		GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails extractorDetails = new GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails();
		GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetailsHeads extractorHead = new GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetailsHeads();
		extractorHead.setHeadId(0);
		extractorDetails.addHeadsItem(extractorHead);
		extractorDetails.addHeadsItem(extractorHead);
		extractorDetails.addHeadsItem(extractorHead);
		extractorDetails.addHeadsItem(extractorHead);
		extractorPinInactive.setExtractorDetails(extractorDetails);

		baseFacility = new PlanetaryFacility.Builder()
				               .withEsiDataAdapter(esiDataAdapter)
				               .withPlanetType(PlanetType.GAS)
				               .withFacilityItem(new EveItem(extractorPinInactive.getTypeId()))
				               .withPin(extractorPinInactive)
				               .build();
		final ExtractorFacility extractorFacilityInactive = new ExtractorFacility.Builder()
				                                            .withPlanetaryFacility(baseFacility)
				                                            .build();
		facilities.add(extractorFacilityInactive);

		// - EXTRACTOR FACILITY - RUNNING
		final GetCharactersCharacterIdPlanetsPlanetIdOkPins extractorPin = new GetCharactersCharacterIdPlanetsPlanetIdOkPins();
		extractorPin.setPinId(1024811499544L);
		extractorPin.setTypeId(2848);
		extractorPin.setInstallTime(new DateTime("2019-06-14T23:33:07Z"));
		extractorPin.setLastCycleStart(new DateTime("2019-06-14T23:33:07Z"));
		extractorPin.setLatitude(0.0F);
		extractorPin.setLongitude(0.0F);
		extractorDetails = new GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails();
		extractorDetails.setCycleTime(7200);
		extractorDetails.setProductTypeId(2270);
		extractorDetails.setQtyPerCycle(5376);
		extractorDetails.setHeadRadius(0.0337348617613F);
		extractorHead = new GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetailsHeads();
		extractorHead.setHeadId(0);
		extractorDetails.addHeadsItem(extractorHead);
		extractorDetails.addHeadsItem(extractorHead);
		extractorDetails.addHeadsItem(extractorHead);
		extractorDetails.addHeadsItem(extractorHead);
		extractorDetails.addHeadsItem(extractorHead);
		extractorPin.setExtractorDetails(extractorDetails);

		baseFacility = new PlanetaryFacility.Builder()
				               .withEsiDataAdapter(esiDataAdapter)
				               .withPlanetType(PlanetType.GAS)
				               .withFacilityItem(new EveItem(extractorPin.getTypeId()))
				               .withPin(extractorPin)
				               .build();
		final ExtractorFacility extractorFacility = new ExtractorFacility.Builder()
				                                            .withPlanetaryFacility(baseFacility)
				                                            .build();
		facilities.add(extractorFacility);

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
			this.addModelContents(new AppVersion().setName("NeoCom").setVersion("v 0.16 VT"));
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

		public Builder withEsiDataAdapter( final ESIDataAdapter esiDataAdapter ) {
			this.onConstruction.esiDataAdapter = esiDataAdapter;
			return this;
		}
	}
}
