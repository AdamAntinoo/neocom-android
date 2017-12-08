//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.dimensinfin.eveonline.neocom.activity.core.ANeoComPagerActivity;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.fragment.SplashTasksFragment;

import java.util.logging.Logger;

//- CLASS IMPLEMENTATION ...................................................................................

/**
 * This is the application launcher Activity. It is the first Activity to show to the user and its responsibility is to
 * show the initialization progress of the Application data structures. It is based on the generic MVC Activity that
 * shown Views on a Header section and a list of Views on the List section.<br> The elements shown on the header are the
 * name, version and other version characteristics that represent a singular version implementation, like the build or
 * the CCP database version.<br> The elements to show on the list are the different initialization tasks to execute or
 * pending execution and composed in the standard list way. We start with a model node that that gets converted to a
 * Part on the Factory and that Part manages the Renderer View that is finally shown on the screen.
 *
 * @author Adam Antinoo
 */
public class NeoComSplashActivity extends ANeoComPagerActivity {
	// - S T A T I C - S E C T I O N ..........................................................................
	public static Logger logger = Logger.getLogger("NeoComSplashActivity");
	private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1234;

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * <p>Creates the Activity structure with the standard MVC layout and adds a single page that used both of the display
	 * areas to show the next information:</p> <p><ul><li>The name of the application</li> <li>The current installed
	 * version and if applicable the latest available version of the application.</li> <li>The applicable stardate that is
	 * represented by the version of the CCP database. Each CCP update and deployment that changes the data model should
	 * be placed on an stardate so if items are not properly shown it will be easy to identify it this is the cause.</li>
	 * <li>Ay other fancy application information to be read while initializing the data structures.</li></ul> </p> <p> On
	 * the list are aI will draw the initialization tasks being run and the completed ones. There is no progress
	 * information available so I think about adding a timer to show the time that the current task is running. </p>
	 *
	 * @param savedInstanceState not applicable to this initial Activity.
	 */
	@Override
	protected void onCreate (@Nullable final Bundle savedInstanceState) {
		logger.info(">> [NeoComSplashActivity.onCreate]"); //$NON-NLS-1$
		super.onCreate(savedInstanceState);
		try {
			// Disable go home for this activity since this is home.
			_actionBar.setDisplayHomeAsUpEnabled(false);
			// Hide the Actionbar because this activity should not show it.
			_actionBar.hide();
			// Create the pages that form this Activity. Each page implemented by a Fragment.
		} catch (RuntimeException rtex) {
			logger.warning("RTEX [NeoComSplashActivity.onCreate]> " + rtex.getMessage());
			rtex.printStackTrace();
			this.stopActivity(new RuntimeException("RTEX [NeoComSplashActivity.onCreate]> " + rtex.getMessage()));
		}
		try {
			int page = 0;
			this.addPage(new SplashTasksFragment().setVariant(ENeoComVariants.SPLASH_SCREEN.name()), page++);
		} catch (RuntimeException rtex) {
			logger.warning("RTEX [NeoComSplashActivity.onCreate]> " + rtex.getMessage());
			rtex.printStackTrace();
			this.stopActivity(new RuntimeException("RTEX [NeoComSplashActivity.onCreate]> " + rtex.getMessage()));
		}
		// Reinitialize the tile and subtitle from the first page.
		this.updateInitialTitle();
		logger.info("<< [NeoComSplashActivity.onCreate]"); //$NON-NLS-1$
	}

	/**
	 * <p>New on this release version is the check for the Application Privileges that we should have granted to perform
	 * some of the functionalities. On Android version 23 this is no longer a requirement from the AndroidManifest but
	 * something the application should manage internaly with code. This implementation s a base test and needs more
	 * elaboration.</p>
	 * After the Activity UI is created we should start the initialization checks. We do this on
	 * background threads out of the main thread that is used solely to update and draw the progress interface. The
	 * correct implementation is a list of Runnable code blocks that are executed in a fixed sequence. An improvement may
	 * be to allow each block to determine the next block to execute in the sequence, that will allow to include new
	 * bocks
	 * depending on if clauses. Each of the Runnable tasks should have 4 stages:
	 * <p>
	 * <ul>
	 * <li><b>Initial Stage.- </b>Add the task item to the display and start the time counter signaling the user the
	 * task description that is about to
	 * start.</li>
	 * <li><b>Content Stage.- </b>Does the main task. This should be a complete block maybe constructed with
	 * more that one block but that has a single temination code, be it an OK or a FAILURE.</li>
	 * <li><b>Next task Stage.- </b>Depending on the termination code we select the next Runnable block to run.</li>
	 * <li><b>Termination Stage.- </b>Update the UI representation to inform the user of the termination of the task.</li>
	 * </ul>
	 * </p>
	 */
	protected void onStart () {
		logger.info(">> [NeoComSplashActivity.onStart]");
		super.onStart();
		obtainPrivileges();
		logger.info("<< [NeoComSplashActivity.onStart]");
	}
	private boolean obtainPrivileges(){
		// Request application privileges if not already obtained.
		if ( Build.VERSION.SDK_INT < 23 ) return true;
		if ( Build.VERSION.SDK_INT >= 23 ) {
			String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
			if ( this.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED ) {
				// Should we show an explanation?
				boolean ask = this.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
				if ( !ask ) {
					this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
							MY_PERMISSIONS_REQUEST_READ_CONTACTS);
				}
			} else {
				return true;
			}
		}
		return false;
	}
}
