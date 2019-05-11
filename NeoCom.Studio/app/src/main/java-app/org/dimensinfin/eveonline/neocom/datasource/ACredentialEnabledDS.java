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
package org.dimensinfin.eveonline.neocom.datasource;

import android.os.Bundle;

import org.dimensinfin.android.mvc.datasource.DataSourceLocator;
import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.eveonline.neocom.entities.Credential;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public abstract class ACredentialEnabledDS extends MVCDataSource {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger = LoggerFactory.getLogger("ACredentialEnabledDS");

	// - F I E L D - S E C T I O N ............................................................................
	/** Credential to be used on all database requests and data retrievals. */
	protected Credential credential = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public ACredentialEnabledDS( final DataSourceLocator locator, final String variant
			, final IPartFactory factory, final Bundle extras ) {
		super(locator, variant, factory, extras);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public ACredentialEnabledDS setCredential( final Credential credential ) {
		this.credential = credential;
		return this;
	}
}
// - UNUSED CODE ............................................................................................
//[01]
