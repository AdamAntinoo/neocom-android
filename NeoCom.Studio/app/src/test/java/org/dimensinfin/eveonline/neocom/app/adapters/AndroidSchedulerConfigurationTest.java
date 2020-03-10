package org.dimensinfin.eveonline.neocom.app.adapters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.dimensinfin.eveonline.neocom.provider.IPreferencesProvider;

public class AndroidSchedulerConfigurationTest {
	@Test
	public void buildComplete() {
		final IPreferencesProvider preferencesProvider = Mockito.mock( IPreferencesProvider.class );
		final AndroidSchedulerConfiguration androidSchedulerConfiguration = new AndroidSchedulerConfiguration.Builder()
				                                                                    .withPreferencesProvider( preferencesProvider ).build();
		Assertions.assertNotNull( androidSchedulerConfiguration );
	}

	@Test
	public void buildFailure() {
		final IPreferencesProvider preferencesProvider = Mockito.mock( IPreferencesProvider.class );
		Assertions.assertThrows( NullPointerException.class, () -> {
			final AndroidSchedulerConfiguration androidSchedulerConfiguration = new AndroidSchedulerConfiguration.Builder()
					                                                                    .withPreferencesProvider( null ).build();
		} );
		Assertions.assertThrows( NullPointerException.class, () -> {
			final AndroidSchedulerConfiguration androidSchedulerConfiguration = new AndroidSchedulerConfiguration.Builder().build();
		} );
	}

	@Test
	public void getAllowedToRun() {
		final IPreferencesProvider preferencesProvider = Mockito.mock( IPreferencesProvider.class );
		final AndroidSchedulerConfiguration androidSchedulerConfiguration = new AndroidSchedulerConfiguration.Builder()
				                                                                    .withPreferencesProvider( preferencesProvider ).build();
		Mockito.when( preferencesProvider.getBooleanPreference( Mockito.anyString() ) ).thenReturn( true );
		Assertions.assertTrue( androidSchedulerConfiguration.getAllowedToRun() );
	}

	@Test
	public void getAllowedMiningExtractions() {
		final IPreferencesProvider preferencesProvider = Mockito.mock( IPreferencesProvider.class );
		final AndroidSchedulerConfiguration androidSchedulerConfiguration = new AndroidSchedulerConfiguration.Builder()
				                                                                    .withPreferencesProvider( preferencesProvider ).build();
		Mockito.when( preferencesProvider.getBooleanPreference( Mockito.anyString() ) ).thenReturn( false );
		Assertions.assertFalse( androidSchedulerConfiguration.getAllowedMiningExtractions() );
	}

	@Test
	public void getAllowedAssets() {
		final IPreferencesProvider preferencesProvider = Mockito.mock( IPreferencesProvider.class );
		final AndroidSchedulerConfiguration androidSchedulerConfiguration = new AndroidSchedulerConfiguration.Builder()
				                                                                    .withPreferencesProvider( preferencesProvider ).build();
		Mockito.when( preferencesProvider.getBooleanPreference( Mockito.anyString() ) ).thenReturn( false );
		Assertions.assertFalse( androidSchedulerConfiguration.getAllowedAssets() );
	}
}
