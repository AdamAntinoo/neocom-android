package org.dimensinfin.eveonline.neocom.app.activity;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Browser;
import android.support.annotation.WorkerThread;
import android.util.Base64;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import org.dimensinfin.android.mvc.exception.MVCExceptionHandler;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.EExtras;
import org.dimensinfin.eveonline.neocom.app.adapters.NeoComComponentFactory;
import org.dimensinfin.eveonline.neocom.auth.TokenRequestBody;
import org.dimensinfin.eveonline.neocom.auth.TokenTranslationResponse;
import org.dimensinfin.eveonline.neocom.auth.VerifyCharacterResponse;
import org.dimensinfin.eveonline.neocom.database.entities.Credential;
import org.dimensinfin.eveonline.neocom.database.repositories.CredentialRepository;
import org.dimensinfin.eveonline.neocom.datamngmt.ESIGlobalAdapter;
import org.dimensinfin.eveonline.neocom.exception.NeoComRuntimeException;
import org.dimensinfin.eveonline.neocom.interfaces.IConfigurationProvider;
import org.dimensinfin.eveonline.neocom.sharedcode.dataservice.AndroidGlobalDataManager;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

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
	private static Logger logger = LoggerFactory.getLogger(AuthorizationFlowActivity.class);
	private static final String DATASOURCE_PREFERENCE = "prefkey_dataSourceList";

	private SharedPreferences preferences;
	private IConfigurationProvider configurationProvider;
	private ESIGlobalAdapter esiAdapter;
	private ImageView spinner = null;

	/**
	 * Initiates a call to the CCP authentication platform. If the authorization is accepted by CCP the return event will be converted
	 * into another intent to this activity.
	 * This firing should be launched only if we come from the <code>NewLoginPart</code> part.
	 */
	protected void onFireAuth() {
		this.runOnUiThread(() -> {
			final String esiServer = this.preferences.getString(DATASOURCE_PREFERENCE, NeoComComponentFactory.DEFAULT_ESI_SERVER);
			this.esiAdapter.activateEsiServer(esiServer);
			final Uri url = Uri.parse(this.esiAdapter.getAuthorizationUrl4Server(esiServer));
			final Intent intent = new Intent(Intent.ACTION_VIEW, url);
			intent.putExtra(Browser.EXTRA_APPLICATION_ID, getApplicationContext().getPackageName());
			startActivity(intent);
		});
	}

	/**
	 * Registering by converting the one use authorization token into a usable token that will bet stored with the
	 * credential.
	 *
	 * @param authCode the authorization code received from the Eve Online Developer Application site.
	 */
	@WorkerThread
	private void registerInBackground( final String authCode ) {
		logger.info(">> [AuthorizationFlowActivity.registerInBackground]");
		logger.info("-- [AuthorizationFlowActivity.registerInBackground]> Creating access token request.");
		// Get from the properties the user selected esi source.
		final String esiServer = this.preferences.getString("prefkey_dataSourceList", NeoComComponentFactory.DEFAULT_ESI_SERVER).toLowerCase();
		try {
			// Create a Retrofit request service to encapsulate the call.
			final GetAccessToken serviceGetAccessToken = new Retrofit.Builder()
					                                             .baseUrl(this.configurationProvider.getResourceString("P.esi."
							                                                                                                   + esiServer.toLowerCase()
							                                                                                                   + ".authorization.server"))
					                                             .addConverterFactory(JacksonConverterFactory.create())
					                                             .build()
					                                             .create(GetAccessToken.class);
			logger.info("-- [AuthorizationFlowActivity.registerInBackground]> Creating request body.");
			final TokenRequestBody tokenRequestBody = new TokenRequestBody().setCode(authCode);
			logger.info("-- [AuthorizationFlowActivity.registerInBackground]> Creating request call.");
			final String peckString = this.configurationProvider.getResourceString("P.esi." + esiServer + ".authorization.clientid")
					                          + ":"
					                          + this.configurationProvider.getResourceString("P.esi." + esiServer + ".authorization.secretkey");
			String peck = Base64.encodeToString(peckString.getBytes(), Base64.DEFAULT);
			// Reprocess Android result to remove the \n characters.
			peck = peck.replaceAll("\n", "");
			final Call<TokenTranslationResponse> request = serviceGetAccessToken.getAccessToken("Basic " + peck
					, this.configurationProvider.getResourceString("P.esi.authorization.content.type")
					, tokenRequestBody);

			// Getting the request response to be stored if valid.
			final Response<TokenTranslationResponse> response = request.execute();
			if (response.isSuccessful()) {
				logger.info("-- [AuthorizationFlowActivity.registerInBackground]> Response is 200 OK.");
				final TokenTranslationResponse token = response.body();
				// Create a security verification instance.
				OkHttpClient.Builder verifyClient =
						new OkHttpClient.Builder()
								.certificatePinner(
										new CertificatePinner.Builder()
												.add("login.eveonline.com", "sha256/5UeWOuDyX7IUmcKnsVdx+vLMkxEGAtzfaOUQT/caUBE=")
												.add("login.eveonline.com", "sha256/980Ionqp3wkYtN9SZVgMzuWQzJta1nfxNPwTem1X0uc=")
												.add("login.eveonline.com", "sha256/du6FkDdMcVQ3u8prumAo6t3i3G27uMP2EOhR8R0at/U=")
												.build())
								.addInterceptor(chain -> chain.proceed(
										chain.request()
												.newBuilder()
												.addHeader("User-Agent", "org.dimensinfin")
												.build()));
				// Verify the character authenticated and create the Credential.
				logger.info("-- [AuthorizationFlowActivity.registerInBackground]> Creating character verification.");
				final VerifyCharacter verificationService = new Retrofit.Builder()
						                                            .baseUrl(this.configurationProvider.getResourceString("P.esi."
								                                                                                                  + esiServer.toLowerCase()
								                                                                                                  + ".authorization.server"))
						                                            .addConverterFactory(JacksonConverterFactory.create())
						                                            .client(verifyClient.build())
						                                            .build()
						                                            .create(VerifyCharacter.class);
				final String accessToken = token.getAccessToken();
				final Response<VerifyCharacterResponse> verificationResponse = verificationService.getVerification("Bearer " + accessToken).execute();

				if (verificationResponse.isSuccessful()) {
					logger.info("-- [AuthorizationFlowActivity.registerInBackground]> Character verification OK.");
					// Create the credential and store it on the application repository.
					final CredentialRepository credentialRepository = NeoComComponentFactory.getSingleton().getCredentialRepository();
					final int newAccountIdentifier = Long.valueOf(verificationResponse.body().getCharacterID()).intValue();
					final Credential credential = new Credential.Builder(newAccountIdentifier).build();
					credential.setAccountName(verificationResponse.body().getCharacterName())
							.setAccessToken(token.getAccessToken())
							.setTokenType(token.getTokenType())
							.setExpires(Instant.now().plus(TimeUnit.SECONDS.toMillis(token.getExpires())).getMillis())
							.setRefreshToken(token.getRefreshToken())
							.setDataSource(esiServer.toUpperCase())
							.setScope(this.esiAdapter.getStringScopes());
					try {
						credentialRepository.persist(credential);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					// Once the credential is stored we go back to the Dashboard Activity.
					this.runOnUiThread(() -> {
						final Intent intent = new Intent(this, AppSplashActivity.class);
						this.startActivity(intent);
					});
				} else {
					Toast.makeText(getApplicationContext(), "Character verification failed. " + verificationResponse.message(),
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "Authorization token conversion failed. " + response.message(),
						Toast.LENGTH_LONG).show();
			}
		} catch (IOException ioe) {
			//			AndroidGlobalDataManager.registerException("AuthorizationFlowActivity.registerInBackground"
			//					, ioe, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			//			ioe.printStackTrace();
			throw new NeoComRuntimeException(ioe.getMessage());
			//		} catch (RuntimeException rtex) {
			//			AndroidGlobalDataManager.registerRuntimeException("AuthorizationFlowActivity.registerInBackground"
			//					, rtex, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			//			rtex.printStackTrace();
		} finally {
			logger.info("<< [AuthorizationFlowActivity.registerInBackground]");
		}
	}

	/**
	 * Performs the actions after the creation of the Activity. It will locate the UI elements, setup their contents ans start the spinner.
	 */
	protected void connectUI() {
		// If we are not receiving the code from CCP we should then create the Activity contents.
		try {
			// Set the layout to the core activity that defines the background, the indicator and the fragment container.
			this.setContentView(R.layout.activity_loginprogress);
			// Gets the activity's default ActionBar
			final ActionBar actionBar = this.getActionBar();
			actionBar.show();
			actionBar.setDisplayHomeAsUpEnabled(false);

			// Locate the elements of the page and store in global data.
			spinner = (ImageView) this.findViewById(R.id.spinner);

			// Start the spinner.
			Animation rotation = AnimationUtils.loadAnimation(this, R.anim.clockwise_rotation);
			rotation.setRepeatCount(Animation.INFINITE);
			spinner.startAnimation(rotation);
		} catch (final RuntimeException rtex) {
			AndroidGlobalDataManager.registerRuntimeException("AuthorizationFlowActivity.onCreate"
					, rtex, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			rtex.printStackTrace();
		}
	}
	// - A C T I V I T Y   L I F E C Y C L E

	/**
	 * Create a default Activity that shows the authorization progress while we wait for the ESI CCP callback or while we do the rest of the
	 * authorization cycle until finally complete a new Credential.
	 * Do the firing only when the extras have the right data configuration.
	 */
	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		logger.info(">> [AuthorizationFlowActivity.onCreate]");
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new MVCExceptionHandler(this));
		this.injectComponents(); // Inject components.
		// Detect if we are receiving this intent from the action button of from the flow.
		final Intent intent = getIntent();
		if (null != intent) {
			final Bundle extras = this.getIntent().getExtras();
			// Check that we have the tag identifier to detect if we are reaching this point from the Part.
			final String tag = extras.getString(EExtras.FIRE_AUTHORIZATION_FLOW.name());
			if ((null != tag) && (tag.equalsIgnoreCase("NEW-LOGIN-PART"))) {
				// Fire the credential authorization flow on a background task.
				AndroidGlobalDataManager.getSingleton().submitJob(() -> {
					onFireAuth();
				});
			} else {
				if (Intent.ACTION_VIEW.equals(intent.getAction())) {
					if (null == intent.getData()) {
					} else {
						// Get the state and verify we are the caller.
						final Uri urlData = intent.getData();
						final String state = urlData.getQueryParameter("state");
						if (state.equals("NEOCOM-VERIFICATION-STATE")) {
							final String authCode = urlData.getQueryParameter("code");
							if (StringUtils.isNotBlank(authCode)) {
								// Fire the conversion to the authenticated Credential but do it on background.
								AndroidGlobalDataManager.getSingleton().submitJob(() -> {
									this.registerInBackground(authCode);
								});
							}
						}
					}
				}
			}
		}
		connectUI();
		logger.info("<< [AuthorizationFlowActivity.onCreate]"); //$NON-NLS-1$
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
		logger.info(">> [AuthorizationFlowActivity.onNewIntent]");
		super.onNewIntent(intent);
		if (null != intent) {
			final Bundle extras = this.getIntent().getExtras();
			// Check that we have the tag identifier to detect if we are reaching this point from the Part.
			final String tag = extras.getString(EExtras.FIRE_AUTHORIZATION_FLOW.name());
			if (tag.equalsIgnoreCase("NEW-LOGIN-PART")) {
				// Fire the credential authorization flow on a background task.
				AndroidGlobalDataManager.getSingleton().submitJob(() -> {
					onFireAuth();
				});
			} else {
				if (Intent.ACTION_VIEW.equals(intent.getAction())) {
					if (null == intent.getData()) {
					} else {
						// Get the state and verify we are the caller.
						final Uri urlData = intent.getData();
						final String state = urlData.getQueryParameter("state");
						if (state.equals("NEOCOM-VERIFICATION-STATE")) {
							final String authCode = urlData.getQueryParameter("code");
							if (StringUtils.isNotBlank(authCode)) {
								// Fire the conversion to the authenticated Credential but do it on background.
								AndroidGlobalDataManager.getSingleton().submitJob(() -> {
									this.registerInBackground(authCode);
								});
							}
						}
					}
				}
			}
		}
		connectUI();
		logger.info("<< [AuthorizationFlowActivity.onNewIntent]");
	}

	public interface VerifyCharacter {
		@GET("/oauth/verify")
		Call<VerifyCharacterResponse> getVerification( @Header("Authorization") String token );
	}

	public interface GetAccessToken {
		//		@POST("/persistPerson")
		@POST("/oauth/token")
		Call<TokenTranslationResponse> getAccessToken( @Header("Authorization") String token
				, @Header("Content-Type") String contentType
				, @Body TokenRequestBody body );
	}

	private void injectComponents() {
		this.configurationProvider = NeoComComponentFactory.getSingleton().getConfigurationProvider();
		this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
		this.esiAdapter = NeoComComponentFactory.getSingleton().getEsiAdapter();
	}
}
