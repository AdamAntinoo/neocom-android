package org.dimensinfin.eveonline.neocom.app.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Browser;
import android.support.annotation.WorkerThread;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.sql.SQLException;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import org.dimensinfin.android.mvc.core.MVCScheduler;
import org.dimensinfin.android.mvc.exception.ExceptionRenderGenerator;
import org.dimensinfin.android.mvc.render.RenderViewGenerator;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.annotation.LogEnterExit;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.app.domain.WarningPanel;
import org.dimensinfin.eveonline.neocom.app.factory.AppControllerFactory;
import org.dimensinfin.eveonline.neocom.auth.NeoComOAuth2Flow;
import org.dimensinfin.eveonline.neocom.auth.TokenVerification;
import org.dimensinfin.eveonline.neocom.core.domain.EExtras;
import org.dimensinfin.eveonline.neocom.core.domain.PageNamesType;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;
import org.dimensinfin.eveonline.neocom.provider.ESIDataProvider;
import org.dimensinfin.eveonline.neocom.service.logger.NeoComLogger;
import org.dimensinfin.eveonline.neocom.updater.CredentialUpdater;

/**
 * Activity entry points are <b>onCreate()</b> and <b>onNewIntent()</b>. The difference is that onNewIntent is called when the Activity
 * already exists and it should only be brought to the front. So I should differentiate entry phases if used with some key data on the
 * Extras and not only expecting the right <b>ACTION</b>.
 *
 * We enter this Activity at least from two points. One by a direct intent call from the <code>NewLoginPart</code> and the other when the
 * Oauth ESI authorization flow completes and the registered Developer Application redirects the browser to this activity. On that case
 * we should have a differentiate action verb that is signaled as <b>ACTION_VIEW</b>
 *
 * @author Adam Antinoo
 */
public class AuthorizationFlowActivity extends Activity {
	//	private static final String V1_OAUTH = "oauth/authorize/";
//	private static final String V2_OAUTH = "v2/oauth/authorize/";
//	private static final String LOGIN_URL = "https://login.eveonline.com/" + V1_OAUTH +
//			                                        "?response_type=code&" +
//			                                        "redirect_uri=eveauth-neocom%3A%2F%2Fesiauthentication&" +
//			                                        "scope=publicData esi-location.read_location.v1 esi-location.read_ship_type.v1 esi-mail.read_mail.v1 esi-skills.read_skills.v1 esi-skills.read_skillqueue.v1 esi-wallet.read_character_wallet.v1 esi-wallet.read_corporation_wallet.v1 esi-search.search_structures.v1 esi-clones.read_clones.v1 esi-universe.read_structures.v1 esi-assets.read_assets.v1 esi-planets.manage_planets.v1 esi-fittings.read_fittings.v1 esi-industry.read_character_jobs.v1 esi-markets.read_character_orders.v1 esi-characters.read_blueprints.v1 esi-contracts.read_character_contracts.v1 esi-clones.read_implants.v1 esi-wallet.read_corporation_wallets.v1 esi-characters.read_notifications.v1 esi-corporations.read_divisions.v1 esi-assets.read_corporation_assets.v1 esi-corporations.read_blueprints.v1 esi-contracts.read_corporation_contracts.v1 esi-industry.read_corporation_jobs.v1 esi-markets.read_corporation_orders.v1 esi-industry.read_character_mining.v1 esi-industry.read_corporation_mining.v1";
//	private static final String DATASOURCE_PREFERENCE = "prefkey_dataSourceList";
//	private static final ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();
//	private static Logger logger = LoggerFactory.getLogger( AuthorizationFlowActivity.class );

	private ImageView spinner;
	private LinearLayout notifications;
	private Bundle extras;

	// -  C O M P O N E N T S
	private SharedPreferences preferences;
	//	private IConfigurationService configurationProvider;
//	private ESIDataProvider esiDataAdapter;
	private NeoComOAuth2Flow oauthFlow;
	private CredentialRepository credentialRepository;

	/**
	 * Initiates a call to the CCP authentication platform. If the authorization is accepted by CCP the return event will be converted
	 * into another intent to this activity.
	 * This firing should be launched only if we come from the <code>NewLoginPart</code> part.
	 */
	protected void onFireAuth() {
		this.runOnUiThread( () -> {
			final Uri url = Uri.parse( this.oauthFlow.generateLoginUrl() );
			final Intent intent = new Intent( Intent.ACTION_VIEW, url );
			intent.putExtra( Browser.EXTRA_APPLICATION_ID, getApplicationContext().getPackageName() );
			startActivity( intent );
		} );
	}


