package org.dimensinfin.eveonline.neocom.test.support;

import android.content.Intent;
import android.os.Bundle;
import androidx.test.core.app.ApplicationProvider;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;

import org.dimensinfin.eveonline.neocom.core.domain.EExtras;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;

public class AuthorizedStep extends StepFramework {
	protected Credential credential;

	public AuthorizedStep( final NeoComWorld world ) {
		super( world );
	}

	public void initializeCredential() {
		this.credential = new Credential.Builder( 93813310 )
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
	}


	public Intent authorizationIntent( final Class destinationActivity ) {
		final Intent authorizationIntent = new Intent( ApplicationProvider.getApplicationContext(), destinationActivity );
		Bundle bundle = new Bundle();
		bundle.putInt( EExtras.CAPSULEER_IDENTIFIER.name(), 2113197470 );
		authorizationIntent.putExtras( bundle );
		return authorizationIntent;
	}

	public void connectAuthorizedMockComponents() {
		this.initializeCredential();
		// Mock the credential information for credential dependent activities.
		final CredentialRepository credentialRepository = Mockito.mock( CredentialRepository.class );
		final List<Credential> credentialList = new ArrayList<>();
		credentialList.add( this.credential );
		Mockito.when( credentialRepository.findAllByServer( Mockito.anyString() ) )
		       .thenReturn( credentialList );
		NeoComComponentFactory.getSingleton().getESIDataProvider();
		NeoComComponentFactory.getSingleton().replaceCredentialRepository( credentialRepository );
	}
}
