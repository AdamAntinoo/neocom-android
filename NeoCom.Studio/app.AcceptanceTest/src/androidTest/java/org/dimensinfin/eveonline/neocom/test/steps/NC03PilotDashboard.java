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
import org.dimensinfin.eveonline.neocom.app.controller.PilotSectionController;
import org.dimensinfin.eveonline.neocom.app.render.DashboardActionBarRender;
import org.dimensinfin.eveonline.neocom.test.support.AcceptanceNeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.AuthorizedStep;
import org.dimensinfin.eveonline.neocom.test.support.ControllerMatcher;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;
import org.dimensinfin.eveonline.neocom.test.support.PanelTypes;
import org.dimensinfin.eveonline.neocom.test.support.Ristretto;


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

	public NC03PilotDashboard( final NeoComWorld world ) {
		super( world );
	}

	public void initializeScenario() throws IOException, SQLException {
		AcceptanceNeoComLogger.info( "[BEFORE] initializeScenario" );
		this.stopScheduler();
		this.initializeApplicationStorage();
		this.initializeApplicationEsi();
		this.deleteDatabaseRecords();
		NeoComComponentFactory.getSingleton().getESIDataProvider();
	}

	@Given("the PilotDashboardActivity")
	public void the_PilotDashboardActivity() throws IOException, SQLException {
		this.initializeScenario();
		AcceptanceNeoComLogger.info( "[GIVEN] the PilotDashboardActivity" );
		this.authorizationIntent = this.authorizationIntent( PilotDashboardActivity.class );
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

	@Then("the data section has a {string} panel with the next information")
	public void the_data_section_has_a_panel_with_the_next_information( final String panelModelType,
	                                                                    final List<Map<String, String>> dataTable ) {
		final List<PilotController> targetPanelList = new ControllerMatcher<PilotController>()
				                                              .withType( Ristretto.accessDataPanels( this.world.getSelectedPage() ),
						                                              PilotController.class );
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

	@Then("the assets section panel shows an spinner")
	public void the_assets_section_panel_shows_an_spinner() {
		AcceptanceNeoComLogger.info( "[THEN] the assets section panel shows an spinner" );
		final List<PilotSectionController> targets = new ControllerMatcher<PilotSectionController>()
				                                             .withType( Ristretto.accessDataPanels( this.selectedPage ),
						                                             PilotSectionController.class );
		// Search for the assets panel.
		for (PilotSectionController panel : targets) {
			if (panel.getModel().getTitle().equalsIgnoreCase( "Assets" )) {
				final View icon = Objects.requireNonNull( panel.getViewCache().findViewById( R.id.clickIcon ) );
				Assert.assertEquals( new Integer( R.drawable.progress_spinner_white ), (Integer) icon.getTag() );
			}
		}
	}

	@Then("there is a action bar of type DashboardActionBar with the next fields")
	public void there_is_a_action_bar_of_type_DashboardActionBar_with_the_next_fields( io.cucumber.datatable.DataTable dataTable ) {
		// Write code here that turns the phrase above into concrete actions
		// For automatic transformation, change DataTable to one of
		// E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
		// Double, Byte, Short, Long, BigInteger or BigDecimal.
		//
		// For other transformations you can register a DataTableType.
		throw new cucumber.api.PendingException();
	}

	//	@When("the pilot activates the Assets information panel")
	public void the_pilot_activates_the_Assets_information_panel() {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	//	@Then("the assets section panel shows the next data")
	public void the_assets_section_panel_shows_the_next_data( io.cucumber.datatable.DataTable dataTable ) {
		// Write code here that turns the phrase above into concrete actions
		// For automatic transformation, change DataTable to one of
		// E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
		// Double, Byte, Short, Long, BigInteger or BigDecimal.
		//
		// For other transformations you can register a DataTableType.
		throw new cucumber.api.PendingException();
	}

	//	@Given("the Credential Identifier {string}")
	public void the_Credential_Identifier( String string ) {
		// Write code here that turns the phrase above into concrete actions
		throw new cucumber.api.PendingException();
	}

	//	@Then("the data section has a Exception panel with the next information")
	public void the_data_section_has_a_Exception_panel_with_the_next_information( io.cucumber.datatable.DataTable dataTable ) {
		// Write code here that turns the phrase above into concrete actions
		// For automatic transformation, change DataTable to one of
		// E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
		// Double, Byte, Short, Long, BigInteger or BigDecimal.
		//
		// For other transformations you can register a DataTableType.
		throw new cucumber.api.PendingException();
	}

	private boolean validatePilot4DashboardRender( final Map<String, String> expectedData,
	                                               final View obtainedView ) {
		final String PILOT_NAME = "PilotName";
		final String PILOT_IDENTIFIER = "PilotId";
		final String PILOT_RACE = "PilotRace";
		final String CORPORATION_NAME = "CorporationName";
		Assert.assertEquals( expectedData.get( PILOT_NAME ),
				((TextView) obtainedView.findViewById( R.id.pilotName )).getText() );
		Assert.assertEquals( expectedData.get( PILOT_IDENTIFIER ),
				((TextView) obtainedView.findViewById( R.id.pilotIdentifier )).getText() );
		Assert.assertEquals( expectedData.get( PILOT_RACE ),
				((TextView) obtainedView.findViewById( R.id.raceName )).getText() );
		Assert.assertEquals( expectedData.get( CORPORATION_NAME ),
				((TextView) obtainedView.findViewById( R.id.corporationName )).getText() );

		return true;
	}
}
