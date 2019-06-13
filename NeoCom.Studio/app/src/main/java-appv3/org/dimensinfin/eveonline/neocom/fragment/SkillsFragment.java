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
import android.os.Handler;
import android.os.Looper;

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.core.PartFactory;
import org.dimensinfin.android.mvc.core.RootPart;
import org.dimensinfin.android.mvc.datasource.DataSourceLocator;
import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.android.mvc.interfaces.IRootPart;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.model.RootNode;
import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.app.adapters.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.datasource.ACredentialEnabledDS;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdSkillsOk;
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdSkillsOkSkills;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.interfaces.ICredentialActivity;
import org.dimensinfin.eveonline.neocom.model.SkillGroupContainer;
import org.dimensinfin.eveonline.neocom.model.SkillInTraining;
import org.dimensinfin.eveonline.neocom.model.SkillTrained;
import org.dimensinfin.eveonline.neocom.part.SkillGroupContainerPart;
import org.dimensinfin.eveonline.neocom.part.SkillInTrainingPart;
import org.dimensinfin.eveonline.neocom.part.SkillTrainedPart;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class SkillsFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	// --- O V E R I D E D   A B S T R A C T P A G E R F R A G M E N T
	@Override
	public IPartFactory createFactory() {
		return new SkillsPartFactory(this.getVariant());
	}

	@Override
	public String getSubtitle() {
		String subtitle = "";
		if ( this.getVariant() == NeoComApplication.EVariants.SKILLS_QUEUE.name() ) {
			subtitle = getAppContext().getResources().getString(R.string.activity_subtitle_SkillsQueue);
		}
		if ( this.getVariant() == NeoComApplication.EVariants.SKILLS_TRAINED.name() ) {
			subtitle = getAppContext().getResources().getString(R.string.activity_subtitle_SkillsTrained);
		}
		return subtitle;
	}

	@Override
	public String getTitle() {
		return ((ICredentialActivity) this.getAppContext()).getCredential().getAccountName();
	}

	@Override
	protected List<ICollaboration> registerHeaderSource() {
		return new ArrayList<>();
	}

	@Override
	protected org.dimensinfin.android.mvc.interfaces.IPartsDataSource registerDataSource() {
		logger.info(">> [SkillsFragment.registerDataSource]");
		org.dimensinfin.android.mvc.interfaces.IPartsDataSource ds = null;
		// Get access to the parameters and reconstruct the data chain if required.
		if ( getVariant() == NeoComApplication.EVariants.SKILLS_QUEUE.name() ) {
			int characterid = this.getExtras().getInt(EExtras.CAPSULEER_IDENTIFIER.name());
			// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
			DataSourceLocator locator = new DataSourceLocator()
					.addIdentifier(this.getVariant())
					.addIdentifier(characterid)
					.addIdentifier("SKILLS-MANAGER");
			ds = new SkillsOnQueueDataSource(locator, getVariant(), getFactory(), getExtras())
					.setCredential(((ICredentialActivity) this.getAppContext()).getCredential())
					.setRefreshTime(1)
					.setCacheable(true);
		}
		if ( getVariant() == NeoComApplication.EVariants.SKILLS_TRAINED.name() ) {
			int characterid = this.getExtras().getInt(EExtras.CAPSULEER_IDENTIFIER.name());
			// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
			DataSourceLocator locator = new DataSourceLocator()
					.addIdentifier(this.getVariant())
					.addIdentifier(characterid)
					.addIdentifier("SKILLS-MANAGER");
			ds = new SkillsTrainedDataSource(locator, getVariant(), getFactory(), getExtras())
					.setCredential(((ICredentialActivity) this.getAppContext()).getCredential())
					.setRefreshTime(1)
					.setCacheable(true);
		}
		logger.info("<< [SkillsFragment.registerDataSource]");
		return ds;
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	public static class SkillsPartFactory extends PartFactory {
		// - F I E L D - S E C T I O N ............................................................................

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public SkillsPartFactory( final String variantSelected ) {
			super(variantSelected);
		}
		// - M E T H O D - S E C T I O N ..........................................................................

		/**
		 * The method should create the matching part for the model received but there is no other place where we
		 * should create the next levels of the hierarchy. So we will create the part transformations here.
		 */
		@Override
		public IPart createPart( final ICollaboration node ) {
			logger.info("-- [SkillsPartFactory.createPart]> Node class: ", node.getClass().getSimpleName());
			if ( node instanceof SkillInTraining ) {
				IPart part = new SkillInTrainingPart((SkillInTraining) node).setFactory(this)
						.setRenderMode(this.getVariant());
				return part;
			}
			if ( node instanceof SkillTrained ) {
				IPart part = new SkillTrainedPart((SkillTrained) node).setFactory(this)
						.setRenderMode(this.getVariant());
				return part;
			}
			if ( node instanceof SkillGroupContainer ) {
				IPart part = new SkillGroupContainerPart((SkillGroupContainer) node).setFactory(this)
						.setRenderMode(this.getVariant());
				return part;
			}
			// If no part is trapped then call the parent chain until one is found.
			return super.createPart(node);
		}
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	public static class SkillsOnQueueDataSource extends ACredentialEnabledDS {
		protected int refreshTime = 1;

		public SkillsOnQueueDataSource( DataSourceLocator locator, String variant, IPartFactory factory, Bundle extras ) {
			super(locator, variant, factory, extras);
		}

		public SkillsOnQueueDataSource setRefreshTime( final int time ) {
			this.refreshTime = time;
			return this;
		}

		@Override
		public void collaborate2Model() {
			logger.info(">> [SkillsOnQueueDataSource.collaborate2Model]");
			// Check if we should use the cached version.
			if ( !isCached() ) {
				// Check that required parameters are not null.
				if ( null == this.credential )
					throw new NeoComRuntimeException("[SkillsOnQueueDataSource.collaborate2Model]> Required Credential not available.");

				// Get the skill queue and insert it into the model container.
				List<SkillInTraining> skillQueue = AndroidGlobalDataManager.downloadSkillQueue4Credential(credential);
				for (SkillInTraining skill : skillQueue) {
					this.addModelContents(skill);
				}
			}
			scheduleRefresh(this.refreshTime);
			logger.info("<< [SkillsOnQueueDataSource.collaborate2Model]");
		}

		protected void scheduleRefresh( final int time ) {
			// Schedule an event in 5 minutes to update this datasource and then the view.
			final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
			Runnable delayedTask = new Runnable() {
				@Override
				public void run() {
					logger.info("-- [MiningLedgerDataSource.collaborate2Model]> Firing data source refresh process.");
					propertyChange(new PropertyChangeEvent(this, SystemWideConstants.events.EVENTSTRUCTURE_REFRESHDATA.name()
							, null, null));
				}
			};
			mainThreadHandler.postDelayed(delayedTask, TimeUnit.MINUTES.toMillis(time));
		}
	}

	// - CLASS IMPLEMENTATION ...................................................................................
	public static class SkillsTrainedDataSource extends ACredentialEnabledDS {
		protected int refreshTime = 1;

		public SkillsTrainedDataSource( DataSourceLocator locator, String variant, IPartFactory factory, Bundle extras ) {
			super(locator, variant, factory, extras);
		}

		public SkillsTrainedDataSource setRefreshTime( final int time ) {
			this.refreshTime = time;
			return this;
		}

		@Override
		public void collaborate2Model() {
			logger.info(">> [SkillsTrainedDataSource.collaborate2Model]");
			// Check if we should use the cached version.
			if ( !isCached() ) {
				// Check that required parameters are not null.
				if ( null == this.credential )
					throw new NeoComRuntimeException("[SkillsTrainedDataSource.collaborate2Model]> Required Credential not available.");

				final HashMap<Integer, SkillGroupContainer> groups = new HashMap<>();
				// Get the skills trained and group them on skill groups before rendering.
				final GetCharactersCharacterIdSkillsOk pilotSkillsData = AndroidGlobalDataManager.downloadSkillsTrained4Credential(credential);
				for (GetCharactersCharacterIdSkillsOkSkills skillOk : pilotSkillsData.getSkills()) {
					final SkillTrained newskill = AndroidGlobalDataManager.modelMapper.map(skillOk, SkillTrained.class);
					int groupId = newskill.getGroupId();
					SkillGroupContainer hit = groups.get(groupId);
					if ( null == hit ) {
						// New group.
						hit = new SkillGroupContainer(newskill.getGroupName());
						groups.put(groupId, hit);
						this.addModelContents(hit);
					}
					hit.addSkill(newskill);
				}
			}
			scheduleRefresh(this.refreshTime);
			logger.info("<< [SkillsTrainedDataSource.collaborate2Model]");
		}

		@Override
		public IRootPart createRootPart() {
			return new NameOrderedRootPart(this.getRootModel(), getFactory()).setAscending(true);
		}

		protected void scheduleRefresh( final int time ) {
			// Schedule an event in 5 minutes to update this datasource and then the view.
			final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
			Runnable delayedTask = new Runnable() {
				@Override
				public void run() {
					logger.info("-- [MiningLedgerDataSource.collaborate2Model]> Firing data source refresh process.");
					propertyChange(new PropertyChangeEvent(this, SystemWideConstants.events.EVENTSTRUCTURE_REFRESHDATA.name()
							, null, null));
				}
			};
			mainThreadHandler.postDelayed(delayedTask, TimeUnit.MINUTES.toMillis(time));
		}
	}

	public static class NameOrderedRootPart extends RootPart {
		private Comparator<IPart> nodeNameComparator = null;

		private boolean ascending = true;

		public NameOrderedRootPart( final RootNode node, final IPartFactory factory ) {
			super(node, factory);
			// Set the default comparator to ascending.
			nodeNameComparator = ( IPart part1, IPart part2 ) -> {
				String partName1 = ((INamedPart) part1).getDelegatedName().toUpperCase();
				String partName2 = ((INamedPart) part2).getDelegatedName().toUpperCase();

				//ascending order
				return partName1.compareTo(partName2);
			};
		}

		public boolean isAscending() {
			return ascending;
		}

		public NameOrderedRootPart setAscending( final boolean ascending ) {
			this.ascending = ascending;
			if ( this.ascending ) {
				nodeNameComparator = ( IPart part1, IPart part2 ) -> {
					String partName1 = ((INamedPart) part1).getDelegatedName().toUpperCase();
					String partName2 = ((INamedPart) part2).getDelegatedName().toUpperCase();

					//ascending order
					return partName1.compareTo(partName2);
				};
			} else {
				nodeNameComparator = ( IPart part1, IPart part2 ) -> {
					String partName1 = ((INamedPart) part1).getDelegatedName().toUpperCase();
					String partName2 = ((INamedPart) part2).getDelegatedName().toUpperCase();

					//descending order
					return partName1.compareTo(partName2);
				};
			}
			return this;
		}

		@Override
		public List<IPart> runPolicies( List<IPart> targets ) {
			// Sort contents alphabetically by the asset name.
			Collections.sort(targets, nodeNameComparator);
			return super.runPolicies(targets);
		}
	}
}
// - UNUSED CODE ............................................................................................
//[01]
