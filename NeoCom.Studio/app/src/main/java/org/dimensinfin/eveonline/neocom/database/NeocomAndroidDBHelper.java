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
package org.dimensinfin.eveonline.neocom.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.database.entity.Colony;
import org.dimensinfin.eveonline.neocom.database.entity.ColonyStorage;
import org.dimensinfin.eveonline.neocom.database.entity.Credential;
import org.dimensinfin.eveonline.neocom.database.entity.TimeStamp;
import org.dimensinfin.eveonline.neocom.enums.ERequestClass;
import org.dimensinfin.eveonline.neocom.model.ApiKey;
import org.dimensinfin.eveonline.neocom.model.DatabaseVersion;
import org.dimensinfin.eveonline.neocom.model.EveLocation;
import org.dimensinfin.eveonline.neocom.model.NeoComAsset;
import org.dimensinfin.eveonline.neocom.model.NeoComBlueprint;
import org.dimensinfin.eveonline.neocom.model.Property;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryResource;
import org.dimensinfin.eveonline.neocom.planetary.ResourceList;

import java.sql.SQLException;
import java.util.logging.Logger;

// - CLASS IMPLEMENTATION ...................................................................................
public class NeocomAndroidDBHelper extends OrmLiteSqliteOpenHelper implements INeoComDBHelper {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = Logger.getLogger("NeocomAndroidDBHelper");

	// - F I E L D - S E C T I O N ............................................................................
	private Dao<DatabaseVersion, String> versionDao = null;
	private Dao<TimeStamp, String> timeStampDao = null;
	private Dao<ApiKey, String> apiKeysDao = null;
	private Dao<Credential, String> credentialDao = null;
	private Dao<Colony, String> colonyDao = null;
	private Dao<org.dimensinfin.eveonline.neocom.database.entity.ColonyStorage, String> colonyStorageDao = null;

