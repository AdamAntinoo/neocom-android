//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.interfaces;

import java.io.File;

import org.dimensinfin.android.mvc.connector.IMVCAppConnector;
import org.dimensinfin.eveonline.neocom.connector.AndroidCacheConnector;
import org.dimensinfin.eveonline.neocom.connector.AndroidDatabaseConnector;
import org.dimensinfin.eveonline.neocom.connector.ICCPDatabaseConnector;
import org.dimensinfin.eveonline.neocom.connector.IStorageConnector;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

// - CLASS IMPLEMENTATION ...................................................................................
public interface INeoComAppConnector extends IMVCAppConnector {
	public File accessAppStorage(final String resourceString);

	public String getAppFilePath(final int fileresourceid);

	public String getAppFilePath(final String fileresourceName);

	public AndroidCacheConnector getCacheConnector();

	public ICCPDatabaseConnector getCCPDBConnector();

	public AndroidDatabaseConnector getDBConnector();

	public NeoComModelStore getModelStore();

	public IStorageConnector getStorageConnector();

	public void startTimer();
}

// - UNUSED CODE ............................................................................................
