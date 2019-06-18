package org.dimensinfin.eveonline.neocom.appvisualtest.datasources;

import java.util.ArrayList;
import java.util.List;

import org.dimensinfin.android.mvc.datasource.AMVCDataSource;
import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkContents;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetailsHeads;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkPins;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.planetary.CommandCenterFacility;
import org.dimensinfin.eveonline.neocom.planetary.ExtractorFacility;
import org.dimensinfin.eveonline.neocom.planetary.IPlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.PlanetType;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryFacility;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryStorage;

import org.joda.time.DateTime;

public class VisualTestsLayoutDataSource extends AMVCDataSource {
	private ESIDataAdapter esiDataAdapter /*= NeoComComponentFactory.getSingleton().getEsiDataAdapter()*/;
	private List<IPlanetaryFacility> facilities = new ArrayList<>();

	@Override
	public void prepareModel() {
		final GetCharactersCharacterIdPlanetsPlanetIdOkPins pin = new GetCharactersCharacterIdPlanetsPlanetIdOkPins();
		pin.setPinId(1012696414582L);
		pin.setTypeId(2524);
		pin.setLatitude(0.0F);
		pin.setLongitude(0.0F);

		GetCharactersCharacterIdPlanetsPlanetIdOkContents content = new GetCharactersCharacterIdPlanetsPlanetIdOkContents();
		content.setTypeId(3689);
		content.setAmount(100L);
		pin.addContentsItem(content);

		PlanetaryFacility baseFacility = new PlanetaryFacility.Builder()
				                                 .withEsiDataAdapter(esiDataAdapter)
				                                 .withPlanetType(PlanetType.GAS)
				                                 .withFacilityItem(new EveItem(2136))
				                                 .withPin(pin)
				                                 .build();
		final CommandCenterFacility commandCenter = new CommandCenterFacility.Builder()
				                                            .withLevel(2)
				                                            .withPlanetaryFacility(baseFacility)
				                                            .withPlanetaryStorage(new PlanetaryStorage(baseFacility.getContents(), this.esiDataAdapter))
				                                            .build();
		commandCenter.setCpuInUse(1000);
		commandCenter.setPowerInUse(3000);
		this.facilities.add(commandCenter);

		// - EXTRACTOR FACILITY - RUNNING
//		final GetCharactersCharacterIdPlanetsPlanetIdOkPins extractorPin = new GetCharactersCharacterIdPlanetsPlanetIdOkPins();
//		extractorPin.setPinId(1024811499544L);
//		extractorPin.setTypeId(2848);
//		extractorPin.setInstallTime(new DateTime("2019-06-14T23:33:07Z"));
//		extractorPin.setLastCycleStart(DateTime.now().minusHours(1));
//		extractorPin.setLatitude(0.0F);
//		extractorPin.setLongitude(0.0F);
//		extractorDetails = new GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails();
//		extractorDetails.setCycleTime(7200);
//		extractorDetails.setProductTypeId(2270);
//		extractorDetails.setQtyPerCycle(5376);
//		extractorDetails.setHeadRadius(0.0337348617613F);
//		extractorHead = new GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetailsHeads();
//		extractorHead.setHeadId(0);
//		extractorDetails.addHeadsItem(extractorHead);
//		extractorDetails.addHeadsItem(extractorHead);
//		extractorDetails.addHeadsItem(extractorHead);
//		extractorDetails.addHeadsItem(extractorHead);
//		extractorDetails.addHeadsItem(extractorHead);
//		extractorPin.setExtractorDetails(extractorDetails);

//		baseFacility = new PlanetaryFacility.Builder()
//				               .withEsiDataAdapter(esiDataAdapter)
//				               .withPlanetType(PlanetType.GAS)
//				               .withFacilityItem(new EveItem(extractorPin.getTypeId()))
//				               .withPin(extractorPin)
//				               .build();
//		final ExtractorFacility extractorFacility = new ExtractorFacility.Builder()
//				                                            .withPlanetaryFacility(baseFacility)
//				                                            .withExtractorDetails(extractorPin)
//				                                            .build();
//		this.facilities.add(extractorFacility);
	}

	@Override
	public void collaborate2Model() {
		for (IPlanetaryFacility facility : this.facilities) {
			this.addModelContents(facility);
		}
	}
	// - B U I L D E R
	public static class Builder extends AMVCDataSource.BaseBuilder<VisualTestsLayoutDataSource, VisualTestsLayoutDataSource.Builder> {
		private VisualTestsLayoutDataSource onConstruction;

		@Override
		protected VisualTestsLayoutDataSource getActual() {
			if (null == this.onConstruction) this.onConstruction = new VisualTestsLayoutDataSource();
			return this.onConstruction;
		}

		@Override
		protected VisualTestsLayoutDataSource.Builder getActualBuilder() {
			return this;
		}

		public VisualTestsLayoutDataSource.Builder withEsiDataAdapter( final ESIDataAdapter esiDataAdapter ) {
			this.onConstruction.esiDataAdapter = esiDataAdapter;
			return this;
		}
	}
}
