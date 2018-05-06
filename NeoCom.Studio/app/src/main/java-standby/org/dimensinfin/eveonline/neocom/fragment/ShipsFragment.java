//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2016 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.fragment;

import java.util.logging.Logger;

import org.dimensinfin.activity.AbstractPagerFragment;
import org.dimensinfin.android.datasource.DataSourceLocator;
import org.dimensinfin.android.mvc.datasource.DataSourceManager;
import org.dimensinfin.android.mvc.datasource.SpecialDataSource;
import org.dimensinfin.eveonline.neocom.activity.ShipDirectorActivity.EShipsVariants;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.datasource.ShipsDataSource;
import org.dimensinfin.eveonline.neocom.factory.ShipPartFactory;
import org.dimensinfin.eveonline.neocom.model.NeoComCharacter;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

// - CLASS IMPLEMENTATION ...................................................................................
public class ShipsFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = Logger.getLogger("ShipsFragment");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	@Override
	public void createFactory() {
		this.setFactory(new ShipPartFactory(this.getVariant()));
	}

	@Override
	public String getSubtitle() {
		String st = "";
		if (this.getVariant() == EShipsVariants.SHIPS_BYLOCATION.name()) {
			st = "Ships - by Location";
		}
		if (this.getVariant() == EShipsVariants.SHIPS_BYCLASS.name()) {
			st = "Ships - by Class";
		}
		return st;
	}

	@Override
	public String getTitle() {
		return this.getPilotName();
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public void onStart() {
		ShipsFragment.logger.info(">> [ShipsFragment.onStart]");
		try {
			//		this.setIdentifier(_variant.hashCode());
			this.registerDataSource();
		} catch (final RuntimeException rtex) {
			ShipsFragment.logger.severe("RTEX [ShipsFragment.onStart]> " + rtex.getMessage());
			rtex.printStackTrace();
			// Instead blocking the application drop a toast and move to the First Activity.
			this.goFirstActivity(rtex);
		}
		super.onStart();
		ShipsFragment.logger.info("<< [ShipsFragment.onStart]");
	}

	/**
	 * This is a special RegiterDataSource because we can use the fragment in more than one page but the
	 * DataSource can be valid for both variants. So the variant information is not valid because we have a
	 * single DS and multiple usages.
	 */
	@Override
	protected void registerDataSource() {
		ShipsFragment.logger.info(">> [ShipsFragment.registerDataSource]");
		// This is an special case. A single DataSource serves both variants
		DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getPilotName())
				.addIdentifier(this.getVariant());
		SpecialDataSource ds = new ShipsDataSource(locator, this.getFactory());
		ds.setVariant(this.getVariant());
		ds.addParameter(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name(), this.getPilot().getCharacterID());
		this.setDataSource(DataSourceManager.registerDataSource(ds));
		ShipsFragment.logger.info("<< [ShipsFragment.registerDataSource]");
	}

	@Override
	protected void setHeaderContents() {
		// TODO Auto-generated method stub

	}

	private NeoComCharacter getPilot() {
		return NeoComModelStore.getSingleton().getCurrentPilot();
	}

	private String getPilotName() {
		return NeoComModelStore.getSingleton().getCurrentPilot().getName();
	}
}

// - UNUSED CODE ............................................................................................
