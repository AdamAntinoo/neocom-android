//	PROJECT:        NeoCom.Databases (NEOC.D)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Java 1.8 Library.
//	DESCRIPTION:		SQLite database access library. Isolates Neocom database access from any
//									environment limits.
package org.dimensinfin.eveonline.neocom.databases;

import java.util.logging.Logger;

import org.dimensinfin.eveonline.neocom.connector.AppConnector;
import org.dimensinfin.eveonline.neocom.constant.R;
import org.dimensinfin.eveonline.neocom.constant.R1;

// - CLASS IMPLEMENTATION ...................................................................................
public class NeocomDBHelper implements ISQLiteOpenHelper {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = Logger.getLogger(R.getStringResource("R.string.0")); //$NON-NLS-1$

	// - F I E L D - S E C T I O N ............................................................................
	private static final String				DATABASE_NAME			= AppConnector.getStorageConnector()
			.accessAppStorage(AppConnector.getResourceString("R.string.appdatabasefilename")).getAbsolutePath(); //$NON-NLS-1$
	private static final int					DATABASE_VERSION	= new Integer(
			AppConnector.getResourceString("R.string.databaseversion")).intValue();

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeocomDBHelper() {
	}

	// - M E T H O D - S E C T I O N ..........................................................................
}AppConnector.getResourceString(R1.string.appdatabasefilename)
// - UNUSED CODE ............................................................................................
