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
package org.dimensinfin.eveonline.neocom;

import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.database.entity.TimeStamp;
import org.dimensinfin.eveonline.neocom.enums.EDataUpdateJobs;
import org.dimensinfin.eveonline.neocom.factory.ModelFactory;
import org.dimensinfin.eveonline.neocom.service.TimeTickReceiver;
import org.dimensinfin.eveonline.neocom.service.TimeTickReceiver.Job;
import org.joda.time.Instant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
@RunWith(MockitoJUnitRunner.class)
//@RunWith(AndroidJUnit4.class)
public class UpdateJobManagementTestUnit {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger(UpdateJobManagementTestUnit.class);

	// - F I E L D - S E C T I O N ............................................................................
//	@Mock
//	private TimeStamp testTime = null;
	//	@Mock
	//	Context mMockContext;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	//	public UpdateJobManagementTestUnit () {
	//		super();
	//	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Test
	public TimeStamp TimeStamp_TestApi () {
		final TimeStamp timeStampEmpty = new TimeStamp(Job.constructReference(EDataUpdateJobs.COLONYDATA, 123l), Instant.now())
				.store();
		final TimeStamp timeStampNow = new TimeStamp(Job.constructReference(EDataUpdateJobs.COLONYDATA, 123l), Instant.now())
	//			.setReference(Job.constructReference(EDataUpdateJobs.COLONYDATA, 123l))
				.store();
		final TimeStamp timeStampComplete = new TimeStamp(Job.constructReference(EDataUpdateJobs.COLONYDATA, 123l), Instant.now())
				.setReference(Job.constructReference(EDataUpdateJobs.COLONYDATA, 1232))
				.setTimeStamp(Instant.now().plus(TimeUnit.SECONDS.toMillis(5)).getMillis())
				.store();

	}

	public void Job_TestApi () {
		timeStamp = TimeStamp_TestApi();
		newJob = new Job(timeStamp)
				.setReference(currentrequestReference)
				.setCredentialIdentifier(credential.getAccountId())
				.setJobClass(EDataUpdateJobs.CHARACTER_CORE)
				.setTask(() -> {
					NeoComAppConnector.getSingleton().getCacheConnector().incrementTopCounter();
					TimeTickReceiver.logger.info("-- [TimeTickReceiver.onReceive]> Downloading Pilot v1 information for: [{}]", credential.getAccountName());
					ModelFactory.udpatePilotV1(credential.getAccountId());
					NeoComAppConnector.getSingleton().getCacheConnector().decrementTopCounter();
				})
				.submit();

	}

	@Test
	public void emailValidator_CorrectEmailSimple_ReturnsTrue () {
		when(mMockContext.getString(R.string.hello_word))
				.thenReturn(FAKE_STRING);
		ClassUnderTest myObjectUnderTest = new ClassUnderTest(mMockContext);
		// ...when the string is returned from the object under test...
		String result = myObjectUnderTest.getHelloWorldString();

		// ...then the result should be the expected one.
		assertThat(result, is(FAKE_STRING));
	}

	@Override
	public String toString () {
		StringBuffer buffer = new StringBuffer("UpdateJobManagementTestUnit [");
		buffer.append("name: ").append(0);
		buffer.append("]");
		buffer.append("->").append(super.toString());
		return buffer.toString();
	}
}
// - UNUSED CODE ............................................................................................
//[01]
