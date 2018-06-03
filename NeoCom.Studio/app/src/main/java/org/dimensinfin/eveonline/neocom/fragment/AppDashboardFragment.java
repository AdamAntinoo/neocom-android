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
package org.dimensinfin.eveonline.neocom.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.android.mvc.core.PartFactory;
import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.android.mvc.interfaces.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.datasource.DataSourceLocator;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.model.AppVersion;
import org.dimensinfin.eveonline.neocom.part.AppVersionPart;

/**
 * Fragments are the structures that support a page's contents. They define the elements to be included on the header section and the
 * data providers that should generate any list of data to be rendered on the body of the page. All those pages share the same structure.
 * A fixed header and an scrollable content list. Just the data source will make each page different from each other.
 *
 * Also each page defines som elements of the ActionBar. The title and the subtitle are mandatory but fragments can adapt to other
 * ActionBar structures if the Activity demands that specialization.
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class AppDashboardFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("AppDashboardFragment");

	// - F I E L D - S E C T I O N ............................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public IPartFactory createFactory() {
		return new AppDashboardFactory(getVariant());
	}

	@Override
	public String getSubtitle() {
		return "Online: 34.876 capsuleers";
	}

	/**
	 * The page title is the application name and version as defined on the initialization constants. To read them we should access the
	 * Application Context so we can be sure that we have access to the application resources even if the fragment is still not tied up to
	 * an Activity.
	 * @return the title string.
	 */
	@Override
	public String getTitle() {
		return getAppContext().getResources().getString(R.string.appname)
				+ " "
				+ getAppContext().getResources().getString(R.string.appversionname);
	}

	@Override
	protected List<ICollaboration> registerHeaderSource() {
		return new ArrayList<>(); // No header contents. Return an empty model list.
	}

	@Override
	protected IDataSource registerDataSource() {
		return new AppDashboardDataSource(new DataSourceLocator().addIdentifier("APP-DASHBOARD")
				, getVariant()
				, createFactory()
				, getExtras())
				.setCacheable(true)
				.setVariant(getVariant());
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	public static class AppDashboardFactory extends PartFactory {
		// - F I E L D - S E C T I O N ............................................................................

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public AppDashboardFactory( final String selectedVariant ) {
			super(selectedVariant);
		}

		// - M E T H O D - S E C T I O N ..........................................................................

		/**
		 * This method returns the matching Part for an input node class. Depending on the class and the variant we can generate a different
		 * set of controllers to build up our model-view-controller structures.
		 * @param node the application node for what we should find a Part and create an instance.
		 * @return new <code>IPart</code> instance that is able to manage this class of node.
		 */
		@Override
		public IPart createPart( final ICollaboration node ) {
			logger.info("-- [AppDashboardFactory.createPart]> Node class: {}"+ node.getClass().getSimpleName());
			if ( node instanceof AppVersion ) {
				IPart part = new AppVersionPart((AppVersion) node).setFactory(this)
				                                                  .setRenderMode(this.getVariant());
				return part;
			}
			return super.createPart(node);
		}
	}
	//...........................................................................................................

	// - CLASS IMPLEMENTATION ...................................................................................
	public static class AppDashboardDataSource extends MVCDataSource {
		// - F I E L D - S E C T I O N ............................................................................

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public AppDashboardDataSource( final DataSourceLocator locator, final String variant, final IPartFactory factory, final Bundle extras ) {
			super(locator, variant, factory, extras);
		}

		// - M E T H O D - S E C T I O N ..........................................................................

		/**
		 * This is the method that creates the model nodes that are going to compose the source of data for the page body content list. This
		 * being the application dashboard page we should add (but on different versions) the next list of items:
		 * - The Application Version Box.
		 * - The list of NeoCom notifications and change reports.
		 * - The list of pending to read Eve Online notifications.
		 * - The list of registered Credentials.
		 *
		 * On this first version we should just add the Version Box and the list of Credentials to be able to create or select a Pilot.
		 */
		@Override
		public void collaborate2Model() {
			logger.info(">> [AppDashboardDataSource.collaborate2Model]");
			// Check if the data is already on the cache so we should not have to process again the model generation.
			if ( !isCached() ) {
				try {
					this.addModelContents(
							new AppVersion()
									.setName(AndroidGlobalDataManager.getResourceString(R.string.appname))
									.setVersion(AndroidGlobalDataManager.getResourceString(R.string.appversionname))
					);
				} catch (RuntimeException rtex) {
					AndroidGlobalDataManager.registerRuntimeException("AppDashboardDataSource.collaborate2Model"
							, rtex, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
					rtex.printStackTrace();
				}
			}
			logger.info("<< [AppDashboardDataSource.collaborate2Model]");
		}
	}
	//...........................................................................................................
}
// - UNUSED CODE ............................................................................................
//[01]