	/**
	 * Performs the actions after the creation of the Activity. It will locate the UI elements, setup their contents ans start the spinner.
	 */
	protected void connectUI() {
		// If we are not receiving the code from CCP we should then create the Activity contents.
		// Set the layout to the core activity that defines the background, the indicator and the fragment container.
		this.setContentView( R.layout.activity_loginprogress );
		// Gets the activity's default ActionBar
		final ActionBar actionBar = this.getActionBar();
		actionBar.show();
		actionBar.setDisplayHomeAsUpEnabled( false );

		// Locate the elements of the page and store in global data.
		this.spinner = Objects.requireNonNull( this.findViewById( R.id.spinner ) );
		this.notifications = Objects.requireNonNull( this.findViewById( R.id.notifications ) );

		// Start the spinner.
		Animation rotation = AnimationUtils.loadAnimation( this, R.anim.clockwise_rotation );
		rotation.setRepeatCount( Animation.INFINITE );
		this.spinner.startAnimation( rotation );
	}
	// - A C T I V I T Y   L I F E C Y C L E

	/**
	 * Create a default Activity that shows the authorization progress while we wait for the ESI CCP callback or while we do the rest of the
	 * authorization cycle until finally complete a new Credential.
	 * Do the firing only when the extras have the right data configuration.
	 */
	@LogEnterExit
	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		NeoComLogger.enter();
		try {
			super.onCreate( savedInstanceState );
			this.injectComponents(); // Inject components.
			this.connectUI();
			final Intent intent = Objects.requireNonNull( this.resolveExtras() );
			final String tag = this.extras.getString( EExtras.FIRE_AUTHORIZATION_FLOW.name() );
			if ((null != tag) && (tag.equalsIgnoreCase( EExtras.FIRE_AUTHORIZATION_FLOW.name() ))) {
				// Fire the credential authorization flow on a background task.
				NeoComLogger.info( "Launching the ESI login request." );
				MVCScheduler.backgroundExecutor.submit( this::onFireAuth );
			}
			if (Intent.ACTION_VIEW.equals( intent.getAction() )) {
				NeoComLogger.info( "Verify this is the other activity launch action." );
				if (null != intent.getData()) {
					// Get the state and verify we are the caller.
					final Uri urlData = intent.getData();
					final String state = urlData.getQueryParameter( "state" );
					if (this.oauthFlow.verifyState( state )) {
						final String authCode = urlData.getQueryParameter( "code" );
						if (StringUtils.isNotBlank( authCode )) {
							// Fire the conversion to the authenticated Credential but do it on background.
							MVCScheduler.backgroundExecutor.submit( () -> {
								this.registerInBackground( authCode, state );
							} );
						}
					}
				}
			}
		} catch (final RuntimeException rtex) {
			rtex.printStackTrace();
			this.showException( rtex ); // Show any exception data on the empty page.
		}
		NeoComLogger.exit();
	}

	private Intent resolveExtras() {
		final Intent intent = this.getIntent();
		if (null != intent) {
			final Bundle extras = this.getIntent().getExtras();
			if (null != extras) {
				this.extras = extras;
				return intent;
			}
		}
		this.notifications.addView( new RenderViewGenerator.Builder<WarningPanel>()
				                            .withContext( this )
				                            .withFactory( new AppControllerFactory( PageNamesType.AUTHORIZATION.name() ) )
				                            .withModel( new WarningPanel.Builder().withType( "WARNING" )
						                                        .withDescription(
								                                        "The request has not the data required. Unable to request authentication to Esi server." )
						                                        .build() )
				                            .generateView() );
		return null;
	}

	/**
	 * This is called when the Activity responds to a new request as selected by the OS. For example when a callback
	 * from CCP is issued to a host/path it gets intercepted by the AndroidManifest and then passed to the matching
	 * Activity, entering with the <code>onNewIntent</code> instead of using the <code>onCreate</code>.
	 *
	 * Handles VIEW requests that com from the authentication CCP site. Verify the data is for us and then call the
	 * registration process to create a new Credential entry that gets stored at the database.
	 */
	protected void onNewIntent( final Intent intent ) {
		NeoComLogger.enter();
		super.onNewIntent( intent );
		try {
			this.injectComponents(); // Inject components.
			this.connectUI();
			if (null != intent) {
				final Bundle extras = this.getIntent().getExtras();
				// Check that we have the tag identifier to detect if we are reaching this point from the Part.
				final String tag = Objects.requireNonNull( Objects.requireNonNull( extras ).getString( EExtras.FIRE_AUTHORIZATION_FLOW.name() ) );
				if (tag.equalsIgnoreCase( "NEW-LOGIN-PART" )) {
					// Fire the credential authorization flow on a background task.
					MVCScheduler.backgroundExecutor.submit( this::onFireAuth );
				} else {
					if (Intent.ACTION_VIEW.equals( intent.getAction() )) {
						if (null == intent.getData()) {
						} else {
							// Get the state and verify we are the caller.
							final Uri urlData = intent.getData();
							final String state = urlData.getQueryParameter( "state" );
							if (Objects.requireNonNull( state ).equals( "NEOCOM-VERIFICATION-STATE" )) {
								final String authCode = urlData.getQueryParameter( "code" );
								if (StringUtils.isNotBlank( authCode )) {
									// Fire the conversion to the authenticated Credential but do it on background.
									MVCScheduler.backgroundExecutor.submit( () -> {
										this.registerInBackground( authCode, state );
									} );
								}
							}
						}
					}
				}
			}
		} catch (final RuntimeException rtex) {
			rtex.printStackTrace();
			this.showException( rtex ); // Show any exception data on the empty page.
		}
		NeoComLogger.exit();
	}

	protected void showException( final Exception exception ) {
		final ViewGroup exceptionContainer = this.findViewById( R.id.exceptionContainer );
		if (null != exceptionContainer) {
			final View exceptionView = new ExceptionRenderGenerator.Builder( exception )
					                           .withContext( this )
					                           .withFactory( new AppControllerFactory( "-DEFAULT-" ) )
					                           .build().getView();
			exceptionContainer.removeAllViews();
			exceptionContainer.addView( exceptionView );
			exceptionContainer.setVisibility( View.VISIBLE );
		}
	}

	/**
	 * Registering by converting the one use authorization token into a usable token that will bet stored with the
	 * credential.
	 *
	 * @param authCode the authorization code received from the Eve Online Developer Application site.
	 */
	@LogEnterExit
	@WorkerThread
	private void registerInBackground( final String authCode, final String state ) {
		NeoComLogger.enter();
		NeoComLogger.info( "Creating access token request..." );
		// Get from the properties the user selected esi source.
		final String esiServer = this.preferences.getString( "prefkey_dataSourceList", ESIDataProvider.DEFAULT_ESI_SERVER ).toLowerCase();
		this.oauthFlow.onStartFlow( authCode, state, esiServer );
		if (this.oauthFlow.verifyState( state )) {
			final TokenVerification tokenStore = this.oauthFlow.onTranslationStep();
			NeoComLogger.info( "Character verification OK." );
			// Create the credential and store it on the application repository.
			this.credentialRepository = NeoComComponentFactory.getSingleton().getCredentialRepository();
			final int newAccountIdentifier = Long.valueOf( tokenStore.getVerifyCharacterResponse().getCharacterID() ).intValue();
			final Credential credential = new Credential.Builder( newAccountIdentifier )
					                              .withAccountName( tokenStore.getVerifyCharacterResponse().getCharacterName() )
					                              .withTokenType( tokenStore.getTokenTranslationResponse().getTokenType() )
					                              .withAccessToken( tokenStore.getTokenTranslationResponse().getAccessToken() )
					                              .withRefreshToken( tokenStore.getTokenTranslationResponse().getRefreshToken() )
					                              .withDataSource( esiServer )
					                              .withScope( tokenStore.getVerifyCharacterResponse().getScopes() )
					                              .build();
			new CredentialUpdater( credential ).onRun();
			try {
				this.credentialRepository.persist( credential );
				NeoComLogger.info( "Credential #{}-{} created successfully.",
						credential.getAccountId() + "", credential.getAccountName() );
			} catch (final SQLException sqle) {
				sqle.printStackTrace();
			}
			// Once the credential is stored we go back to the Dashboard Activity.
			this.runOnUiThread( () -> {
				this.startActivity( new Intent( this, AppSplashActivity.class ) );
			} );
		}
	}

	private void injectComponents() {
//		this.configurationProvider = NeoComComponentFactory.getSingleton().getConfigurationService();
		this.preferences = PreferenceManager.getDefaultSharedPreferences( this );
//		this.esiDataAdapter = NeoComComponentFactory.getSingleton().getESIDataProvider();
		this.oauthFlow = new NeoComOAuth2Flow.Builder()
				                 .withConfigurationService( NeoComComponentFactory.getSingleton().getConfigurationService() )
				                 .build();
		this.credentialRepository = NeoComComponentFactory.getSingleton().getCredentialRepository();
	}
}
