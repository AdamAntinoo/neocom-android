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
import org.dimensinfin.android.mvc.core.PartFactory;
import org.dimensinfin.android.mvc.interfaces.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.datasource.DataSourceLocator;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.NeoComApplication;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.core.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.datasource.ACredentialEnabledDS;
import org.dimensinfin.eveonline.neocom.interfaces.ICredentialActivity;
import org.dimensinfin.eveonline.neocom.model.Skill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class SkillsFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("SkillsFragment");

	// - F I E L D - S E C T I O N ............................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public IPartFactory createFactory() {
		return new SkillsPartFactory(this.getVariant());
	}

	@Override
	public String getSubtitle() {
		String subtitle = "";
		if (this.getVariant() == NeoComApplication.EVariants.ASSETS_BYLOCATION.name()) {
			subtitle = getAppContext().getResources().getString(R.string.activity_subtitle_AssetsByLocation);
		}
		if (this.getVariant() == NeoComApplication.EVariants.ASSETS_BYCATEGORY.name()) {
			subtitle = getAppContext().getResources().getString(R.string.activity_subtitle_AssetsByCategory);
		}
		return subtitle;
	}

	@Override
	public String getTitle() {
		return "";
	}

	@Override
	protected List<ICollaboration> registerHeaderSource() {
		return new ArrayList<>();
	}

	@Override
	protected IDataSource registerDataSource() {
		logger.info(">> [SkillsFragment.registerDataSource]");
		// Get access to the parameters and reconstruct the data chain if required.
		if (getVariant() == NeoComApplication.EVariants.SKILLS_QUEUE.name()) {
			int characterid = this.getExtras().getInt(NeoComApplication.EExtras.CAPSULEER_IDENTIFIER.name());
			// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
			DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant())
					.addIdentifier(characterid)
					.addIdentifier("ASSETS-MANAGER");
			return new SkillsOnQueueDataSource(locator, getVariant(), getFactory(), getExtras())
					.setCredential(((ICredentialActivity) this.getAppContext()).getCredential())
					.setCacheable(true);
		}
		logger.info("<< [SkillsFragment.registerDataSource]");
		return new RefiningDetailFragment.EmptyDataSource(new DataSourceLocator().addIdentifier("EMPTY")
				, getVariant(), getFactory(), getExtras());
	}

	public static class SkillsPartFactory extends PartFactory {
		public SkillsPartFactory( String selectedVariant ) {
			super(selectedVariant);
		}
	}

	public static class SkillsOnQueueDataSource extends ACredentialEnabledDS {
		public SkillsOnQueueDataSource( DataSourceLocator locator, String variant, IPartFactory factory, Bundle extras ) {
			super(locator, variant, factory, extras);
		}

		@Override
		public void collaborate2Model() {
			logger.info(">> [SkillsOnQueueDataSource.collaborate2Model]");
			// Check if we should use the cached version.
			if (!isCached()) {
				// Check that required parameters are not null.
				if (null == this.credential)
					throw new NeoComRuntimeException("[SkillsOnQueueDataSource.collaborate2Model]> Required Credential not available.");

				// Get the skill queue and insert it into the model container.
				List<Skill> skillQueue = AndroidGlobalDataManager.downloadSkillQueue4Credential(credential);
				for (Skill skill : skillQueue) {
					this.addModelContents(skill);
				}
			}
			logger.info("<< [SkillsOnQueueDataSource.collaborate2Model]");
		}
	}
}
// - UNUSED CODE ............................................................................................
//[01]
