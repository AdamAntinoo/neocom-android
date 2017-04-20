//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2016 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.databases;

import java.sql.SQLException;
import java.util.logging.Logger;

import org.dimensinfin.eveonline.neocom.planetary.ResourceListHandler;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

// - CLASS IMPLEMENTATION ...................................................................................
public class NeocomDatabaseAdapter {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger											logger				= Logger.getLogger("NeocomDatabaseAdapter");
	private static final String								DATABASE_URL	= "jdbc:sqlite:src/main/resources/neocom.db";

	// - F I E L D - S E C T I O N ............................................................................
	private ConnectionSource									connectionSource;
	private Dao<ResourceListHandler, String>	accountDao;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeocomDatabaseAdapter() {
		//		String databaseUrl = "jdbc:h2:mem:account";
		// create a connection source to our database
		try {
			connectionSource = new JdbcConnectionSource(DATABASE_URL);
			accountDao = DaoManager.createDao(connectionSource, ResourceListHandler.class);
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}

	// - M E T H O D - S E C T I O N ..........................................................................
}

// - UNUSED CODE ............................................................................................
