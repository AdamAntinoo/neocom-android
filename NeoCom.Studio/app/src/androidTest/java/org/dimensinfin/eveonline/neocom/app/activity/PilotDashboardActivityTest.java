package org.dimensinfin.eveonline.neocom.app.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.rule.GrantPermissionRule;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import org.dimensinfin.android.mvc.activity.IPagerFragment;
import org.dimensinfin.android.mvc.activity.MVCMultiPageActivity;
import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.controller.PilotSectionController;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;
import org.dimensinfin.eveonline.neocom.support.AcceptanceNeoComLogger;

public class PilotDashboardActivityTest {
	static {
		System.setProperty( "org.mockito.android.target", ApplicationProvider.getApplicationContext().getCacheDir().getPath() );
	}

	@Rule
	public GrantPermissionRule permissionInternet = GrantPermissionRule.grant( Manifest.permission.INTERNET );
	@Rule
	public GrantPermissionRule permissionNetworkState = GrantPermissionRule.grant( Manifest.permission.ACCESS_NETWORK_STATE );
	@Rule
	public GrantPermissionRule permissionWriteStorage = GrantPermissionRule.grant( Manifest.permission.WRITE_EXTERNAL_STORAGE );

	@Before
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

	@Test
	public void checkAssetsSectionActivatesAssetDsiplayActivity() {
		// Create the plot identification intent.
		final Intent intent = new Intent( ApplicationProvider.getApplicationContext(), PilotDashboardActivity.class );
		Bundle bundle = new Bundle();
		bundle.putInt( EExtras.CAPSULEER_IDENTIFIER.name(), 93813310 );
		intent.putExtras( bundle );
		// Start the activity test.
		try (ActivityScenario<PilotDashboardActivity> scenario = ActivityScenario.launch( intent )) {
			scenario.onActivity( ( activity ) -> {
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
//				Espresso.onView( ViewMatchers.withId( R.id.login_button)).perform( ViewActions.click());

//				Espresso.onData( hasToString( startsWith( "ASSETS" ) ) )
//				        .inAdapterView( withId( R.id.myListView ) )
//				        .perform( click() );
//				Espresso.onData( withItemValue("ASSETS"))
//				        .inAdapterView(ViewMatchers.withId( org.dimensinfin.android.mvc.R.id.listContainer))
//				        .perform( checkRender());

//				intended( hasComponent( AssetsManagerActivity.class.getName() ) );
			} );
		}
	}

	public static Matcher<Object> withItemValue( final String value ) {
		return new BoundedMatcher<Object, PilotSectionController>( PilotSectionController.class ) {
			@Override
			public void describeTo( Description description ) {
				description.appendText( "has value " + value );
			}

			@Override
			public boolean matchesSafely( final PilotSectionController item ) {
				return item.getModel().getTitle().equalsIgnoreCase( value );
			}
		};
	}

	public static ViewActions checkRender() {
		return null;
	}

	protected View searchPanelByClass( final Class filterClass, final ListView container ) {
		final int contentCount = container.getChildCount();
		for (int i = 0; i < contentCount; i++) {
			final View target = this.getViewByPosition( i, container );
			final IAndroidController controller = (IAndroidController) target.getTag();
			final Object model = controller.getModel();
			if (model.getClass() == filterClass) return target;
		}
		return null;
	}

	protected android.view.View getViewByPosition( int position, ListView listView ) {
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

		if (position < firstListItemPosition || position > lastListItemPosition) {
			return listView.getAdapter().getView( position, listView.getChildAt( position ), listView );
		} else {
			final int childIndex = position - firstListItemPosition;
			return listView.getChildAt( childIndex );
		}
	}

	public static class ActivityResolver<A extends MVCMultiPageActivity, F extends IPagerFragment> {
		public F fragmentResolver( A activity, int pageNumber ) {
			return (F) activity.accessPageAdapter().getItem( pageNumber );
		}
	}
}

//final class Ristretto {
//	public static ControllerInteraction onController( final Matcher<AndroidController<ICollaboration>> controllerMatcher ) {
//		return BASE.plus( new ViewInteractionModule( viewMatcher ) ).viewInteraction();
//	}
//}

final class ControllerMatcher<C extends AndroidController> {
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

//final class ControllerInteraction {
//}
final class PilotDashboardActivityMock extends PilotDashboardActivity {
	@Override
	protected void injectComponents() {
		super.injectComponents();
		this.credential = new Credential.Builder( 93813310 )
				                  .withAccountId( 93813310 )
				                  .withAccountName( "Perico Tuerto" )
				                  .withAccessToken( "P940P9FpVhR8oq2V96D7pbcLzndNWTsAVgVAMt0HE5tJT15zg83MMqfsZhW1yf1XoFn9_IQJN5LrIa3NA90Ifw" )
				                  .withRefreshToken(
						                  "52HSB2sQiYBOrvaPidnxvnc-DIgT7DP5gUoCEOCW4v61dBfHOrCplfuwma0En0eZsLff2L6OJ6csIDTEQhqDmr0iVB6XmuNloTYhTT2Lx-x15j37Oo91jRrbHiC414DMX2nDPz-JGAdPLDtOzG2-4ofHR61rvw7sGY8Z1CnAgdGexAN6M4ZX93D_UWBEvlFd" )
				                  .withDataSource( "tranquility" )
				                  .withScope(
						                  "publicData esi-location.read_location.v1 esi-location.read_ship_type.v1 esi-mail.read_mail.v1 esi-skills.read_skills.v1 esi-skills.read_skillqueue.v1 esi-wallet.read_character_wallet.v1 esi-wallet.read_corporation_wallet.v1 esi-search.search_structures.v1 esi-clones.read_clones.v1 esi-universe.read_structures.v1 esi-assets.read_assets.v1 esi-planets.manage_planets.v1 esi-fittings.read_fittings.v1 esi-industry.read_character_jobs.v1 esi-markets.read_character_orders.v1 esi-characters.read_blueprints.v1 esi-contracts.read_character_contracts.v1 esi-clones.read_implants.v1 esi-wallet.read_corporation_wallets.v1 esi-characters.read_notifications.v1 esi-corporations.read_divisions.v1 esi-assets.read_corporation_assets.v1 esi-corporations.read_blueprints.v1 esi-contracts.read_corporation_contracts.v1 esi-industry.read_corporation_jobs.v1 esi-markets.read_corporation_orders.v1 esi-industry.read_character_mining.v1 esi-industry.read_corporation_mining.v1" )
				                  .withAssetsCount( 1476 )
				                  .withWalletBalance( 6.309543632E8 )
				                  .withRaceName( "Amarr" )
				                  .build();
		this.credentialRepository = Mockito.mock( CredentialRepository.class );
		final List<Credential> credentialList = new ArrayList<>();
		credentialList.add( credential );
		Mockito.when( this.credentialRepository.findAllByServer( Mockito.anyString() ) )
		       .thenReturn( credentialList );
	}
}

final class CheckRender implements ViewAction {

	@Override
	public Matcher<View> getConstraints() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public void perform( final UiController uiController, final View view ) {

	}
}
