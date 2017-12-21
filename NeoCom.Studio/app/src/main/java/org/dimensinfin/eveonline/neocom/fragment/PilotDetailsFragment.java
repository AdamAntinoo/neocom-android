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
import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.core.datasource.AbstractGenerator;
import org.dimensinfin.core.datasource.DataSourceLocator;
import org.dimensinfin.core.datasource.ModelGeneratorStore;
import org.dimensinfin.core.interfaces.IModelGenerator;
import org.dimensinfin.core.model.RootNode;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.factory.PilotPartFactory;
import org.dimensinfin.eveonline.neocom.model.NeoComCharacter;

//- CLASS IMPLEMENTATION ...................................................................................
/**
 * This fragment will manage the Managers that apply to the selected and current Character. The single usage
 * of the fragment is inside the Activity so to reduce file count I have switched to store all single usage
 * code together.
 *
 * @author Adam Antinoo
 */
public class PilotDetailsFragment extends AbstractPagerFragment {
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
		return NeoComAppConnector.getSingleton().getResourceString(R.string.activity_title_PilotDetailsActivity);
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
		AbstractPagerFragment.logger.info(">> [PilotDetailsFragment.registerDataSource]");
		// Get access to the parameters and reconstruct the data chain if required.
		String loginid = this.getExtras().getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
		long characterid = this.getExtras().getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
		NeoComAppConnector.getSingleton().getModelStore().activateLoginIdentifier(loginid);
		final NeoComCharacter pilot = NeoComAppConnector.getSingleton().getModelStore().activatePilot(characterid);
		// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
		DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant())
		                                                   .addIdentifier(loginid)
		                                                   .addIdentifier(characterid);
		IModelGenerator generator = ModelGeneratorStore
				.registerGenerator(new PilotManagersGenerator(locator, this.getVariant(), pilot));
		setGenerator(generator);
		// Register the datasource. If this same datasource is already at the manager we get it
		// instead creating a new one.
//		SpecialDataSource ds = (SpecialDataSource) DataSourceManager
//				.registerDataSource(new PilotManagersDataSource(locator, generator, this.getFactory()));
//		ds.setVariant(this.getVariant());
//		ds.setCacheable(true);
//		this.setDataSource(ds);
		AbstractPagerFragment.logger.info("<< [PilotDetailsFragment.registerDataSource]");
	}

	/**
	 * This methods is used to define the parts that go inside the header section of the page. On this activity
	 * there is no content on the head section.
	 */
	@Override
	protected void setHeaderContents() {
		AbstractPagerFragment.logger.info(">> [PilotDetailsFragment.setHeaderContents]");
		// Set the part for the header that contains a detailed list of most Pilot characteristics.
		this.addtoHeader((AbstractAndroidPart) this.getFactory()
		                                           .createPart(NeoComAppConnector.getSingleton().getModelStore().getActiveCharacter())
		                                           .setRenderMode(ENeoComVariants.PILOT_HEADER.name()));
		AbstractPagerFragment.logger.info("<< [PilotDetailsFragment.setHeaderContents]");
	}
}

////- CLASS IMPLEMENTATION ...................................................................................
///**
// * Creates the DataSource adaptation code for the new generator received as a parameter. This code possibly
// * will be comon and standard so maybe will return back to its own source file. On current implementation go
// * on with the file count reduction for single usage code.
// *
// * @author Adam Antinoo
// */
//final class PilotManagersDataSource extends SpecialDataSource {
//	// - S T A T I C - S E C T I O N ..........................................................................
//	private static final long serialVersionUID = -8821864120476622316L;
//
//	// - F I E L D - S E C T I O N ............................................................................
//
//	// - C O N S T R U C T O R - S E C T I O N ................................................................
//	public PilotManagersDataSource(final DataSourceLocator locator, final IModelGenerator generator,
//			final IPartFactory partFactory) {
//		super(locator, partFactory);
//		if (null == generator)
//			throw new NeocomRuntimeException("UNEX. The Model Generator should not be empty when creating a DataSource.");
//		// REFACTOR The next code should go to the SpecialDataSource once all the DataSources comply to the new implementation.
//		_modelGenerator = generator;
//	}
//
//	// - M E T H O D - S E C T I O N ..........................................................................
//	/**
//	 * Initialize the root part and set the sorting properties before going to the SuperDataSource to perform
//	 * the function.
//	 */
//	@Override
//	public void createContentHierarchy() {
//		_partModelRoot = new RootPart(_dataModelRoot, _partFactory)
//				.setSorting(PartComparatorFactory.createPartComparator(AppWideConstants.comparators.COMPARATOR_NAME));
//		super.createContentHierarchy();
//	}
//
//	/**
//	 * This method is called whenever there is an event on any Part related to this DataSource. We just process
//	 * structure changes that need the DataSource to reconstruct the Part model from the new Model state. Wrong.
//	 * The model does not change but the result from the collaborate2View transformation does.
//	 */
//	@Override
//	public void propertyChange(final PropertyChangeEvent event) {
//		// The expand/collapse state has changed.
//		if ( SystemWideConstants.events
//				.valueOf(event.getPropertyName()) == SystemWideConstants.events.EVENTSTRUCTURE_ACTIONEXPANDCOLLAPSE) {
//			// REFACTOR Trying to fire a new model generation to solve the problem of renderization.
//			//	_dataModelRoot = modelGenerator.collaborate2Model();
//			this.createContentHierarchy();
//
//			_bodyParts = new ArrayList<IPart>();
//			_bodyParts.addAll(_partModelRoot.collaborate2View());
//			this.fireStructureChange(SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES.name(), event.getOldValue(),
//					event.getNewValue());
//			return;
//		}
//		// TODO Check if we should get this event and fire it again.
//		if (SystemWideConstants.events
//				.valueOf(event.getPropertyName()) == SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES) {
//			this.fireStructureChange(SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES.name(), event.getOldValue(),
//					event.getNewValue());
//			return;
//		}
//		super.propertyChange(event);
//	}
//}

//- CLASS IMPLEMENTATION ...................................................................................
final class PilotManagersGenerator extends AbstractGenerator implements IModelGenerator {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
//	private final String		login			= "Default";
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
		AbstractGenerator.logger.info(">> [PilotManagersGenerator.collaborate2Model]");
		// Initialize the Adapter data structures.
		this.setDataModel(new RootNode());

		// Add to the data model list the Directors that can be used on this character.
		_dataModelRoot.addChild(character.getAssetsManager());
		_dataModelRoot.addChild(character.getPlanetaryManager());
		_dataModelRoot.addChild(character.getBlueprintsManager());

		AbstractGenerator.logger.info("<< [PilotManagersGenerator.collaborate2Model]");
		return _dataModelRoot;
	}
}

// - UNUSED CODE ............................................................................................
