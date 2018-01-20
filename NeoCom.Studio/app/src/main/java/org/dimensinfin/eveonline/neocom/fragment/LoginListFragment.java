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

import android.os.Bundle;
import android.support.annotation.WorkerThread;

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.core.datasource.AbstractGenerator;
import org.dimensinfin.core.datasource.DataSourceLocator;
import org.dimensinfin.core.datasource.ModelGeneratorStore;
import org.dimensinfin.core.interfaces.IModelGenerator;
import org.dimensinfin.core.model.RootNode;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.core.model.Separator.ESeparatorType;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.factory.ModelFactory;
import org.dimensinfin.eveonline.neocom.factory.PilotPartFactory;
import org.dimensinfin.eveonline.neocom.database.entity.Credential;
import org.dimensinfin.eveonline.neocom.model.NewLoginAction;
import org.dimensinfin.eveonline.neocom.model.PilotV1;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

//- CLASS IMPLEMENTATION ...................................................................................
public class LoginListFragment extends AbstractPagerFragment {
	//- CLASS IMPLEMENTATION ...................................................................................
	public static class LoginListGenerator extends AbstractGenerator implements IModelGenerator {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public LoginListGenerator (final DataSourceLocator locator, final String variant, final Bundle extras) {
			super(locator, variant);
		}

		// - M E T H O D - S E C T I O N ..........................................................................

		/**
		 * This adapter should generate the model for all the Eve Online characters associated to a set of api keys
		 * or ESI authorization credentials. The old api key model will get obsolete on 2018 and the new authorization
		 * should be active before that date.
		 * The Character list of obtained with a merge and filter from the api keys and the credentials stored at
		 * the database and the result is shown on this Fragment.
		 */
		@WorkerThread
		public RootNode collaborate2Model () {
			AbstractGenerator.logger.info(">> [LoginListGenerator.collaborate2Model]");
			// Initialize the Adapter data structures.
			this.setDataModel(new RootNode());
			// Add each Credential to the list.
			for (Credential login :  NeoComModelStore.accessCredentialList()) {
				_dataModelRoot.addChild(login);
				// Preload the matching pilot.
				final PilotV1 pilot = ModelFactory.getPilotV1(login.getAccountId());
				AbstractGenerator.logger
						.info("-- [LoginListGenerator.collaborate2Model]> Adding '" + login.getAccountName() + "' to the _dataModelRoot");
			}
			// Add the node to display the new New Login Button.
			_dataModelRoot.addChild(new Separator().setType(ESeparatorType.LINE_WHITE));
			_dataModelRoot.addChild(new NewLoginAction());
			AbstractGenerator.logger.info("<< [LoginListGenerator.collaborate2Model]");
			return _dataModelRoot;
		}
	}
	// - S T A T I C - S E C T I O N ..........................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public void createFactory () {
		this.setFactory(new PilotPartFactory(this.getVariant()));
	}

	@Override
	public String getSubtitle () {
		return "";
	}

	@Override
	public String getTitle () {
		return NeoComAppConnector.getSingleton().getResourceString(R.string.activity_title_LoginListActivity);
	}

	/**
	 * This is the method that any new fragment should override to implement the right DataSource association
	 * that corresponds to this activity contents. The new implementation divides the creation into two steps.
	 * One creates the custom model generator that will be used on Android and on the Web platform. With this
	 * model generator we then instantiate the DataSource that is only used on the Android platform because the
	 * Web uses javascript to manage the model and the MVC presentation layer.
	 */
	@Override
	protected void registerDataSource () {
		AbstractPagerFragment.logger.info(">> [LoginListFragment.registerDataSource]");
		// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
		DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant());
		IModelGenerator generator = ModelGeneratorStore
				.registerGenerator(new LoginListGenerator(locator, this.getVariant(), new Bundle()));
		setGenerator(generator);
		AbstractPagerFragment.logger.info("<< [LoginListFragment.registerDataSource]");
	}

	/**
	 * This methods is used to define the parts that go inside the header section of the page. On this activity
	 * there is no content on the head section.
	 */
	@Override
	protected void setHeaderContents () {
	}
}
// - UNUSED CODE ............................................................................................
