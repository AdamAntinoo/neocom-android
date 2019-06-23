package org.dimensinfin.eveonline.neocom.planetary;

import java.util.List;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.adapters.ESIDataAdapter;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkPins;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.planetary.facilities.ExtractorFacility;
import org.dimensinfin.eveonline.neocom.planetary.facilities.PlanetaryFacility;
import org.dimensinfin.eveonline.neocom.support.PojoTestUtils;
import org.dimensinfin.eveonline.neocom.support.TestConfigurationProvider;
import org.dimensinfin.eveonline.neocom.support.TestFileSystem;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ExtractorFacilityTest {
	private static PlanetaryFacility facility;
	private static GetCharactersCharacterIdPlanetsPlanetIdOkPins pin;
	private static ESIDataAdapter esiDataAdapter;

	@Before
	public void setUp() throws Exception {
		facility = Mockito.mock(PlanetaryFacility.class);
		pin = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOkPins.class);
		esiDataAdapter = new ESIDataAdapter.Builder(
				new TestConfigurationProvider.Builder("properties").build(),
				new TestFileSystem()
		).build();
		EveItem.injectEsiDataAdapter(esiDataAdapter);
	}

	@Test
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
		final GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails extractorDetails = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails.class);
		Mockito.when(pin.getExtractorDetails()).thenReturn(extractorDetails);
		Mockito.when(pin.getInstallTime()).thenReturn(DateTime.now());
		Mockito.when(extractorDetails.getCycleTime()).thenReturn(3600);
		final ExtractorFacility extractor = new ExtractorFacility.Builder()
				                                    .withPlanetaryFacility(facility)
				                                    .withExtractorDetails(pin)
				                                    .build();
		Assert.assertNotNull(extractor);

		final Integer obtained = extractor.getCycleTime();
		Assert.assertNotNull(obtained);
		Assert.assertEquals("The cycle time should be the value inserted.", 3600, obtained.intValue());
	}

	@Test
	public void cycleTime_conversion() {
		final GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails extractorDetails = Mockito.mock(GetCharactersCharacterIdPlanetsPlanetIdOkExtractorDetails.class);
		Mockito.when(pin.getExtractorDetails()).thenReturn(extractorDetails);
		Mockito.when(pin.getInstallTime()).thenReturn(DateTime.now());
		Mockito.when(extractorDetails.getCycleTime()).thenReturn(3600);
		Mockito.when(facility.getLastCycleStart()).thenReturn(DateTime.now().minusHours(1));
		final ExtractorFacility extractor = new ExtractorFacility.Builder()
				                                    .withPlanetaryFacility(facility)
				                                    .withExtractorDetails(pin)
				                                    .build();
		Assert.assertNotNull(extractor);

		final Integer cycle = extractor.getCycleTime();
		final DateTime lastCycleStart = extractor.getLastCycleStart();
		final Seconds elapsed = Seconds.secondsBetween(lastCycleStart, DateTime.now());
		Assert.assertTrue("Check that the number of seconds elapsed is at least one hour.", elapsed.getSeconds() >= 3600);
		final DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
		final String formattedCycle = formatter.print(elapsed.getSeconds() % cycle) + " / " + formatter.print(cycle);
		Assert.assertEquals("Verify that the formatted time is just one hour.", "01:00:00", formattedCycle);

	}
}
