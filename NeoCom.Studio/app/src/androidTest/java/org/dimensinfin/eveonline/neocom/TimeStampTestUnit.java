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

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.j256.ormlite.dao.Dao;

import junit.framework.Assert;

import org.dimensinfin.eveonline.neocom.connector.AndroidDatabaseConnector;
import org.dimensinfin.eveonline.neocom.connector.ICCPDatabaseConnector;
import org.dimensinfin.eveonline.neocom.connector.ICacheConnector;
import org.dimensinfin.eveonline.neocom.connector.IModelAppConnector;
import org.dimensinfin.eveonline.neocom.connector.INeoComModelDatabase;
import org.dimensinfin.eveonline.neocom.connector.ModelAppConnector;
import org.dimensinfin.eveonline.neocom.database.INeoComDBHelper;
import org.dimensinfin.eveonline.neocom.database.NeoComDatabase;
import org.dimensinfin.eveonline.neocom.database.entity.TimeStamp;
import org.dimensinfin.eveonline.neocom.enums.EDataUpdateJobs;
import org.dimensinfin.eveonline.neocom.service.TimeTickReceiver.Job;
import org.joda.time.Instant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
@RunWith(AndroidJUnit4.class)
public class TimeStampTestUnit implements IModelAppConnector {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger(TimeStampTestUnit.class);

	// - F I E L D - S E C T I O N ............................................................................
	private Context context = null;
	private AndroidDatabaseConnector dbNeocomConnector = null;
	private ModelAppConnector connector = null;
	private Dao<TimeStamp, String> mTimeStampDao = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public TimeStampTestUnit () {
		super();
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Before
	public void initializeApplicationConnectors () throws SQLException {
		logger.info(">> [UpdateJobManagementTestUnit.initializeApplicationConnectors]");
		context = InstrumentationRegistry.getTargetContext();
		try {
			connector = new ModelAppConnector(this);
			mTimeStampDao = NeoComDatabase.getImplementer().getTimeStampDao();
			Assert.assertNotNull("> Verifying the Dao is available", mTimeStampDao);
			logger.info("<< [UpdateJobManagementTestUnit.initializeApplicationConnectors]");
		} catch (RuntimeException rtex) {
			rtex.printStackTrace();
		}
	}

	@After
	public void closeDb () throws IOException {
		//		NeoComDatabase.close();
	}

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
	//	@Override
	//	public String toString () {
	//		StringBuffer buffer = new StringBuffer("TimeStampTestUnit [");
	//		buffer.append("name: ").append(0);
	//		buffer.append("]");
	//		buffer.append("->").append(super.toString());
	//		return buffer.toString();
	//	}

	@Override
	public ICacheConnector getCacheConnector () {
		return null;
	}

	@Override
	public ICCPDatabaseConnector getCCPDBConnector () {
		return null;
	}

	@Override
	public INeoComModelDatabase getDBConnector () {
		return null;
	}

	@Override
	public INeoComDBHelper getNewDBConnector () throws SQLException {
		if ( null == dbNeocomConnector ) {
			dbNeocomConnector = new AndroidDatabaseConnector(context);
		}
		return dbNeocomConnector.getNeocomDBHelper();
	}
}
// - UNUSED CODE ............................................................................................
//[01]
