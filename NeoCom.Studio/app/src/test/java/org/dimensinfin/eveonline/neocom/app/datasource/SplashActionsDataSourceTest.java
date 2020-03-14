package org.dimensinfin.eveonline.neocom.app.datasource;

import android.os.Bundle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.android.mvc.exception.ExceptionReport;
import org.dimensinfin.core.domain.EEvents;
import org.dimensinfin.core.domain.IntercommunicationEvent;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.adapter.LocationCatalogService;
import org.dimensinfin.eveonline.neocom.app.controller.CredentialController;
import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;
import org.dimensinfin.eveonline.neocom.app.domain.PanelTitle;
import org.dimensinfin.eveonline.neocom.backend.NeoComBackendService;
import org.dimensinfin.eveonline.neocom.backend.rest.v1.CredentialStoreResponse;
import org.dimensinfin.eveonline.neocom.core.domain.PageNamesType;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.AssetRepository;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;
import org.dimensinfin.eveonline.neocom.database.repositories.MiningRepository;
import org.dimensinfin.eveonline.neocom.provider.ESIDataProvider;
import org.dimensinfin.eveonline.neocom.provider.IConfigurationService;
import org.dimensinfin.eveonline.neocom.service.scheduler.JobScheduler;
import org.dimensinfin.eveonline.neocom.service.scheduler.conf.ISchedulerConfiguration;

class SplashActionsDataSourceTest {
	private Bundle extras;
	private ControllerFactory factory;
	private IConfigurationService configurationService;
	private ESIDataProvider esiDataProvider;
	private LocationCatalogService locationCatalogService;
	private CredentialRepository credentialRepository;
	private AssetRepository assetRepository;
	private MiningRepository miningRepository;
	private ISchedulerConfiguration schedulerConfiguration;
	private NeoComBackendService backendService;

	@BeforeEach
	void setUp() {
		// Given
		this.extras = new Bundle();
		this.factory = Mockito.mock( ControllerFactory.class );
		this.configurationService = Mockito.mock( IConfigurationService.class );
		this.esiDataProvider = Mockito.mock( ESIDataProvider.class );
		this.locationCatalogService = Mockito.mock( LocationCatalogService.class );
		this.credentialRepository = Mockito.mock( CredentialRepository.class );
		this.assetRepository = Mockito.mock( AssetRepository.class );
		this.miningRepository = Mockito.mock( MiningRepository.class );
		this.schedulerConfiguration = Mockito.mock( ISchedulerConfiguration.class );
		this.backendService = Mockito.mock( NeoComBackendService.class );
	}

	@Test
	void buildComplete() {
		final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
				                                           .addIdentifier( 123 )
				                                           .addIdentifier( 345L )
				                                           .addIdentifier( "-TEST-IDENTIFIER-" )
				                                           .withExtras( null )
				                                           .withVariant( PageNamesType.SPLASH.name() )
				                                           .withFactory( this.factory )
				                                           .withConfigurationService( this.configurationService )
				                                           .withEsiDataProvider( this.esiDataProvider )
				                                           .withLocationCatalogService( this.locationCatalogService )
				                                           .withCredentialRepository( this.credentialRepository )
				                                           .withAssetRepository( this.assetRepository )
				                                           .withMiningRepository( this.miningRepository )
				                                           .withSchedulerConfiguration( this.schedulerConfiguration )
				                                           .withBackendService( this.backendService )
				                                           .withName( "-TEST-APPLICATION-NAME-" )
				                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
				                                           .build();
		Assertions.assertNotNull( dataSource );
	}

	@Test
	public void receiveEvent() throws SQLException {
		// Given
		final IntercommunicationEvent event = Mockito.mock( IntercommunicationEvent.class );
		final CredentialController credentialController = Mockito.mock( CredentialController.class );
		final Credential credential = Mockito.mock( Credential.class );
		final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
				                                           .addIdentifier( 123 )
				                                           .addIdentifier( 345L )
				                                           .addIdentifier( "-TEST-IDENTIFIER-" )
				                                           .withExtras( null )
				                                           .withVariant( PageNamesType.SPLASH.name() )
				                                           .withFactory( this.factory )
				                                           .withConfigurationService( this.configurationService )
				                                           .withEsiDataProvider( this.esiDataProvider )
				                                           .withLocationCatalogService( this.locationCatalogService )
				                                           .withCredentialRepository( this.credentialRepository )
				                                           .withAssetRepository( this.assetRepository )
				                                           .withMiningRepository( this.miningRepository )
				                                           .withSchedulerConfiguration( this.schedulerConfiguration )
				                                           .withBackendService( this.backendService )
				                                           .withName( "-TEST-APPLICATION-NAME-" )
				                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
				                                           .build();
		// When
		Mockito.when( event.getPropertyName() ).thenReturn( EEvents.EVENT_REFRESHDATA.name() );
		Mockito.when( event.getNewValue() ).thenReturn( credentialController );
		Mockito.when( credentialController.getModel() ).thenReturn( credential );
		// Test
		dataSource.receiveEvent( event );
		// Assertions
		Mockito.verify( this.credentialRepository, Mockito.times( 1 ) ).persist( Mockito.any( Credential.class ) );
	}

