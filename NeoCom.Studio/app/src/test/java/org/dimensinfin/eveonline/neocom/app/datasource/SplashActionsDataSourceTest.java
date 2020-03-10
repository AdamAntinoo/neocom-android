package org.dimensinfin.eveonline.neocom.app.datasource;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.dimensinfin.android.mvc.controller.ControllerFactory;
import org.dimensinfin.android.mvc.exception.ExceptionReport;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.eveonline.neocom.adapter.LocationCatalogService;
import org.dimensinfin.eveonline.neocom.app.domain.AppVersion;
import org.dimensinfin.eveonline.neocom.app.domain.PanelTitle;
import org.dimensinfin.eveonline.neocom.backend.NeoComBackendService;
import org.dimensinfin.eveonline.neocom.core.domain.PageNamesType;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.AssetRepository;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;
import org.dimensinfin.eveonline.neocom.database.repositories.MiningRepository;
import org.dimensinfin.eveonline.neocom.provider.ESIDataProvider;
import org.dimensinfin.eveonline.neocom.provider.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.scheduler.ISchedulerConfiguration;

class SplashActionsDataSourceTest {
	private Bundle extras;
	private ControllerFactory factory;
	private SplashActionsDataSource dataSource4Test;
	private CredentialRepository credentialRepository;

	@BeforeEach
	void setUp() {
		this.extras = new Bundle();
		this.factory = Mockito.mock( ControllerFactory.class );
		this.credentialRepository = Mockito.mock( CredentialRepository.class );
		final Bundle extras = new Bundle();
		final ControllerFactory factory = Mockito.mock( ControllerFactory.class );
		final ESIDataProvider esiDataProvider = Mockito.mock( ESIDataProvider.class );
		final AssetRepository assetRepository = Mockito.mock( AssetRepository.class );
		final CredentialRepository credentialRepository = Mockito.mock( CredentialRepository.class );
		final LocationCatalogService locationCatalogService = Mockito.mock( LocationCatalogService.class );
		this.dataSource4Test = new SplashActionsDataSource.Builder()
				                       .addIdentifier( 123 )
				                       .addIdentifier( 345L )
				                       .addIdentifier( "-TEST-IDENTIFIER-" )
				                       .withExtras( extras )
				                       .withVariant( PageNamesType.SPLASH.name() )
				                       .withFactory( factory )
				                       .withEsiDataProvider( esiDataProvider )
				                       .withAssetRepository( assetRepository )
				                       .withCredentialRepository( credentialRepository )
				                       .withLocationCatalogService( locationCatalogService )
				                       .withName( "-TEST-APPLICATION-NAME-" )
				                       .withVersion( "-TEST-APPLICATION-VERSION-" )
				                       .build();
		Assertions.assertNotNull( this.dataSource4Test );
	}

	@Test
	void buildComplete() {
		final Bundle extras = new Bundle();
		final ControllerFactory factory = Mockito.mock( ControllerFactory.class );
		final IConfigurationProvider configurationService = Mockito.mock( IConfigurationProvider.class );
		final ESIDataProvider esiDataProvider = Mockito.mock( ESIDataProvider.class );
		final LocationCatalogService locationCatalogService = Mockito.mock( LocationCatalogService.class );
		final CredentialRepository credentialRepository = Mockito.mock( CredentialRepository.class );
		final AssetRepository assetRepository = Mockito.mock( AssetRepository.class );
		final MiningRepository miningRepository = Mockito.mock( MiningRepository.class );
		final ISchedulerConfiguration schedulerConfiguration = Mockito.mock( ISchedulerConfiguration.class );
		final NeoComBackendService backendService = Mockito.mock( NeoComBackendService.class );
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
				                                           .withBackendService( backendService )
				                                           .withName( "-TEST-APPLICATION-NAME-" )
				                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
				                                           .build();
		Assertions.assertNotNull( dataSource );
	}

	@Test
	void prepareModel() {
		final ESIDataProvider esiDataProvider = Mockito.mock( ESIDataProvider.class );
		final AssetRepository assetRepository = Mockito.mock( AssetRepository.class );
		final CredentialRepository credentialRepository = Mockito.mock( CredentialRepository.class );
		final LocationCatalogService locationCatalogService = Mockito.mock( LocationCatalogService.class );
		final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
				                                           .addIdentifier( 123 )
				                                           .addIdentifier( 345L )
				                                           .addIdentifier( "-TEST-IDENTIFIER-" )
				                                           .withExtras( extras )
				                                           .withVariant( PageNamesType.SPLASH.name() )
				                                           .withFactory( factory )
				                                           .withEsiDataProvider( esiDataProvider )
				                                           .withAssetRepository( assetRepository )
				                                           .withCredentialRepository( credentialRepository )
				                                           .withLocationCatalogService( locationCatalogService )
				                                           .withName( "-TEST-APPLICATION-NAME-" )
				                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
				                                           .build();
		dataSource.prepareModel();
		Assertions.assertEquals( 1, dataSource.getHeaderModelRoot().size() );
		final ICollaboration item = dataSource.getHeaderModelRoot().get( 0 );
		Assertions.assertTrue( item instanceof AppVersion );
	}

