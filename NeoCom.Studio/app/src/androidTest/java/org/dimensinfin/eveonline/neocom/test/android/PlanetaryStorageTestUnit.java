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

import junit.framework.Assert;

import org.dimensinfin.eveonline.neocom.database.NeoComDatabase;
import org.dimensinfin.eveonline.neocom.database.entity.Colony;
import org.dimensinfin.eveonline.neocom.database.entity.Credential;
import org.dimensinfin.eveonline.neocom.database.entity.TimeStamp;
import org.dimensinfin.eveonline.neocom.enums.EDataUpdateJobs;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.network.NetworkManager;
import org.dimensinfin.eveonline.neocom.service.TimeTickReceiver.Job;
import org.joda.time.Instant;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
@RunWith(AndroidJUnit4.class)
public class PlanetaryStorageTestUnit extends UpdateJobManagementTestUnit {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	@Test
	public void createJobToDownloadColonies () throws Exception {
		logger.info(">> [PlanetaryStorageTestUnit.createJobToDownloadColonies]");
		// Check initialization with setting time point.
		final Credential credentialVerification = credentialUsageTest();
		Assert.assertEquals("> Validating Credential Token: ", "l503NeX7tjKwbwXfKeEQCibp8v8KknuIldR09FxhOjH3PaC4Tpsoa9yJV7Zi284JjNleq0JLzk4tNZmHGJ1P0w2", credentialVerification.getAccessToken());
		Assert.assertEquals("> Validating Credential Refresh token: ", "rILzXqFN4C8aYKr2bY6rr-tvIcRuD3m_Qk5kHeedCn3aIV2vj3JjmucYgp_7TIeoS0wKEfIKs7vAhgopGAlIRQ7R47S1cuPHEVkILck-NQWPZH10Betk972YcFjHZVGO6IjbY-WjtFwsmGKgkBxaf67LgLTt5x_zsK1uRvURyYLMMA0Eh1bWyCStpo2Wg7evW1c79uBiEYaEP9OYpmfrXOKbzGFYb8U5McM7rr0o5lyeYlr4-H4xFG8yVe31b0d4PMlOPCQInENnGcd9-epl3QahYJYvpdkZtocVSC67JV9Q60-vgoTD9D6ZVoO4Iic_Hmm7i5OueCJctqupuV5G8Jja9M0kP6y7a3dl3WKFQUiTZs49X1YgqJPePVD--KvgoUXiwHTZQlQEiqZ2mWVsUQIrlD-7JAzCBQ1Fl89sCQ-iZ8RMUU6OtyDZqrMoQfSYdbuazovF4ueo2lX_PqA71NkL6GT-H_fUiKeurr35yLAU1vfphzckltF9gf7LC7cKuEiGJ14dpE20uLQU7zkzz2jF-zQpjdo61HNk8LXMXbERBhT4imtEC9V8msbsVPn4z04AInFvvNEwdlc2GLRJsoH8xNWWXPsuBIFGGsHiMUs1", credentialVerification.getRefreshToken());

		final String keyReference = Job.constructReference(EDataUpdateJobs.COLONYDATA, credentialVerification.getAccountId());
		final long timePoint = Instant.now().plus(TimeUnit.SECONDS.toMillis(5)).getMillis();
		final TimeStamp timeStampComplete = new TimeStamp(keyReference, Instant.now())
				.setTimeStamp(timePoint)
				.store();
		logger.info("-- [PlanetaryStorageTestUnit.createJobToDownloadColonies]> Created TimeStamp 5 seconds in the future.");
		logger.info("-- [DownloadManager.downloadColonyList]> Download colony list for identifier: {}", credentialVerification.getAccountId());
		//		server=ModelAppConnector.getSingleton().getResourceString(R.string.esisourceserver);
		final String server = "tranquility";
		final List<GetCharactersCharacterIdPlanets200Ok> colonyInstances = NetworkManager.getCharactersCharacterIdPlanets(credentialVerification.getAccountId(), credentialVerification.getRefreshToken(), server);

		// Transform the received OK instance into a NeoCom compatible model instance.
		for (GetCharactersCharacterIdPlanets200Ok colonyOK : colonyInstances) {
			Colony col = modelMapper.map(colonyOK, Colony.class);
			logger.info("-- [DownloadManager.downloadColonyList]> Transformed colony: {}", col.getPlanetId());
			col.store();
			final String colonyReference = Long.valueOf(col.getPlanetId()).toString();
			final Colony colonyVerification = NeoComDatabase.getImplementer().getColonyDao().queryForId(colonyReference);
			Assert.assertEquals("> Validating colony data stored [planetId]: ", col.getPlanetId(), colonyVerification.getPlanetId());
			Assert.assertEquals("> Validating colony data stored [numPins]: ", col.getNumPins(), colonyVerification.getNumPins());
			Assert.assertEquals("> Validating colony data stored [planetname]: ", col.getPlanetName(), colonyVerification.getPlanetName());
		}
	}
}
// - UNUSED CODE ............................................................................................
//[01]