	@Test
	void buildMandatory() {
		Assertions.assertThrows( NullPointerException.class, () -> {
			final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
					                                           .addIdentifier( 123 )
					                                           .addIdentifier( 345L )
					                                           .addIdentifier( "-TEST-IDENTIFIER-" )
					                                           .withExtras( null )
					                                           .withVariant( PageNamesType.SPLASH.name() )
					                                           .withFactory( this.factory )
					                                           .withEsiDataProvider( this.esiDataProvider )
					                                           .withLocationCatalogService( this.locationCatalogService )
					                                           .withCredentialRepository( this.credentialRepository )
					                                           .withAssetRepository( this.assetRepository )
					                                           .withMiningRepository( this.miningRepository )
					                                           .withSchedulerConfiguration( this.schedulerConfiguration )
					                                           .withBackendService( this.backendService )
					                                           .withName( "-TEST-APPLICATION-NAME-" )
					                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
					                                           .build();
		} );
		Assertions.assertThrows( NullPointerException.class, () -> {
			final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
					                                           .addIdentifier( 123 )
					                                           .addIdentifier( 345L )
					                                           .addIdentifier( "-TEST-IDENTIFIER-" )
					                                           .withExtras( extras )
					                                           .withVariant( PageNamesType.SPLASH.name() )
					                                           .withFactory( factory )
					                                           .withConfigurationService( configurationService )
					                                           .withLocationCatalogService( locationCatalogService )
					                                           .withCredentialRepository( credentialRepository )
					                                           .withAssetRepository( assetRepository )
					                                           .withMiningRepository( miningRepository )
					                                           .withSchedulerConfiguration( schedulerConfiguration )
					                                           .withBackendService( backendService )
					                                           .withName( "-TEST-APPLICATION-NAME-" )
					                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
					                                           .build();
		} );
		Assertions.assertThrows( NullPointerException.class, () -> {
			final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
					                                           .addIdentifier( 123 )
					                                           .addIdentifier( 345L )
					                                           .addIdentifier( "-TEST-IDENTIFIER-" )
					                                           .withExtras( extras )
					                                           .withVariant( PageNamesType.SPLASH.name() )
					                                           .withFactory( factory )
					                                           .withConfigurationService( configurationService )
					                                           .withEsiDataProvider( esiDataProvider )
					                                           .withCredentialRepository( credentialRepository )
					                                           .withAssetRepository( assetRepository )
					                                           .withMiningRepository( miningRepository )
					                                           .withSchedulerConfiguration( schedulerConfiguration )
					                                           .withBackendService( backendService )
					                                           .withName( "-TEST-APPLICATION-NAME-" )
					                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
					                                           .build();
		} );
		Assertions.assertThrows( NullPointerException.class, () -> {
			final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
					                                           .addIdentifier( 123 )
					                                           .addIdentifier( 345L )
					                                           .addIdentifier( "-TEST-IDENTIFIER-" )
					                                           .withExtras( extras )
					                                           .withVariant( PageNamesType.SPLASH.name() )
					                                           .withFactory( factory )
					                                           .withConfigurationService( configurationService )
					                                           .withEsiDataProvider( esiDataProvider )
					                                           .withLocationCatalogService( locationCatalogService )
					                                           .withAssetRepository( assetRepository )
					                                           .withMiningRepository( miningRepository )
					                                           .withSchedulerConfiguration( schedulerConfiguration )
					                                           .withBackendService( backendService )
					                                           .withName( "-TEST-APPLICATION-NAME-" )
					                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
					                                           .build();
		} );
		Assertions.assertThrows( NullPointerException.class, () -> {
			final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
					                                           .addIdentifier( 123 )
					                                           .addIdentifier( 345L )
					                                           .addIdentifier( "-TEST-IDENTIFIER-" )
					                                           .withExtras( extras )
					                                           .withVariant( PageNamesType.SPLASH.name() )
					                                           .withFactory( factory )
					                                           .withConfigurationService( configurationService )
					                                           .withEsiDataProvider( esiDataProvider )
					                                           .withLocationCatalogService( locationCatalogService )
					                                           .withCredentialRepository( credentialRepository )
					                                           .withMiningRepository( miningRepository )
					                                           .withSchedulerConfiguration( schedulerConfiguration )
					                                           .withBackendService( backendService )
					                                           .withName( "-TEST-APPLICATION-NAME-" )
					                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
					                                           .build();
		} );
		Assertions.assertThrows( NullPointerException.class, () -> {
			final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
					                                           .addIdentifier( 123 )
					                                           .addIdentifier( 345L )
					                                           .addIdentifier( "-TEST-IDENTIFIER-" )
					                                           .withExtras( extras )
					                                           .withVariant( PageNamesType.SPLASH.name() )
					                                           .withFactory( factory )
					                                           .withConfigurationService( configurationService )
					                                           .withEsiDataProvider( esiDataProvider )
					                                           .withLocationCatalogService( locationCatalogService )
					                                           .withCredentialRepository( credentialRepository )
					                                           .withAssetRepository( assetRepository )
					                                           .withSchedulerConfiguration( schedulerConfiguration )
					                                           .withBackendService( backendService )
					                                           .withName( "-TEST-APPLICATION-NAME-" )
					                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
					                                           .build();
		} );
		Assertions.assertThrows( NullPointerException.class, () -> {
			final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
					                                           .addIdentifier( 123 )
					                                           .addIdentifier( 345L )
					                                           .addIdentifier( "-TEST-IDENTIFIER-" )
					                                           .withExtras( extras )
					                                           .withVariant( PageNamesType.SPLASH.name() )
					                                           .withFactory( factory )
					                                           .withConfigurationService( configurationService )
					                                           .withEsiDataProvider( esiDataProvider )
					                                           .withLocationCatalogService( locationCatalogService )
					                                           .withCredentialRepository( credentialRepository )
					                                           .withAssetRepository( assetRepository )
					                                           .withMiningRepository( miningRepository )
					                                           .withBackendService( backendService )
					                                           .withName( "-TEST-APPLICATION-NAME-" )
					                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
					                                           .build();
		} );
		Assertions.assertThrows( NullPointerException.class, () -> {
			final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
					                                           .addIdentifier( 123 )
					                                           .addIdentifier( 345L )
					                                           .addIdentifier( "-TEST-IDENTIFIER-" )
					                                           .withExtras( extras )
					                                           .withVariant( PageNamesType.SPLASH.name() )
					                                           .withFactory( factory )
					                                           .withConfigurationService( configurationService )
					                                           .withEsiDataProvider( esiDataProvider )
					                                           .withLocationCatalogService( locationCatalogService )
					                                           .withCredentialRepository( credentialRepository )
					                                           .withAssetRepository( assetRepository )
					                                           .withMiningRepository( miningRepository )
					                                           .withSchedulerConfiguration( schedulerConfiguration )
					                                           .withName( "-TEST-APPLICATION-NAME-" )
					                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
					                                           .build();
		} );
	}

