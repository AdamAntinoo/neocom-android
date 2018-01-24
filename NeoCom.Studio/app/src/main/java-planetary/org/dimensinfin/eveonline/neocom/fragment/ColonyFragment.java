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

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.core.datasource.AbstractGenerator;
import org.dimensinfin.core.datasource.DataSourceLocator;
import org.dimensinfin.core.datasource.ModelGeneratorStore;
import org.dimensinfin.core.interfaces.IModelGenerator;
import org.dimensinfin.core.model.RootNode;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.connector.NeoComCacheManager;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.database.entity.Colony;
import org.dimensinfin.eveonline.neocom.datamngmt.manager.GlobalAndroidDataManager;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.factory.ModelFactory;
import org.dimensinfin.eveonline.neocom.factory.PlanetaryPartFactory;
import org.dimensinfin.eveonline.neocom.manager.PlanetaryManager;
import org.dimensinfin.eveonline.neocom.model.PilotV1;
import org.dimensinfin.eveonline.neocom.storage.DataManagementModelStore;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

import java.util.List;

/**
 * Fragment that composes the list of Planetary Colonies for the current character. The fragment defines also
 * a new header where the PlanetaryManager can show some statistics and maybe calculations of the Planetary
 * Interaction status for tthis character.
 * <p>
 * This is the first and only Fragment that implements the new ActionBar under development and UI testing.
 */
//- CLASS IMPLEMENTATION ...................................................................................
public class ColonyFragment extends AbstractPagerFragment {
	//- CLASS IMPLEMENTATION ...................................................................................
	public static class ColonyStructureGenerator extends AbstractGenerator implements IModelGenerator {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		//		private final Bundle _extras;
		private int characterid = -1;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public ColonyStructureGenerator (final DataSourceLocator locator, final String variant, final Bundle extras) {
			super(locator, variant);
			characterid = extras.getInt(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
			setCaching(false);
		}

		// - M E T H O D - S E C T I O N ..........................................................................

		/**
		 * If the debug flag is true (usually has that value or POCs we should get the model from the mockup model
		 * generator.
		 */
		public RootNode collaborate2Model () {
			AbstractGenerator.logger.info(">> [ColonyStructureGenerator.collaborate2Model]");
			// Check if the model is already generated.
			if ( cacheIsInvalid() ) {
				// Get the list of colonies for this character identifier form the Global.
				//				final List<Colony> colonies = GlobalAndroidDataManager.accessColonies4Credential(characterid);
				// Get access to the Planetary Manager and connect the Global to get the colony information.
				final PlanetaryManager manager = GlobalAndroidDataManager.getPlanetaryManager(DataManagementModelStore.getCredential4Id
						(characterid));
				final List<Colony> colonies = manager.accessAllColonies();
				for (Colony colony : colonies) {
					_dataModelRoot.addChild(colony);
				}
			}
			AbstractGenerator.logger.info("<< [ColonyStructureGenerator.collaborate2Model]");
			return _dataModelRoot;
		}
		//		}
	}
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	private ImageView pilotIcon;
	private ImageView activityIcon;
	private TextView toolBarTitleLeft;
	private TextView toolBarTitleRight;
	private TextView toolBarSubTitle;

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public void createFactory () {
		this.setFactory(new PlanetaryPartFactory(this.getVariant()));
	}

	@Override
	public String getSubtitle () {
		// TODO The getSubtitle is called too early and the Fragment is still waiting to be associated with the Activity
		final Activity act = getActivity();
		if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M ) {
			final Context con = getContext();
		}
		return NeoComAppConnector.getSingleton().getResourceString(R.string.activity_subtitle_ColonyActivity);
	}

	@Override
	public String getTitle () {
		return NeoComModelStore.getActiveCredential().getName();
	}

