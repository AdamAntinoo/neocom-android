//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API22.
//  DESCRIPTION: Android Application related to the Eve Online game. The purpose is to download and organize
//                 the game data to help capsuleers organize and prioritize activities. The strong points are
//                 help at the Industry level tracking and calculating costs and benefits. Also the market
//                 information update service will help to identify best prices and locations.
//               Planetary Interaction and Ship fittings are point under development.
//               ESI authorization is a new addition that will give continuity and allow download game data
//                 from the new CCP data services.
//               This is the Android application version but shares libraries and code with other application
//                 designed for Spring Boot Angular 4 platform.
//               The model management is shown using a generic Model View Controller that allows make the
//                 rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.fragment;


import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beimin.eveapi.parser.ApiAuthorization;

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.core.datasource.AbstractGenerator;
import org.dimensinfin.core.datasource.DataSourceLocator;
import org.dimensinfin.core.datasource.ModelGeneratorStore;
import org.dimensinfin.core.interfaces.IModelGenerator;
import org.dimensinfin.core.model.RootNode;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.connector.NeoComCacheManager;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.factory.PlanetaryPartFactory;
import org.dimensinfin.eveonline.neocom.model.NeoComCharacter;
import org.dimensinfin.eveonline.neocom.model.Pilot;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;

//- CLASS IMPLEMENTATION ...................................................................................
public class ColonyFragment extends AbstractPagerFragment {
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
		return getActivity().getResources().getString(R.string.activity_subtitle_ColonyActivity);
	}

	@Override
	public String getTitle () {
		return NeoComModelStore.getSingleton().getActiveCharacter().getName();
	}

	public View onCreateView (final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		// Create the Action Bar custom and replace it on the Bar.
		activateActionBar(inflater);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

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
					String link = NeoComModelStore.getSingleton().getActiveCharacter().getURLForAvatar();
					pilotIcon.setImageDrawable(NeoComCacheManager.getCacheDrawable(link, pilotIcon));
				}
				if ( null != activityIcon ) {
					activityIcon.setImageResource(R.drawable.planets);
				}
				if ( null != toolBarTitleLeft ) {
					toolBarTitleLeft.setText(NeoComModelStore.getSingleton().getActiveCharacter().getName());
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
			try{
			// Leave the action bar as the standard.
			getActivity().getActionBar().setDisplayShowCustomEnabled(false);
			getActivity().getActionBar().setDisplayShowTitleEnabled(true);
			} catch (RuntimeException rtexreleased) {}
		}
	}

	/**
	 * Generate a simple Generator identifier to be registered and then create our custom mockup generator..
	 */
	@Override
	protected void registerDataSource () {
		logger.info(">> [ColonyFragment.registerDataSource]");
		// Get access to the parameters and reconstruct the data chain if required.
		String loginid = this.getExtras().getString(AppWideConstants.EExtras.EXTRA_LOGINID.name());
		long characterid = this.getExtras().getLong(AppWideConstants.EExtras.EXTRA_CAPSULEERID.name());
		NeoComModelStore.getSingleton().activateLoginIdentifier(loginid);
		NeoComModelStore.getSingleton().activatePilot(characterid);
		// Create a unique identifier to locate this Model hierarchy and their matching DataSource.
		DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant())
																											 .addIdentifier(loginid)
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

//- CLASS IMPLEMENTATION ...................................................................................
final class ColonyStructureGenerator extends AbstractGenerator implements IModelGenerator {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	private final Bundle _extras;
	private String _loginid = null;
	private long _characterid = -1;
//	private long _planetid = -2;
	private NeoComCharacter _pilot = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public ColonyStructureGenerator (final DataSourceLocator locator, final String variant, final Bundle extras) {
		super(locator, variant);
		_extras = extras;

		_loginid = extras.getString(EExtras.EXTRA_LOGINID.name());
		_characterid = extras.getLong(EExtras.EXTRA_CAPSULEERID.name());
//		_planetid = extras.getLong(EExtras.EXTRA_PLANETID.name());
				_pilot = NeoComModelStore.getSingleton().activatePilot(_characterid);
//		_pilot = new Pilot();
//		_pilot.setID(_characterid);
//		_pilot.setAuthorization(new ApiAuthorization(2889577, _characterid, "Mb6iDKR14m9Xjh9maGTQCGTkpjRHPjOgVUkvK6E9r6fhMtOWtipaqybp0qCzxuuw"));
	}

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * If the debug flag is true (usually has that value or POCs we should get the model from the mockup model generator.
	 */
	public RootNode collaborate2Model () {
		AbstractGenerator.logger.info(">> [ColonyStructureGenerator.collaborate2Model]");
		// Check if the model is already generated.
		if ( null == _dataModelRoot ) {
			this.setDataModel(new RootNode());

			// Initialize the Planetary Manager and connect to the ESI api to get the Colony information.


			// Get the Colony Manager for this pilot.
			final ColonyManager manager = new ColonyManager(_pilot);
			final List<Colony> colonies = manager.accessAllColonies();
			for (Colony colony : colonies) {
				_dataModelRoot.addChild(colony);
			}

			AbstractGenerator.logger.info("<< [ColonyStructureGenerator.collaborate2Model]");
			return _dataModelRoot;
		} else {
			AbstractGenerator.logger.info("<< [ColonyStructureGenerator.collaborate2Model]");
			return _dataModelRoot;
		}
	}
}
// - UNUSED CODE ............................................................................................
//[01]
