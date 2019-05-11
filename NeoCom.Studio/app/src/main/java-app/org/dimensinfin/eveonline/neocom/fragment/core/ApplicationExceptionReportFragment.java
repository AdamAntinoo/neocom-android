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
package org.dimensinfin.eveonline.neocom.fragment.core;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.android.mvc.core.PartFactory;
import org.dimensinfin.android.mvc.datasource.DataSourceLocator;
import org.dimensinfin.android.mvc.datasource.ExceptionDataSource;
import org.dimensinfin.android.mvc.interfaces.IPartsDataSource;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class ApplicationExceptionReportFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("ApplicationExceptionReportFragment");

	// - F I E L D - S E C T I O N ............................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public IPartFactory createFactory() {
		return new PartFactory(getVariant());
	}

	@Override
	public String getSubtitle() {
		return "Exception Message";
	}

	@Override
	public String getTitle() {
		return "NeoCom Application Exception";
	}

	@Override
	protected List<ICollaboration> registerHeaderSource() {
		return new ArrayList<>();
	}

	@Override
	protected IPartsDataSource registerDataSource() {
		logger.info(">< [ApplicationExceptionReportFragment.registerDataSource]");
					// Return the default exceptional data source to display the exception message.
			return new ExceptionDataSource(new DataSourceLocator().addIdentifier("EXCEPTION")
					, getVariant()
					, createFactory(), getExtras())
					.registerException(AndroidGlobalDataManager.getLastException());
	}
}
// - UNUSED CODE ............................................................................................
//[01]
