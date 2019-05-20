package org.dimensinfin.eveonline.neocom.app.pilot;

import java.util.concurrent.TimeUnit;

import org.dimensinfin.eveonline.neocom.datamngmt.ESIGlobalAdapter;
import org.dimensinfin.eveonline.neocom.entities.Credential;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetUniverseRaces200Ok;

import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

public class DataDownloaderServiceTest {

	@Test
	public void dataDownloaderBuilder() {
		final ESIGlobalAdapter adapter = Mockito.mock(ESIGlobalAdapter.class);
		final DataDownloaderService service = new DataDownloaderService.Builder()
				                                      .withEsiAdapter(adapter)
				                                      .build();
	}

	@Test(expected = NullPointerException.class)
	public void dataDownloaderBuilder_notesiadapter() {
		final DataDownloaderService service = new DataDownloaderService.Builder()
				                                      .build();
	}

	@Test
	public void accessPilotPublicData() throws InterruptedException {
		final ESIGlobalAdapter adapter = Mockito.mock(ESIGlobalAdapter.class);
		final GetCharactersCharacterIdOk publicData = Mockito.mock(GetCharactersCharacterIdOk.class);
		final IPilotDataDownloadCallback destination = Mockito.mock(IPilotDataDownloadCallback.class);
		final Credential credential = new Credential();

		final DataDownloaderService service = new DataDownloaderService.Builder()
				                                      .withEsiAdapter(adapter)
				                                      .build();
		Mockito.when(adapter.getCharactersCharacterId(any(Integer.class), any(String.class), any(String.class))).thenReturn(publicData);
		Mockito.when(destination.getCredential()).thenReturn(credential);
		service.accessPilotPublicData(destination, PilotDataSections.PILOT_PUBLICDATA);
		Thread.sleep(TimeUnit.SECONDS.toMillis(1));
		Mockito.verify(destination, times(1)).signalCompletion(PilotDataSections.PILOT_PUBLICDATA, publicData);
	}
	@Test
	public void accessPilotPublicData_notfound() throws InterruptedException {
		final ESIGlobalAdapter adapter = Mockito.mock(ESIGlobalAdapter.class);
		final GetCharactersCharacterIdOk publicData = Mockito.mock(GetCharactersCharacterIdOk.class);
		final IPilotDataDownloadCallback destination = Mockito.mock(IPilotDataDownloadCallback.class);
		final Credential credential = new Credential();

		final DataDownloaderService service = new DataDownloaderService.Builder()
				                                      .withEsiAdapter(adapter)
				                                      .build();
		Mockito.when(adapter.getCharactersCharacterId(any(Integer.class), any(String.class), any(String.class))).thenReturn(null);
		Mockito.when(destination.getCredential()).thenReturn(credential);
		service.accessPilotPublicData(destination, PilotDataSections.PILOT_PUBLICDATA);
		Thread.sleep(TimeUnit.SECONDS.toMillis(1));
		Mockito.verify(destination, times(0)).signalCompletion(PilotDataSections.PILOT_PUBLICDATA, publicData);
	}

	@Test
	public void accessPilotRace() throws InterruptedException {
		final ESIGlobalAdapter adapter = Mockito.mock(ESIGlobalAdapter.class);
		final GetUniverseRaces200Ok race = Mockito.mock(GetUniverseRaces200Ok.class);
		final DataDownloaderService service = new DataDownloaderService.Builder()
				                                      .withEsiAdapter(adapter)
				                                      .build();
		final IPilotDataDownloadCallback destination = Mockito.mock(IPilotDataDownloadCallback.class);
		Mockito.when(adapter.searchSDERace(12)).thenReturn(race);
		Mockito.when(destination.getRaceId()).thenReturn(12);
		service.accessPilotRace(destination, PilotDataSections.PILOT_RACE);
		Thread.sleep(TimeUnit.SECONDS.toMillis(1));
		Mockito.verify(destination, times(1)).signalCompletion(PilotDataSections.PILOT_RACE, race);
	}
	@Test
	public void accessPilotRace_notfound() throws InterruptedException {
		final ESIGlobalAdapter adapter = Mockito.mock(ESIGlobalAdapter.class);
		final GetUniverseRaces200Ok race = Mockito.mock(GetUniverseRaces200Ok.class);
		final DataDownloaderService service = new DataDownloaderService.Builder()
				                                      .withEsiAdapter(adapter)
				                                      .build();
		final IPilotDataDownloadCallback destination = Mockito.mock(IPilotDataDownloadCallback.class);
		Mockito.when(adapter.searchSDERace(12)).thenReturn(null);
		Mockito.when(destination.getRaceId()).thenReturn(12);
		service.accessPilotRace(destination, PilotDataSections.PILOT_RACE);
		Thread.sleep(TimeUnit.SECONDS.toMillis(1));
		Mockito.verify(destination, times(0)).signalCompletion(PilotDataSections.PILOT_RACE, race);
	}
}
