//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.activity;

import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.android.mvc.core.RootPart;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.eveonline.neocom.NeoComApp;
import org.dimensinfin.eveonline.neocom.activity.core.AbstractPagerActivity;
import org.dimensinfin.eveonline.neocom.connector.AppConnector;
import org.dimensinfin.eveonline.neocom.connector.DataSourceLocator;
import org.dimensinfin.eveonline.neocom.connector.IModelGenerator;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.constant.EActivityVariants;
import org.dimensinfin.eveonline.neocom.core.NeocomRuntimeException;
import org.dimensinfin.eveonline.neocom.datasource.DataSourceManager;
import org.dimensinfin.eveonline.neocom.datasource.SpecialDataSource;
import org.dimensinfin.eveonline.neocom.factory.PilotPartFactory;
import org.dimensinfin.eveonline.neocom.fragment.core.AbstractNewPagerFragment;
import org.dimensinfin.eveonline.neocom.generator.PilotDirectorsGenerator;
import org.dimensinfin.eveonline.neocom.manager.ModelGeneratorManager;
import org.dimensinfin.eveonline.neocom.storage.AppModelStore;

import android.os.Bundle;

// - CLASS IMPLEMENTATION ...................................................................................
/**
 * This Activity will show a detailed list of properties of the selected Pilot or Corporation on the header.
 * And then on the body list it will list the Managers with some summary information about them like the
 * number of assets on the Assets Manager or the value of all the Planetary stuff on the Planetary Manager.
 * 
 * @author Adam Antinoo
 */
public class PilotDetailActivity extends AbstractPagerActivity {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * Return the name of this activity
	 * 
	 * @return
	 */
	public String getName() {
		return "Pilot Detailed Information";
	}

	/**
	 * On the Activity create phase we will set the layout, then create the action bar and all other UI elements
	 * and finally creates and sets the fragments. This is to avoid the multiple creation and addition of more
	 * fragments when the activity is put again on the foreground.
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		AbstractPagerActivity.logger.info(">> [PilotDetailActivity.onCreate]"); //$NON-NLS-1$
		super.onCreate(savedInstanceState);
		try {
			// Register this Activity as the current active Activity.
			AppModelStore.getSingleton().activateActivity(this);
			int page = 0;
			this.addPage(new PilotDetailedFragment().setVariant(EActivityVariants.PILOT_DETAILS.name()), page++);
		} catch (final Exception rtex) {
			AbstractPagerActivity.logger
					.severe("[PilotDetailActivity.onCreate]> RTEX> Runtime Exception." + rtex.getMessage());
			rtex.printStackTrace();
			this.stopActivity(rtex);
		}
		// Reinitialize the tile and subtitle from the first page.
		this.updateInitialTitle();
		AbstractPagerActivity.logger.info("<< [PilotDetailActivity.onCreate]"); //$NON-NLS-1$
	}
}

//- CLASS IMPLEMENTATION ...................................................................................
/**
 * This fragment will manage the Managers that apply to the selected and current Character. The single usage
 * of the fragment is inside the Activity so to reduce file count I have switched to store all single usage
 * code together.
 * 
 * @author Adam Antinoo
 */
final class PilotDetailedFragment extends AbstractNewPagerFragment {
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
		return "Pilot Details";
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
		AbstractNewPagerFragment.logger.info(">> [PilotDetailedFragment.registerDataSource]");
		// Get the model generator with the informed variant. And use the global login that is ever defined.
		String login = AppConnector.getModelStore().getLoginIdentifier();
		// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
		DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant()).addIdentifier(login);
		IModelGenerator generator = ModelGeneratorManager.registerGenerator(new PilotDirectorsGenerator(locator, login));
		// Register the datasource. If this same datasource is already at the manager we get it
		// instead creating a new one.
		SpecialDataSource ds = (SpecialDataSource) DataSourceManager
				.registerDataSource(new PilotManagersDataSource(locator, generator, this.getFactory()));
		ds.setVariant(this.getVariant());
		ds.setCacheable(true);
		this.setDataSource(ds);
		AbstractNewPagerFragment.logger.info("<< [PilotDetailedFragment.registerDataSource]");
	}

	/**
	 * This methods is used to define the parts that go inside the header section of the page. On this activity
	 * there is no content on the head section.
	 */
	@Override
	protected void setHeaderContents() {
		AbstractNewPagerFragment.logger.info(">> [PilotDetailedFragment.setHeaderContents]");
		// Set the part for the header that contains a detailed list of most Pilot characteristics.
		this.addtoHeader(
				(AbstractAndroidPart) this.getFactory().createPart(AppModelStore.getSingleton().getCurrentPilot()));
		AbstractNewPagerFragment.logger.info("<< [PilotDetailedFragment.setHeaderContents]");
	}
}

//- CLASS IMPLEMENTATION ...................................................................................
/**
 * Creates the DataSource adaptation code for the new generator received as a parameter. This code possibly
 * will be comon and standard so maybe will return back to its own source file. On current implementation go
 * on with the file count reduction for single usage code.
 * 
 * @author Adam Antinoo
 */
final class PilotManagersDataSource extends SpecialDataSource {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = -8821864120476622316L;

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public PilotManagersDataSource(final DataSourceLocator locator, final IModelGenerator generator,
			final IPartFactory partFactory) {
		super(locator, partFactory);
		if (null == generator)
			throw new NeocomRuntimeException("UNEX. The Model Generator should not be empty when creating a DataSource.");
		// REFACTOR The next code should go to the SpecialDataSource once all the DataSources comply to the new implementation.
		_modelGenerator = generator;
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * Initialize the root part and set the sorting roperties before going to the SuperDataSource to perform the
	 * function.
	 */
	@Override
	public void createContentHierarchy() {
		_partModelRoot = new RootPart(_dataModelRoot, _partFactory)
				.setSorting(NeoComApp.createPartComparator(AppWideConstants.comparators.COMPARATOR_NAME));
		super.createContentHierarchy();
	}
}

// - UNUSED CODE ............................................................................................
