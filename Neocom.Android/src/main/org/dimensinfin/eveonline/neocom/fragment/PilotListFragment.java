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
import org.dimensinfin.eveonline.neocom.datamodel.PilotRoasterModelAdapter;
import org.dimensinfin.eveonline.neocom.datasource.DataSourceManager;
import org.dimensinfin.eveonline.neocom.datasource.PilotListDataSource;
import org.dimensinfin.eveonline.neocom.datasource.SpecialDataSource;
import org.dimensinfin.eveonline.neocom.factory.PilotPartFactory;
import org.dimensinfin.eveonline.neocom.fragment.core.AbstractNewPagerFragment;
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

	//	@Override
	//	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
	//		Log.i("NEOCOM", ">> PilotListFragment.onCreateView");
	//		final View theView = super.onCreateView(inflater, container, savedInstanceState);
	//		try {
	//			//			this.setIdentifier(_variant.hashCode());
	//			this.registerDataSource();
	//		} catch (final RuntimeException rtex) {
	//			Log.e("EVEI", "RTEX> PilotListFragment.onCreateView - " + rtex.getMessage());
	//			rtex.printStackTrace();
	//			this.stopActivity(new RuntimeException("RTEX> PilotListFragment.onCreateView - " + rtex.getMessage()));
	//		}
	//		Log.i("NEOCOM", "<< PilotListFragment.onCreateView");
	//		return theView;
	//	}
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
		IModelGenerator adapter = ModelGeneratorManager.registerGenerator(new PilotRoasterModelAdapter(locator,login));
		// Register the datasource. If this same datasource is already at the manager we get it
		// instead creating a new one.
//		SpecialDataSource ds = ;
		SpecialDataSource		ds = (SpecialDataSource) DataSourceManager.registerDataSource(new PilotListDataSource(locator, this.getFactory()));
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

//// - CLASS IMPLEMENTATION ...................................................................................
//final class PilotListPartFactory implements IPartFactory {
//	// - S T A T I C - S E C T I O N ..........................................................................
//	// - F I E L D - S E C T I O N ............................................................................
//	private EFragment variant = AppWideConstants.EFragment.DEFAULT_VARIANT;
//
//	// - C O N S T R U C T O R - S E C T I O N ................................................................
//	public PilotListPartFactory(final EFragment variantSelected) {
//		variant = variantSelected;
//	}
//
//	//- M E T H O D - S E C T I O N ..........................................................................
//	/**
//	 * The method should create the matching part for the model received but there is no other place where we
//	 * should create the next levels of the hierarchy. So we will create the part trasnformationes here.
//	 */
//	@Override
//	public IEditPart createPart(final IGEFNode node) {
//		if (node instanceof NeoComApiKey) {
//			AbstractCorePart part = new APIKeyPart((AbstractComplexNode) node).setFactory(this);
//			return part;
//		}
//		if (node instanceof NeoComCharacter) {
//			AbstractCorePart part = new PilotInfoPart((AbstractComplexNode) node).setFactory(this);
//			return part;
//		}
//		return null;
//	}
//
//	public String getVariant() {
//		return variant.name();
//	}
//}
//
// - UNUSED CODE ............................................................................................
