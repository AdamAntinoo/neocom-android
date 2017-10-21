//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2016 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.fragment;

import java.util.Vector;

import org.dimensinfin.activity.AbstractPagerFragment;
import org.dimensinfin.android.datasource.AbstractGenerator;
import org.dimensinfin.android.datasource.DataSourceLocator;
import org.dimensinfin.android.datasource.ModelGeneratorStore;
import org.dimensinfin.android.interfaces.IModelGenerator;
import org.dimensinfin.android.mvc.datasource.DataSourceManager;
import org.dimensinfin.android.mvc.datasource.SpecialDataSource;
import org.dimensinfin.core.model.RootNode;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.datasource.NeoComDataSource;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.factory.AssetPartFactory;
import org.dimensinfin.eveonline.neocom.industry.Resource;
import org.dimensinfin.eveonline.neocom.manager.PlanetaryManager;
import org.dimensinfin.eveonline.neocom.model.NeoComCharacter;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryProcessor;
import org.dimensinfin.eveonline.neocom.planetary.PlanetaryScenery;
import org.dimensinfin.eveonline.neocom.planetary.ProcessingAction;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

//- CLASS IMPLEMENTATION ...................................................................................
public class PlanetaryOptimizationFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	//- M E T H O D - S E C T I O N ..........................................................................
	@Override
	public void createFactory() {
		PlanetaryOptimizationFragment.logger.info(">> [PlanetaryOptimizationFragment.createFactory]");
		this.setFactory(new AssetPartFactory(this.getVariant()));
		PlanetaryOptimizationFragment.logger.info("<< [PlanetaryOptimizationFragment.createFactory]");
	}

	@Override
	public String getSubtitle() {
		return NeoComAppConnector.getSingleton()
				.getResourceString(R.string.activity_subtitle_PlanetaryOptimizationActivity);
	}

	@Override
	public String getTitle() {
		return NeoComModelStore.getSingleton().getActiveCharacter().getName();
	}

	@Override
	public void registerDataSource() {
		PlanetaryOptimizationFragment.logger.info(">> [PlanetaryOptimizationFragment.registerDataSource]");
		// Get access to the parameters and reconstruct the data chain if required.
		String loginid = this.getExtras().getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
		long characterid = this.getExtras().getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
		long locationid = this.getExtras().getLong(AppWideConstants.EExtras.EXTRA_LOCATIONID.name());

		NeoComAppConnector.getSingleton().getModelStore().activateLoginIdentifier(loginid);
		NeoComAppConnector.getSingleton().getModelStore().activatePilot(characterid);
		// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
		DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant()).addIdentifier(loginid)
				.addIdentifier(characterid).addIdentifier(locationid);
		// This part of the code may depend on the variant so surround it with the detector.
		IModelGenerator generator = null;
		if (this.getVariant() == ENeoComVariants.PLANETARY_OPTIMIZATION.name()) {
			generator = ModelGeneratorStore.registerGenerator(
					new PlanetaryOptimization4LocationGenerator(locator, this.getVariant(), loginid, characterid, locationid));
		}
		// Register the datasource. If this same datasource is already at the manager we get it instead creating a new one.
		SpecialDataSource ds = new NeoComDataSource(locator, this.getFactory(), generator);
		ds.setVariant(this.getVariant());
		this.setDataSource(DataSourceManager.registerDataSource(ds));
		PlanetaryOptimizationFragment.logger.info("<< [PlanetaryOptimizationFragment.registerDataSource]");
	}

	@Override
	public void setHeaderContents() {
	}
}

//- CLASS IMPLEMENTATION ...................................................................................
final class PlanetaryOptimization4LocationGenerator extends AbstractGenerator implements IModelGenerator {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	private String	_login				= null;
	private long		_characterid	= -1;
	private long		_locationid		= -2;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public PlanetaryOptimization4LocationGenerator(final DataSourceLocator locator, final String variant,
			final String login, final long characterid, final long locationid) {
		super(locator, variant);
		_login = login;
		_characterid = characterid;
		_locationid = locationid;
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	/**
	 * This adapter get only the Planetary Resources that are on a location, independently of the deep level of
	 * its storage. This is a single list of Resources that is then transformed into another list of
	 * <code>ProcessingAction</code>.<br>
	 */
	public RootNode collaborate2Model() {
		AbstractGenerator.logger.info(">> [PlanetaryOptimization4LocationGenerator.collaborate2Model]");
		// Get the Assets Manager active at the character.
		NeoComCharacter pilot = NeoComAppConnector.getSingleton().getModelStore().getActiveCharacter();
		PlanetaryManager planetaryMan = pilot.getPlanetaryManager();
		// Check it is initialized.
		if (!planetaryMan.isInitialized()) {
			planetaryMan.initialize();
		}
		try {
			// Get the location and then process all its contents to extract the Planetary Resources.
			Vector<Resource> resources = planetaryMan.getLocationContents(Long.valueOf(_locationid).toString());
			// The Planetary Advisor requires a list of Planetary Resources to be stocked to start the profit calculations.
			PlanetaryScenery scenery = new PlanetaryScenery();
			scenery.stock(resources);
			// Create the initial processing point and start the optimization recursively.
			PlanetaryProcessor proc = new PlanetaryProcessor(scenery);
			// Start running the best profit search.
			Vector<ProcessingAction> bestScenario = proc.startProfitSearch(null);

			// Initialize the Adapter data structures.
			this.setDataModel(new RootNode());
			for (ProcessingAction action : bestScenario) {
				_dataModelRoot.addChild(action);
				AbstractGenerator.logger.info("-- [PlanetaryOptimization4LocationGenerator.collaborate2Model]> Adding '"
						+ action.toString() + "' to the _dataModelRoot");
			}
		} catch (RuntimeException rtex) {
			rtex.printStackTrace();
		}
		AbstractGenerator.logger.info("<< [PlanetaryOptimization4LocationGenerator.collaborate2Model]");
		return _dataModelRoot;
	}

}
//- UNUSED CODE ............................................................................................
