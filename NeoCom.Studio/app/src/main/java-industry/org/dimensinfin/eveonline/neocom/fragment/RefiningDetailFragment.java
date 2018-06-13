//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API22.
//  DESCRIPTION: Android Application related to the Eve Online game. The purpose is to download and organize
//               the game data to help capsuleers organize and prioritize activities. The strong points are
//               help at the Industry level tracking and calculating costs and benefits. Also the market
//               information update service will help to identify best prices and locations.
//               Planetary Interaction and Ship fittings are point under development.
//               ESI authorization is a new addition that will give continuity and allow download game data
//               from the new CCP data services.
//               This is the Android application version but shares libraries and code with other application
//               designed for Spring Boot Angular 4 platform.
//               The model management is shown using a generic Model View Controller that allows make the
//               rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.fragment;

import android.os.Bundle;

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.android.mvc.interfaces.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.datasource.DataSourceLocator;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.factory.IndustryPartFactory;
import org.dimensinfin.eveonline.neocom.model.PanelTitle;
import org.dimensinfin.eveonline.neocom.model.RefiningDetailReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class RefiningDetailFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("RefiningDetailFragment");

	// - F I E L D - S E C T I O N ............................................................................
	private int resourceTypeId = 1230;

	// - M E T H O D - S E C T I O N ..........................................................................
	public RefiningDetailFragment setResourceTypeId( int resourceTypeId ) {
		this.resourceTypeId = resourceTypeId;
		return this;
	}

	// --- P A G E R F R A G M E N T   O V E R R I D E S
	@Override
	public IPartFactory createFactory() {
		return new IndustryPartFactory(getVariant());
	}

	@Override
	public String getSubtitle() {
		return "-";
	}

	@Override
	public String getTitle() {
		return "Refining Detail";
	}

	@Override
	protected List<ICollaboration> registerHeaderSource() {
		List<ICollaboration> headerContents = new ArrayList<>();
		headerContents.add(new RefiningDetailReport(resourceTypeId));
		return headerContents;
	}

	@Override
	protected IDataSource registerDataSource() {
		return new EmptyDataSource(new DataSourceLocator().addIdentifier("EMPTY"), getVariant(), getFactory(), getExtras());
	}

	//- CLASS IMPLEMENTATION ...................................................................................
	public static class EmptyDataSource extends MVCDataSource {

		public EmptyDataSource( DataSourceLocator locator, String variant, IPartFactory factory, Bundle extras ) {
			super(locator, variant, factory, extras);
		}

		@Override
		public void collaborate2Model() {
			this.addModelContents(new PanelTitle("EOR"));
		}
	}
}
// - UNUSED CODE ............................................................................................
//[01]
