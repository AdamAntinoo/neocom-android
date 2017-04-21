//	PROJECT:        MS-PlanetaryOptimizer (MS-PO)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:	SpringBoot-MS-Java 1.8.
//	DESCRIPTION:	Initial step to have an Eve application based on the Android code. This version exports
//								a list of assets and also has access to the Eve Item CCP database to get some of the
//								asset items properties..
package org.dimensinfin.eveonline.neocom.microservices.adapter;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.dimensinfin.eveonline.neocom.connector.IConnector;

// - CLASS IMPLEMENTATION ...................................................................................
public abstract class MicroServiceAbstractConnector implements IConnector {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger								logger					= Logger.getLogger("MicroServiceAdapter.java");
	private static final String					BUNDLE_NAME			= "org.dimensinfin.eveonline.neocom.constant.R";	//$NON-NLS-1$
	private static final ResourceBundle	RESOURCE_BUNDLE	= ResourceBundle.getBundle(BUNDLE_NAME);

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public MicroServiceAbstractConnector() {
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public String getResourceString(int key) {
		try {
			return RESOURCE_BUNDLE.getString(Integer.valueOf(key).toString());
		} catch (MissingResourceException e) {
			return '!' + Integer.valueOf(key).toString() + '!';
		}
	}

	public String getResourceString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}

// - UNUSED CODE ............................................................................................
