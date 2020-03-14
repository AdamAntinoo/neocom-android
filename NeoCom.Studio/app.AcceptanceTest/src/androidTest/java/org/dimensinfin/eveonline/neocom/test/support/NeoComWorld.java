package org.dimensinfin.eveonline.neocom.test.support;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.ActivityTestRule;

import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.joda.time.LocalDate;

import org.dimensinfin.android.mvc.activity.MVCPagerFragment;
import org.dimensinfin.android.mvc.render.MVCRender;
import org.dimensinfin.eveonline.neocom.app.NeoComApplication;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.activity.AppInitializationActivity;
import org.dimensinfin.eveonline.neocom.app.activity.AppSplashActivity;
import org.dimensinfin.eveonline.neocom.app.activity.AuthorizationFlowActivity;
import org.dimensinfin.eveonline.neocom.app.activity.PilotDashboardActivity;
import org.dimensinfin.eveonline.neocom.assets.activity.AssetDisplayActivity;
import org.dimensinfin.eveonline.neocom.core.activity.SettingsActivity;
import org.dimensinfin.eveonline.neocom.core.domain.EExtras;
import org.dimensinfin.eveonline.neocom.database.repositories.MiningRepository;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.adapters.SupportCredentialRepository;
import org.dimensinfin.eveonline.neocom.test.support.adapters.SupportMiningRepository;

public class NeoComWorld {
	private ActivityScenario<AppInitializationActivity> appInitializationActivityScenario;
	private ActivityScenario<SettingsActivity> settingsActivityScenario;
	private ActivityScenario<AppSplashActivity> appSplashActivityScenario;
	private ActivityScenario<PilotDashboardActivity> pilotDashboardScenario;
	private ActivityScenario<AssetDisplayActivity> assetDisplayScenario;
	private ActivityScenario<AuthorizationFlowActivity> authorizationFlowScenario;
	private Activity activeActivity;
	private Integer selectedPage = 0;
	private Integer capsuleerIdentifier;
	private Intent authorizationIntent;
	// - C O M P O N E N T S
	private SupportCredentialRepository credentialRepository;
	private SupportMiningRepository miningRepository;

	public static final ExecutorService backgroundExecutor = Executors.newFixedThreadPool( 1 );
	private NeoComApplication application;
	private ActivityScenario activityScenario;
	private View activePanel;
	private MVCRender activeRender;

	private ActivityTestRule activityRule;
	private MVCPagerFragment fragment;
	private LocalDate todayDate;
	private View actionBar;

	public NeoComWorld() {
		NeoComLogger.enter();
		NeoComLogger.info( "Registering global components..." );
		try {
			this.credentialRepository = new SupportCredentialRepository.Builder()
					                            .withCredentialDao( NeoComComponentFactory.getSingleton()
							                                                .getNeoComDBHelper().getCredentialDao() )
					                            .build();
			NeoComComponentFactory.getSingleton().setCredentialRepository( this.credentialRepository );
		} catch (final SQLException sqle) {
			AcceptanceNeoComLogger.error( sqle );
		}
		try {
			this.miningRepository = new SupportMiningRepository.Builder()
					                        .withMiningExtractionDao( NeoComComponentFactory.getSingleton()
							                                                  .getNeoComDBHelper().getMiningExtractionDao() )
					                        .withLocationCatalogService( NeoComComponentFactory.getSingleton()
							                                                     .getLocationCatalogService() )
					                        .build();
			NeoComComponentFactory.getSingleton().setMiningRepository( this.miningRepository );
		} catch (final SQLException sqle) {
			AcceptanceNeoComLogger.error( sqle );
		}
	}

	public ActivityScenario<AppInitializationActivity> getAppInitializationActivityScenario() {
		return this.appInitializationActivityScenario;
	}

	public NeoComWorld setAppInitializationActivityScenario( final ActivityScenario<AppInitializationActivity> appInitializationActivityScenario ) {
		this.appInitializationActivityScenario = appInitializationActivityScenario;
		return this;
	}

	public ActivityScenario<SettingsActivity> getSettingsActivityScenario() {
		return this.settingsActivityScenario;
	}

