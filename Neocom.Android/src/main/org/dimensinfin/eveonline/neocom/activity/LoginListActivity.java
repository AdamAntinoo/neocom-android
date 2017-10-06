//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2016 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.activity;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import org.dimensinfin.android.datasource.DataSourceLocator;
import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.core.DataSourceManager;
import org.dimensinfin.android.mvc.core.RootPart;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.model.RootNode;
import org.dimensinfin.eveonline.neocom.NeoComApp;
import org.dimensinfin.eveonline.neocom.activity.core.ANeoComPagerActivity;
import org.dimensinfin.eveonline.neocom.activity.core.AbstractPagerActivity;
import org.dimensinfin.eveonline.neocom.connector.AppConnector;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.datasource.SpecialDataSource;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.factory.PilotPartFactory;
import org.dimensinfin.eveonline.neocom.fragment.core.AbstractPagerFragment;
import org.dimensinfin.eveonline.neocom.generator.AbstractGenerator;
import org.dimensinfin.eveonline.neocom.generator.ModelGeneratorStore;
import org.dimensinfin.eveonline.neocom.interfaces.IModelGenerator;
import org.dimensinfin.eveonline.neocom.model.Login;
import org.dimensinfin.eveonline.neocom.storage.AppModelStore;

import android.os.Bundle;

// - CLASS IMPLEMENTATION ...................................................................................
public class LoginListActivity extends ANeoComPagerActivity {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger = Logger.getLogger("PilotListActivity");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * Return the name of this activity
	 * 
	 * @return
	 */
	public String getName() {
		return "Capsuleer List";
	}

	/**
	 * On the Activity create phase we will set the layout, then create the action bar and all other UI elements
	 * and finally creates and sets the fragments. This is to avoid the multiple creation and addition of more
	 * fragments when the activity is put again on the foreground.
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		AbstractPagerActivity.logger.info(">> [PilotListActivity.onCreate]"); //$NON-NLS-1$
		super.onCreate(savedInstanceState);
		// Disable go home for this activity since this is home.
		_actionBar.setDisplayHomeAsUpEnabled(false);
		try {
			// Process the parameters into the context. This initial Activity is the only one with no parameters.
			// Create the pages that form this Activity. Each page implemented by a Fragment.
			int page = 0;
			// Register this Activity as the current active Activity.
			AppModelStore.getSingleton().activateActivity(this);
			this.addPage(new LoginListFragment().setVariant(ENeoComVariants.CAPSULEER_LIST.name()), page++);
		} catch (final Exception rtex) {
			AbstractPagerActivity.logger.severe("[PilotListActivity.onCreate]> RTEX> Runtime Exception." + rtex.getMessage());
			rtex.printStackTrace();
			this.stopActivity(rtex);
		}
		// Reinitialize the tile and subtitle from the first page.
		this.updateInitialTitle();
		AbstractPagerActivity.logger.info("<< [PilotListActivity.onCreate]"); //$NON-NLS-1$
	}
}

//- CLASS IMPLEMENTATION ...................................................................................
/**
 * Generates the list of keys and then the character authenticated for each key. By default the keys are
 * expanded but the user may choose to collapse them and that information will be stored inside the model. The
 * current version does not store the expand/collapse state at any other place.<br>
 * New version. This class connects to the model Generator to ge the list and hierarchy of model objects that
 * should be represented and then runs the other methods to generate the list of views to feed to the Adapter.
 * The <code>collaborate2Model</code> feature is then moved to the <code>modelGenerator</code> instance
 * assigned during creatin.
 * 
 * @author Adam Antinoo
 */
final class LoginListDataSource extends SpecialDataSource {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = -33788421750264209L;

	// - F I E L D - S E C T I O N ............................................................................
	//	private IModelGenerator		modelGenerator		= null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	//	public LoginListDataSource2(final DataSourceLocator locator, final IPartFactory partFactory) {
	//		super(locator, partFactory);
	//	}

