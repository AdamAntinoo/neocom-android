//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.fragment;

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.core.datasource.AbstractGenerator;
import org.dimensinfin.core.datasource.DataSourceLocator;
import org.dimensinfin.core.datasource.ModelGeneratorStore;
import org.dimensinfin.core.interfaces.IModelGenerator;
import org.dimensinfin.core.model.RootNode;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.factory.PilotPartFactory;
import org.dimensinfin.eveonline.neocom.model.Login;
import org.dimensinfin.eveonline.neocom.model.NewLoginAction;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

//- CLASS IMPLEMENTATION ...................................................................................
public class LoginListFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public void createFactory() {
		this.setFactory(new PilotPartFactory(this.getVariant()));
	}

	@Override
	public String getSubtitle() {
		return "";
	}

	@Override
	public String getTitle() {
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
	protected void registerDataSource() {
		AbstractPagerFragment.logger.info(">> [LoginListFragment.registerDataSource]");
		// Get the model generator with the informed variant. And use the global login that is ever defined.
		//	String login = AppModelStore.getSingleton().getLoginIdentifier();
		// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
		DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant());
		IModelGenerator generator = ModelGeneratorStore
				.registerGenerator(new LoginListGenerator(locator, this.getVariant()));
		setGenerator(generator);
//		// Register the datasource. If this same datasource is already at the manager we get it
//		// instead creating a new one.
//		SpecialDataSource ds = (SpecialDataSource) DataSourceManager
//				.registerDataSource(new MVCDataSource(locator, this.getFactory(), generator));
//		ds.setVariant(this.getVariant());
//		ds.setCacheable(true);
//		this.setDataSource(ds);
		AbstractPagerFragment.logger.info("<< [LoginListFragment.registerDataSource]");
	}

	/**
	 * This methods is used to define the parts that go inside the header section of the page. On this activity
	 * there is no content on the head section.
	 */
	@Override
	protected void setHeaderContents() {
	}
}

//- CLASS IMPLEMENTATION ...................................................................................
final class LoginListGenerator extends AbstractGenerator implements IModelGenerator {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public LoginListGenerator(final DataSourceLocator locator, final String variant) {
		super(locator, variant);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * This adapter should generate the model for all the EVE characters associated to a set of api keys. The
	 * set is selected on the value of the login identifier that should already have stores that identifier
	 * along the api key on the Neocom database for retrieval. So from the unique login we get access to the set
	 * of keys and from there to the set of characters.
	 */
	public RootNode collaborate2Model() {
		AbstractGenerator.logger.info(">> [LoginListGenerator.collaborate2Model]");
		// Initialize the Adapter data structures.
		this.setDataModel(new RootNode());
		// Add each Login to the list. They will expand to show the Character selection.
		for (Login login : NeoComModelStore.getSingleton().accessLoginList().values()) {
			_dataModelRoot.addChild(login);
			AbstractGenerator.logger
					.info("-- [LoginListGenerator.collaborate2Model]> Adding '" + login.getName() + "' to the _dataModelRoot");
		}
		// Add the node to display the new New Login Button.
		_dataModelRoot.addChild(new NewLoginAction());
		AbstractGenerator.logger.info("<< [LoginListGenerator.collaborate2Model]");
		return _dataModelRoot;
	}
}
// - UNUSED CODE ............................................................................................