	@Test
	void buildFailure() {
		Assertions.assertThrows( NullPointerException.class, () -> {
			final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
					                                           .addIdentifier( 123 )
					                                           .addIdentifier( 345L )
					                                           .addIdentifier( "-TEST-IDENTIFIER-" )
					                                           .withExtras( this.extras )
					                                           .withVariant( PageNamesType.SPLASH.name() )
					                                           .withFactory( this.factory )
					                                           .withConfigurationService( null )
					                                           .withEsiDataProvider( this.esiDataProvider )
					                                           .withLocationCatalogService( this.locationCatalogService )
					                                           .withCredentialRepository( this.credentialRepository )
					                                           .withAssetRepository( this.assetRepository )
					                                           .withMiningRepository( this.miningRepository )
					                                           .withSchedulerConfiguration( this.schedulerConfiguration )
					                                           .withBackendService( this.backendService )
					                                           .withName( "-TEST-APPLICATION-NAME-" )
					                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
					                                           .build();
		} );
		Assertions.assertThrows( NullPointerException.class, () -> {
			final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
					                                           .addIdentifier( 123 )
					                                           .addIdentifier( 345L )
					                                           .addIdentifier( "-TEST-IDENTIFIER-" )
					                                           .withExtras( this.extras )
					                                           .withVariant( PageNamesType.SPLASH.name() )
					                                           .withFactory( this.factory )
					                                           .withConfigurationService( this.configurationService )
					                                           .withEsiDataProvider( null )
					                                           .withLocationCatalogService( this.locationCatalogService )
					                                           .withCredentialRepository( this.credentialRepository )
					                                           .withAssetRepository( this.assetRepository )
					                                           .withMiningRepository( this.miningRepository )
					                                           .withSchedulerConfiguration( this.schedulerConfiguration )
					                                           .withBackendService( this.backendService )
					                                           .withName( "-TEST-APPLICATION-NAME-" )
					                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
					                                           .build();
		} );
		Assertions.assertThrows( NullPointerException.class, () -> {
			final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
					                                           .addIdentifier( 123 )
					                                           .addIdentifier( 345L )
					                                           .addIdentifier( "-TEST-IDENTIFIER-" )
					                                           .withExtras( this.extras )
					                                           .withVariant( PageNamesType.SPLASH.name() )
					                                           .withFactory( this.factory )
					                                           .withConfigurationService( this.configurationService )
					                                           .withEsiDataProvider( this.esiDataProvider )
					                                           .withLocationCatalogService( null )
					                                           .withCredentialRepository( this.credentialRepository )
					                                           .withAssetRepository( this.assetRepository )
					                                           .withMiningRepository( this.miningRepository )
					                                           .withSchedulerConfiguration( this.schedulerConfiguration )
					                                           .withBackendService( this.backendService )
					                                           .withName( "-TEST-APPLICATION-NAME-" )
					                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
					                                           .build();
		} );
		Assertions.assertThrows( NullPointerException.class, () -> {
			final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
					                                           .addIdentifier( 123 )
					                                           .addIdentifier( 345L )
					                                           .addIdentifier( "-TEST-IDENTIFIER-" )
					                                           .withExtras( this.extras )
					                                           .withVariant( PageNamesType.SPLASH.name() )
					                                           .withFactory( this.factory )
					                                           .withConfigurationService( this.configurationService )
					                                           .withEsiDataProvider( this.esiDataProvider )
					                                           .withLocationCatalogService( this.locationCatalogService )
					                                           .withCredentialRepository( null )
					                                           .withAssetRepository( this.assetRepository )
					                                           .withMiningRepository( this.miningRepository )
					                                           .withSchedulerConfiguration( this.schedulerConfiguration )
					                                           .withBackendService( this.backendService )
					                                           .withName( "-TEST-APPLICATION-NAME-" )
					                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
					                                           .build();
		} );
		Assertions.assertThrows( NullPointerException.class, () -> {
			final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
					                                           .addIdentifier( 123 )
					                                           .addIdentifier( 345L )
					                                           .addIdentifier( "-TEST-IDENTIFIER-" )
					                                           .withExtras( this.extras )
					                                           .withVariant( PageNamesType.SPLASH.name() )
					                                           .withFactory( this.factory )
					                                           .withConfigurationService( this.configurationService )
					                                           .withEsiDataProvider( this.esiDataProvider )
					                                           .withLocationCatalogService( this.locationCatalogService )
					                                           .withCredentialRepository( this.credentialRepository )
					                                           .withAssetRepository( null )
					                                           .withMiningRepository( this.miningRepository )
					                                           .withSchedulerConfiguration( this.schedulerConfiguration )
					                                           .withBackendService( this.backendService )
					                                           .withName( "-TEST-APPLICATION-NAME-" )
					                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
					                                           .build();
		} );
		Assertions.assertThrows( NullPointerException.class, () -> {
			final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
					                                           .addIdentifier( 123 )
					                                           .addIdentifier( 345L )
					                                           .addIdentifier( "-TEST-IDENTIFIER-" )
					                                           .withExtras( this.extras )
					                                           .withVariant( PageNamesType.SPLASH.name() )
					                                           .withFactory( this.factory )
					                                           .withConfigurationService( this.configurationService )
					                                           .withEsiDataProvider( this.esiDataProvider )
					                                           .withLocationCatalogService( this.locationCatalogService )
					                                           .withCredentialRepository( this.credentialRepository )
					                                           .withAssetRepository( this.assetRepository )
					                                           .withMiningRepository( null )
					                                           .withSchedulerConfiguration( this.schedulerConfiguration )
					                                           .withBackendService( this.backendService )
					                                           .withName( "-TEST-APPLICATION-NAME-" )
					                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
					                                           .build();
		} );
		Assertions.assertThrows( NullPointerException.class, () -> {
			final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
					                                           .addIdentifier( 123 )
					                                           .addIdentifier( 345L )
					                                           .addIdentifier( "-TEST-IDENTIFIER-" )
					                                           .withExtras( this.extras )
					                                           .withVariant( PageNamesType.SPLASH.name() )
					                                           .withFactory( this.factory )
					                                           .withConfigurationService( this.configurationService )
					                                           .withEsiDataProvider( this.esiDataProvider )
					                                           .withLocationCatalogService( this.locationCatalogService )
					                                           .withCredentialRepository( this.credentialRepository )
					                                           .withAssetRepository( this.assetRepository )
					                                           .withMiningRepository( this.miningRepository )
					                                           .withSchedulerConfiguration( null )
					                                           .withBackendService( this.backendService )
					                                           .withName( "-TEST-APPLICATION-NAME-" )
					                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
					                                           .build();
		} );
		Assertions.assertThrows( NullPointerException.class, () -> {
			final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
					                                           .addIdentifier( 123 )
					                                           .addIdentifier( 345L )
					                                           .addIdentifier( "-TEST-IDENTIFIER-" )
					                                           .withExtras( this.extras )
					                                           .withVariant( PageNamesType.SPLASH.name() )
					                                           .withFactory( this.factory )
					                                           .withConfigurationService( this.configurationService )
					                                           .withEsiDataProvider( this.esiDataProvider )
					                                           .withLocationCatalogService( this.locationCatalogService )
					                                           .withCredentialRepository( this.credentialRepository )
					                                           .withAssetRepository( this.assetRepository )
					                                           .withMiningRepository( this.miningRepository )
					                                           .withSchedulerConfiguration( this.schedulerConfiguration )
					                                           .withBackendService( null )
					                                           .withName( "-TEST-APPLICATION-NAME-" )
					                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
					                                           .build();
		} );
	}

