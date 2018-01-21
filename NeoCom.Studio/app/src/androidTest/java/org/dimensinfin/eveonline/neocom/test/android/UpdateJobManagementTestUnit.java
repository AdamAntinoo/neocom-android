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

import org.dimensinfin.core.util.Chrono;
import org.dimensinfin.core.util.Chrono.ChonoOptions;
import org.dimensinfin.eveonline.neocom.database.NeoComDatabase;
import org.dimensinfin.eveonline.neocom.database.entity.Colony;
import org.dimensinfin.eveonline.neocom.database.entity.Credential;
import org.dimensinfin.eveonline.neocom.database.entity.TimeStamp;
import org.dimensinfin.eveonline.neocom.enums.EDataUpdateJobs;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdPlanets200Ok;
import org.dimensinfin.eveonline.neocom.factory.ModelFactory;
import org.dimensinfin.eveonline.neocom.network.NetworkManager;
import org.dimensinfin.eveonline.neocom.service.TimeTickReceiver.Job;
import org.dimensinfin.eveonline.neocom.service.TimeTickReceiver.UpdateJobManager;
import org.joda.time.Instant;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
//@RunWith(MockitoJUnitRunner.class)
@RunWith(AndroidJUnit4.class)
public class UpdateJobManagementTestUnit extends NeoComCoreTestEngine {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger = LoggerFactory.getLogger(UpdateJobManagementTestUnit.class);

	// - F I E L D - S E C T I O N ............................................................................
	private TimeStamp timeStampVerification = null;
	private String keyReference = null;
	private Credential credentialVerification = null;
	//	@Mock
	//	private TimeStamp testTime = null;
	//	@Mock
	//	Context mMockContext;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	//	public UpdateJobManagementTestUnit () {
	//		super();
	//	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public TimeStamp timeStampProperTimeManagementTest () throws Exception {
		logger.info(">> [UpdateJobManagementTestUnit.timeStampProperTimeManagementTest]");
		// Check reference creation.
		keyReference = Job.constructReference(EDataUpdateJobs.COLONYDATA, 1001);
		Assert.assertEquals("> Testing the job constructor reference geneeates a proper code: ", "COLONYDATA/1001", keyReference);

		// Check initialization with default values.
		keyReference = Job.constructReference(EDataUpdateJobs.COLONYDATA, 1002);
		final TimeStamp timeStampNow = new TimeStamp(keyReference, Instant.now())
				.store();
		timeStampVerification = NeoComDatabase.getImplementer().getTimeStampDao().queryForId(keyReference);
		Assert.assertEquals("> Testing credentialId not initialized: ", -1, timeStampVerification.getCredentialId());

		// Check initialization with setting time point.
		keyReference = Job.constructReference(EDataUpdateJobs.COLONYDATA, 1003);
		final long timePoint = Instant.now().plus(TimeUnit.SECONDS.toMillis(5)).getMillis();
		final TimeStamp timeStampComplete = new TimeStamp(keyReference, Instant.now())
				//				.setReference(keyReference)
				.setTimeStamp(timePoint)
				.store();
		Assert.assertEquals("> Testing reference overwritten before writing: ", keyReference, timeStampComplete.getReference());
		Assert.assertEquals("> Testing time point before writing: ", timePoint, timeStampComplete.getTimeStamp());
		timeStampVerification = NeoComDatabase.getImplementer().getTimeStampDao().queryForId(keyReference);
		Assert.assertEquals("> Testing reference overwritten after writing: ", keyReference, timeStampVerification.getReference());
		Assert.assertEquals("> Testing time point after writing: ", timePoint, timeStampVerification.getTimeStamp());

		// Check TimeStamp evolution if changing the reference on the fly.
		//		keyReference = Job.constructReference(EDataUpdateJobs.COLONYDATA, 1004);
		//		final TimeStamp timeStampFly = new TimeStamp(keyReference, Instant.now());
		//		Assert.assertEquals("> Testing reference on creation: ", keyReference, timeStampFly.getReference());
		//		timeStampVerification = NeoComDatabase.getImplementer().getTimeStampDao().queryForId(keyReference);
		//		Assert.assertEquals("> Testing reference on creation: ", keyReference, timeStampVerification.getReference());
		//		final String keyReferenceFly = Job.constructReference(EDataUpdateJobs.COLONYDATA, 1005);
		//		timeStampFly.setReference(keyReferenceFly)
		//								.store();
		//		Assert.assertEquals("> Testing reference on modification: ", keyReference, timeStampFly.getReference());
		//		Assert.assertEquals("> Testing reference on modification: ", keyReferenceFly, timeStampFly.getReference());

