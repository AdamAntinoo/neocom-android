//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.connector;

import java.io.File;

import org.dimensinfin.android.mvc.connector.IMVCAppConnector;
import org.joda.time.Duration;
import org.joda.time.Instant;

import android.graphics.drawable.Drawable;

// - CLASS IMPLEMENTATION ...................................................................................
public interface INeoComAppConnector extends IMVCAppConnector, IModelAppConnector {
	public File accessAppStorage(final String resourceString);

	public void addDrawableToCache(final String key, final Drawable image);

	public boolean checkExpiration(final Instant timestamp, final long window);

	public boolean checkExpiration(final long timestamp, final long window);

	public String getAppFilePath(final int fileresourceid);

	public String getAppFilePath(final String fileresourceName);

	public AndroidCacheConnector getCacheConnector();

	public IStorageConnector getStorageConnector();

	public void startChrono();

	public void startTimer();

	public Duration timeLapse();
}

// - UNUSED CODE ............................................................................................
