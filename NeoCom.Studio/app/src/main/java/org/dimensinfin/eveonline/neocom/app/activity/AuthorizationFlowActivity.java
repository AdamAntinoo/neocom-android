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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.app.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.auth.NeoComOauth2Flow;
import org.dimensinfin.eveonline.neocom.auth.TokenVerification;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;
import org.dimensinfin.eveonline.neocom.provider.ESIDataProvider;
import org.dimensinfin.eveonline.neocom.provider.IConfigurationProvider;
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
	private static final String LOGIN_URL = "https://login.eveonline.com/v2/oauth/authorize/?response_type=code&client_id" +
			                                        "=dbc9c2b1d18d49d8adacd23436c5281d&state" +
			                                        "=NEOCOM-VERIFICATION-STATE&redirect_uri=eveauth-neocom%3A%2F%2Fesiauthentication&scope=publicData esi-location.read_location.v1 esi-location.read_ship_type.v1 esi-mail.read_mail.v1 esi-skills.read_skills.v1 esi-skills.read_skillqueue.v1 esi-wallet.read_character_wallet.v1 esi-wallet.read_corporation_wallet.v1 esi-search.search_structures.v1 esi-clones.read_clones.v1 esi-universe.read_structures.v1 esi-assets.read_assets.v1 esi-planets.manage_planets.v1 esi-fittings.read_fittings.v1 esi-industry.read_character_jobs.v1 esi-markets.read_character_orders.v1 esi-characters.read_blueprints.v1 esi-contracts.read_character_contracts.v1 esi-clones.read_implants.v1 esi-wallet.read_corporation_wallets.v1 esi-characters.read_notifications.v1 esi-corporations.read_divisions.v1 esi-assets.read_corporation_assets.v1 esi-corporations.read_blueprints.v1 esi-contracts.read_corporation_contracts.v1 esi-industry.read_corporation_jobs.v1 esi-markets.read_corporation_orders.v1 esi-industry.read_character_mining.v1 esi-industry.read_corporation_mining.v1";
	private static final String DATASOURCE_PREFERENCE = "prefkey_dataSourceList";
	private static final ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();
	private static Logger logger = LoggerFactory.getLogger( AuthorizationFlowActivity.class );
	private SharedPreferences preferences;
	private IConfigurationProvider configurationProvider;
	private ESIDataProvider esiDataAdapter;
	private ImageView spinner = null;

	/**
	 * Initiates a call to the CCP authentication platform. If the authorization is accepted by CCP the return event will be converted
	 * into another intent to this activity.
	 * This firing should be launched only if we come from the <code>NewLoginPart</code> part.
	 */
	protected void onFireAuth() {
		this.runOnUiThread( () -> {
			final String esiServer = this.preferences.getString( DATASOURCE_PREFERENCE, ESIDataProvider.DEFAULT_ESI_SERVER );
//			this.esiDataAdapter.activateEsiServer( esiServer );
			final Uri url = Uri.parse( LOGIN_URL );
			final Intent intent = new Intent( Intent.ACTION_VIEW, url );
			intent.putExtra( Browser.EXTRA_APPLICATION_ID, getApplicationContext().getPackageName() );
			startActivity( intent );
		} );
	}
	// - A C T I V I T Y   L I F E C Y C L E

	/**
	 * Registering by converting the one use authorization token into a usable token that will bet stored with the
	 * credential.
	 *
	 * @param authCode the authorization code received from the Eve Online Developer Application site.
	 */
	@WorkerThread
	private void registerInBackground( final String authCode, final String state ) {
		logger.info( ">> [AuthorizationFlowActivity.registerInBackground]" );
		logger.info( "-- [AuthorizationFlowActivity.registerInBackground]> Creating access token request." );
		// Get from the properties the user selected esi source.
		final String esiServer = this.preferences.getString( "prefkey_dataSourceList", ESIDataProvider.DEFAULT_ESI_SERVER ).toLowerCase();
//		try {
		final NeoComOauth2Flow flow = new NeoComOauth2Flow.Builder().withConfigurationProvider( this.configurationProvider ).build();
		flow.onStartFlow( authCode, state, esiServer );
		final TokenVerification tokenStore = flow.onTranslationStep();
		logger.info( "-- [AuthorizationFlowActivity.registerInBackground]> Character verification OK." );
		// Create the credential and store it on the application repository.
		final CredentialRepository credentialRepository = NeoComComponentFactory.getSingleton().getCredentialRepository();
		final int newAccountIdentifier = Long.valueOf( tokenStore.getVerifyCharacterResponse().getCharacterID() ).intValue();
		final Credential credential = new Credential.Builder( newAccountIdentifier )
				                              .withAccountId( newAccountIdentifier )
				                              .withAccountName( tokenStore.getVerifyCharacterResponse().getCharacterName() )
				                              .withTokenType( tokenStore.getTokenTranslationResponse().getTokenType() )
				                              .withAccessToken( tokenStore.getTokenTranslationResponse().getAccessToken() )
				                              .withRefreshToken( tokenStore.getTokenTranslationResponse().getRefreshToken() )
				                              .withDataSource( esiServer )
				                              .withScope( tokenStore.getVerifyCharacterResponse().getScopes() )
				                              .build();
		backgroundExecutor.submit( () -> new CredentialUpdater( credential ).onRun() );
		try {
			credentialRepository.persist( credential );
			logger.info( "-- [AuthorizationFlowActivity.registerInBackground]> Credential #{}-{} created successfully."
					, credential.getAccountId(), credential.getAccountName() );
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Once the credential is stored we go back to the Dashboard Activity.
		this.runOnUiThread( () -> {
			final Intent intent = new Intent( this, AppSplashActivity.class );
			this.startActivity( intent );
		} );

//			// Create a Retrofit request service to encapsulate the call.
//			// TODO - Check that the call dop not require converters
////			final GetAccessToken serviceGetAccessToken = new Retrofit.Builder()
////					                                             .baseUrl( this.configurationProvider.getResourceString( "P.esi."
////							                                                                                                     + esiServer.toLowerCase()
////							                                                                                                     + ".authorization.server" ) )
////					                                             .addConverterFactory( JacksonConverterFactory.create() )
////					                                             .build()
////					                                             .create( GetAccessToken.class );
//			logger.info( "-- [AuthorizationFlowActivity.registerInBackground]> Creating request body." );
//			final TokenRequestBody tokenRequestBody = new TokenRequestBody().setCode( authCode );
//			logger.info( "-- [AuthorizationFlowActivity.registerInBackground]> Creating request call." );
//			final String peckString = this.configurationProvider.getResourceString( "P.esi." + esiServer + ".authorization.clientid" )
//					                          + ":"
//					                          + this.configurationProvider.getResourceString( "P.esi." + esiServer + ".authorization.secretkey" );
//			String peck = Base64.encodeToString( peckString.getBytes(), Base64.DEFAULT );
//			// Reprocess Android result to remove the \n characters.
//			peck = peck.replaceAll( "\n", "" );
//			final Call<TokenTranslationResponse> request = serviceGetAccessToken.getAccessToken(
//					ESIDataProvider.DEFAULT_CONTENT_TYPE,
//					this.configurationProvider.getResourceString( "P.esi.tranquility.authorization.server.url" ),
//					"Basic " + peck,
//					tokenRequestBody );
//
//			// Getting the request response to be stored if valid.
//			final Response<TokenTranslationResponse> response = request.execute();
//			if (response.isSuccessful()) {
//				logger.info( "-- [AuthorizationFlowActivity.registerInBackground]> Response is 200 OK." );
//				final TokenTranslationResponse token = response.body();
//				// Create a security verification instance.
//				OkHttpClient.Builder verifyClient =
//						new OkHttpClient.Builder()
//								.certificatePinner(
//										new CertificatePinner.Builder()
//												.add( "login.eveonline.com", "sha256/5UeWOuDyX7IUmcKnsVdx+vLMkxEGAtzfaOUQT/caUBE=" )
//												.add( "login.eveonline.com", "sha256/980Ionqp3wkYtN9SZVgMzuWQzJta1nfxNPwTem1X0uc=" )
//												.add( "login.eveonline.com", "sha256/du6FkDdMcVQ3u8prumAo6t3i3G27uMP2EOhR8R0at/U=" )
//												.build() )
//								.addInterceptor( chain -> chain.proceed(
//										chain.request()
//										     .newBuilder()
//										     .addHeader( "User-Agent", "org.dimensinfin" )
//										     .build() ) );
//				// Verify the character authenticated and create the Credential.
//				logger.info( "-- [AuthorizationFlowActivity.registerInBackground]> Creating character verification." );
//				final VerifyCharacter verificationService = new Retrofit.Builder()
//						                                            .baseUrl( this.configurationProvider.getResourceString( "P.esi."
//								                                                                                                    + esiServer.toLowerCase()
//								                                                                                                    + ".authorization.server" ) )
//						                                            .addConverterFactory( JacksonConverterFactory.create() )
//						                                            .client( verifyClient.build() )
//						                                            .build()
//						                                            .create( VerifyCharacter.class );
//				final String accessToken = token.getAccessToken();
//				final Response<VerifyCharacterResponse> verificationResponse = verificationService.getVerification(
//						"Bearer " + accessToken ).execute();

//				if (verificationResponse.isSuccessful()) {
//					logger.info( "-- [AuthorizationFlowActivity.registerInBackground]> Character verification OK." );
//					// Create the credential and store it on the application repository.
//					final CredentialRepository credentialRepository = NeoComComponentFactory.getSingleton().getCredentialRepository();
//					final int newAccountIdentifier = Long.valueOf( verificationResponse.body().getCharacterID() ).intValue();
//					final Credential credential = new Credential.Builder( newAccountIdentifier )
//							                              .withAccountId( newAccountIdentifier )
//							                              .withAccountName( verificationResponse.body().getCharacterName() )
//							                              .withTokenType( token.getTokenType() )
//							                              .withAccessToken( token.getAccessToken() )
//							                              .withRefreshToken( token.getRefreshToken() )
//							                              .withDataSource( esiServer )
//							                              .withScope( verificationResponse.body().getScopes() )
//							                              .build();
		// Update the additional render fields.
//					backgroundExecutor.submit( () -> new CredentialUpdater( credential ).onRun() );
//
//					// TODO - Upgrade to the new Credential implementation using a Builder.
//					//					credential.setAccessToken(token.getAccessToken())
//					//							.setTokenType(token.getTokenType())
//					//							//							.setExpires(Instant.now().plus(TimeUnit.SECONDS.toMillis(token.getExpires())).getMillis())
//					//							.setRefreshToken(token.getRefreshToken())
//					//							.setDataSource(esiServer.toUpperCase())
//					//							.setScope(this.esiDataProvider.getStringScopes());
//					try {
//						credentialRepository.persist( credential );
//						logger.info( "-- [AuthorizationFlowActivity.registerInBackground]> Credential #{}-{} created successfully."
//								, credential.getAccountId(), credential.getAccountName() );
//					} catch (SQLException e) {
//						e.printStackTrace();
//					}
//					// Once the credential is stored we go back to the Dashboard Activity.
//					this.runOnUiThread( () -> {
//						final Intent intent = new Intent( this, AppSplashActivity.class );
//						this.startActivity( intent );
//					} );
//				} else {
//					Toast.makeText( getApplicationContext(), "Character verification failed. " + verificationResponse.message(),
//							Toast.LENGTH_LONG ).show();
//				}
//			} else {
//				Toast.makeText( getApplicationContext(), "Authorization token conversion failed. " + response.message(),
//						Toast.LENGTH_LONG ).show();
//			}
//		} catch (IOException ioe) {
//			//			AndroidGlobalDataManager.registerException("AuthorizationFlowActivity.registerInBackground"
//			//					, ioe, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
//			//			ioe.printStackTrace();
//			throw new NeoComRuntimeException( ioe.getMessage() );
//			//		} catch (RuntimeException rtex) {
//			//			AndroidGlobalDataManager.registerRuntimeException("AuthorizationFlowActivity.registerInBackground"
//			//					, rtex, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
//			//			rtex.printStackTrace();
//		} finally {
//			logger.info( "<< [AuthorizationFlowActivity.registerInBackground]" );
//		}
	}

	/**
	 * Performs the actions after the creation of the Activity. It will locate the UI elements, setup their contents ans start the spinner.
	 */
	protected void connectUI() {
		// If we are not receiving the code from CCP we should then create the Activity contents.
		try {
			// Set the layout to the core activity that defines the background, the indicator and the fragment container.
			this.setContentView( R.layout.activity_loginprogress );
			// Gets the activity's default ActionBar
			final ActionBar actionBar = this.getActionBar();
			actionBar.show();
			actionBar.setDisplayHomeAsUpEnabled( false );

			// Locate the elements of the page and store in global data.
			spinner = (ImageView) this.findViewById( R.id.spinner );

			// Start the spinner.
			Animation rotation = AnimationUtils.loadAnimation( this, R.anim.clockwise_rotation );
			rotation.setRepeatCount( Animation.INFINITE );
			spinner.startAnimation( rotation );
		} catch (final RuntimeException rtex) {
			//			AndroidGlobalDataManager.registerRuntimeException("AuthorizationFlowActivity.onCreate"
			//					, rtex, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			rtex.printStackTrace();
		}
	}

	/**
	 * Create a default Activity that shows the authorization progress while we wait for the ESI CCP callback or while we do the rest of the
	 * authorization cycle until finally complete a new Credential.
	 * Do the firing only when the extras have the right data configuration.
	 */
	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		logger.info( ">> [AuthorizationFlowActivity.onCreate]" );
		super.onCreate( savedInstanceState );
//		Thread.setDefaultUncaughtExceptionHandler( new MVCExceptionHandler( this ) );
		this.injectComponents(); // Inject components.
		// Detect if we are receiving this intent from the action button of from the flow.
		final Intent intent = getIntent();
		if (null != intent) {
			final Bundle extras = this.getIntent().getExtras();
			// Check that we have the tag identifier to detect if we are reaching this point from the Part.
			final String tag = extras.getString( EExtras.FIRE_AUTHORIZATION_FLOW.name() );
			if ((null != tag) && (tag.equalsIgnoreCase( "NEW-LOGIN-PART" ))) {
				// Fire the credential authorization flow on a background task.
				backgroundExecutor.submit( () -> {
					onFireAuth();
				} );
			} else {
				if (Intent.ACTION_VIEW.equals( intent.getAction() )) {
					if (null == intent.getData()) {
					} else {
						// Get the state and verify we are the caller.
						final Uri urlData = intent.getData();
						final String state = urlData.getQueryParameter( "state" );
						if (state.equals( "NEOCOM-VERIFICATION-STATE" )) {
							final String authCode = urlData.getQueryParameter( "code" );
							if (StringUtils.isNotBlank( authCode )) {
								// Fire the conversion to the authenticated Credential but do it on background.
								backgroundExecutor.submit( () -> {
									this.registerInBackground( authCode, state );
								} );
							}
						}
					}
				}
			}
		}
		connectUI();
		logger.info( "<< [AuthorizationFlowActivity.onCreate]" ); //$NON-NLS-1$
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
		logger.info( ">> [AuthorizationFlowActivity.onNewIntent]" );
		super.onNewIntent( intent );
		if (null != intent) {
			final Bundle extras = this.getIntent().getExtras();
			// Check that we have the tag identifier to detect if we are reaching this point from the Part.
			final String tag = extras.getString( EExtras.FIRE_AUTHORIZATION_FLOW.name() );
			if (tag.equalsIgnoreCase( "NEW-LOGIN-PART" )) {
				// Fire the credential authorization flow on a background task.
				backgroundExecutor.submit( () -> {
					onFireAuth();
				} );
			} else {
				if (Intent.ACTION_VIEW.equals( intent.getAction() )) {
					if (null == intent.getData()) {
					} else {
						// Get the state and verify we are the caller.
						final Uri urlData = intent.getData();
						final String state = urlData.getQueryParameter( "state" );
						if (state.equals( "NEOCOM-VERIFICATION-STATE" )) {
							final String authCode = urlData.getQueryParameter( "code" );
							if (StringUtils.isNotBlank( authCode )) {
								// Fire the conversion to the authenticated Credential but do it on background.
								backgroundExecutor.submit( () -> {
									this.registerInBackground( authCode, state );
								} );
							}
						}
					}
				}
			}
		}
		connectUI();
		logger.info( "<< [AuthorizationFlowActivity.onNewIntent]" );
	}

	private void injectComponents() {
		this.configurationProvider = NeoComComponentFactory.getSingleton().getConfigurationProvider();
		this.preferences = PreferenceManager.getDefaultSharedPreferences( this );
		this.esiDataAdapter = NeoComComponentFactory.getSingleton().getESIDataProvider();
	}
}
