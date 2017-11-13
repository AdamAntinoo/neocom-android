//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom;

import android.app.Activity;
import android.app.Application;

import org.dimensinfin.eveonline.neocom.decorator.INeoComAppDecorator;

import java.util.logging.Logger;

public class NeoComApplication extends Application implements INeoComAppDecorator{
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger						= Logger.getLogger("NeoComApplication");
	private static NeoComApplication					singleton					= null;

	// - F I E L D - S E C T I O N ............................................................................
	private NeoComAppConnector					_appConnector				= null;
	private ModelAppConnector						_modelAppConnector	= null;
	/**
	 * Reference to the startup activity of the application. This Activity gets restarted when there is an
	 * intercepted runtime exception.
	 */
	private Activity										_firstActivity			= null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeoComApplication() {
		logger.info(">> [NeoComApplication.<contructor>]");
		// Setup the referencing structures that will serve as proxy and global references.
		if (null == singleton) {
			// Just connect the singleton and the Connector chanin to get access to all the methods.
			singleton = this;
			_appConnector = new NeoComAppConnector(this);
			_modelAppConnector = new ModelAppConnector(this);
		}
		logger.info("<< [NeoComApplication.<consructor>]");
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * This method is used by shared libraries to move to a safe Activity page when there is an unexpected exception. All Dimensinfin applications
	 * should implement this method to connect it to an application start Activity.
	 * @return
	 */
	@Override
	public Activity getFirstActivity () {
		if (null == _firstActivity) throw new RuntimeException(
				"RTEX [NeoComApp.getFirstActivity]> Target destionation First Activity of application is not defined.");
		return _firstActivity;
	}
}
