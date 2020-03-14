package org.dimensinfin.eveonline.neocom.test.steps;

import android.Manifest;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.GrantPermissionRule;

import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.test.support.converters.CucumberTable2CredentialConverter;
import org.junit.Rule;

import org.dimensinfin.eveonline.neocom.test.support.AcceptanceNeoComLogger;
import org.dimensinfin.eveonline.neocom.test.support.AuthorizedStep;
import org.dimensinfin.eveonline.neocom.test.support.NeoComWorld;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cucumber.api.java.en.Given;

public class NC06MiningLedger extends AuthorizedStep {
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

	public NC06MiningLedger( final NeoComWorld world ) {
		super( world );
	}
	public void initializeScenario() {
		AcceptanceNeoComLogger.info( "[BEFORE] initializeScenario" );
//		NeoComComponentFactory.getSingleton().getESIDataProvider(); // Probably to run the initialization code for family data. N/A now.
	}

	@Given("the MiningLegderActivity")
	public void the_MiningLegderActivity()  {
		this.initializeScenario();
		AcceptanceNeoComLogger.info( "[GIVEN] the MiningLegderActivity" );
//		this.authorizationIntent = this.authorizationIntent( PilotDashboardActivity.class );
	}
	@Given("the following MiningExtractionEntity into my repository")
	public void the_following_MiningExtractionEntity_into_my_repository( final List<Map<String, String>> dataTable ) throws SQLException {
		AcceptanceNeoComLogger.info( "[GIVEN] the following MiningExtractionEntity into my repository" );
		for (Map<String, String> row : dataTable) {
			final Credential credential = new CucumberTable2MiningExtractionEntityConverter().convert( row );
			this.world.getCredentialRepository().persist( credential );
		}
	}
}