		logger.info("<< [UpdateJobManagementTestUnit.timeStampProperTimeManagementTest]");
		return timeStampVerification;
	}

	public Credential credentialUsageTest () throws Exception {
		logger.info(">> [UpdateJobManagementTestUnit.credentialUsageTest]");
		final int accountIdentifier = 325476;
		// Check minimal Credential state.
		final Credential credentialMinimum = new Credential(accountIdentifier);
		Assert.assertEquals("> Testing time point after writing: ", false, credentialMinimum.isXMLCompatible());
		Assert.assertEquals("> Testing time point after writing: ", false, credentialMinimum.isESICompatible());

		// Check valid credential for testing.
		final int newAccountIdentifier = 92002067;
		final Credential credentialTest = new Credential(newAccountIdentifier)
				//			.setAccountId(92223647)
				.setAccountName("Beth Ripley")
				.setActive(true)
				.setAccessToken("l503NeX7tjKwbwXfKeEQCibp8v8KknuIldR09FxhOjH3PaC4Tpsoa9yJV7Zi284JjNleq0JLzk4tNZmHGJ1P0w2")
				.setRefreshToken("rILzXqFN4C8aYKr2bY6rr-tvIcRuD3m_Qk5kHeedCn3aIV2vj3JjmucYgp_7TIeoS0wKEfIKs7vAhgopGAlIRQ7R47S1cuPHEVkILck-NQWPZH10Betk972YcFjHZVGO6IjbY-WjtFwsmGKgkBxaf67LgLTt5x_zsK1uRvURyYLMMA0Eh1bWyCStpo2Wg7evW1c79uBiEYaEP9OYpmfrXOKbzGFYb8U5McM7rr0o5lyeYlr4-H4xFG8yVe31b0d4PMlOPCQInENnGcd9-epl3QahYJYvpdkZtocVSC67JV9Q60-vgoTD9D6ZVoO4Iic_Hmm7i5OueCJctqupuV5G8Jja9M0kP6y7a3dl3WKFQUiTZs49X1YgqJPePVD--KvgoUXiwHTZQlQEiqZ2mWVsUQIrlD-7JAzCBQ1Fl89sCQ-iZ8RMUU6OtyDZqrMoQfSYdbuazovF4ueo2lX_PqA71NkL6GT-H_fUiKeurr35yLAU1vfphzckltF9gf7LC7cKuEiGJ14dpE20uLQU7zkzz2jF-zQpjdo61HNk8LXMXbERBhT4imtEC9V8msbsVPn4z04AInFvvNEwdlc2GLRJsoH8xNWWXPsuBIFGGsHiMUs1")
				.setTokenType("Bearer")
				.setExpires(0)
				.store();
		credentialVerification = NeoComDatabase.getImplementer().getCredentialDao().queryForId(Long.valueOf(newAccountIdentifier).toString());
		Assert.assertEquals("> Validating Credential Name: ", "Beth Ripley", credentialVerification.getAccountName());
		Assert.assertEquals("> Validating Credential Type ESI: ", true, credentialVerification.isESICompatible());
		Assert.assertEquals("> Validating Credential Type XML: ", false, credentialVerification.isXMLCompatible());
		Assert.assertEquals("> Validating Credential Token: ", "l503NeX7tjKwbwXfKeEQCibp8v8KknuIldR09FxhOjH3PaC4Tpsoa9yJV7Zi284JjNleq0JLzk4tNZmHGJ1P0w2", credentialVerification.getAccessToken());
		Assert.assertEquals("> Validating Credential Refresh token: ", "rILzXqFN4C8aYKr2bY6rr-tvIcRuD3m_Qk5kHeedCn3aIV2vj3JjmucYgp_7TIeoS0wKEfIKs7vAhgopGAlIRQ7R47S1cuPHEVkILck-NQWPZH10Betk972YcFjHZVGO6IjbY-WjtFwsmGKgkBxaf67LgLTt5x_zsK1uRvURyYLMMA0Eh1bWyCStpo2Wg7evW1c79uBiEYaEP9OYpmfrXOKbzGFYb8U5McM7rr0o5lyeYlr4-H4xFG8yVe31b0d4PMlOPCQInENnGcd9-epl3QahYJYvpdkZtocVSC67JV9Q60-vgoTD9D6ZVoO4Iic_Hmm7i5OueCJctqupuV5G8Jja9M0kP6y7a3dl3WKFQUiTZs49X1YgqJPePVD--KvgoUXiwHTZQlQEiqZ2mWVsUQIrlD-7JAzCBQ1Fl89sCQ-iZ8RMUU6OtyDZqrMoQfSYdbuazovF4ueo2lX_PqA71NkL6GT-H_fUiKeurr35yLAU1vfphzckltF9gf7LC7cKuEiGJ14dpE20uLQU7zkzz2jF-zQpjdo61HNk8LXMXbERBhT4imtEC9V8msbsVPn4z04AInFvvNEwdlc2GLRJsoH8xNWWXPsuBIFGGsHiMUs1", credentialVerification.getRefreshToken());

		logger.info("<< [UpdateJobManagementTestUnit.credentialUsageTest]");
		return credentialVerification;
	}

	@Test
	public void jobApiUseTest () throws Exception {
		final TimeStamp timeStamp = timeStampProperTimeManagementTest();
		final Credential credential = credentialUsageTest();
		final Future<?> newJob = new Job(timeStamp)
				.setCredentialIdentifier(credential.getAccountId())
				.setJobClass(EDataUpdateJobs.CHARACTER_CORE)
				.setTask(() -> {
					logger.info("-- [UpdateJobManagementTestUnit.jobApiUseTest]> Downloading Pilot v1 information for: [{}]", credential.getAccountName());
					ModelFactory.udpatePilotV1(credential.getAccountId());
				})
				.submit();
		Chrono timer = new Chrono();
		while (!newJob.isDone()) {
			logger.info("-- [UpdateJobManagementTestUnit.jobApiUseTest]> Waiting job completion: [{}]", timer.printElapsed(ChonoOptions.DEFAULT));
			// Wait a delay of 1 second to allow to watch the counter.
			try {
				Thread.sleep(TimeUnit.SECONDS.toMillis(1));
			} catch (InterruptedException ex) {
			}
		}
	}

	@Test
	public void colonyStoreUseTest () throws Exception {
		final Credential credential = credentialUsageTest();
		logger.info(
				"-- [TimeTickReceiver.onReceive]> Downloading Colony list for: [{}]", credential.getAccountName());
		// Get the list of planet colonies for this credential and update each one on the factory.
		final List<GetCharactersCharacterIdPlanets200Ok> colonyInstances = NetworkManager.getCharactersCharacterIdPlanets(Long.valueOf(credential.getAccountId()).intValue(), credential.getRefreshToken(), "tranquility");
		for (GetCharactersCharacterIdPlanets200Ok colonyOK : colonyInstances) {
			logger.info("-- [TimeTickReceiver.onReceive]> Mapping Colony: [{}]", colonyOK.getPlanetId());
			Colony col = modelMapper.map(colonyOK, Colony.class);
			logger.info("-- [TimeTickReceiver.onReceive]> Saving Colony data at DB: [{}]", colonyOK.getPlanetId());
			col.create(colonyOK.getPlanetId());
//			NeoComAppConnector.getSingleton().getCacheConnector().decrementTopCounter();
		}
	}

	@Test
	public void filteringDuplicatedJobSubmission () throws Exception {
		logger.info(">> [UpdateJobManagementTestUnit.filteringDuplicatedJobSubmission]");
		// Check initialization with setting time point.
		keyReference = Job.constructReference(EDataUpdateJobs.COLONYDATA, 1003);
		final long timePoint = Instant.now().plus(TimeUnit.SECONDS.toMillis(5)).getMillis();
		final TimeStamp timeStampComplete = new TimeStamp(keyReference, Instant.now())
				.setTimeStamp(timePoint)
				.store();
		logger.info("-- [UpdateJobManagementTestUnit.filteringDuplicatedJobSubmission]> Created job 5 seconds in the future.");
		final Credential credential = credentialUsageTest();
		final Job newJob = new Job(timeStampComplete)
				.setCredentialIdentifier(credential.getAccountId())
				.setJobClass(EDataUpdateJobs.CHARACTER_CORE)
				.setTask(() -> {
					logger.info("-- [TimeTickReceiver.onReceive]> Downloading Pilot v1 information for: [{}]", credential.getAccountName());
					ModelFactory.udpatePilotV1(credential.getAccountId());
				});
		logger.info("-- [UpdateJobManagementTestUnit.filteringDuplicatedJobSubmission]> Submitting first job. {}",newJob.getReference());
		UpdateJobManager.submit(newJob);
		Assert.assertEquals("> Number of Jobs on queue: ", 1, UpdateJobManager.runningJobs.size());

		logger.info("<< [UpdateJobManagementTestUnit.filteringDuplicatedJobSubmission]");
	}
}
// - UNUSED CODE ............................................................................................
//[01]
