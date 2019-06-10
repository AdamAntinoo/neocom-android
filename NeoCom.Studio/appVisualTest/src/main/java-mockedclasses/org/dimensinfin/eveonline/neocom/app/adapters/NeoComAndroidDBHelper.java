package org.dimensinfin.eveonline.neocom.app.adapters;

import java.sql.SQLException;
import java.util.Objects;

import android.content.Context;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.interfaces.IFileSystem;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

public class NeoComAndroidDBHelper {
	private static Logger logger = LoggerFactory.getLogger(NeoComAndroidDBHelper.class);
	private static String DEFAULT_CONNECTION_DESCRIPTOR = "jdbc:mysql://localhost:3306/neocom?user=NEOCOMTEST&password=01.Alpha";
	private static NeoComAndroidDBHelper singleton;

	// - F I E L D - S E C T I O N
	private boolean databaseValid = false;
	private boolean isOpen = false;
	private String storageLocation = "";
	private int databaseVersion = 0;

	// - C O N S T R U C T O R S
	private NeoComAndroidDBHelper( final Context context, final String databaseName, final int databaseVersion ) {
//		super(context, databaseName, null, databaseVersion, R.raw.ormlite_config);
		logger.info("-- [NeocomAndroidDBHelper.<constructor>]> Initializing instance with configuration: databasePath {}"
				, databaseName);
		logger.info("-- [NeocomAndroidDBHelper.<constructor>]> Initializing instance with configuration: databaseVersion {}"
				, databaseVersion);
		this.storageLocation = databaseName;
		this.databaseVersion = databaseVersion;
	}

//	@Override
	public Dao<Credential, String> getCredentialDao() throws SQLException {
//		if (null == credentialDao) {
//			credentialDao = DaoManager.createDao(this.getConnectionSource(), Credential.class);
//		}
		return  Mockito.mock(Dao.class);
	}

	// -  B U I L D E R
	public static class Builder {
		private Context appContext;
		private IFileSystem fileSystem;
		private String storageLocation;
		private String databaseName;
		private int databaseVersion = 0;

		public Builder() {
		}

		public Builder withAppContext( final Context appContext ) {
			this.appContext = appContext;
			return this;
		}

		public Builder withFileSystem( final IFileSystem fileSystem ) {
			this.fileSystem = fileSystem;
			return this;
		}

		public Builder setDatabaseLocation( final String location ) {
			this.storageLocation = location;
			return this;
		}

		public Builder setDatabaseName( final String instanceName ) {
			this.databaseName = instanceName;
			return this;
		}

		public Builder setDatabaseVersion( final int newVersion ) {
			this.databaseVersion = newVersion;
			return this;
		}

		public NeoComAndroidDBHelper build() throws SQLException {
			// Check and create only a singleton helper.
			if (null == singleton) {
				Objects.requireNonNull(this.appContext);
				Objects.requireNonNull(this.fileSystem);
				singleton = new NeoComAndroidDBHelper(this.appContext
						, this.fileSystem.accessResource4Path(databaseName)
						, databaseVersion);
			}
			return singleton;
		}
	}
}
