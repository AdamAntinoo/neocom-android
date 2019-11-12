package org.dimensinfin.eveonline.neocom.test.steps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.GrantPermissionRule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.mockito.Mockito;

import org.dimensinfin.android.mvc.activity.IPagerFragment;
import org.dimensinfin.android.mvc.activity.MVCMultiPageActivity;
import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardActivity;
import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardFragment;
import org.dimensinfin.eveonline.neocom.app.controller.PilotSectionController;
import org.dimensinfin.eveonline.neocom.assets.activity.AssetDisplayActivity;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;
import org.dimensinfin.eveonline.neocom.provider.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.provider.IFileSystem;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.AcceptanceNeoComLogger;


import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

public class NC03AssetsDisplay {
	static {
		System.setProperty( "org.mockito.android.target", ApplicationProvider.getApplicationContext().getCacheDir().getPath() );
	}

	private Context applicationContext = NeoComComponentFactory.getSingleton().getApplication().getApplicationContext();
	private IFileSystem fileSystemAdapter = NeoComComponentFactory.getSingleton().getFileSystemAdapter();
	private IConfigurationProvider configurationProvider = NeoComComponentFactory.getSingleton().getConfigurationProvider();

	private ActivityScenario<PilotDashboardActivity> scenario;
	@Rule
	public GrantPermissionRule permissionInternet = GrantPermissionRule.grant( Manifest.permission.INTERNET );
	@Rule
	public GrantPermissionRule permissionNetworkState = GrantPermissionRule.grant( Manifest.permission.ACCESS_NETWORK_STATE );
	@Rule
	public GrantPermissionRule permissionWriteStorage = GrantPermissionRule.grant( Manifest.permission.WRITE_EXTERNAL_STORAGE );
	@Rule
	public GrantPermissionRule permissionReadStorage = GrantPermissionRule.grant( Manifest.permission.READ_EXTERNAL_STORAGE );

	public void initializeApplication() throws IOException {
		final boolean createAppResult = this.createAppDir();
		final boolean createCacheResult = this.createCacheDirectories();
		if (!this.sdeDatabaseExists()) {
			this.fileSystemAdapter.copyFromAssets(
					this.getResources().getString( R.string.sdedatabasefilename ), null );
		}

	}

	public void connectComponents() {
		// Mock the credential information for credential dependen activities.
		final Credential credential = new Credential.Builder( 93813310 )
				                              .withAccountId( 93813310 )
				                              .withAccountName( "Perico Tuerto" )
				                              .withAccessToken(
						                              "P940P9FpVhR8oq2V96D7pbcLzndNWTsAVgVAMt0HE5tJT15zg83MMqfsZhW1yf1XoFn9_IQJN5LrIa3NA90Ifw" )
				                              .withRefreshToken(
						                              "52HSB2sQiYBOrvaPidnxvnc-DIgT7DP5gUoCEOCW4v61dBfHOrCplfuwma0En0eZsLff2L6OJ6csIDTEQhqDmr0iVB6XmuNloTYhTT2Lx-x15j37Oo91jRrbHiC414DMX2nDPz-JGAdPLDtOzG2-4ofHR61rvw7sGY8Z1CnAgdGexAN6M4ZX93D_UWBEvlFd" )
				                              .withDataSource( "tranquility" )
				                              .withScope(
						                              "publicData esi-location.read_location.v1 esi-location.read_ship_type.v1 esi-mail.read_mail.v1 esi-skills.read_skills.v1 esi-skills.read_skillqueue.v1 esi-wallet.read_character_wallet.v1 esi-wallet.read_corporation_wallet.v1 esi-search.search_structures.v1 esi-clones.read_clones.v1 esi-universe.read_structures.v1 esi-assets.read_assets.v1 esi-planets.manage_planets.v1 esi-fittings.read_fittings.v1 esi-industry.read_character_jobs.v1 esi-markets.read_character_orders.v1 esi-characters.read_blueprints.v1 esi-contracts.read_character_contracts.v1 esi-clones.read_implants.v1 esi-wallet.read_corporation_wallets.v1 esi-characters.read_notifications.v1 esi-corporations.read_divisions.v1 esi-assets.read_corporation_assets.v1 esi-corporations.read_blueprints.v1 esi-contracts.read_corporation_contracts.v1 esi-industry.read_corporation_jobs.v1 esi-markets.read_corporation_orders.v1 esi-industry.read_character_mining.v1 esi-industry.read_corporation_mining.v1" )
				                              .withAssetsCount( 1476 )
				                              .withWalletBalance( 6.309543632E8 )
				                              .withRaceName( "Amarr" )
				                              .build();
		final CredentialRepository credentialRepository = Mockito.mock( CredentialRepository.class );
		final List<Credential> credentialList = new ArrayList<>();
		credentialList.add( credential );
		Mockito.when( credentialRepository.findAllByServer( Mockito.anyString() ) )
		       .thenReturn( credentialList );
		NeoComComponentFactory.getSingleton().getESIDataProvider();
		NeoComComponentFactory.getSingleton().replaceCredentialRepository( credentialRepository );
	}

