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

import android.os.Bundle;

import com.tlabs.android.evanova.adapter.ApplicationCloudAndroidAdapter;

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.core.datasource.AbstractGenerator;
import org.dimensinfin.core.datasource.DataSourceLocator;
import org.dimensinfin.core.datasource.ModelGeneratorStore;
import org.dimensinfin.core.interfaces.IModelGenerator;
import org.dimensinfin.core.model.RootNode;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.core.model.Separator.ESeparatorType;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants.EExtras;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.factory.ManagerStore;
import org.dimensinfin.eveonline.neocom.factory.ModelFactory;
import org.dimensinfin.eveonline.neocom.factory.PilotPartFactory;
import org.dimensinfin.eveonline.neocom.model.PilotV1;
import org.dimensinfin.eveonline.neocom.storage.DataManagementModelStore;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

//- CLASS IMPLEMENTATION ...................................................................................

/**
 * This fragment will manage the Managers that apply to the selected and current Character. The single usage
 * of the fragment is inside the Activity so to reduce file count I have switched to store all single usage
 * code together.
 *
 * @author Adam Antinoo
 */
public class PilotDetailsFragment extends AbstractPagerFragment {
	//- CLASS IMPLEMENTATION ...................................................................................
	public static class PilotManagersGenerator extends AbstractGenerator implements IModelGenerator {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		private final long capsulerrid;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public PilotManagersGenerator (final DataSourceLocator locator, final String variant, final Bundle extras) {
			super(locator, variant);
			capsulerrid = extras.getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
		}

		// - M E T H O D - S E C T I O N ..........................................................................

		/**
		 * The collaboration for a Pilot or Corporation on this Activity is the list of Managers that can be applied
		 * to the Character. So we can move the decision to the place where it better suits that is the Character
		 * that is already selected. This is performed internally in a recursive way by calling the model
		 * collaboration depending on the variant.
		 */
		public RootNode collaborate2Model () {
			AbstractGenerator.logger.info(">> [PilotManagersGenerator.collaborate2Model]");
			// Initialize the Adapter data structures.
			this.setDataModel(new RootNode());

			// Add to the data model list the Directors that can be used on this character.
			_dataModelRoot.addChild(new Separator().setType(ESeparatorType.LINE_WHITE));
			_dataModelRoot.addChild(ManagerStore.getAssetsManager(capsulerrid));
			_dataModelRoot.addChild(ManagerStore.getPlanetaryManager(DataManagementModelStore.getCredential4Id(capsulerrid)));
			// Accelerate the availability of data.
			ApplicationCloudAndroidAdapter.submit2downloadExecutor(()->{
				ManagerStore.getPlanetaryManager(DataManagementModelStore.getCredential4Id(capsulerrid)).accessAllColonies();
			});
			//			_dataModelRoot.addChild(character.getBlueprintsManager());

			AbstractGenerator.logger.info("<< [PilotManagersGenerator.collaborate2Model]");
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
	protected void registerDataSource () {
		AbstractPagerFragment.logger.info(">> [PilotDetailsFragment.registerDataSource]");
		// Get access to the parameters and reconstruct the data chain if required.
		long characterid = this.getExtras().getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
		NeoComModelStore.activateCredential(characterid);
		// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
		DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant())
																											 .addIdentifier(characterid);
		IModelGenerator generator = ModelGeneratorStore
				.registerGenerator(new PilotManagersGenerator(locator, this.getVariant(), getExtras()));
		setGenerator(generator);
		AbstractPagerFragment.logger.info("<< [PilotDetailsFragment.registerDataSource]");
	}

	/**
	 * This methods is used to define the parts that go inside the header section of the page. On this activity
	 * there is no content on the head section.
	 */
	@Override
	protected void setHeaderContents () {
		AbstractPagerFragment.logger.info(">> [PilotDetailsFragment.setHeaderContents]");
		// Set the part for the header that contains a detailed list of most Pilot characteristics.
		// Get the Pilot instance to use as the origin model.
		final long identifier = this.getExtras().getLong(EExtras.EXTRA_CAPSULEERID.name());
		final PilotV1 pilot = ModelFactory.getPilotV1(identifier);
		this.addtoHeader((AbstractAndroidPart) this.getFactory()
																							 .createPart(pilot)
																							 .setRenderMode(ENeoComVariants.PILOT_HEADER.name()));
		AbstractPagerFragment.logger.info("<< [PilotDetailsFragment.setHeaderContents]");
	}
}
// - UNUSED CODE ............................................................................................
