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

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.android.mvc.core.PartFactory;
import org.dimensinfin.android.mvc.datasource.DataSourceLocator;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.exception.NeoComRegisteredException;
import org.dimensinfin.eveonline.neocom.interfaces.ICredentialActivity;
import org.dimensinfin.eveonline.neocom.model.PilotV2;
import org.dimensinfin.eveonline.neocom.part.NeoComMenuBarPart;
import org.dimensinfin.eveonline.neocom.part.PilotDashbordPart;
import org.dimensinfin.eveonline.neocom.ui.NeoComMenuBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class PilotDashboardFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("PilotDashboardFragment");

	// - F I E L D - S E C T I O N ............................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	// --- P A G E R F R A G M E N T   O V E R R I D E S
	@Override
	public IPartFactory createFactory() {
		return new DashboardFactory(getVariant());
	}

	@Override
	public String getSubtitle() {
		return "";
	}

	@Override
	public String getTitle() {
		return "";
	}

	@Override
	protected List<ICollaboration> registerHeaderSource() {
		if (this.getAppContext() instanceof ICredentialActivity) {
			List<ICollaboration> headerContents = new ArrayList<>();
			try {
				headerContents.add(AndroidGlobalDataManager.fastRequestPilotV2(((ICredentialActivity) this.getAppContext()).getCredential()));
				headerContents.add(new NeoComMenuBar(((ICredentialActivity) this.getAppContext()).getCredential()));
			} catch (NeoComRegisteredException neoe) {
				neoe.printStackTrace();
			}
			return headerContents;
		} else return new ArrayList<>();
	}

	@Override
	protected org.dimensinfin.android.mvc.interfaces.IPartsDataSource registerDataSource() {
		return new RefiningDetailFragment.EmptyDataSource(new DataSourceLocator().addIdentifier("PilotDashboardFragment")
				, getVariant(), getFactory(), getExtras());
	}

	public static class DashboardFactory extends PartFactory {
		// - F I E L D - S E C T I O N ............................................................................

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public DashboardFactory( String selectedVariant ) {
			super(selectedVariant);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public IPart createPart( final ICollaboration node ) {
			logger.info("-- [DashboardFactory.createPart]> Node class: {}", node.getClass().getSimpleName());
			if (node instanceof PilotV2) {
				IPart part = new PilotDashbordPart((PilotV2) node).setFactory(this)
						.setRenderMode(this.getVariant());
				return part;
			}
			if (node instanceof NeoComMenuBar) {
				IPart part = new NeoComMenuBarPart((NeoComMenuBar) node).setFactory(this)
						.setRenderMode(this.getVariant());
				return part;
			}
			return super.createPart(node);
		}
	}
}
// - UNUSED CODE ............................................................................................
//[01]