	private Dao<Property, String> propertyDao = null;
	private Dao<ResourceList, String> resourceList = null;
	private Dao<PlanetaryResource, String> planetaryResourceDao = null;
	private Dao<NeoComAsset, String> assetDao = null;
	private Dao<NeoComBlueprint, String> blueprintDao = null;
	//	private Dao<Job, String>								jobDao								= null;
	//	private Dao<NeoComMarketOrder, String>	marketOrderDao				= null;
	private Dao<EveLocation, String> locationDao = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeocomAndroidDBHelper (final Context context) {
		super(context,
				NeoComAppConnector.getSingleton().getStorageConnector()
													.accessAppStorage(NeoComAppConnector.getSingleton().getResourceString(R.string.appdatabasefilename))
													.getAbsolutePath(),
				null, new Integer(NeoComAppConnector.getSingleton().getResourceString(R.string.databaseversion)).intValue(),
				R.raw.ormlite_config);
		logger.info("-- [NeocomAndroidDBHelper.<constructor>]> Initializing instance with configuration: databasePath {}"
				+ NeoComAppConnector.getSingleton()
														.getStorageConnector()
														.accessAppStorage(NeoComAppConnector.getSingleton().getResourceString(R.string.appdatabasefilename))
														.getAbsolutePath());
		logger.info("-- [NeocomAndroidDBHelper.<constructor>]> Initializing instance with configuration: databaseVersion {}"
				+ new Integer(NeoComAppConnector.getSingleton().getResourceString(R.string.databaseversion)).intValue());
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close () {
		super.close();
		assetDao = null;
		blueprintDao = null;
	}

	public Dao<DatabaseVersion, String> getVersionDao () throws java.sql.SQLException {
		if ( null == versionDao ) {
			versionDao = DaoManager.createDao(this.getConnectionSource(), DatabaseVersion.class);
		}
		return versionDao;
	}

	public Dao<TimeStamp, String> getTimeStampDao () throws SQLException {
		if ( null == timeStampDao ) {
			timeStampDao = DaoManager.createDao(this.getConnectionSource(), TimeStamp.class);
		}
		return timeStampDao;
	}

	public Dao<ApiKey, String> getApiKeysDao () throws SQLException {
		if ( null == apiKeysDao ) {
			apiKeysDao = DaoManager.createDao(this.getConnectionSource(), ApiKey.class);
		}
		return apiKeysDao;
	}

	@Override
	public Dao<Credential, String> getCredentialDao () throws SQLException {
		if ( null == credentialDao ) {
			credentialDao = DaoManager.createDao(this.getConnectionSource(), Credential.class);
		}
		return credentialDao;
	}

	@Override
	public Dao<Colony, String> getColonyDao () throws SQLException {
		if ( null == colonyDao ) {
			colonyDao = DaoManager.createDao(this.getConnectionSource(), Colony.class);
		}
		return colonyDao;
	}

	@Override
	public Dao<org.dimensinfin.eveonline.neocom.database.entity.ColonyStorage, String> getColonyStorageDao () throws SQLException {
		if ( null == colonyStorageDao ) {
			colonyStorageDao = DaoManager.createDao(this.getConnectionSource(), org.dimensinfin.eveonline.neocom.database.entity.ColonyStorage.class);
		}
		return colonyStorageDao;
	}

	public Dao<NeoComAsset, String> getAssetDao () throws java.sql.SQLException {
		if ( null == assetDao ) {
			assetDao = DaoManager.createDao(this.getConnectionSource(), NeoComAsset.class);
		}
		return assetDao;
	}

	public Dao<NeoComBlueprint, String> getBlueprintDao () throws java.sql.SQLException {
		if ( null == blueprintDao ) {
			blueprintDao = DaoManager.createDao(this.getConnectionSource(), NeoComBlueprint.class);
		}
		return blueprintDao;
	}


	//	public Dao<Job, String> getJobDAO() throws java.sql.SQLException {
	//		if (null == jobDao) {
	//			jobDao = DaoManager.createDao(this.getConnectionSource(), Job.class);
	//		}
	//		return jobDao;
	//	}

	public Dao<EveLocation, String> getLocationDao () throws java.sql.SQLException {
		if ( null == locationDao ) {
			locationDao = DaoManager.createDao(this.getConnectionSource(), EveLocation.class);
		}
		return locationDao;
	}

	//	public Dao<NeoComMarketOrder, String> getMarketOrderDAO() throws java.sql.SQLException {
	//		if (null == marketOrderDao) {
	//			marketOrderDao = DaoManager.createDao(this.getConnectionSource(), NeoComMarketOrder.class);
	//		}
	//		return marketOrderDao;
	//	}

	public Dao<PlanetaryResource, String> getPlanetaryResourceDao () throws java.sql.SQLException {
		if ( null == planetaryResourceDao ) {
			planetaryResourceDao = DaoManager.createDao(this.getConnectionSource(), PlanetaryResource.class);
		}
		return planetaryResourceDao;
	}

	public Dao<Property, String> getPropertyDao () throws java.sql.SQLException {
		if ( null == propertyDao ) {
			propertyDao = DaoManager.createDao(this.getConnectionSource(), Property.class);
		}
		return propertyDao;
	}

	public Dao<ResourceList, String> getResourceListDao () throws java.sql.SQLException {
		if ( null == resourceList ) {
			resourceList = DaoManager.createDao(this.getConnectionSource(), ResourceList.class);
		}
		return resourceList;
	}

	public void loadSeedData () {
		logger.info(">> [NeocomAndroidDBHelper.loadSeedData]");
		// Add seed data to the new database is the tables are empty.
		try {
			//---  D A T A B A S E    V E R S I O N
			logger.info("-- [NeocomAndroidDBHelper.loadSeedData]> Loading DatabaseVersion");
			Dao<DatabaseVersion, String> version = this.getVersionDao();
			QueryBuilder<DatabaseVersion, String> queryBuilder = version.queryBuilder();
			queryBuilder.setCountOf(true);
			// Check that at least one Version record exists on the database. It is a singleton.
			long records = version.countOf(queryBuilder.prepare());
			logger.info("-- [NeocomAndroidDBHelper.loadSeedData]> DatabaseVersion records: " + records);

			// If the table is empty then insert the seeded Api Keys
			if ( records < 1 ) {
				DatabaseVersion key = new DatabaseVersion(
						NeoComAppConnector.getSingleton().getResourceString(R.string.databaseversion));
				logger.info("-- [NeocomAndroidDBHelper.loadSeedData]> Setting DatabaseVersion to: " + key.getVersionNumber());
			}
		} catch (SQLException sqle) {
			NeocomAndroidDBHelper.logger.severe("E [NeocomAndroidDBHelper.loadSeedData]> Error creating the initial table on the app database.");
			sqle.printStackTrace();
		}

		//--- A P I   K E Y S
		try {
			Dao<ApiKey, String> apikey = this.getApiKeysDao();
			QueryBuilder<ApiKey, String> queryBuilder = apikey.queryBuilder();
			queryBuilder.setCountOf(true);
			long records = apikey.countOf(queryBuilder.prepare());

			// If the table is empty then insert the seeded Api Keys
			if ( records < 1 ) {
				//				ApiKey key = new ApiKey("Beth Ripley").setKeynumber(2889577)
				//				                                      .setValidationcode("Mb6iDKR14m9Xjh9maGTQCGTkpjRHPjOgVUkvK6E9r6fhMtOWtipaqybp0qCzxuuw")
				//				                                      .setActive(true);
				//				key = new ApiKey("Perico").setKeynumber(3106761)
				//				                          .setValidationcode("gltCmvVoZl5akrM8d6DbNKZn7Jm2SaukrmqjnSOyqKbvzz5CtNfknTEwdBe6IIFf").setActive(false);
				//				ApiKey		key = new ApiKey("CapitanHaddock09").setKeynumber(924767)
				//				                                    .setValidationcode("2qBKUY6I9ozYhKxYUBPnSIix0fHFCqveD1UEAv0GbYqLenLLTIfkkIWeOBejKX5P").setActive(true);
				//				key = new ApiKey("CapitanHaddock29").setKeynumber(6472981)
				//				                                    .setValidationcode("pj1NJKKb0pNO8LTp0qN2yJSxZoZUO0UYYq8qLtOeFXNsNBRpiz7orcqVAu7UGF7z").setActive(true);
				ApiKey key = new ApiKey("CapitanHaddock").setKeynumber(924767)
																								 .setValidationcode("2qBKUY6I9ozYhKxYUBPnSIix0fHFCqveD1UEAv0GbYqLenLLTIfkkIWeOBejKX5P").setActive(false);
				key = new ApiKey("CapitanHaddock").setKeynumber(6472981)
																					.setValidationcode("pj1NJKKb0pNO8LTp0qN2yJSxZoZUO0UYYq8qLtOeFXNsNBRpiz7orcqVAu7UGF7z").setActive(false);
			}
		} catch (SQLException sqle) {
			NeocomAndroidDBHelper.logger.severe("E> Error creating the initial table on the app database.");
			sqle.printStackTrace();
		}
		logger.info("<< [NeocomAndroidDBHelper.loadSeedData]");
	}

	@Override
	public int getDatabaseVersion () {
		return new Integer(NeoComAppConnector.getSingleton().getResourceString(R.string.databaseversion)).intValue();
	}

	@Override
	public int getStoredVersion () throws SQLException {
		return new Integer(NeoComAppConnector.getSingleton().getResourceString(R.string.databaseversion)).intValue();
	}

	public void onCreate (final ConnectionSource databaseConnection) {
	}

	@Override
	public void onUpgrade (final ConnectionSource databaseConnection, final int oldVersion, final int newVersion) {
	}

	@Override
	public void onCreate (final SQLiteDatabase database, final ConnectionSource databaseConnection) {
		logger.info(">> [NeocomAndroidDBHelper.onCreate]");
		try {
			// Now open the DAO connector and create tables if they not exist
			TableUtils.createTableIfNotExists(databaseConnection, DatabaseVersion.class);
			TableUtils.createTableIfNotExists(databaseConnection, ApiKey.class);
			TableUtils.createTableIfNotExists(databaseConnection, Credential.class);
			TableUtils.createTableIfNotExists(databaseConnection, TimeStamp.class);
			TableUtils.createTableIfNotExists(databaseConnection, Colony.class);
			TableUtils.createTableIfNotExists(databaseConnection, ColonyStorage.class);
			TableUtils.createTableIfNotExists(databaseConnection, EveLocation.class);
			TableUtils.createTableIfNotExists(databaseConnection, NeoComAsset.class);

			TableUtils.createTableIfNotExists(databaseConnection, NeoComBlueprint.class);
			//			TableUtils.createTableIfNotExists(databaseConnection, Job.class);
			//			TableUtils.createTableIfNotExists(databaseConnection, NeoComMarketOrder.class);
			TableUtils.createTableIfNotExists(databaseConnection, PlanetaryResource.class);
			TableUtils.createTableIfNotExists(databaseConnection, Property.class);
			TableUtils.createTableIfNotExists(databaseConnection, ResourceList.class);
		} catch (SQLException sqle) {
			NeocomAndroidDBHelper.logger.severe("E> Error creating the initial table on the app database.");
			sqle.printStackTrace();
		}
		this.loadSeedData();
		logger.info("<< [NeocomAndroidDBHelper.onCreate]");
	}

	@Override
	public void onUpgrade (final SQLiteDatabase database, final ConnectionSource databaseConnection, final int oldVersion,
												 final int newVersion) {
		logger.info(">> [NeocomAndroidDBHelper.onUpgrade]");
		// Execute different actions depending the version.
		if ( oldVersion < 93 ) {
			try {
				TableUtils.dropTable(databaseConnection, ApiKey.class, true);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E [NeocomAndroidDBHelper.onUpgrade]> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E [NeocomAndroidDBHelper.onUpgrade]> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
		}
		if ( oldVersion < 94 ) {
			try {
				TableUtils.dropTable(databaseConnection, EveLocation.class, true);
				// Post the events to reload the Locations data.
				NeoComAppConnector.getSingleton().getCacheConnector().addLocationUpdateRequest(ERequestClass.CITADELUPDATE);
				NeoComAppConnector.getSingleton().getCacheConnector().addLocationUpdateRequest(ERequestClass.OUTPOSTUPDATE);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E [NeocomAndroidDBHelper.onUpgrade]> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E[NeocomAndroidDBHelper.onUpgrade]> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
		}
		if ( oldVersion < 95 ) {
			try {
				TableUtils.dropTable(databaseConnection, Credential.class, true);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E [NeocomAndroidDBHelper.onUpgrade]> Error dropping table on Database new version.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E [NeocomAndroidDBHelper.onUpgrade]> Error dropping table on Database new version.");
				sqle.printStackTrace();
			}
		}
		if ( oldVersion < 103 ) {
			try {
				TableUtils.dropTable(databaseConnection, ApiKey.class, true);
				TableUtils.dropTable(databaseConnection, Credential.class, true);
				TableUtils.dropTable(databaseConnection, TimeStamp.class, true);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E [NeocomAndroidDBHelper.onUpgrade]> Error dropping table on Database new version 103.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E [NeocomAndroidDBHelper.onUpgrade]> Error dropping table on Database new version 103.");
				sqle.printStackTrace();
			}
		}
		if ( oldVersion < 104 ) {
			try {
				TableUtils.dropTable(databaseConnection, ApiKey.class, true);
				TableUtils.dropTable(databaseConnection, Credential.class, true);
				TableUtils.dropTable(databaseConnection, TimeStamp.class, true);
				TableUtils.dropTable(databaseConnection, Colony.class, true);
				TableUtils.dropTable(databaseConnection, NeoComAsset.class, true);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E [NeocomAndroidDBHelper.onUpgrade]> Error dropping table on Database " +
						"new version 104.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E [NeocomAndroidDBHelper.onUpgrade]> Error dropping table on Database " +
						"new version 104.");
				sqle.printStackTrace();
			}
		}
		if ( oldVersion < 105 ) {
			try {
				TableUtils.dropTable(databaseConnection, TimeStamp.class, true);
				TableUtils.dropTable(databaseConnection, Colony.class, true);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E [NeocomAndroidDBHelper.onUpgrade]> Error dropping table on Database " +
						"new version 105.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E [NeocomAndroidDBHelper.onUpgrade]> Error dropping table on Database " +
						"new version 105.");
				sqle.printStackTrace();
			}
		}
		if ( oldVersion < 106 ) {
			try {
				TableUtils.dropTable(databaseConnection, Colony.class, true);
			} catch (RuntimeException rtex) {
				NeocomAndroidDBHelper.logger.severe("E [NeocomAndroidDBHelper.onUpgrade]> Error dropping table on Database " +
						"new version 106.");
				rtex.printStackTrace();
			} catch (SQLException sqle) {
				NeocomAndroidDBHelper.logger.severe("E [NeocomAndroidDBHelper.onUpgrade]> Error dropping table on Database " +
						"new version 106.");
				sqle.printStackTrace();
			}
		}
		this.onCreate(database, databaseConnection);
		logger.info("<< [NeocomAndroidDBHelper.onUpgrade]");
	}
}
// - UNUSED CODE ............................................................................................