	@Test
	void prepareModel() {
		// When
		Mockito.when( this.configurationService.getResourceString(Mockito.anyString(),Mockito.anyString()) ).thenReturn( "* - *" );
		// Test
		final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
				                                           .addIdentifier( 123 )
				                                           .addIdentifier( 345L )
				                                           .addIdentifier( "-TEST-IDENTIFIER-" )
				                                           .withExtras( null )
				                                           .withVariant( PageNamesType.SPLASH.name() )
				                                           .withFactory( this.factory )
				                                           .withConfigurationService( this.configurationService )
				                                           .withEsiDataProvider( this.esiDataProvider )
				                                           .withLocationCatalogService( this.locationCatalogService )
				                                           .withCredentialRepository( this.credentialRepository )
				                                           .withAssetRepository( this.assetRepository )
				                                           .withMiningRepository( this.miningRepository )
				                                           .withSchedulerConfiguration( this.schedulerConfiguration )
				                                           .withBackendService( this.backendService )
				                                           .withName( "-TEST-APPLICATION-NAME-" )
				                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
				                                           .build();
		dataSource.prepareModel();
		// Assertions
		Assertions.assertEquals( 1, dataSource.getHeaderModelRoot().size() );
		final ICollaboration item = dataSource.getHeaderModelRoot().get( 0 );
		Assertions.assertTrue( item instanceof AppVersion );
		Assertions.assertEquals( 1, JobScheduler.getJobScheduler().getJobCount() );
	}
	@Test
	public void collaborate2Model() {
		// Given
		final CredentialRepository credentialRepositoryLocal = Mockito.mock( CredentialRepository.class );
		final Credential credential = Mockito.mock( Credential.class );
		final List<Credential> credentialList = new ArrayList<>();
		credentialList.add( credential );
		final NeoComBackendService backendServiceLocal = Mockito.mock( NeoComBackendService.class );
		final CredentialStoreResponse credentialStoreResponse = Mockito.mock( CredentialStoreResponse.class );

		final LocationCatalogService locationCatalogService = Mockito.mock( LocationCatalogService.class );
		// When
		Mockito.when( credentialRepositoryLocal.accessAllCredentials() ).thenReturn( credentialList );
		Mockito.when( credential.getRefreshToken() ).thenReturn( "-TEST-REFRESH-DUMMY-TOKEN-" );
		Mockito.when( credential.getAccountName() ).thenReturn( "-TEST-ACCOUNT-NAME-" );
		Mockito.when( backendServiceLocal.storeCredential( Mockito.any( Credential.class ) ) ).thenReturn( credentialStoreResponse );
		Mockito.when( credentialStoreResponse.getJwtToken() ).thenReturn( "-JWT-TOKEN-" );
		Mockito.when( credential.getLastUpdateTime() ).thenReturn( DateTime.now() );
		// Test
		final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
				                                           .addIdentifier( 123 )
				                                           .addIdentifier( 345L )
				                                           .addIdentifier( "-TEST-IDENTIFIER-" )
				                                           .withExtras( null )
				                                           .withVariant( PageNamesType.SPLASH.name() )
				                                           .withFactory( this.factory )
				                                           .withConfigurationService( this.configurationService )
				                                           .withEsiDataProvider( this.esiDataProvider )
				                                           .withLocationCatalogService( this.locationCatalogService )
				                                           .withCredentialRepository( credentialRepositoryLocal )
				                                           .withAssetRepository( this.assetRepository )
				                                           .withMiningRepository( this.miningRepository )
				                                           .withSchedulerConfiguration( this.schedulerConfiguration )
				                                           .withBackendService( backendServiceLocal )
				                                           .withName( "-TEST-APPLICATION-NAME-" )
				                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
				                                           .build();
		dataSource.collaborate2Model();
		// Assertions
		Assertions.assertEquals( 0, dataSource.getHeaderModelRoot().size() );
		Assertions.assertEquals( 4, dataSource.getDataModelRoot().size() );
		Assertions.assertEquals( PanelTitle.class, dataSource.getDataModelRoot().get( 0 ).getClass() );
		Assertions.assertEquals( credential.getClass(), dataSource.getDataModelRoot().get( 1 ).getClass() );
	}