	@Test
	public void collaborate2Model() {
		final Bundle extras = new Bundle();
		final ControllerFactory factory = Mockito.mock( ControllerFactory.class );
		final ESIDataProvider esiDataProvider = Mockito.mock( ESIDataProvider.class );
		final AssetRepository assetRepository = Mockito.mock( AssetRepository.class );
		final Credential credential = Mockito.mock( Credential.class );
		Mockito.when( credential.getRefreshToken() ).thenReturn( "-TEST-REFRESH-DUMMY-TOKEN-" );
		Mockito.when( credential.getAccountName() ).thenReturn( "-TEST-ACCOUNT-NAME-" );
		final List<Credential> credentialList = new ArrayList<>();
		credentialList.add( credential );
		final CredentialRepository credentialRepository = Mockito.mock( CredentialRepository.class );
		Mockito.when( credentialRepository.accessAllCredentials() ).thenReturn( credentialList );
		final LocationCatalogService locationCatalogService = Mockito.mock( LocationCatalogService.class );
		final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
				                                           .addIdentifier( 123 )
				                                           .addIdentifier( 345L )
				                                           .addIdentifier( "-TEST-IDENTIFIER-" )
				                                           .withExtras( extras )
				                                           .withVariant( PageNamesType.SPLASH.name() )
				                                           .withFactory( factory )
				                                           .withEsiDataProvider( esiDataProvider )
				                                           .withAssetRepository( assetRepository )
				                                           .withCredentialRepository( credentialRepository )
				                                           .withLocationCatalogService( locationCatalogService )
				                                           .withName( "-TEST-APPLICATION-NAME-" )
				                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
				                                           .build();
		dataSource.collaborate2Model();

		Assertions.assertEquals( 0, dataSource.getHeaderModelRoot().size() );
		Assertions.assertEquals( 4, dataSource.getDataModelRoot().size() );
		Assertions.assertEquals( PanelTitle.class, dataSource.getDataModelRoot().get( 0 ).getClass() );
		Assertions.assertEquals( credential.getClass(), dataSource.getDataModelRoot().get( 1 ).getClass() );
	}

	@Test
	public void collaborate2ModelException() {
		final Bundle extras = new Bundle();
		final ControllerFactory factory = Mockito.mock( ControllerFactory.class );
		final ESIDataProvider esiDataProvider = Mockito.mock( ESIDataProvider.class );
		final AssetRepository assetRepository = Mockito.mock( AssetRepository.class );
		final Credential credential = Mockito.mock( Credential.class );
		Mockito.when( credential.getRefreshToken() ).thenReturn( "-TEST-REFRESH-DUMMY-TOKEN-" );
		Mockito.when( credential.getAccountName() ).thenReturn( "-TEST-ACCOUNT-NAME-" );
		final List<Credential> credentialList = new ArrayList<>();
		credentialList.add( credential );
		final CredentialRepository credentialRepository = Mockito.mock( CredentialRepository.class );
		Mockito.when( credentialRepository.accessAllCredentials() ).thenThrow( new RuntimeException( "-TEST-RUNTIME-EXCEPTION-" ) );
		final LocationCatalogService locationCatalogService = Mockito.mock( LocationCatalogService.class );
		final SplashActionsDataSource dataSource = new SplashActionsDataSource.Builder()
				                                           .addIdentifier( 123 )
				                                           .addIdentifier( 345L )
				                                           .addIdentifier( "-TEST-IDENTIFIER-" )
				                                           .withExtras( extras )
				                                           .withVariant( PageNamesType.SPLASH.name() )
				                                           .withFactory( factory )
				                                           .withEsiDataProvider( esiDataProvider )
				                                           .withAssetRepository( assetRepository )
				                                           .withCredentialRepository( credentialRepository )
				                                           .withLocationCatalogService( locationCatalogService )
				                                           .withName( "-TEST-APPLICATION-NAME-" )
				                                           .withVersion( "-TEST-APPLICATION-VERSION-" )
				                                           .build();
		dataSource.collaborate2Model();

		Assertions.assertEquals( 0, dataSource.getHeaderModelRoot().size() );
		Assertions.assertEquals( 4, dataSource.getDataModelRoot().size() );
		Assertions.assertEquals( PanelTitle.class, dataSource.getDataModelRoot().get( 0 ).getClass() );
		Assertions.assertEquals( ExceptionReport.class, dataSource.getDataModelRoot().get( 1 ).getClass() );
	}
}
