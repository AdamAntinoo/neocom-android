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

import org.dimensinfin.android.mvc.core.PartFactory;
import org.dimensinfin.android.mvc.core.RootPart;
import org.dimensinfin.android.mvc.interfaces.IAndroidPart;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.datasource.AbstractGenerator;
import org.dimensinfin.core.datasource.DataSourceLocator;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.interfaces.IModelGenerator;
import org.dimensinfin.core.model.RootNode;
import org.dimensinfin.eveonline.neocom.model.AppVersion;
import org.dimensinfin.eveonline.neocom.model.InitializationTask;
import org.dimensinfin.eveonline.neocom.parts.AppVersionPart;

import java.util.ArrayList;

//- CLASS IMPLEMENTATION ...................................................................................

/**
 * This Fragment is created with the new MVC structure. As previous fragments will initialize the key data structures
 * required by the core code as the Title, Subtitle, Header contents and List contents providers. But wil many of the
 * new simplifications like the removal of the need to define any DataSource or manage any Part directly.
 */
public class SplashTasksFragment extends NewPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	//- M E T H O D - S E C T I O N ..........................................................................
	@Override
	public void createFactory () {
		logger.info(">> [SplashTasksFragment.createFactory]");
		this.setFactory(new SplashPartFactory(this.getVariant()));
		logger.info("<< [SplashTasksFragment.createFactory]");
	}

	@Override
	protected void registerDataSource () {
		logger.info(">> [AssetsFragment.registerDataSource]");
		// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
		DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant());

		// Create a new Model Genrator and register it onto the Fragment to use on request to generate the root model.
		setGenerator(new InitializationGenerator(locator, this.getVariant()));
		logger.info("<< [AssetsFragment.registerDataSource]");
	}
	/**
	 * Set the model or model generator that defines the content of the Header section (non scroll list of View) of the
	 * current Page. The current version inserts directly the Parts so the Fragment will have the resposibility to
	 * generate all the content parts from the supposed model. This sample code will have to be added to the Superclass to
	 * remove all that knowledge from the development fragment.
	 */
	@Override
	protected void setHeaderContents () {
//		return;
		// Get the model from a generator or from a List.
		try {
			ArrayList<ICollaboration> headerModelContents = new ArrayList<ICollaboration>();
			headerModelContents.add(new AppVersion());
			RootNode headerModel = new RootNode();
			headerModel.addChildren(headerModelContents);

			// Do the same operations as in the body contents. Create a root, add to it the model elements and then
			// recursively generate the Part list.
			final RootPart partModelRoot = new RootPart(headerModel, getFactory());
			partModelRoot.refreshChildren();
			final ArrayList<IPart> bodyParts = new ArrayList<IPart>();
			// Select for the body contents only the viewable Parts from the Part model. Make it a list.
			bodyParts.addAll(partModelRoot.collaborate2View());

			// Now do the old functionality by copying each of the resulting parts to the Header container.
			for (IPart part : bodyParts) {
				addtoHeader((IAndroidPart) part);
			}
		} catch (RuntimeException rtex) {
			rtex.printStackTrace();
		}
	}
}

// - CLASS IMPLEMENTATION ...................................................................................
final class SplashPartFactory extends PartFactory implements IPartFactory {
	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger = Logger.getLogger("AssetPartFactory");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public SplashPartFactory (final String variantSelected) {
		super(variantSelected);
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * The method should create the matching part for the model received but there is no other place where we
	 * should create the next levels of the hierarchy. So we will create the part transformations here.
	 */
	@Override
	public IPart createPart (final ICollaboration node) {
		logger.info(">< [SplashPartFactory.createPart]> Node class: " + node.getClass().getSimpleName());
		if ( node instanceof AppVersion ) {
			IPart part = new AppVersionPart((AppVersion) node).setFactory(this)
			                                                  .setRenderMode(this.getVariant());
			return part;
		}
		//		if (node instanceof InitializationTask ) {
		//			IPart part = new RegionPart((Region) node).setFactory(this)
		//			                                          .setRenderMode(ENeoComVariants.valueOf(this.getVariant()).name());
		//			return part;
		//		}

		// If no part is trapped then call the parent chain until one is found.
		return super.createPart(node);
	}
}

// - CLASS IMPLEMENTATION ...................................................................................
final class InitializationGenerator extends AbstractGenerator implements IModelGenerator {

	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger = Logger.getLogger("AssetPartFactory");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public InitializationGenerator (final DataSourceLocator locator, final String variant) {
		super(locator,variant);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * This is the real initialization code manager. It will be generating the initialization steps and then the tasks inside them once one by one
	 * are being executed. This code is most a test of how to use Observers while managing the model objects.
	 * @return
	 */
	@Override
	public RootNode collaborate2Model () {
		logger.info(">> [InitializationGenerator.collaborate2Model]");
		// Initialize the Adapter data structures.
		this.setDataModel(new RootNode());

		// Add the steps to the list of works to do.
		_dataModelRoot.addChild(new InitializationTask());

		logger.info("<< [InitializationGenerator.collaborate2Model]> nodes: "+_dataModelRoot.getChildren().size());
		return _dataModelRoot;
	}
}
// - UNUSED CODE ............................................................................................
