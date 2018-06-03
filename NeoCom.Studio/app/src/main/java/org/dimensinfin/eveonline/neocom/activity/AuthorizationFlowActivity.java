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

import java.util.Hashtable;
import java.util.Hashtable;
import java.util.Hashtable;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dimensinfin.android.mvc.activity.AbstractPagerActivity;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.fragment.CredentialListFragment;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class AuthorizationFlowActivity {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("AuthorizationFlowActivity");

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public AuthorizationFlowActivity() {
		super();
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("AuthorizationFlowActivity [ ");
		buffer.append("name: ").append(0);
		buffer.append("]");
		buffer.append("->").append(super.toString());
		return buffer.toString();
	}
}
// - UNUSED CODE ............................................................................................
//[01]
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

		import android.content.Intent;
		import android.os.Bundle;
		import android.view.MenuItem;

		import org.dimensinfin.android.mvc.activity.AbstractPagerActivity;
		import org.dimensinfin.eveonline.neocom.activity.core.ANeoComPagerActivity;
		import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
		import org.dimensinfin.eveonline.neocom.fragment.CredentialListFragment;

// - CLASS IMPLEMENTATION ...................................................................................
public class CredentialListActivity extends ANeoComPagerActivity {
	// - S T A T I C - S E C T I O N ..........................................................................
	//		private static Logger logger = LoggerFactory.getLogger("LoginProgressActivity");
	//	public static final String[] SCOPES = {
	//	};

	// - F I E L D - S E C T I O N ............................................................................
	//	private final NeoComAuthService service = new NeoComAuthService();
	//	private TokenTranslationResponse token;
	//	private String accessToken;
	//	private int newAccountIdentifier;
	//
	//	private Response<TokenTranslationResponse> response;
	//	private Response<VerifyCharacterResponse> verificationResponse;

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * Return the name of this activity
	 */
	public String getName () {
		//		return ApplicationCloudAndroidAdapter.getSingleton().getResourceString(R.string.activity_title_LoginListActivity);
		return "Login";
	}

	/**
	 * On the Activity create phase we will set the layout, then create the action bar and all other UI elements
	 * and finally creates and sets the fragments. This is to avoid the multiple creation and addition of more
	 * fragments when the activity is put again on the foreground.
	 */
	@Override
	protected void onCreate (final Bundle savedInstanceState) {
		logger.info(">> [LoginProgressActivity.onCreate]"); //$NON-NLS-1$
		super.onCreate(savedInstanceState);
		final Intent intent = getIntent();
		if ( Intent.ACTION_VIEW.equals(intent.getAction()) ) onNewIntent(intent);

		// Disable go home for this activity since this is home.
		//		_actionBar.setDisplayHomeAsUpEnabled(false);
		try {
			// Process the parameters into the context. This initial Activity is the only one with no parameters.
			// Create the pages that form this Activity. Each page implemented by a Fragment.
			int page = 0;
			// Register this Activity as the current active Activity.
			//		ApplicationCloudAndroidAdapter.getSingleton().registerActivity(this);
			this.addPage(new CredentialListFragment().setVariant(ENeoComVariants.CAPSULEER_LIST.name()), page++);
		} catch (final Exception rtex) {
			AbstractPagerActivity.logger.severe("[LoginProgressActivity.onCreate]> RTEX> Runtime Exception." + rtex.getMessage());
			rtex.printStackTrace();
			this.stopActivity(rtex);
		}
		// Reinitialize the tile and subtitle from the first page.
		this.updateInitialTitle();
		AbstractPagerActivity.logger.info("<< [LoginProgressActivity.onCreate]"); //$NON-NLS-1$
	}

	// - L O G I N   T O   E S I   B L O C K
	//[02]