	public View onCreateView (final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		// Create the Action Bar custom and replace it on the Bar.
		//		updateInitialTitle();
		activateActionBar(inflater);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	//	protected void updateInitialTitle () {
	//		final ActionBar actionBar = getActivity().getActionBar()
	//		if ( null != actionBar ) {
	//			Fragment firstFragment = this.getPageAdapter().getInitialPage();
	//			// REFACTOR This IF can be removed once this code works.
	//			if ( firstFragment instanceof AbstractPagerFragment ) {
	//				actionBar.setTitle(((AbstractPagerFragment) firstFragment).getTitle());
	//				actionBar.setSubtitle(((AbstractPagerFragment) firstFragment).getSubtitle());
	//			}
	//		}
	//	}

	/**
	 * Create a new ActionBar block to be used on this fragment only.
	 */
	private void activateActionBar (final LayoutInflater inflater) {
		try {
			View newbar = inflater.inflate(R.layout.actionbar_planetary, null);
			final ActionBar actionBar = getActivity().getActionBar();
			if ( (null != newbar) && (null != actionBar) ) {
				// Get the fields of the Action bar and setup their values.
				pilotIcon = (ImageView) newbar.findViewById(R.id.pilotIcon);
				activityIcon = (ImageView) newbar.findViewById(R.id.activityIcon);
				toolBarTitleLeft = (TextView) newbar.findViewById(R.id.toolBarTitleLeft);
				toolBarTitleRight = (TextView) newbar.findViewById(R.id.toolBarTitleRight);
				toolBarSubTitle = (TextView) newbar.findViewById(R.id.toolBarSubTitle);
				if ( null != pilotIcon ) {
					final int characterid = this.getExtras().getInt(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
					final PilotV1 pilot = ModelFactory.getPilotV1(characterid);
					String link = pilot.getURLForAvatar();
					pilotIcon.setImageDrawable(NeoComCacheManager.getCacheDrawable(link, pilotIcon));
				}
				if ( null != activityIcon ) {
					activityIcon.setImageResource(R.drawable.planets);
				}
				if ( null != toolBarTitleLeft ) {
					toolBarTitleLeft.setText(NeoComModelStore.getSingleton().getActiveCredential().getName());
				}
				if ( null != toolBarTitleRight ) {
					toolBarTitleRight.setVisibility(View.GONE);
				}
				if ( null != toolBarSubTitle ) {
					toolBarSubTitle.setText(getActivity().getResources().getString(R.string.activity_subtitle_ColonyActivity));
				}
				actionBar.setCustomView(newbar);
				actionBar.setDisplayShowCustomEnabled(true);
				actionBar.setDisplayShowTitleEnabled(false);
			}
		} catch (RuntimeException rtex) {
			try {
				// Leave the action bar as the standard.
				getActivity().getActionBar().setDisplayShowCustomEnabled(false);
				getActivity().getActionBar().setDisplayShowTitleEnabled(true);
			} catch (RuntimeException rtexreleased) {
			}
		}
	}

	/**
	 * Generate a simple Generator identifier to be registered and then create our custom mockup generator..
	 */
	@Override
	protected void registerDataSource () {
		logger.info(">> [ColonyFragment.registerDataSource]");
		// Get access to the parameters and reconstruct the data chain if required.
		int characterid = this.getExtras().getInt(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
		//		NeoComModelStore.activateCredential(characterid);
		// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
		DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant())
		                                                   .addIdentifier(characterid);
		// This part of the code may depend on the variant so surround it with the detector.
		IModelGenerator generator = null;
		if ( this.getVariant() == ENeoComVariants.PLANETARY_COLONIES.name() ) {
			generator = ModelGeneratorStore.registerGenerator(
					new ColonyStructureGenerator(locator, this.getVariant(), getExtras()));
		}
		if ( null != generator )
			setGenerator(generator);
		logger.info("<< [ColonyFragment.registerDataSource]");
	}

	/**
	 * This methods is used to define the parts that go inside the header section of the page. On this activity
	 * there is no content on the head section.
	 */
	@Override
	protected void setHeaderContents () {
	}
}
// - UNUSED CODE ............................................................................................
//[01]
