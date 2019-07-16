package org.dimensinfin.eveonline.neocom.planetary.facilities;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetailsHeads;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkPins;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.planetary.PlanetType;
import org.dimensinfin.eveonline.neocom.support.EsiDataAdapterSupportTest;
import org.dimensinfin.eveonline.neocom.support.PojoTestUtils;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExtractorFacilityTest extends EsiDataAdapterSupportTest {
	private static final PlanetType TEST_PLANET_TYPE_BARREN = PlanetType.BARREN;
	private static final int EXTRACTOR_FACILITY_TYPE = 2848;
	private static PlanetaryFacility facility;
	private static GetCharactersCharacterIdPlanetsPlanetIdOkPins pin;

	@Before
	public void setUp()  {
		super.setUp();
		facility = Mockito.mock(PlanetaryFacility.class);
		pin = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOkPins.class);
	}

//	@Test
	public void accessorContract() {
		PojoTestUtils.validateAccessors(ExtractorFacility.class);
	}

	@Test
	public void build_idle_complete() {
		final GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails extractorDetails = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails.class);
		Mockito.when(pin.getExtractorDetails()).thenReturn(extractorDetails);
		Mockito.when(pin.getInstallTime()).thenReturn(null);
		final ExtractorFacility extractor = new ExtractorFacility.Builder()
				.withPlanetaryFacility(facility)
				.withExtractorDetails(pin)
				.build();
		Assert.assertNotNull(extractor);
	}

	@Test
	public void build_running_complete() {
		final GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails extractorDetails = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails.class);
		Mockito.when(pin.getExtractorDetails()).thenReturn(extractorDetails);
		Mockito.when(pin.getInstallTime()).thenReturn(DateTime.now());
		final ExtractorFacility extractor = new ExtractorFacility.Builder()
				.withPlanetaryFacility(facility)
				.withExtractorDetails(pin)
				.build();
		Assert.assertNotNull(extractor);
	}

	@Test
	public void collaborate2Model() {
		final GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails extractorDetails = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails.class);
		Mockito.when(pin.getExtractorDetails()).thenReturn(extractorDetails);
		final ExtractorFacility extractor = new ExtractorFacility.Builder()
				.withPlanetaryFacility(facility)
				.withExtractorDetails(pin)
				.build();
		Assert.assertNotNull(extractor);

		final List<ICollaboration> obtained = extractor.collaborate2Model("TEST-VARIANT");
		Assert.assertNotNull(obtained);
		Assert.assertTrue(obtained.size() == 0);
	}

	@Test
	public void getCycleTime_idle() {
		final GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails extractorDetails = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails.class);
		Mockito.when(pin.getExtractorDetails()).thenReturn(extractorDetails);
		Mockito.when(pin.getInstallTime()).thenReturn(null);
		final ExtractorFacility extractor = new ExtractorFacility.Builder()
				.withPlanetaryFacility(facility)
				.withExtractorDetails(pin)
				.build();
		Assert.assertNotNull(extractor);

		final Integer obtained = extractor.getCycleTime();
		Assert.assertNotNull(obtained);
		Assert.assertTrue(obtained == 0);
	}

	@Test
	public void getCycleTime_running() {
	final 	ExtractorFacility extractor = this.createFacility4Extractor(EXTRACTOR_FACILITY_TYPE, 1);
		extractor.setInstallTime(DateTime.now());
		Assert.assertNotNull(extractor);

		final Integer obtained = extractor.getCycleTime();
		Assert.assertNotNull(obtained);
		Assert.assertEquals("The cycle time should be the value inserted.", 3600 * 2, obtained.intValue());
	}

