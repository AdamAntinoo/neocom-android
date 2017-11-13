//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.activity;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import org.dimensinfin.activity.AbstractPagerActivity;
import org.dimensinfin.activity.AbstractPagerFragment;
import org.dimensinfin.android.datasource.AbstractGenerator;
import org.dimensinfin.android.datasource.DataSourceLocator;
import org.dimensinfin.android.datasource.ModelGeneratorStore;
import org.dimensinfin.android.interfaces.IModelGenerator;
import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.android.mvc.core.RootPart;
import org.dimensinfin.android.mvc.datasource.DataSourceManager;
import org.dimensinfin.android.mvc.datasource.SpecialDataSource;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.model.RootNode;
import org.dimensinfin.eveonline.neocom.NeoComApp;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.core.NeocomRuntimeException;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.factory.PilotPartFactory;
import org.dimensinfin.eveonline.neocom.manager.AbstractManager;
import org.dimensinfin.eveonline.neocom.model.NeoComCharacter;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

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
	private String	loginid			= null;
	private long		characterid	= -1;

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
		// Disable go home for this activity since this is home.
		_actionBar.setDisplayHomeAsUpEnabled(false);
		try {
			// Register this Activity as the current active Activity.
			NeoComModelStore.getSingleton().activateActivity(this);
			// Get the parameters and save them on local fields to be stored on destruction and passed to Fragments.
			Bundle extras = null;
			if (null != savedInstanceState) {
				extras = savedInstanceState;
				loginid = savedInstanceState.getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
				characterid = savedInstanceState.getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
			} else {
				extras = this.getIntent().getExtras();
				loginid = extras.getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
				characterid = extras.getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
			}
			int page = 0;
			this.addPage(new PilotDetailedFragment().setVariant(ENeoComVariants.PILOT_DETAILS.name()).setExtras(extras),
					page++);
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

	@Override
	protected void onRestoreInstanceState(final Bundle savedInstanceState) {
		loginid = savedInstanceState.getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
		characterid = savedInstanceState.getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * Save the extra parameters information to be able to restore the page to their same state. There is no
	 * more need to save the AppModel state because in case of resucitation we can follow the data chain and
	 * reconstruct the AppModel state.
	 *
	 */
	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		// Save the extra parameters information to be able to restore the page to their same state.
		outState.putString(AppWideConstants.EExtras.EXTRA_LOGINID.name(), loginid);
		outState.putLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), characterid);
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
final class PilotDetailedFragment extends AbstractPagerFragment {
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
		AbstractPagerFragment.logger.info(">> [PilotDetailedFragment.registerDataSource]");
		// Get access to the parameters and reconstruct the data chain if required.
		String loginid = this.getExtras().getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
		long characterid = this.getExtras().getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
		NeoComAppConnector.getSingleton().getModelStore().activateLoginIdentifier(loginid);
		NeoComAppConnector.getSingleton().getModelStore().activatePilot(characterid);
		//		// Get the model generator with the informed variant. And use the global login that is ever defined.
		//		String login = NeoComAppConnector.getSingleton().getModelStore().getLoginIdentifier();
		// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
		DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant()).addIdentifier(loginid);
		IModelGenerator generator = ModelGeneratorStore.registerGenerator(new PilotManagersGenerator(locator,
				this.getVariant(), NeoComAppConnector.getSingleton().getModelStore().getActiveCharacter()));
		// Register the datasource. If this same datasource is already at the manager we get it
		// instead creating a new one.
		SpecialDataSource ds = (SpecialDataSource) DataSourceManager
				.registerDataSource(new PilotManagersDataSource(locator, generator, this.getFactory()));
		ds.setVariant(this.getVariant());
		ds.setCacheable(true);
		this.setDataSource(ds);
		AbstractPagerFragment.logger.info("<< [PilotDetailedFragment.registerDataSource]");
	}

	/**
	 * This methods is used to define the parts that go inside the header section of the page. On this activity
	 * there is no content on the head section.
	 */
	@Override
	protected void setHeaderContents() {
		AbstractPagerFragment.logger.info(">> [PilotDetailedFragment.setHeaderContents]");
		// Set the part for the header that contains a detailed list of most Pilot characteristics.
		this.addtoHeader((AbstractAndroidPart) this.getFactory()
				.createPart(NeoComAppConnector.getSingleton().getModelStore().getActiveCharacter())
				.setRenderMode(ENeoComVariants.PILOT_HEADER.name()));
		AbstractPagerFragment.logger.info("<< [PilotDetailedFragment.setHeaderContents]");
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
	 * Initialize the root part and set the sorting properties before going to the SuperDataSource to perform
	 * the function.
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
		if (SystemWideConstants.events
				.valueOf(event.getPropertyName()) == SystemWideConstants.events.EVENTSTRUCTURE_ACTIONEXPANDCOLLAPSE) {
			// REFACTOR Trying to fire a new model generation to solve the problem of renderization.
			//	_dataModelRoot = modelGenerator.collaborate2Model();
			this.createContentHierarchy();

			_bodyParts = new ArrayList<IPart>();
			_bodyParts.addAll(_partModelRoot.collaborate2View());
			this.fireStructureChange(SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES.name(), event.getOldValue(),
					event.getNewValue());
			return;
		}
		// TODO Check if we should get this event and fire it again.
		if (SystemWideConstants.events
				.valueOf(event.getPropertyName()) == SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES) {
			this.fireStructureChange(SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES.name(), event.getOldValue(),
					event.getNewValue());
			return;
		}
		super.propertyChange(event);
	}
}

//- CLASS IMPLEMENTATION ...................................................................................
final class PilotManagersGenerator extends AbstractGenerator implements IModelGenerator {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	private final String		login			= "Default";
	private NeoComCharacter	character	= null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public PilotManagersGenerator(final DataSourceLocator locator, final String variant,
			final NeoComCharacter pilotCorp) {
		super(locator, variant);
		character = pilotCorp;
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * The collaboration for a Pilot or Corporation on this Activity is the list of Managers that can be applied
	 * to the Character. So we can move the decission to the place where it better suits that is the Character
	 * that is already selected. This is performed internally in a recursive way by calling the model
	 * collaboration depending on the variant.
	 */
	public RootNode collaborate2Model() {
		AbstractGenerator.logger.info(">> [PilotDirectorsGenerator.collaborate2Model]");
		// Initialize the Adapter data structures.
		this.setDataModel(new RootNode());

		// Add to the data model list the Directors that can be used on this character.
		AbstractManager manager = character.getAssetsManager();
		manager.initialize();
		_dataModelRoot.addChild(manager);
		manager = character.getPlanetaryManager();
		manager.initialize();
		_dataModelRoot.addChild(manager);

		AbstractGenerator.logger.info("<< [PilotDirectorsGenerator.collaborate2Model]");
		return _dataModelRoot;
	}
}

// - UNUSED CODE ............................................................................................
