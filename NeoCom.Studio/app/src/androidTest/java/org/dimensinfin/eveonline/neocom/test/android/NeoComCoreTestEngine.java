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

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.beimin.eveapi.EveApi;
import com.beimin.eveapi.connectors.ApiConnector;
import com.beimin.eveapi.connectors.CachingConnector;
import com.beimin.eveapi.connectors.LoggingConnector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.j256.ormlite.dao.Dao;

import junit.framework.Assert;

import org.dimensinfin.eveonline.neocom.connector.AndroidCCPDatabaseConnector;
import org.dimensinfin.eveonline.neocom.connector.AndroidDatabaseConnector;
import org.dimensinfin.eveonline.neocom.connector.ICCPDatabaseConnector;
import org.dimensinfin.eveonline.neocom.connector.ICacheConnector;
import org.dimensinfin.eveonline.neocom.connector.IModelAppConnector;
import org.dimensinfin.eveonline.neocom.connector.INeoComModelDatabase;
import org.dimensinfin.eveonline.neocom.connector.ModelAppConnector;
import org.dimensinfin.eveonline.neocom.core.NeoComConnector;
import org.dimensinfin.eveonline.neocom.database.INeoComDBHelper;
import org.dimensinfin.eveonline.neocom.database.NeoComDatabase;
import org.dimensinfin.eveonline.neocom.database.entity.TimeStamp;
import org.junit.After;
import org.junit.Before;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class NeoComCoreTestEngine implements IModelAppConnector {
	// - S T A T I C - S E C T I O N ..........................................................................
	protected static Logger logger = LoggerFactory.getLogger(NeoComCoreTestEngine.class);
	static {
		EveApi.setConnector(new NeoComConnector(new CachingConnector(new LoggingConnector())));
		// Remove the secure XML access and configure the ApiConnector.
		ApiConnector.setSecureXmlProcessing(false);
	}
	public static final ModelMapper globalModelMapper = new ModelMapper();
	static {
		globalModelMapper.getConfiguration()
		           .setFieldMatchingEnabled(true)
		           .setMethodAccessLevel(Configuration.AccessLevel.PRIVATE);
	}

	/** Jackson mapper to use for object json serialization. */
	public static final ObjectMapper objectMapper = new ObjectMapper();
	static {
		objectMapper.registerModule(new JodaModule());
	}

	/** Background executor to use for long downloading jobs. */
	private static final ExecutorService downloadExecutor = Executors.newSingleThreadExecutor();

	// - F I E L D - S E C T I O N ............................................................................
	private Context context = null;
	private AndroidCCPDatabaseConnector dbCCPConnector = null;
	private AndroidDatabaseConnector dbNeocomConnector = null;
	private ModelAppConnector connector = null;
//	protected static ModelMapper modelMapper = new ModelMapper();

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	@Before
	public void initializeApplicationConnectors () throws SQLException {
		logger.info(">> [NeoComCoreTestEngine.initializeApplicationConnectors]");
		context = InstrumentationRegistry.getTargetContext();
		try {
			connector = new ModelAppConnector(this);
			Dao<TimeStamp, String> mTimeStampDao = NeoComDatabase.getImplementer().getTimeStampDao();
			Assert.assertNotNull("> Verifying the Dao is available", mTimeStampDao);
			logger.info("<< [NeoComCoreTestEngine.initializeApplicationConnectors]");
		} catch (RuntimeException rtex) {
			rtex.printStackTrace();
		}
//		modelMapper.getConfiguration()
//							 .setFieldMatchingEnabled(true)
//							 .setMethodAccessLevel(AccessLevel.PRIVATE);
	}

	@After
	public void closeDb () throws IOException {
		//		NeoComDatabase.close();
	}

	@Override
	public ICacheConnector getCacheConnector () {
		return null;
	}

	@Override
	public ICCPDatabaseConnector getCCPDBConnector () {
		if ( null == dbCCPConnector ) {
			dbCCPConnector = new AndroidCCPDatabaseConnector();
		}
		return dbCCPConnector;
	}

	@Override
	public INeoComModelDatabase getDBConnector () {
		if ( null == dbNeocomConnector ) {
			dbNeocomConnector = new AndroidDatabaseConnector(context);
		}
		return dbNeocomConnector;
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
