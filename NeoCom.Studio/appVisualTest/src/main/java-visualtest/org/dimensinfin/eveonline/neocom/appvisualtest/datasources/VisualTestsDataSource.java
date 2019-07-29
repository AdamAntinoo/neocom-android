package org.dimensinfin.eveonline.neocom.appvisualtest.datasources;

import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.app.PageNamesType;
import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;
import org.dimensinfin.eveonline.neocom.database.repositories.PlanetaryRepository;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetailsHeads;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkPins;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniversePlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseTypesTypeIdOk;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.planetary.ColonyPack;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.PlanetType;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryStorage;
import org.dimensinfin.eveonline.neocom.planetary.facilities.CommandCenterFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.ExtractorFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.FactoryFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.PlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.StorageFacility;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VisualTestsDataSource extends MVCDataSource {
	private ESIDataAdapter esiDataAdapter;
	private PlanetaryRepository planetaryRepository;
	private ColonyPack colonyPack;
	private List<IPlanetaryFacility> facilities = new ArrayList<>();

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

		final GetCharactersCharacterIdPlanetsPlanetIdOkPins pin = new GetCharactersCharacterIdPlanetsPlanetIdOkPins();
		pin.setPinId(1012696414582L);
		pin.setTypeId(2524);
		pin.setLatitude(2.25581721989F);
		pin.setLongitude(1.92512624408F);

		GetCharactersCharacterIdPlanetsPlanetIdOkContents content = new GetCharactersCharacterIdPlanetsPlanetIdOkContents();
		content.setTypeId(3689);
		content.setAmount(100L);
		pin.addContentsItem(content);

		PlanetaryFacility baseFacility = new PlanetaryFacility.Builder()
//				                                 .withEsiDataAdapter(esiDataAdapter)
				                                 .withPlanetType(PlanetType.GAS)
				                                 .withFacilityItem(new EveItem(facilityCommandCenterItem.getTypeId()))
				                                 .withPin(pin)
				                                 .build();
		final CommandCenterFacility commandCenter = new CommandCenterFacility.Builder()
//				                                            .wit(colony.getUpgradeLevel())
				                                            .withPlanetaryFacility(baseFacility)
				                                            .withPlanetaryStorage(new PlanetaryStorage(baseFacility.getContents()))
				                                            .build();
		facilities.add(commandCenter);

		// - STORAGE FACILITY
		final GetCharactersCharacterIdPlanetsPlanetIdOkPins storagePin = new GetCharactersCharacterIdPlanetsPlanetIdOkPins();
		storagePin.setPinId(1024811499528L);
		storagePin.setTypeId(2536);
		storagePin.setLatitude(2.24360776989F);
		storagePin.setLongitude(1.9253451294F);
		content = new GetCharactersCharacterIdPlanetsPlanetIdOkContents();
		content.setTypeId(9836);
		content.setAmount(1000L);
		storagePin.addContentsItem(content);
		content = new GetCharactersCharacterIdPlanetsPlanetIdOkContents();
		content.setTypeId(2309);
		content.setAmount(1227L);
		storagePin.addContentsItem(content);
		baseFacility = new PlanetaryFacility.Builder()
//				               .withEsiDataAdapter(esiDataAdapter)
				               .withPlanetType(PlanetType.GAS)
				               .withFacilityItem(new EveItem(2536))
				               .withPin(storagePin)
				               .build();
		final StorageFacility storageFacility = new StorageFacility.Builder()
				                                        .withPlanetaryFacility(baseFacility)
				                                        .withPlanetaryStorage(new PlanetaryStorage(storagePin.getContents()))
				                                        .build();
		facilities.add(storageFacility);

		// - EXTRACTOR FACILITY - INACTIVE
		final GetCharactersCharacterIdPlanetsPlanetIdOkPins extractorPinInactive = new GetCharactersCharacterIdPlanetsPlanetIdOkPins();
		extractorPinInactive.setPinId(1024811499533L);
		extractorPinInactive.setTypeId(2848);
		extractorPinInactive.setLastCycleStart(new DateTime("2019-06-14T23:07:15Z"));
		extractorPinInactive.setLatitude(2.23147946851F);
		extractorPinInactive.setLongitude(1.92579924956F);
		GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails extractorDetails = new GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails();
		GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetailsHeads extractorHead = new GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetailsHeads();
		extractorHead.setHeadId(0);
		extractorDetails.addHeadsItem(extractorHead);
		extractorDetails.addHeadsItem(extractorHead);
		extractorDetails.addHeadsItem(extractorHead);
		extractorDetails.addHeadsItem(extractorHead);
		extractorPinInactive.setExtractorDetails(extractorDetails);

		baseFacility = new PlanetaryFacility.Builder()
//				               .withEsiDataAdapter(esiDataAdapter)
				               .withPlanetType(PlanetType.GAS)
				               .withFacilityItem(new EveItem(extractorPinInactive.getTypeId()))
				               .withPin(extractorPinInactive)
				               .build();
		final ExtractorFacility extractorFacilityInactive = new ExtractorFacility.Builder()
				                                                    .withPlanetaryFacility(baseFacility)
				                                                    .withExtractorDetails(extractorPinInactive)
				                                                    .build();
		facilities.add(extractorFacilityInactive);

		// - EXTRACTOR FACILITY - RUNNING
		final GetCharactersCharacterIdPlanetsPlanetIdOkPins extractorPin = new GetCharactersCharacterIdPlanetsPlanetIdOkPins();
		extractorPin.setPinId(1024811499544L);
		extractorPin.setTypeId(2848);
		extractorPin.setInstallTime(new DateTime("2019-06-14T23:33:07Z"));
		extractorPin.setExpiryTime(DateTime.now().plus(TimeUnit.DAYS.toMillis(1)));
		extractorPin.setLastCycleStart(DateTime.now().minusHours(1));
		extractorPin.setLatitude(2.28056793197F);
		extractorPin.setLongitude(1.92444325504F);
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
//				               .withEsiDataAdapter(esiDataAdapter)
				               .withPlanetType(PlanetType.GAS)
				               .withFacilityItem(new EveItem(extractorPin.getTypeId()))
				               .withPin(extractorPin)
				               .build();
		final ExtractorFacility extractorFacility = new ExtractorFacility.Builder()
				                                            .withPlanetaryFacility(baseFacility)
				                                            .withExtractorDetails(extractorPin)
				                                            .build();
		facilities.add(extractorFacility);

		// - EXTRACTOR FACILITY - EXPIRED
		final GetCharactersCharacterIdPlanetsPlanetIdOkPins extractorExpiredPin = new GetCharactersCharacterIdPlanetsPlanetIdOkPins();
		extractorExpiredPin.setPinId(1023334852408L);
		extractorExpiredPin.setTypeId(3060);
		extractorExpiredPin.setInstallTime(new DateTime("2019-06-14T23:33:07Z"));
		extractorExpiredPin.setExpiryTime(DateTime.now().minus(TimeUnit.HOURS.toMillis(2)));
		extractorExpiredPin.setLastCycleStart(DateTime.now().minusHours(1));
		extractorExpiredPin.setLatitude(-0.002F);
		extractorExpiredPin.setLongitude(0.060F);
		extractorDetails = new GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails();
		extractorDetails.setCycleTime(7200);
		extractorDetails.setProductTypeId(2268);
		extractorDetails.setQtyPerCycle(4672);
		extractorDetails.setHeadRadius(0.0337348617613F);
		extractorHead = new GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetailsHeads();
		extractorHead.setHeadId(0);
		extractorDetails.addHeadsItem(extractorHead);
		extractorDetails.addHeadsItem(extractorHead);
		extractorDetails.addHeadsItem(extractorHead);
		extractorDetails.addHeadsItem(extractorHead);
		extractorDetails.addHeadsItem(extractorHead);
		extractorExpiredPin.setExtractorDetails(extractorDetails);

		baseFacility = new PlanetaryFacility.Builder()
//				               .withEsiDataAdapter(esiDataAdapter)
				               .withPlanetType(PlanetType.GAS)
				               .withFacilityItem(new EveItem(extractorExpiredPin.getTypeId()))
				               .withPin(extractorExpiredPin)
				               .build();
		final ExtractorFacility extractorExpiredFacility = new ExtractorFacility.Builder()
				                                                   .withPlanetaryFacility(baseFacility)
				                                                   .withExtractorDetails(extractorExpiredPin)
				                                                   .build();
		facilities.add(extractorExpiredFacility);

		// - BASE INDUSTRY FACILITY
		GetCharactersCharacterIdPlanetsPlanetIdOkPins baseIndustryPin = new GetCharactersCharacterIdPlanetsPlanetIdOkPins();
		baseIndustryPin.setPinId(1023355532557L);
		baseIndustryPin.setTypeId(2492);
		baseIndustryPin.setLastCycleStart(new DateTime("2019-06-02T08:40:35Z"));
		baseIndustryPin.setSchematicId(123);
		baseIndustryPin.setLatitude(2.2622209669F);
		baseIndustryPin.setLongitude(1.93942649917F);
		content = new GetCharactersCharacterIdPlanetsPlanetIdOkContents();
		content.setTypeId(2309);
		content.setAmount(2950L);
		baseIndustryPin.addContentsItem(content);
		baseFacility = new PlanetaryFacility.Builder()
//				               .withEsiDataAdapter(esiDataAdapter)
				               .withPlanetType(PlanetType.GAS)
				               .withFacilityItem(new EveItem(baseIndustryPin.getTypeId()))
				               .withPin(baseIndustryPin)
				               .build();
		FactoryFacility baseIndustryFacility = new FactoryFacility.Builder()
				                                       .withPlanetaryFacility(baseFacility)
				                                       .withSchematics(baseIndustryPin.getSchematicId())
				                                       .withPlanetaryRepository(this.planetaryRepository)
				                                       .build();
		facilities.add(baseIndustryFacility);

		// - BASE INDUSTRY FACILITY
		baseIndustryPin = new GetCharactersCharacterIdPlanetsPlanetIdOkPins();
		baseIndustryPin.setPinId(1023355532557L);
		baseIndustryPin.setTypeId(2492);
		baseIndustryPin.setLastCycleStart(new DateTime("2019-06-02T08:40:35Z"));
		baseIndustryPin.setSchematicId(123);
		baseIndustryPin.setLatitude(2.2622209669F);
		baseIndustryPin.setLongitude(1.93942649917F);
		content = new GetCharactersCharacterIdPlanetsPlanetIdOkContents();
		content.setTypeId(2309);
		content.setAmount(3000L);
		baseIndustryPin.addContentsItem(content);
		baseFacility = new PlanetaryFacility.Builder()
//				               .withEsiDataAdapter(esiDataAdapter)
				               .withPlanetType(PlanetType.GAS)
				               .withFacilityItem(new EveItem(baseIndustryPin.getTypeId()))
				               .withPin(baseIndustryPin)
				               .build();
		baseIndustryFacility = new FactoryFacility.Builder()
				                       .withPlanetaryFacility(baseFacility)
				                       .withSchematics(baseIndustryPin.getSchematicId())
				                       .withPlanetaryRepository(this.planetaryRepository)
				                       .build();
		facilities.add(baseIndustryFacility);

		// - BASE INDUSTRY FACILITY EMPTY - USED FOR POSITIONING
		baseIndustryPin = new GetCharactersCharacterIdPlanetsPlanetIdOkPins();
		baseIndustryPin.setPinId(1023355532557L);
		baseIndustryPin.setTypeId(2492);
		baseIndustryPin.setLastCycleStart(new DateTime("2019-06-02T08:40:35Z"));
		baseIndustryPin.setSchematicId(123);
		baseIndustryPin.setLatitude(2.24941441851F);
		baseIndustryPin.setLongitude(1.91171681673F);
		content = new GetCharactersCharacterIdPlanetsPlanetIdOkContents();
		content.setTypeId(2309);
		content.setAmount(1350L);
		baseIndustryPin.addContentsItem(content);
		baseFacility = new PlanetaryFacility.Builder()
//				               .withEsiDataAdapter(esiDataAdapter)
				               .withPlanetType(PlanetType.GAS)
				               .withFacilityItem(new EveItem(baseIndustryPin.getTypeId()))
				               .withPin(baseIndustryPin)
				               .build();
		final FactoryFacility baseIndustryFacility2 = new FactoryFacility.Builder()
				                                              .withPlanetaryFacility(baseFacility)
				                                              .withSchematics(baseIndustryPin.getSchematicId())
				                                              .withPlanetaryRepository(this.planetaryRepository)
				                                              .build();
		facilities.add(baseIndustryFacility2);

		// - ADVANCED INDUSTRY FACILITY EMPTY
		baseIndustryPin = new GetCharactersCharacterIdPlanetsPlanetIdOkPins();
		baseIndustryPin.setPinId(1023415101256L);
		baseIndustryPin.setTypeId(2494);
		baseIndustryPin.setLastCycleStart(new DateTime("2019-06-02T08:10:35Z"));
		baseIndustryPin.setSchematicId(66);
		baseIndustryPin.setLatitude(2.25494623457F);
		baseIndustryPin.setLongitude(1.897539911F);
		content = new GetCharactersCharacterIdPlanetsPlanetIdOkContents();
		content.setTypeId(2390);
		content.setAmount(40L);
		baseIndustryPin.addContentsItem(content);
		baseFacility = new PlanetaryFacility.Builder()
//				               .withEsiDataAdapter(esiDataAdapter)
				               .withPlanetType(PlanetType.GAS)
				               .withFacilityItem(new EveItem(baseIndustryPin.getTypeId()))
				               .withPin(baseIndustryPin)
				               .build();
		final FactoryFacility baseIndustryFacility3 = new FactoryFacility.Builder()
				                                              .withPlanetaryFacility(baseFacility)
				                                              .withSchematics(baseIndustryPin.getSchematicId())
				                                              .withPlanetaryRepository(this.planetaryRepository)
				                                              .build();
		facilities.add(baseIndustryFacility3);

		colony.setNumPins(facilities.size());
		colonyPack = new ColonyPack.Builder()
				             .withPilotIdentifier(123456)
				             .withColony(colony)
				             .withPlanetData(planetData)
				             .withFacilities(facilities)
				             .build();
	}

	@Override
	public void collaborate2Model() {
		if (this.getVariant().equalsIgnoreCase(PageNamesType.SPLASH.name())) {
			// - S P L A S H
			this.addModelContents(new AppVersion());
			this.addModelContents(new AppVersion().setName("NeoCom").setVersion("v 0.16 VT"));
		}
		if (this.getVariant().equalsIgnoreCase(PageNamesType.PLANETS_LIST.name())) {
			// -  P L A N E T A R Y
			this.addModelContents(colonyPack);
		}
		if (this.getVariant().equalsIgnoreCase(PageNamesType.PLANET_FACILITIES_LAYOUT.name())) {
			for (IPlanetaryFacility facility : this.facilities) {
				this.addModelContents(facility);
			}
		}
	}

	// - B U I L D E R
	public static class Builder extends MVCDataSource.BaseBuilder<VisualTestsDataSource, Builder> {
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

		public Builder withPlanetaryRepository( final PlanetaryRepository planetaryRepository ) {
			Objects.requireNonNull(planetaryRepository);
			this.onConstruction.planetaryRepository = planetaryRepository;
			return this;
		}
	}
}
