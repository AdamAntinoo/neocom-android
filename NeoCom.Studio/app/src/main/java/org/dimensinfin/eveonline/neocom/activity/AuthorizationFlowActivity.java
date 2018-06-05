//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API22.
//  DESCRIPTION: Android Application related to the Eve Online game. The purpose is to download and organize
//               the game data to help capsuleers organize and prioritize activities. The strong points are
//               help at the Industry level tracking and calculating costs and benefits. Also the market
//               information update service will help to identify best prices and locations.
//               Planetary Interaction and Ship fittings are point under development.
//               ESI authorization is a new addition that will give continuity and allow download game data
//               from the new CCP data services.
//               This is the Android application version but shares libraries and code with other application
//               designed for Spring Boot Angular 4 platform.
//               The model management is shown using a generic Model View Controller that allows make the
//               rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.activity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.support.annotation.WorkerThread;
import android.util.Base64;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.eveonline.neocom.NeoComApplication;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.auth.TokenRequestBody;
import org.dimensinfin.eveonline.neocom.auth.TokenTranslationResponse;
import org.dimensinfin.eveonline.neocom.auth.VerifyCharacterResponse;
import org.dimensinfin.eveonline.neocom.database.entity.Credential;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.datamngmt.ESINetworkManager;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManager;

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
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class AuthorizationFlowActivity extends Activity {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("AuthorizationFlowActivity");

	// - F I E L D - S E C T I O N ............................................................................
	private ImageView spinner = null;

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * Initiates a call to the CCP authentication platform. If the authorization is accepted by CCP the return event will be converted
	 * into another intent to this activity.
	 * This firing should be launched only if we come from the <code>NewLoginPart</code> part.
	 */
	protected void onFireAuth() {
		this.runOnUiThread(() -> {
			final Uri url = Uri.parse(AndroidGlobalDataManager.getAuthorizationUrl());
			final Intent intent = new Intent(Intent.ACTION_VIEW, url);
			intent.putExtra(Browser.EXTRA_APPLICATION_ID, getApplicationContext().getPackageName());
			startActivity(intent);
		});
	}

	/**
	 * Registering by converting the one use authorization token into a usable token that will bet stored with the
	 * credential.
	 * @param authCode the authorization code received from the Eve Online Developer Application site.
	 */
	@WorkerThread
	private void registerInBackground( final String authCode ) {
		logger.info(">> [AuthorizationFlowActivity.registerInBackground]");
		logger.info("-- [AuthorizationFlowActivity.registerInBackground]> Creating access token request.");
		try {
			// Create a Retrofit request service to encapsulate the call.
			final GetAccessToken serviceGetAccessToken = new Retrofit.Builder()
					.baseUrl(GlobalDataManager.getResourceString("R.esi.authorization.authorizationserver"))
					.addConverterFactory(JacksonConverterFactory.create())
					.build()
					.create(GetAccessToken.class);
			logger.info("-- [AuthorizationFlowActivity.registerInBackground]> Creating request body.");
			final TokenRequestBody tokenRequestBody = new TokenRequestBody()
					.setCode(authCode);
			logger.info("-- [AuthorizationFlowActivity.registerInBackground]> Creating request call.");
			final String peckString = GlobalDataManager.getResourceString("R.esi.authorization.clientid")
					+ ":"
					+ GlobalDataManager.getResourceString("R.esi.authorization.secretkey");
			String peck = Base64.encodeToString(peckString.getBytes(), Base64.DEFAULT);
			// Reprocess Android result to remove the \n characters.
			peck = peck.replaceAll("\n", "");
			final Call<TokenTranslationResponse> request = serviceGetAccessToken.getAccessToken("Basic " + peck
					, GlobalDataManager.getResourceString("R.esi.authorization.contenttype")
					, tokenRequestBody);

			// Getting the request response to be stored if valid.
			final Response<TokenTranslationResponse> response = request.execute();
			if ( response.isSuccessful() ) {
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
						.baseUrl(GlobalDataManager.getResourceString("R.esi.authorization.authorizationserver"))
						.addConverterFactory(JacksonConverterFactory.create())
						.client(verifyClient.build())
						.build()
						.create(VerifyCharacter.class);
				final String accessToken = token.getAccessToken();
				final Response<VerifyCharacterResponse> verificationResponse = verificationService.getVerification("Bearer " + accessToken).execute();

				if ( verificationResponse.isSuccessful() ) {
					logger.info("-- [LoginController.exchangeAuthorization]> Character verification OK.");
					// Create the credential and store it on the application repository.
					final int newAccountIdentifier = Long.valueOf(verificationResponse.body().getCharacterID()).intValue();
					final Credential credential = new Credential(newAccountIdentifier);
					credential.setAccountName(verificationResponse.body().getCharacterName())
					          .setAccessToken(token.getAccessToken())
					          .setTokenType(token.getTokenType())
					          .setExpires(Instant.now().plus(TimeUnit.SECONDS.toMillis(token.getExpires())).getMillis())
					          .setRefreshToken(token.getRefreshToken())
					          .setDataSource(GlobalDataManager.SERVER_DATASOURCE)
					          .setScope(ESINetworkManager.getStringScopes())
					          .store();
					// Once the credential is stored we go back to the Dashboard Activity.
					this.runOnUiThread(() -> {
												final Intent intent = new Intent(this, AppDashboardActivity.class);
												this.startActivity(intent);
//						finish();
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
			AndroidGlobalDataManager.registerException("AuthorizationFlowActivity.registerInBackground"
					, ioe, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			ioe.printStackTrace();
		} catch (RuntimeException rtex) {
			AndroidGlobalDataManager.registerRuntimeException("AuthorizationFlowActivity.registerInBackground"
					, rtex, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
			rtex.printStackTrace();
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
	// --- A C T I V I T Y   L I F E C Y C L E

	/**
	 * Create a default Activity that shows the authorization progress while we wait for the ESI CCP callback or while we do the rest of the
	 * authorization cycle until finally complete a new Credential.
	 * Do the firing only when the extras have the right data configuration.
	 */
	@Override
	protected void onCreate( final Bundle savedInstanceState ) {
		logger.info(">> [AuthorizationFlowActivity.onCreate]");
		super.onCreate(savedInstanceState);
		// Detect if we are receiving this intent from the action button of from the flow.
		final Intent intent = getIntent();
		if ( null != intent ) {
			final Bundle extras = this.getIntent().getExtras();
			// Check that we have the tag identifier to detect if we are reaching this point from the Part.
			final String tag = extras.getString(NeoComApplication.EExtras.FIRE_AUTHORIZATION_FLOW.name());
			if ( (null != tag) && (tag.equalsIgnoreCase("NEW-LOGIN-PART")) ) {
				// Fire the credential authorization flow on a background task.
				AndroidGlobalDataManager.submitJob2ui(() -> {
					onFireAuth();
				});
			} else {
				if ( Intent.ACTION_VIEW.equals(intent.getAction()) ) {
					if ( null == intent.getData() ) {
					} else {
						// Get the state and verify we are the caller.
						final Uri urlData = intent.getData();
						final String state = urlData.getQueryParameter("state");
						if ( state.equals("NEOCOM-VERIFICATION-STATE") ) {
							final String authCode = urlData.getQueryParameter("code");
							if ( StringUtils.isNotBlank(authCode) ) {
								// Fire the conversion to the authenticated Credential but do it on background.
								AndroidGlobalDataManager.submitJob2ui(() -> {
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
		if ( null != intent ) {
			final Bundle extras = this.getIntent().getExtras();
			// Check that we have the tag identifier to detect if we are reaching this point from the Part.
			final String tag = extras.getString(NeoComApplication.EExtras.FIRE_AUTHORIZATION_FLOW.name());
			if ( tag.equalsIgnoreCase("NEW-LOGIN-PART") ) {
				// Fire the credential authorization flow on a background task.
				AndroidGlobalDataManager.submitJob2ui(() -> {
					onFireAuth();
				});
			} else {
				if ( Intent.ACTION_VIEW.equals(intent.getAction()) ) {
					if ( null == intent.getData() ) {
					} else {
						// Get the state and verify we are the caller.
						final Uri urlData = intent.getData();
						final String state = urlData.getQueryParameter("state");
						if ( state.equals("NEOCOM-VERIFICATION-STATE") ) {
							final String authCode = urlData.getQueryParameter("code");
							if ( StringUtils.isNotBlank(authCode) ) {
								// Fire the conversion to the authenticated Credential but do it on background.
								AndroidGlobalDataManager.submitJob2ui(() -> {
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
}
// - UNUSED CODE ............................................................................................
//[01]