	/**
	 * This code should override the generic navigation code. Do this for this Activity because this is the first and
	 * there are no other parents
	 * for this.
	 *
	 * @param item menu item selected.
	 */
	@Override
	public boolean onOptionsItemSelected (final MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// This ID represents the Home or Up button. For the top of the parent chain just do nothing.1
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

//- UNUSED CODE ............................................................................................
//[01]
//		// Create the conversion call by coding.
//		logger.info("-- [LoginProgressActivity.registerInBackground]> Preparing Verification HTTP request.");
//		logger.info("-- [LoginProgressActivity.registerInBackground]> Creating access token request.");
//		try {
//			// Create a Retrofit request service to encapsulate the call.
//			final GetAccessToken serviceGetAccessToken = new Builder()
//					.baseUrl("https://login.eveonline.com")
//					.addConverterFactory(JacksonConverterFactory.create())
//					.build()
//					.create(GetAccessToken.class);
//			logger.info("-- [LoginProgressActivity.registerInBackground]> Creating request body.");
//			final TokenRequestBody tokenRequestBody = new TokenRequestBody()
//					.setCode(authCode);
//			logger.info("-- [LoginProgressActivity.registerInBackground]> Creating request call.");
//			final Call<TokenTranslationResponse> request = serviceGetAccessToken.getAccessToken("Basic " + NeoComAuthService.PECK
//					, NeoComAuthService.CONTENT_TYPE
//					, tokenRequestBody);
//[02]
//	/**
//	 * Initiates a call to the CCP authentication platform.
//	 */
//	public void handleLogin () {
//		// Create the service and initialize it.
//		service.createService();
//		//		service = createService();
//		final Uri url = getAuthorizationUrl();
//		final Intent intent = new Intent(Intent.ACTION_VIEW, url);
//		intent.putExtra(Browser.EXTRA_APPLICATION_ID, getApplicationContext().getPackageName());
//		startActivity(intent);
//	}
//
//	private Uri getAuthorizationUrl () {
//		return Uri.parse(service.getAuthorizationUrl());
//	}
//
//	/**
//	 * This is called when the Activity responds to a new request as selected by the OS. For example when a callback
//	 * from CCP is usued to a host/path it gets intercepted by the AndroidManifest and then passed to the matching
//	 * Activity, entering with the <code>onNewIntent</code> instead of using the <code>onCreate</code>
//	 */
//	protected void onNewIntent (final Intent intent) {
//		super.onNewIntent(intent);
//		if ( null != intent ) {
//			//			final Bundle bun = intent.getBundleExtra(Browser.EXTRA_APPLICATION_ID);
//			handleIntent(this, intent);
//		}
//	}
//
//	/**
//	 * Handkles VIEW requests that com from the authentication CCP site. Verify the data is for us and then call the
//	 * registratipo process to create a new Credential entry that gets stored at the database.
//	 *
//	 * @param activity the activity that generates the Intent.
//	 * @param intent   the Intent received.
//	 */
//	private void handleIntent (final Activity activity, final Intent intent) {
//		if ( (null == intent) || (null == intent.getData()) ) {
//			return;
//		}
//		// Get the state and verify we are the caller.
//		final Uri urlData = intent.getData();
//		final String state = urlData.getQueryParameter("state");
//		if ( state.equals("NEOCOM-VERIFICATION-STATE") ) {
//			if ( urlData.getScheme().equals("eveauth-neotest") ) {
//				final String authCode = urlData.getQueryParameter("code");
//				if ( StringUtils.isNotBlank(authCode) ) {
//					ApplicationCloudAdapter.getSingleton().getUIExecutor().submit(new Runnable() {
//						@Override
//						public void run () {
//							registerInBackground(authCode);
//						}
//					});
//				}
//			}
//		}
//	}
//
//	/**
//	 * Registering by converting the one use authorization token into a usable token that will bet stored with the
//	 * credential.
//	 *
//	 * @param authCode the authorization code received from the Eve Online Developer Application site.
//	 */
//	private void registerInBackground (final String authCode) {
//		logger.info("-- [LoginProgressActivity.registerInBackground]> Trying another authentication process.");
//		try {
//			final ApiClient authClient = new ApiClient("ESIEveOnline", NeoComAuthService.CLIENT_ID, NeoComAuthService.SECRET_KEY,
//					"CaptainHaddock09M2012", "50.Beta");
//			final GetAccessToken srv = authClient.createService(GetAccessToken.class);
//			logger.info("-- [LoginProgressActivity.registerInBackground]> Creating request body.");
//			final TokenRequestBody tokenRequestBody = new TokenRequestBody()
//					.setCode(authCode);
//			final Call<TokenTranslationResponse> req = srv.getAccessToken("Basic " + NeoComAuthService.PECK, NeoComAuthService.CONTENT_TYPE, tokenRequestBody);
//			//[01]
//
//			// Getting the request response to be stored if valid.
//			response = req.execute();
//			if ( response.isSuccessful() ) {
//				logger.info("-- [LoginProgressActivity.registerInBackground]> Response is 200 OK.");
//				token = response.body();
//				//			// Create a security verification instance.
//				//			OkHttpClient.Builder verifyClient =
//				//					new OkHttpClient.Builder()
//				//							.certificatePinner(
//				//									new CertificatePinner.Builder()
//				//											.add("login.eveonline.com", "sha256/5UeWOuDyX7IUmcKnsVdx+vLMkxEGAtzfaOUQT/caUBE=")
//				//											.add("login.eveonline.com", "sha256/980Ionqp3wkYtN9SZVgMzuWQzJta1nfxNPwTem1X0uc=")
//				//											.add("login.eveonline.com", "sha256/du6FkDdMcVQ3u8prumAo6t3i3G27uMP2EOhR8R0at/U=")
//				//											.build())
//				//							.addInterceptor(chain -> chain.proceed(
//				//									chain.request()
//				//									     .newBuilder()
//				//									     .addHeader("User-Agent", "org.dimensinfin")
//				//									     .build()));
//				// Verify the character authenticated and create the Credential.
//				logger.info("-- [LoginProgressActivity.registerInBackground]> Creating character verification.");
//				final VerifyCharacter verificationService = new Builder()
//						.baseUrl("https://login.eveonline.com")
//						.addConverterFactory(JacksonConverterFactory.create())
//						//		.client(verifyClient.build())
//						.build()
//						.create(VerifyCharacter.class);
//				accessToken = token.getAccessToken();
//				verificationResponse = verificationService.getVerification("Bearer " + accessToken).execute();
//				if ( verificationResponse.isSuccessful() ) {
//					logger.info("-- [LoginProgressActivity.registerInBackground]> Character verification OK.");
//					// Create the credential and store it.
//					newAccountIdentifier = Long.valueOf(verificationResponse.body().getCharacterID()).intValue();
//					final Credential credential = new Credential(newAccountIdentifier);
//					credential.setAccountName(verificationResponse.body().getCharacterName())
//					          .setAccessToken(token.getAccessToken())
//					          .setTokenType(token.getTokenType())
//					          .setExpires(Instant.now().plus(TimeUnit.SECONDS.toMillis(token.getExpires())).getMillis())
//					          .setRefreshToken(token.getRefreshToken())
//					          .store();
//				} else {
//					Toast.makeText(getApplicationContext(), "Character verification failed. " + verificationResponse.message(),
//							Toast.LENGTH_LONG).show();
//				}
//			} else {
//				Toast.makeText(getApplicationContext(), "Authorization token conversion failed. " + response.message(),
//						Toast.LENGTH_LONG).show();
//			}
//		} catch (IOException ioe) {
//			logger.severe("ER [LoginProgressActivity.registerInBackground]> IO Excetion on authorization request call. " + ioe.getMessage());
//		}
//	}



//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.activity;

		import android.content.Intent;
		import android.os.Bundle;
		import android.view.MenuItem;

		import org.dimensinfin.android.mvc.activity.AbstractPagerActivity;
		import org.dimensinfin.eveonline.neocom.activity.core.ANeoComPagerActivity;
		import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
		import org.dimensinfin.eveonline.neocom.fragment.LoginListFragment;

// - CLASS IMPLEMENTATION ...................................................................................
public class CredentialListActivity extends ANeoComPagerActivity {
	// - S T A T I C - S E C T I O N ..........................................................................
	//		private static Logger logger = LoggerFactory.getLogger("LoginProgressActivity");
	//	public static final String[] SCOPES = {
	//	};

	// - F I E L D - S E C T I O N ............................................................................
	//	private final NeoComAuthService service = new NeoComAuthService();
	//	private TokenTranslationResponse token;
	//	private String accessToken;
	//	private int newAccountIdentifier;
	//
	//	private Response<TokenTranslationResponse> response;
	//	private Response<VerifyCharacterResponse> verificationResponse;

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * Return the name of this activity
	 */
	public String getName () {
		//		return ApplicationCloudAndroidAdapter.getSingleton().getResourceString(R.string.activity_title_LoginListActivity);
		return "Login";
	}

	/**
	 * On the Activity create phase we will set the layout, then create the action bar and all other UI elements
	 * and finally creates and sets the fragments. This is to avoid the multiple creation and addition of more
	 * fragments when the activity is put again on the foreground.
	 */
	@Override
	protected void onCreate (final Bundle savedInstanceState) {
		logger.info(">> [LoginProgressActivity.onCreate]"); //$NON-NLS-1$
		super.onCreate(savedInstanceState);
		final Intent intent = getIntent();
		if ( Intent.ACTION_VIEW.equals(intent.getAction()) ) onNewIntent(intent);

		// Disable go home for this activity since this is home.
		//		_actionBar.setDisplayHomeAsUpEnabled(false);
		try {
			// Process the parameters into the context. This initial Activity is the only one with no parameters.
			// Create the pages that form this Activity. Each page implemented by a Fragment.
			int page = 0;
			// Register this Activity as the current active Activity.
			//		ApplicationCloudAndroidAdapter.getSingleton().registerActivity(this);
			this.addPage(new LoginListFragment().setVariant(ENeoComVariants.CAPSULEER_LIST.name()), page++);
		} catch (final Exception rtex) {
			AbstractPagerActivity.logger.severe("[LoginProgressActivity.onCreate]> RTEX> Runtime Exception." + rtex.getMessage());
			rtex.printStackTrace();
			this.stopActivity(rtex);
		}
		// Reinitialize the tile and subtitle from the first page.
		this.updateInitialTitle();
		AbstractPagerActivity.logger.info("<< [LoginProgressActivity.onCreate]"); //$NON-NLS-1$
	}

	// - L O G I N   T O   E S I   B L O C K
	//[02]

	/**
	 * This code should override the generic navigation code. Do this for this Activity because this is the first and
	 * there are no other parents
	 * for this.
	 *
	 * @param item menu item selected.
	 */
	@Override
	public boolean onOptionsItemSelected (final MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// This ID represents the Home or Up button. For the top of the parent chain just do nothing.1
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

//- UNUSED CODE ............................................................................................
//[01]
//		// Create the conversion call by coding.
//		logger.info("-- [LoginProgressActivity.registerInBackground]> Preparing Verification HTTP request.");
//		logger.info("-- [LoginProgressActivity.registerInBackground]> Creating access token request.");
//		try {
//			// Create a Retrofit request service to encapsulate the call.
//			final GetAccessToken serviceGetAccessToken = new Builder()
//					.baseUrl("https://login.eveonline.com")
//					.addConverterFactory(JacksonConverterFactory.create())
//					.build()
//					.create(GetAccessToken.class);
//			logger.info("-- [LoginProgressActivity.registerInBackground]> Creating request body.");
//			final TokenRequestBody tokenRequestBody = new TokenRequestBody()
//					.setCode(authCode);
//			logger.info("-- [LoginProgressActivity.registerInBackground]> Creating request call.");
//			final Call<TokenTranslationResponse> request = serviceGetAccessToken.getAccessToken("Basic " + NeoComAuthService.PECK
//					, NeoComAuthService.CONTENT_TYPE
//					, tokenRequestBody);
//[02]
//	/**
//	 * Initiates a call to the CCP authentication platform.
//	 */
//	public void handleLogin () {
//		// Create the service and initialize it.
//		service.createService();
//		//		service = createService();
//		final Uri url = getAuthorizationUrl();
//		final Intent intent = new Intent(Intent.ACTION_VIEW, url);
//		intent.putExtra(Browser.EXTRA_APPLICATION_ID, getApplicationContext().getPackageName());
//		startActivity(intent);
//	}
//
//	private Uri getAuthorizationUrl () {
//		return Uri.parse(service.getAuthorizationUrl());
//	}
//
//	/**
//	 * This is called when the Activity responds to a new request as selected by the OS. For example when a callback
//	 * from CCP is usued to a host/path it gets intercepted by the AndroidManifest and then passed to the matching
//	 * Activity, entering with the <code>onNewIntent</code> instead of using the <code>onCreate</code>
//	 */
//	protected void onNewIntent (final Intent intent) {
//		super.onNewIntent(intent);
//		if ( null != intent ) {
//			//			final Bundle bun = intent.getBundleExtra(Browser.EXTRA_APPLICATION_ID);
//			handleIntent(this, intent);
//		}
//	}
//
//	/**
//	 * Handkles VIEW requests that com from the authentication CCP site. Verify the data is for us and then call the
//	 * registratipo process to create a new Credential entry that gets stored at the database.
//	 *
//	 * @param activity the activity that generates the Intent.
//	 * @param intent   the Intent received.
//	 */
//	private void handleIntent (final Activity activity, final Intent intent) {
//		if ( (null == intent) || (null == intent.getData()) ) {
//			return;
//		}
//		// Get the state and verify we are the caller.
//		final Uri urlData = intent.getData();
//		final String state = urlData.getQueryParameter("state");
//		if ( state.equals("NEOCOM-VERIFICATION-STATE") ) {
//			if ( urlData.getScheme().equals("eveauth-neotest") ) {
//				final String authCode = urlData.getQueryParameter("code");
//				if ( StringUtils.isNotBlank(authCode) ) {
//					ApplicationCloudAdapter.getSingleton().getUIExecutor().submit(new Runnable() {
//						@Override
//						public void run () {
//							registerInBackground(authCode);
//						}
//					});
//				}
//			}
//		}
//	}
//
//	/**
//	 * Registering by converting the one use authorization token into a usable token that will bet stored with the
//	 * credential.
//	 *
//	 * @param authCode the authorization code received from the Eve Online Developer Application site.
//	 */
//	private void registerInBackground (final String authCode) {
//		logger.info("-- [LoginProgressActivity.registerInBackground]> Trying another authentication process.");
//		try {
//			final ApiClient authClient = new ApiClient("ESIEveOnline", NeoComAuthService.CLIENT_ID, NeoComAuthService.SECRET_KEY,
//					"CaptainHaddock09M2012", "50.Beta");
//			final GetAccessToken srv = authClient.createService(GetAccessToken.class);
//			logger.info("-- [LoginProgressActivity.registerInBackground]> Creating request body.");
//			final TokenRequestBody tokenRequestBody = new TokenRequestBody()
//					.setCode(authCode);
//			final Call<TokenTranslationResponse> req = srv.getAccessToken("Basic " + NeoComAuthService.PECK, NeoComAuthService.CONTENT_TYPE, tokenRequestBody);
//			//[01]
//
//			// Getting the request response to be stored if valid.
//			response = req.execute();
//			if ( response.isSuccessful() ) {
//				logger.info("-- [LoginProgressActivity.registerInBackground]> Response is 200 OK.");
//				token = response.body();
//				//			// Create a security verification instance.
//				//			OkHttpClient.Builder verifyClient =
//				//					new OkHttpClient.Builder()
//				//							.certificatePinner(
//				//									new CertificatePinner.Builder()
//				//											.add("login.eveonline.com", "sha256/5UeWOuDyX7IUmcKnsVdx+vLMkxEGAtzfaOUQT/caUBE=")
//				//											.add("login.eveonline.com", "sha256/980Ionqp3wkYtN9SZVgMzuWQzJta1nfxNPwTem1X0uc=")
//				//											.add("login.eveonline.com", "sha256/du6FkDdMcVQ3u8prumAo6t3i3G27uMP2EOhR8R0at/U=")
//				//											.build())
//				//							.addInterceptor(chain -> chain.proceed(
//				//									chain.request()
//				//									     .newBuilder()
//				//									     .addHeader("User-Agent", "org.dimensinfin")
//				//									     .build()));
//				// Verify the character authenticated and create the Credential.
//				logger.info("-- [LoginProgressActivity.registerInBackground]> Creating character verification.");
//				final VerifyCharacter verificationService = new Builder()
//						.baseUrl("https://login.eveonline.com")
//						.addConverterFactory(JacksonConverterFactory.create())
//						//		.client(verifyClient.build())
//						.build()
//						.create(VerifyCharacter.class);
//				accessToken = token.getAccessToken();
//				verificationResponse = verificationService.getVerification("Bearer " + accessToken).execute();
//				if ( verificationResponse.isSuccessful() ) {
//					logger.info("-- [LoginProgressActivity.registerInBackground]> Character verification OK.");
//					// Create the credential and store it.
//					newAccountIdentifier = Long.valueOf(verificationResponse.body().getCharacterID()).intValue();
//					final Credential credential = new Credential(newAccountIdentifier);
//					credential.setAccountName(verificationResponse.body().getCharacterName())
//					          .setAccessToken(token.getAccessToken())
//					          .setTokenType(token.getTokenType())
//					          .setExpires(Instant.now().plus(TimeUnit.SECONDS.toMillis(token.getExpires())).getMillis())
//					          .setRefreshToken(token.getRefreshToken())
//					          .store();
//				} else {
//					Toast.makeText(getApplicationContext(), "Character verification failed. " + verificationResponse.message(),
//							Toast.LENGTH_LONG).show();
//				}
//			} else {
//				Toast.makeText(getApplicationContext(), "Authorization token conversion failed. " + response.message(),
//						Toast.LENGTH_LONG).show();
//			}
//		} catch (IOException ioe) {
//			logger.severe("ER [LoginProgressActivity.registerInBackground]> IO Excetion on authorization request call. " + ioe.getMessage());
//		}
//	}
