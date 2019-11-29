package org.dimensinfin.eveonline.neocom.test.steps;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitor;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.Assert;
import org.junit.Rule;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardActivity;
import org.dimensinfin.eveonline.neocom.app.controller.PilotController;
import org.dimensinfin.eveonline.neocom.app.render.DashboardActionBarRender;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.test.support.AcceptanceNeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.AuthorizedStep;
import org.dimensinfin.eveonline.neocom.test.support.ControllerMatcher;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.PanelTypes;
import org.dimensinfin.eveonline.neocom.test.support.Ristretto;
import org.dimensinfin.eveonline.neocom.test.support.adapters.SupportCredentialRepository;
import org.dimensinfin.eveonline.neocom.test.support.converters.CucumberTable2CredentialConverter;


import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class NC03PilotDashboard extends AuthorizedStep {
	static {
		System.setProperty( "org.mockito.android.target", ApplicationProvider.getApplicationContext().getCacheDir().getPath() );
	}

	@Rule
	public GrantPermissionRule permissionInternet = GrantPermissionRule.grant( Manifest.permission.INTERNET );
	@Rule
	public GrantPermissionRule permissionNetworkState = GrantPermissionRule.grant( Manifest.permission.ACCESS_NETWORK_STATE );
	@Rule
	public GrantPermissionRule permissionWriteStorage = GrantPermissionRule.grant( Manifest.permission.WRITE_EXTERNAL_STORAGE );
	@Rule
	public GrantPermissionRule permissionReadStorage = GrantPermissionRule.grant( Manifest.permission.READ_EXTERNAL_STORAGE );

	private Intent authorizationIntent;
	private Integer selectedPage = 0;
	// - C O M P O N E N T S
	private SupportCredentialRepository credentialRepository;

	public NC03PilotDashboard( final NeoComWorld world ) {
		super( world );
	}

	public void initializeScenario() throws IOException, SQLException {
		AcceptanceNeoComLogger.info( "[BEFORE] initializeScenario" );
		this.stopScheduler();
		this.initializeApplicationStorage();
		this.initializeApplicationEsi();
//		this.connectAuthorizedMockComponents();
		this.deleteDatabaseRecords();
	}

	private void deleteDatabaseRecords() throws SQLException {
		this.credentialRepository = new SupportCredentialRepository.Builder()
				                            .withCredentialDao(
						                            NeoComComponentFactory.getSingleton()
								                            .getNeoComDBHelper().getCredentialDao() )
				                            .build();
		NeoComComponentFactory.getSingleton().setCredentialRepository( this.credentialRepository );
		this.credentialRepository.deleteAll();
	}

	@Given("the PilotDashboardActivity")
	public void the_PilotDashboardActivity() throws IOException, SQLException {
		this.initializeScenario();
		AcceptanceNeoComLogger.info( "[GIVEN] the PilotDashboardActivity" );
		this.authorizationIntent = this.authorizationIntent( PilotDashboardActivity.class );
	}

	@Given("the following Credentials into my repository")
	public void the_following_Credentials_into_my_repository( final List<Map<String, String>> dataTable ) throws SQLException {
		AcceptanceNeoComLogger.info( "[GIVEN] the following Credentials into my repository" );
		for (Map<String, String> row : dataTable) {
			final Credential credential = new CucumberTable2CredentialConverter().convert( row );
			this.credentialRepository.persist( credential );
		}
	}

	@When("the PilotDashboardActivity activity lifecycle completes for page {string}")
	public void the_PilotDashboardActivity_activity_lifecycle_completes_for_page( final String pageNumber ) {
		AcceptanceNeoComLogger.info( "[WHEN] the PilotDashboardActivity activity lifecycle completes for page {string}" );
		AcceptanceNeoComLogger.info( "Going to launch the scenario with the PilotDashboardActivity..." );
		final ActivityScenario<PilotDashboardActivity> scenario = ActivityScenario.launch( this.authorizationIntent );
		Assert.assertNotNull( scenario );
		AcceptanceNeoComLogger.info( "Store the scenario..." );
		this.world.setPilotDashboardScenario( scenario );
		scenario.onActivity( ( activity ) -> {
			AcceptanceNeoComLogger.info( "Verify the activity starts without exceptions..." );
			Assert.assertNotNull( activity );
			Assert.assertTrue( activity instanceof PilotDashboardActivity );
			AcceptanceNeoComLogger.info( "Store the activity..." + activity.getClass().getSimpleName() );
			this.world.setActiveActivity( activity );
			AcceptanceNeoComLogger.info( "Set the configured page..." + pageNumber );
			this.world.setSelectedPage( Integer.parseInt( pageNumber ) );
			AcceptanceNeoComLogger.info( "Verify the current activity state..." );
			final ActivityLifecycleMonitor registry = ActivityLifecycleMonitorRegistry.getInstance();
			final Stage stage = registry.getLifecycleStageOf( activity );
			Assert.assertEquals( Stage.RESUMED, stage );
		} );
		Ristretto.waitForBackground( () -> {
			Ristretto.waitForCompletion( () -> {
				AcceptanceNeoComLogger.info( "Invalidate all the display and wait termination..." );
				Ristretto.updateDisplay();
			} );
		} );
	}

	@Then("there is a action bar of type {string} with the next fields")
	public void there_is_a_action_bar_of_type_with_the_next_fields( final String actionBarType,
	                                                                final List<Map<String, String>> dataTable ) {
		final String APP_NAME = "AppName";
		final String APP_VERSION = "AppVersion";
		final String PILOT_NAME = "PilotName";
		final ActionBar actionBar = this.world.getActiveActivity().getActionBar();
		Assert.assertNotNull( actionBarType );
		final Object tag = actionBar.getCustomView().getTag();
		if (tag instanceof DashboardActionBarRender) {
			Assert.assertEquals( dataTable.get( 0 ).get( APP_NAME ),
					((TextView) actionBar.getCustomView().findViewById( R.id.toolBarTitleLeft )).getText() );
			Assert.assertEquals( dataTable.get( 0 ).get( APP_VERSION ),
					((TextView) actionBar.getCustomView().findViewById( R.id.toolBarTitleRight )).getText() );
			Assert.assertEquals( dataTable.get( 0 ).get( PILOT_NAME ),
					((TextView) actionBar.getCustomView().findViewById( R.id.toolBarSubTitle )).getText() );
		}
	}

	@Then("the header has a {string} panel with the next information")
	public void the_header_has_a_panel_with_the_next_information( final String panelModelType,
	                                                              final List<Map<String, String>> dataTable ) {
		final List<PilotController> targetPanelList = new ControllerMatcher<PilotController>()
				                                              .withType( Ristretto.accessHeaderPanels( this.world.getSelectedPage() ),
						                                              PilotController.class );

//				( target ) -> {
//					return PanelTypes.decodeName( panelModelType ).matches( target );
//				} );
		Assert.assertNotNull( targetPanelList );
		Assert.assertTrue( targetPanelList.size() > 0 );
		switch (PanelTypes.decodeName( panelModelType )) {
			case PILOTCONTROLLER:
				final PilotController pilotController = (PilotController) targetPanelList.get( 0 );
				Assert.assertTrue( this.validatePilot4DashboardRender( dataTable.get( 0 ),
						Objects.requireNonNull( pilotController.getViewCache() ) ) );
		}
	}

	@Then("the PilotDashboardActivity has {string} pages")
	public void the_PilotDashboardActivity_has_pages( final String expectedPageCount ) {
		AcceptanceNeoComLogger.info( "[THEN] the PilotDashboardActivity has {string} pages" );
		Assert.assertEquals( Integer.parseInt( expectedPageCount ), Ristretto.activityPageCount() );
	}

	private boolean validatePilot4DashboardRender( final Map<String, String> expectedData,
	                                               final View obtainedView ) {
		final String PILOT_NAME = "PilotName";
		Assert.assertEquals( expectedData.get( PILOT_NAME ),
				((TextView) obtainedView.findViewById( R.id.pilotName )).getText() );

		return true;
	}

//	private static class PilotControllerMatcher implements ControllerMatcher {
//
//		@Override
//		public boolean matches( final IAndroidController item ) {
//			return PanelTypes.decodeName( PanelTypes.PILOTCONTROLLER ).matches( item );
//		}
//	}
}