	public LoginListDataSource(final DataSourceLocator locator, final IPartFactory partFactory,
			final IModelGenerator generator) {
		super(locator, partFactory);
		if (null == generator) throw new RuntimeException(
				"EX [LoginListDataSource.<initialize>]>UNEX. The Model Generator should not be empty.");
		_modelGenerator = generator;
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	//	/**
	//	 * This method is called to initialize the model structures. Those structures are copies or transformations
	//	 * from the main model stored in the <code>store</code> reference. <br>
	//	 * This is the method to initialize the copy of the model structures on the DataSource. Every time this
	//	 * method is called, the complete model is recreated. There are two ways to recreate it, comparing with the
	//	 * old copy and inserting/deleting different nodes or recreating completely the new model copy. Once this
	//	 * method is called we can create the depending part hierarchy. In the current implementation we always
	//	 * recreate the model from scratch.<br>
	//	 * The resulting model always has a RootNode and the contents are stored as children of that node. The model
	//	 * only deals with the first level so each on the childs will create their own set of the model on call when
	//	 * required by the model transformation.
	//	 */
	//	@Override
	//	public RootNode collaborate2Model() {
	//		SpecialDataSource.logger.info(">< [LoginListDataSource.collaborate2Model]");
	//		_dataModelRoot = modelGenerator.collaborate2Model();
	//		return _dataModelRoot;
	//	}

	/**
	 * Set the RootPart and the sort element for it.
	 */
	@Override
	public void createContentHierarchy() {
		_partModelRoot = new RootPart(_dataModelRoot, _partFactory)
				.setSorting(NeoComApp.createPartComparator(AppWideConstants.comparators.COMPARATOR_NAME));
		super.createContentHierarchy();
	}

	/**
	 * This method is called whenever there is an event on any Part related to this DataSource. We just process
	 * structure changes that need the DataSource to reconstruct the Part model from the new Model state. Wrong.
	 * The model does not change but the result from the collaborate2View transformation does.
	 */
	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		// The expand/collapse state has changed.
		if (event.getPropertyName().equalsIgnoreCase(SystemWideConstants.events.EVENTSTRUCTURE_ACTIONEXPANDCOLLAPSE)) {
			// REFACTOR Trying to fire a new model generation to solve the problem of renderization.
			//	_dataModelRoot = modelGenerator.collaborate2Model();
			this.createContentHierarchy();

			_bodyParts = new ArrayList<IPart>();
			_bodyParts.addAll(_partModelRoot.collaborate2View());
			this.fireStructureChange(SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES, event.getOldValue(),
					event.getNewValue());
			return;
		}
		// TODO Check if we should get this event and fire it again.
		if (event.getPropertyName().equalsIgnoreCase(SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES)) {
			this.fireStructureChange(SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES, event.getOldValue(),
					event.getNewValue());
			return;
		}
		super.propertyChange(event);
	}
}

//- CLASS IMPLEMENTATION ...................................................................................
final class LoginListFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................
	//private static Logger logger = Logger.getLogger("PilotListFragment");

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
		return "Capsuleer Selection";
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
		// Register the datasource. If this same datasource is already at the manager we get it
		// instead creating a new one.
		SpecialDataSource ds = (SpecialDataSource) DataSourceManager
				.registerDataSource(new LoginListDataSource(locator, this.getFactory(), generator));
		ds.setVariant(this.getVariant());
		ds.setCacheable(true);
		this.setDataSource(ds);
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
	//private String _login = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public LoginListGenerator(final DataSourceLocator locator, final String variant) {
		super(locator, variant);
		//	_login = login;
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
		// Get the list for registered Logins.
		//	List<NeoComCharacter> characters = AppConnector.getModelStore().getActiveCharacters();
		// Initialize the Adapter data structures.
		this.setDataModel(new RootNode());
		// Add each Login to the list. They will expand to show the Character selection.
		for (Login login : AppConnector.getModelStore().accessLoginList().values()) {
			_dataModelRoot.addChild(login);
			AbstractGenerator.logger
					.info("-- [LoginListGenerator.collaborate2Model]> Adding '" + login.getName() + "' to the _dataModelRoot");
		}
		// Order the logins by name.
		//		Collections.sort(_dataModelRoot.getChildren(),
		//			NeoComApp.createPartComparator(AppWideConstants.comparators.COMPARATOR_NAME));
		AbstractGenerator.logger.info("<< [LoginListGenerator.collaborate2Model]");
		return _dataModelRoot;
	}
}

//- UNUSED CODE ............................................................................................
