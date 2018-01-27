//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API22.
//  DESCRIPTION: Android Application related to the Eve Online game. The purpose is to download and organize
//               the game data to help capsuleers organize and prioritize activities. The strong points are
//               help at the Industry level tracking and calculating costs and benefits. Also the market
//               information update service will help to identify best prices and locations.
//               Planetary Interaction and Ship fittings are point under development.
//               ESI authorization is a new addition that will give continuity and allow download game data
//               from the new CCP data services.
//               This is the Android application version but shares libraries and code with other application
//               designed for Spring Boot Angular 4 platform.
//               The model management is shown using a generic Model View Controller that allows make the
//               rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.test.android;

import android.support.test.runner.AndroidJUnit4;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.dimensinfin.eveonline.neocom.database.entity.ColonySerialized;
import org.dimensinfin.eveonline.neocom.database.entity.ColonyStorage;
import org.dimensinfin.eveonline.neocom.database.entity.Credential;
import org.dimensinfin.eveonline.neocom.datamngmt.manager.GlobalDataManager;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanetsPlanetIdOkPins;
import org.dimensinfin.eveonline.neocom.datamngmt.manager.ESINetworkManager;
import org.dimensinfin.eveonline.neocom.planetary.ColonyCoreStructure;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

import java.util.List;

/**
 * This test will download the colony structures for a preselected character (Beth Ripley [922223647]) and then use the
 * OK
 * classes to get the data json serialized and stored at the databse table ColonyStorage. After verifying the storage
 * is completed it will read it back and deserailize it to check that the result, mapped to MVC compliant classes
 * matches the contents expected and there are no transformation errors.
 *
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
@RunWith(AndroidJUnit4.class)
public class PlanetaryStorageTestUnit extends UpdateJobManagementTestUnit {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final ModelMapper modelMapper = new ModelMapper();

	static {
		modelMapper.getConfiguration()
		           .setFieldMatchingEnabled(true)
		           .setMethodAccessLevel(Configuration.AccessLevel.PRIVATE);
	}

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	public Credential initializeConfiguration () {
		// Load all the Credentials
		final Credential credential = NeoComModelStore.activateCredential(92223647);
		return credential;
	}

	public GetCharactersCharacterIdPlanetsPlanetIdOk getPlanetStructures4Credential (final Credential credental, final int
			planetid) {
		return ESINetworkManager.getCharactersCharacterIdPlanetsPlanetId(credental.getAccountId(), planetid, credental
				.getRefreshToken(), null);
	}

	@Test
	public void testStructureStore () throws Exception {
		logger.info(">> [PlanetaryStorageTestUnit.testStructureStore]");
		// Connect the credential
		final Credential credential = initializeConfiguration();
		// Get the planet identifier we like to test.
		final int planetid = 40208073;
		// Get the whole structures on that planet.
		final GetCharactersCharacterIdPlanetsPlanetIdOk data = getPlanetStructures4Credential(credential, planetid);
		// Convert again the OK data to a json string
		final List<GetCharactersCharacterIdPlanetsPlanetIdOkPins> pinList = data.getPins();
		for (GetCharactersCharacterIdPlanetsPlanetIdOkPins structureOK : pinList) {
			//			ColonyCoreStructure newstruct = modelMapper.map(structureOK, ColonyCoreStructure.class);
			// TODO Convert the structure to a serialized Json string and store it into the database for fast access.
			try {
				final String serialized = objectMapper.writeValueAsString(structureOK);
				final String storageIdentifier = GlobalDataManager.constructPlanetStorageIdentifier(credential.getAccountId()
						, planetid);
				final ColonyStorage storage = new ColonyStorage(structureOK.getPinId())
						.setPlanetIdentifier(storageIdentifier)
						.setColonySerialization(serialized)
						.store();
			} catch (JsonProcessingException jpe) {
				jpe.printStackTrace();
			}
		}
		logger.info("<< [PlanetaryStorageTestUnit.testStructureStore]");
	}

//	@Test
	public void testStructureRead () throws Exception {
		logger.info(">> [PlanetaryStorageTestUnit.testStructureStore]");
		// Connect the credential
		final Credential credential = initializeConfiguration();
		// Get the planet identifier we like to test.
		final int planetid = 40208073;
		// Read the data structures from the database.
		final String storageIdentifier = GlobalDataManager.constructPlanetStorageIdentifier(credential.getAccountId(),
				planetid);
		final String pinid = "1012696414582";
		final ColonyStorage data = GlobalDataManager.getHelper().getColonyStorageDao().queryForId(pinid);
		final GetCharactersCharacterIdPlanetsPlanetIdOkPins structure = objectMapper.readValue(data.getColonySerialization(), GetCharactersCharacterIdPlanetsPlanetIdOkPins.class);
		ColonyCoreStructure newstruct = modelMapper.map(structure, ColonyCoreStructure.class);
		logger.info("<< [PlanetaryStorageTestUnit.testStructureStore]");
	}

	@Test
	public void testStructureSerialization () throws Exception {
		logger.info(">> [PlanetaryStorageTestUnit.testStructureStore]");
		// Connect the credential
		final Credential credential = initializeConfiguration();
		// Get the planet identifier we like to test.
		final int planetid = 40208073;
		// Get the whole structures on that planet.
		final GetCharactersCharacterIdPlanetsPlanetIdOk data = getPlanetStructures4Credential(credential, planetid);
		// Convert again the OK data to a json string
		final List<GetCharactersCharacterIdPlanetsPlanetIdOkPins> pinList = data.getPins();
		for (GetCharactersCharacterIdPlanetsPlanetIdOkPins structureOK : pinList) {
			ColonyCoreStructure newstruct = modelMapper.map(structureOK, ColonyCoreStructure.class);
			// TODO Convert the structure to a serialized Json string and store it into the database for fast access.
			try {
//				final String serialized = objectMapper.writeValueAsString(structureOK);
				final String storageIdentifier = GlobalDataManager.constructPlanetStorageIdentifier(credential.getAccountId()
						, planetid);
				final ColonySerialized storage = new ColonySerialized(structureOK.getPinId())
						.setPlanetIdentifier(storageIdentifier)
						.setColonySerialization(newstruct)
						.store();
			} catch (RuntimeException jpe) {
				jpe.printStackTrace();
			}
		}
		logger.info("<< [PlanetaryStorageTestUnit.testStructureStore]");
	}

}
// - UNUSED CODE ............................................................................................
//[01]
