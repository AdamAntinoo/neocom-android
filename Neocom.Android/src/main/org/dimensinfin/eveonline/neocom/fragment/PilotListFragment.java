//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.fragment;

import java.util.logging.Logger;

import org.dimensinfin.eveonline.neocom.connector.AppConnector;
import org.dimensinfin.eveonline.neocom.connector.DataSourceLocator;
import org.dimensinfin.eveonline.neocom.connector.IModelGenerator;
import org.dimensinfin.eveonline.neocom.datasource.DataSourceManager;
import org.dimensinfin.eveonline.neocom.datasource.PilotListDataSource;
import org.dimensinfin.eveonline.neocom.datasource.SpecialDataSource;
import org.dimensinfin.eveonline.neocom.factory.PilotPartFactory;
import org.dimensinfin.eveonline.neocom.fragment.core.AbstractNewPagerFragment;
import org.dimensinfin.eveonline.neocom.generator.PilotRoasterModelGenerator;
import org.dimensinfin.eveonline.neocom.manager.ModelGeneratorManager;
import org.dimensinfin.eveonline.neocom.storage.AppModelStore;

// - CLASS IMPLEMENTATION ...................................................................................
public class PilotListFragment extends AbstractNewPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = Logger.getLogger("PilotListFragment");

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
		return "Select Capsuleer";
	}
/**
 * This is the method that any new fragment should override to implement the right DataSource association that corresponds to this activity contents.
 * The new implementation divides the creation into two steps. One creates the custom model generator that will be used on Android and on the Web platform.
 * With this model generator we then instantiate the DataSource that is only used on the Android platform
 * because the Web uses javascript to manage the model and the MVC presentation layer.
 */
	@Override
	protected void registerDataSource() {
		PilotListFragment.logger.info(">> [PilotListFragment.registerDataSource]");
		// Get the model generator with the informed variant. And use the global login that is ever defined.
		String login = AppModelStore.getSingleton().getLoginIdentifier();
		// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
		DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant()).addIdentifier(login);
		IModelGenerator generator = ModelGeneratorManager.registerGenerator(new PilotRoasterModelGenerator(locator,login));
		// Register the datasource. If this same datasource is already at the manager we get it
		// instead creating a new one.
		SpecialDataSource		ds = (SpecialDataSource) DataSourceManager.registerDataSource(new PilotListDataSource(locator, generator,this.getFactory()));
		ds.setVariant(this.getVariant());
		ds.setCacheable(true);
		this.setDataSource(ds);
	}
/**
 * This methods is used to define the parts that go inside the header section of the page. On this activity there is no content on the head section.
 */
	@Override
	protected void setHeaderContents() {
	}
}
// - UNUSED CODE ............................................................................................