	@Given("the PilotDashboardActivity")
	public void the_PilotDashboardActivity() throws IOException {
		this.initializeApplication();
		this.connectComponents();
		// Create the plot identification intent.
		final Intent intent = new Intent( ApplicationProvider.getApplicationContext(), PilotDashboardActivity.class );
		Bundle bundle = new Bundle();
		bundle.putInt( EExtras.CAPSULEER_IDENTIFIER.name(), 93813310 );
		intent.putExtras( bundle );
		// Start the activity test.
		this.scenario = ActivityScenario.launch( intent );
		this.scenario.onActivity( ( activity ) -> {
			AcceptanceNeoComLogger.info( "Verify the activity starts without exceptions..." );
			Assert.assertNotNull( activity );
		} );
	}

	@When("the pilot activates the Assets information panel")
	public void the_pilot_activates_the_Assets_information_panel() {
//		this.scenario = ActivityScenario.launch( intent );
		this.scenario.onActivity( ( activity ) -> {
			AcceptanceNeoComLogger.info( "Searching assets panel..." );
			final PilotDashboardFragment resolver = new ActivityResolver<PilotDashboardActivity, PilotDashboardFragment>()
					                                        .fragmentResolver( activity, 0 );
			final ListView dataSectionContainer = resolver.accessDataSectionContainer();

			final List<PilotSectionController> controllers = new ControllerMatcher<PilotSectionController>( dataSectionContainer )
					                                                 .withType( PilotSectionController.class );
			PilotSectionController target = null;
			for (PilotSectionController controller : controllers) {
				if (controller.getModel().getTitle().equalsIgnoreCase( "ASSETS" )) {
					target = controller;
					NeoComLogger.info( "Panel found." );
				}
			}
			Assert.assertNotNull( target );
			Assert.assertNotNull( target.getModel().getClickTarget() );
			NeoComLogger.info( "Panel is clickable." );
			// TODO - Write the code to get the view from the data source adapter.
			final View panelView = target.getViewCache();
			target.onClick( panelView );
//				Espresso.onView( ViewMatchers.withId( R.id.login_button)).perform( ViewActions.click());

//				Espresso.onData( hasToString( startsWith( "ASSETS" ) ) )
//				        .inAdapterView( withId( R.id.myListView ) )
//				        .perform( click() );
//				Espresso.onData( withItemValue("ASSETS"))
//				        .inAdapterView(ViewMatchers.withId( org.dimensinfin.android.mvc.R.id.listContainer))
//				        .perform( checkRender());

//			intended( hasComponent( AssetDisplayActivity.class.getName() ) );
		} );
	}

	@Then("the application moves to the AssetDisplayActivity.")
	public void the_application_moves_to_the_AssetDisplayActivity() {
		// TODO - check how to validate I have moved to another activity
		NeoComLogger.info( "This step has to be implemented." );
	}

	protected boolean createAppDir() {
		// Create file application directory if it doesn't exist
		final File sdCardDir = new File( this.applicationContext.getExternalFilesDir( null )
				, this.getResources().getString( R.string.appname ) );
		if (!sdCardDir.exists()) sdCardDir.mkdir();
		return sdCardDir.exists();
	}

	protected boolean createCacheDirectories() {
		File cachedir = new File( this.applicationContext.getExternalFilesDir( null )
				, this.getResources().getString( R.string.appname )
						  + "/" + this.configurationProvider.getResourceString( "P.cache.directory.path" ) );
		cachedir.mkdir();
		return cachedir.exists();
	}

	protected boolean sdeDatabaseExists() throws IOException {
		return new File( this.fileSystemAdapter.accessResource4Path( this.getResources().getString( R.string.sdedatabasefilename ) ) ).exists();
	}

	private Resources getResources() {
		return NeoComComponentFactory.getSingleton().getApplication().getResources();
	}

	public static class ActivityResolver<A extends MVCMultiPageActivity, F extends IPagerFragment> {
		public F fragmentResolver( A activity, int pageNumber ) {
			return (F) activity.accessPageAdapter().getItem( pageNumber );
		}
	}

	public static class ControllerMatcher<C extends AndroidController> {
		private List<IAndroidController> controllerList = new ArrayList<>();

		public ControllerMatcher( final ListView listContainer ) {
			for (int i = 0; i < listContainer.getChildCount(); i++) {
				final Object item = listContainer.getItemAtPosition( i );
				controllerList.add( (IAndroidController) item );
			}
		}

		public List<C> withType( final Class type ) {
			final List<C> results = new ArrayList<>();
			for (IAndroidController controller : this.controllerList)
				if (type.isInstance( controller ))
					results.add( (C) controller );
			return results;
		}
	}
}