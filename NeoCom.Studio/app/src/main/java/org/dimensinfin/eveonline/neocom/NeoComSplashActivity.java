//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2016 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.logging.Logger;

//- CLASS IMPLEMENTATION ...................................................................................

/**
 * This is the application launcher Activity. It is the first Activity to show to the user and its responsibility is to
 * show the initialization progress of the Application data structures. It is based on the generic MVC Activity that
 * shown Views on a Header section and a list of Views on the List section. The elements to show on the list are the
 * different initialization tasks to execute or pending execution and composed in the standard list way. We start with a
 * model node that that gets converted to a Part on the Factory and that Part manages the Renderer View that is finally
 * shown on the screen.
 *
 * @author Adam Antinoo
 */
public class NeoComSplashActivity extends AppCompatActivity {
	// - S T A T I C - S E C T I O N ..........................................................................
	public static Logger logger = Logger.getLogger("NeoComSplashActivity");

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	protected void onCreate (@Nullable final Bundle savedInstanceState) {
		logger.info(">> [NeoComSplashActivity.onCreate]"); //$NON-NLS-1$
		super.onCreate(savedInstanceState);
		logger.info("<< [NeoComSplashActivity.onCreate]"); //$NON-NLS-1$
	}
}