	public NeoComWorld setSettingsActivityScenario( final ActivityScenario<SettingsActivity> settingsActivityScenario ) {
		this.settingsActivityScenario = settingsActivityScenario;
		return this;
	}

	public ActivityScenario<AppSplashActivity> getAppSplashActivityScenario() {
		return this.appSplashActivityScenario;
	}

	public NeoComWorld setAppSplashActivityScenario( final ActivityScenario<AppSplashActivity> appSplashActivityScenario ) {
		this.appSplashActivityScenario = appSplashActivityScenario;
		return this;
	}

	public ActivityScenario<PilotDashboardActivity> getPilotDashboardScenario() {
		return this.pilotDashboardScenario;
	}

	public NeoComWorld setPilotDashboardScenario( final ActivityScenario<PilotDashboardActivity> pilotDashboardScenario ) {
		this.pilotDashboardScenario = pilotDashboardScenario;
		return this;
	}

	public ActivityScenario<AssetDisplayActivity> getAssetDisplayScenario() {
		return this.assetDisplayScenario;
	}

	public NeoComWorld setAssetDisplayScenario( final ActivityScenario<AssetDisplayActivity> assetDisplayScenario ) {
		this.assetDisplayScenario = assetDisplayScenario;
		return this;
	}

	public ActivityScenario<AuthorizationFlowActivity> getAuthorizationFlowScenario() {
		return this.authorizationFlowScenario;
	}

	public NeoComWorld setAuthorizationFlowScenario( final ActivityScenario<AuthorizationFlowActivity> authorizationFlowScenario ) {
		this.authorizationFlowScenario = authorizationFlowScenario;
		return this;
	}

	public Activity getActiveActivity() {
		return this.activeActivity;
	}

	public NeoComWorld setActiveActivity( final Activity activeActivity ) {
		this.activeActivity = activeActivity;
		return this;
	}

	public Integer getSelectedPage() {
		return this.selectedPage;
	}

	public NeoComWorld setSelectedPage( final Integer selectedPage ) {
		this.selectedPage = selectedPage;
		return this;
	}

	public SupportCredentialRepository getCredentialRepository() {
		return this.credentialRepository;
	}

	public Integer getCapsuleerIdentifier() {
		return this.capsuleerIdentifier;
	}

	public NeoComWorld setCapsuleerIdentifier( final Integer capsuleerIdentifier ) {
		this.capsuleerIdentifier = capsuleerIdentifier;
		return this;
	}

	public Intent generateAuthorizationIntent( final Class activityClass ) {
		return this.generateIntent( activityClass );
	}

	public NeoComWorld setAuthorizationIntent( final Intent authorizationIntent ) {
		this.authorizationIntent = authorizationIntent;
		return this;
	}

	private Intent generateIntent( final Class destinationActivity ) {
		final Intent authorizationIntent = new Intent( ApplicationProvider.getApplicationContext(), destinationActivity );
		Bundle bundle = new Bundle();
		bundle.putInt( EExtras.CAPSULEER_IDENTIFIER.name(), this.getCapsuleerIdentifier() );
		authorizationIntent.putExtras( bundle );
		return authorizationIntent;
	}

	public SupportMiningRepository getMiningRepository() {
		return this.miningRepository;
	}

	// ---------------------------------------------------------------
	public NeoComApplication getApplication() {
		return this.application;
	}

	public NeoComWorld setApplication( final NeoComApplication application ) {
		this.application = application;
		return this;
	}

	public ActivityScenario getActivityScenario() {
		return this.activityScenario;
	}

	public NeoComWorld setActivityScenario( final ActivityScenario activityScenario ) {
		this.activityScenario = activityScenario;
		return this;
	}
	public MVCRender getActiveRender() {
		return this.activeRender;
	}

	public NeoComWorld setActiveRender( final MVCRender activeRender ) {
		this.activeRender = activeRender;
		return this;
	}
	public ViewGroup getHeaderContainer() {
		Objects.requireNonNull( this.fragment );
		return this.fragment.accessHeaderContainer();
	}
	public ListView getDataSectionContainer() {
		Objects.requireNonNull( this.fragment );
		return this.fragment.accessDataSectionContainer();
	}
}
