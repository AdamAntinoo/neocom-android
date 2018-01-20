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
import org.dimensinfin.eveonline.neocom.database.entity.TimeStamp;
import org.dimensinfin.eveonline.neocom.enums.EDataUpdateJobs;
import org.dimensinfin.eveonline.neocom.service.TimeTickReceiver.Job;
import org.joda.time.Instant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
@RunWith(AndroidJUnit4.class)
public class TimeStampTestUnit extends NeoComCoreTestEngine{
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger(TimeStampTestUnit.class);

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
//	public TimeStampTestUnit () {
//		super();
//	}

	// - M E T H O D - S E C T I O N ..........................................................................

	@Test
	public void verifyTimeStampRegistration () throws Exception {
		logger.info(">> [UpdateJobManagementTestUnit.verifyTimeStampRegistration]");
		final String keyReference = Job.constructReference(EDataUpdateJobs.COLONYDATA, 1231);
		Assert.assertEquals("> Testing the job constructor reference geneeates a proper code: ", "COLONYDATA/1231", keyReference);
		final TimeStamp timeStampEmpty = new TimeStamp(keyReference, Instant.now())
				.store();
		final TimeStamp timeStampVerification = NeoComDatabase.getImplementer().getTimeStampDao().queryForId(keyReference);
		Assert.assertEquals("> Verifying that TimeStamp objects match: ", timeStampEmpty, timeStampEmpty);
		logger.info("<< [UpdateJobManagementTestUnit.verifyTimeStampRegistration]");
	}
}
// - UNUSED CODE ............................................................................................
//[01]