	@Test
	public void collaborate2ModelException() {
		// Given
		final CredentialRepository credentialRepositoryLocal = Mockito.mock( CredentialRepository.class );
		final Credential credential = Mockito.mock( Credential.class );
		final List<Credential> credentialList = new ArrayList<>();
		credentialList.add( credential );
		// When
		Mockito.when( credentialRepositoryLocal.accessAllCredentials() ).thenReturn( credentialList );
		Mockito.when( credential.getRefreshToken() ).thenReturn( "-TEST-REFRESH-DUMMY-TOKEN-" );
		Mockito.when( credential.getAccountName() ).thenReturn( "-TEST-ACCOUNT-NAME-" );
		Mockito.when( credential.getLastUpdateTime() ).thenReturn( DateTime.now() );
		// Test
		final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
				                                           .addIdentifier( 123 )
				                                           .addIdentifier( 345L )
				                                           .addIdentifier( "-TEST-IDENTIFIER-" )
				                                           .withExtras( null )
				                                           .withVariant( PageNamesType.SPLASH.name() )
				                                           .withFactory( this.factory )
				                                           .withConfigurationService( this.configurationService )
				                                           .withEsiDataProvider( this.esiDataProvider )
				                                           .withLocationCatalogService( this.locationCatalogService )
				                                           .withCredentialRepository( credentialRepositoryLocal )
				                                           .withAssetRepository( this.assetRepository )
				                                           .withMiningRepository( this.miningRepository )
				                                           .withSchedulerConfiguration( this.schedulerConfiguration )
				                                           .withBackendService( this.backendService )
				                                           .withName( "-TEST-APPLICATION-NAME-" )
				                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
				                                           .build();
		dataSource.collaborate2Model();
		// Assertions
		Assertions.assertEquals( 0, dataSource.getHeaderModelRoot().size() );
		Assertions.assertEquals( 5, dataSource.getDataModelRoot().size() );
		Assertions.assertEquals( PanelTitle.class, dataSource.getDataModelRoot().get( 0 ).getClass() );
		Assertions.assertEquals( ExceptionReport.class, dataSource.getDataModelRoot().get( 2 ).getClass() );
	}
}