//	@Test
	public void getCycleTime_conversion() {
//		final GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails extractorDetails = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails.class);
//		Mockito.when(pin.getExtractorDetails()).thenReturn(extractorDetails);
//		Mockito.when(pin.getInstallTime()).thenReturn(DateTime.now());
//		Mockito.when(extractorDetails.getCycleTime()).thenReturn(3600);
//		Mockito.when(facility.getLastCycleStart()).thenReturn(DateTime.now().minusHours(1));
//		final ExtractorFacility extractor = new ExtractorFacility.Builder()
//				.withPlanetaryFacility(facility)
//				.withExtractorDetails(pin)
//				.build();

	final 	ExtractorFacility extractor = this.createFacility4Extractor(EXTRACTOR_FACILITY_TYPE, 1);
		extractor.setInstallTime(DateTime.now().minus(TimeUnit.MINUTES.toMillis(70)));
		Assert.assertNotNull(extractor);

		final Integer cycle = extractor.getCycleTime();
		final DateTime lastCycleStart = extractor.getLastCycleStart();
		final Seconds elapsed = Seconds.secondsBetween(lastCycleStart, DateTime.now());
		Assert.assertTrue("Check that the number of seconds elapsed is at least one hour.", elapsed.getSeconds() >= 3600);
		final DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
		final String formattedCycle = formatter.print(elapsed.getSeconds() % cycle) + " / " + formatter.print(cycle);
		Assert.assertEquals("Verify that the formatted time is just one hour.", "01:00:00", formattedCycle);

	}

	@Test
	public void getCpuUsage() {
		// Extractor with 1 heads
		ExtractorFacility facility = this.createFacility4Extractor(EXTRACTOR_FACILITY_TYPE, 1);
		Assert.assertNotNull(facility);
		Assert.assertEquals("Check the CPU used.", 400 + 110, facility.getCpuUsage());

		// Extractor with 4 heads
		facility = this.createFacility4Extractor(EXTRACTOR_FACILITY_TYPE, 4);
		Assert.assertNotNull(facility);
		Assert.assertEquals("Check the CPU used.", 400 + 110 * 4, facility.getCpuUsage());

		// Extractor with 6 heads
		facility = this.createFacility4Extractor(EXTRACTOR_FACILITY_TYPE, 6);
		Assert.assertNotNull(facility);
		Assert.assertEquals("Check the CPU used.", 400 + 110 * 6, facility.getCpuUsage());
	}

	@Test
	public void getPowerUsage() {
		// Extractor with 1 heads
		ExtractorFacility facility = this.createFacility4Extractor(EXTRACTOR_FACILITY_TYPE, 1);
		Assert.assertNotNull(facility);
		Assert.assertEquals("Check the Power used.", 2600 + 550, facility.getPowerUsage());

		// Extractor with 4 heads
		facility = this.createFacility4Extractor(EXTRACTOR_FACILITY_TYPE, 4);
		Assert.assertNotNull(facility);
		Assert.assertEquals("Check the Power used.", 2600 + 550 * 4, facility.getPowerUsage());

		// Extractor with 6 heads
		facility = this.createFacility4Extractor(EXTRACTOR_FACILITY_TYPE, 6);
		Assert.assertNotNull(facility);
		Assert.assertEquals("Check the Power used.", 2600 + 550 * 6, facility.getPowerUsage());
	}

	protected ExtractorFacility createFacility4Extractor( final int factoryTypeId, final int headCount ) {
		final GetCharactersCharacterIdPlanetsPlanetIdOkPins pin = new GetCharactersCharacterIdPlanetsPlanetIdOkPins();
		pin.setTypeId(factoryTypeId);
		pin.setPinId(1022847372295L);
		pin.setLastCycleStart(DateTime.now());
		pin.setLatitude(2.27239886788F);
		pin.setLongitude(1.97163458582F);
		final GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails extractorDetails =
				new GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails();
		extractorDetails.setCycleTime(new Long(TimeUnit.HOURS.toSeconds(2)).intValue());
		List<GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetailsHeads> heads = new ArrayList<>();
		for (int i = 0; i < headCount; i++)
			heads.add(new GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetailsHeads());
		extractorDetails.setHeads(heads);
		pin.setExtractorDetails(extractorDetails);
		final EveItem facilityItem = new EveItem(pin.getTypeId()); // The item for this facility.
		final PlanetaryFacility facility = new PlanetaryFacility.Builder()
				.withPin(pin)
				.withPlanetType(TEST_PLANET_TYPE_BARREN)
				.withFacilityItem(facilityItem)
				.build();
		return new ExtractorFacility.Builder()
				.withPlanetaryFacility(facility)
				.withExtractorDetails(pin)
				.build();
	}
}
