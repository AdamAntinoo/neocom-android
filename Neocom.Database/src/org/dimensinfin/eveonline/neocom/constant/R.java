//	PROJECT:        NeoCom.Databases (NEOC.D)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:	Java 1.8 Library.
//	DESCRIPTION:	SQLite database access library. Isolates Neocom database access from any
//					environment limits.
package org.dimensinfin.eveonline.neocom.constant;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

// - CLASS IMPLEMENTATION ...................................................................................
public class R {
	private static final String					BUNDLE_NAME			= "org.dimensinfin.eveonline.neocom.constant.R";	//$NON-NLS-1$

	private static final ResourceBundle	RESOURCE_BUNDLE	= ResourceBundle.getBundle(BUNDLE_NAME);

	private R() {
	}

	public static String getResourceString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}

// - UNUSED CODE ............................................................................................
