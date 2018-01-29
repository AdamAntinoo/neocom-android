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

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.support.annotation.WorkerThread;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tlabs.android.evanova.adapter.ApplicationCloudAdapter;
import com.tlabs.android.evanova.adapter.ApplicationCloudAndroidAdapter;

import org.apache.commons.lang3.StringUtils;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.auth.NeoComAuthService;
import org.dimensinfin.eveonline.neocom.auth.TokenRequestBody;
import org.dimensinfin.eveonline.neocom.auth.TokenTranslationResponse;
import org.dimensinfin.eveonline.neocom.auth.VerifyCharacterResponse;
import org.dimensinfin.eveonline.neocom.database.entity.Credential;
import org.dimensinfin.eveonline.neocom.datamngmt.manager.GlobalAndroidDataManager;
import org.dimensinfin.eveonline.neocom.storage.NeoComModelStore;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

// - CLASS IMPLEMENTATION ...................................................................................
public class LoginProgressActivity extends Activity {

	public interface VerifyCharacter {
		@GET("/oauth/verify")
		Call<VerifyCharacterResponse> getVerification (@Header("Authorization") String token);
	}

	public interface GetAccessToken {
		//		@POST("/persistPerson")
		@POST("/oauth/token")
		Call<TokenTranslationResponse> getAccessToken (@Header("Authorization") String token
				, @Header("Content-Type") String contentType
				, @Body TokenRequestBody body);
	}
	//[01]

	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger("LoginProgressActivity");

	// - F I E L D - S E C T I O N ............................................................................
	private final NeoComAuthService service = new NeoComAuthService();
	private TokenTranslationResponse token;
	private String accessToken;
	private int newAccountIdentifier;

	private Response<TokenTranslationResponse> response;
	private Response<VerifyCharacterResponse> verificationResponse;

	private ActionBar actionBar;
	private ImageView nodeIcon;
	private TextView message;
	private TextView applicationVersion;

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................

	/**
	 * On the Activity create phase we will set the layout, then create the action bar and all other UI elements
	 * and finally creates and sets the fragments. This is to avoid the multiple creation and addition of more
	 * fragments when the activity is put again on the foreground.
	 */
	@Override
	protected void onCreate (final Bundle savedInstanceState) {
		LoginProgressActivity.logger.info(">> [LoginProgressActivity.onCreate]"); //$NON-NLS-1$
		super.onCreate(savedInstanceState);
		// Dispatch VIEW intents to the alternative metgod.
		final Intent intent = getIntent();
		if ( Intent.ACTION_VIEW.equals(intent.getAction()) ) onNewIntent(intent);

		try {
			// Set the layout to the core activity that defines the background, the indicator and the fragment container.
			this.setContentView(R.layout.activity_loginprogress);
			// Gets the activity's default ActionBar
			actionBar = this.getActionBar();
			actionBar.show();
			actionBar.setDisplayHomeAsUpEnabled(false);

			// Locate the elements of the page and store in global data.
			nodeIcon = (ImageView) this.findViewById(R.id.nodeIcon);
			message = (TextView) this.findViewById(R.id.message);
			applicationVersion = (TextView) this.findViewById(R.id.applicationVersion);

			// Start the spinner.
			Animation rotation = AnimationUtils.loadAnimation(this, R.anim.clockwise_rotation);
			rotation.setRepeatCount(Animation.INFINITE);
			nodeIcon.startAnimation(rotation);
			// Update the version.
			applicationVersion.setText(getResources().getString(R.string.appversionname));

			// Fire the credential authorization flow on a background task.
			ApplicationCloudAndroidAdapter.submit2uiExecutor(() -> {
				onFireAuth();
			});
		} catch (final RuntimeException rtex) {
			logger.error("RTEX [AbstractPagerActivity.onCreate]> " + rtex.getMessage());
			rtex.printStackTrace();
		}
		logger.info("<< [LoginProgressActivity.onCreate]"); //$NON-NLS-1$
	}

	// - L O G I N   T O   E S I   B L O C K

	/**
	 * Initiates a call to the CCP authentication platform. Thos message will start another intent on this activity
	 * but will go to the onNewIntent message.
	 */
	protected void onFireAuth () {
		//		super.onStart();
		this.runOnUiThread(() -> {
			service.createService();
			//		service = createService();
			final Uri url = getAuthorizationUrl();
			final Intent intent = new Intent(Intent.ACTION_VIEW, url);
			intent.putExtra(Browser.EXTRA_APPLICATION_ID, getApplicationContext().getPackageName());
			startActivity(intent);
		});
	}

	private Uri getAuthorizationUrl () {
		return Uri.parse(service.getAuthorizationUrl());
	}

	/**
	 * This is called when the Activity responds to a new request as selected by the OS. For example when a callback
	 * from CCP is usued to a host/path it gets intercepted by the AndroidManifest and then passed to the matching
	 * Activity, entering with the <code>onNewIntent</code> instead of using the <code>onCreate</code>.
	 * <p>
	 * Handles VIEW requests that com from the authentication CCP site. Verify the data is for us and then call the
	 * registratipo process to create a new Credential entry that gets stored at the database.
	 */
	protected void onNewIntent (final Intent intent) {
		super.onNewIntent(intent);
		final Bundle bun = intent.getBundleExtra(Browser.EXTRA_APPLICATION_ID);
		if ( (null == intent) || (null == intent.getData()) ) {
			return;
		}
		// Get the state and verify we are the caller.
		final Uri urlData = intent.getData();
		final String state = urlData.getQueryParameter("state");
		if ( state.equals("NEOCOM-VERIFICATION-STATE") ) {
			if ( urlData.getScheme().equals("eveauth-neotest") ) {
				final String authCode = urlData.getQueryParameter("code");
				if ( StringUtils.isNotBlank(authCode) ) {
					ApplicationCloudAdapter.getSingleton().getUIExecutor().submit(() -> {
						registerInBackground(authCode);
					});
				}
			}
		}
	}

	/**
	 * Registering by converting the one use authorization token into a usable token that will bet stored with the
	 * credential.
	 *
	 * @param authCode the authorization code received from the Eve Online Developer Application site.
	 */
	@WorkerThread
	private void registerInBackground (final String authCode) {
		// Create the conversion call by coding.
		logger.info("-- [LoginProgressActivity.registerInBackground]> Preparing Verification HTTP request.");
		logger.info("-- [LoginProgressActivity.registerInBackground]> Creating access token request.");
		try {
			// Create a Retrofit request service to encapsulate the call.
			final GetAccessToken serviceGetAccessToken = new Builder()
					.baseUrl("https://login.eveonline.com")
					.addConverterFactory(JacksonConverterFactory.create())
					.build()
					.create(GetAccessToken.class);
			logger.info("-- [LoginProgressActivity.registerInBackground]> Creating request body.");
			final TokenRequestBody tokenRequestBody = new TokenRequestBody()
					.setCode(authCode);
			logger.info("-- [LoginProgressActivity.registerInBackground]> Creating request call.");
			final Call<TokenTranslationResponse> request = serviceGetAccessToken.getAccessToken("Basic " + NeoComAuthService.PECK
					, NeoComAuthService.CONTENT_TYPE
					, tokenRequestBody);
			// Getting the request response to be stored if valid.
			response = request.execute();
			if ( response.isSuccessful() ) {
				logger.info("-- [LoginProgressActivity.registerInBackground]> Response is 200 OK.");
				token = response.body();
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
				logger.info("-- [LoginProgressActivity.registerInBackground]> Creating character verification.");
				final VerifyCharacter verificationService = new Builder()
						.baseUrl("https://login.eveonline.com")
						.addConverterFactory(JacksonConverterFactory.create())
						.client(verifyClient.build())
						.build()
						.create(VerifyCharacter.class);
				accessToken = token.getAccessToken();
				verificationResponse = verificationService.getVerification("Bearer " + accessToken).execute();
				if ( verificationResponse.isSuccessful() ) {
					logger.info("-- [LoginProgressActivity.registerInBackground]> Character verification OK.");
					// Create the credential and store it.
					newAccountIdentifier = Long.valueOf(verificationResponse.body().getCharacterID()).intValue();
					final Credential credential = new Credential(newAccountIdentifier);
					credential.setAccountName(verificationResponse.body().getCharacterName())
					          .setAccessToken(token.getAccessToken())
					          .setTokenType(token.getTokenType())
					          .setExpires(Instant.now().plus(TimeUnit.SECONDS.toMillis(token.getExpires())).getMillis())
					          .setRefreshToken(token.getRefreshToken())
					          .store();
					// Update the Pilot information.
					GlobalAndroidDataManager.getPilotV1(credential.getAccountId());
					// Then cleanup the credential list.
					NeoComModelStore.getSingleton().cleanModel();
					// Once the credential is stores we go back to the last Activity.
					finish();
				} else {
					Toast.makeText(getApplicationContext(), "Character verification failed. " + verificationResponse.message(),
							Toast.LENGTH_LONG).show();
				}
				//				if ( verificationResponse.isSuccessful() ) {
				//					logger.info("-- [LoginProgressActivity.registerInBackground]> Character verification OK.");
				//					// Create the credential and store it.
				//					newAccountIdentifier = Long.valueOf(verificationResponse.body().getCharacterID()).intValue();
				//					final Credential credential = new Credential(newAccountIdentifier);
				//				} else {
				//					Toast.makeText(getApplicationContext(), "Character verification failed. " + verificationResponse.message(),
				//							Toast.LENGTH_LONG).show();
				//				}
			} else {
				Toast.makeText(getApplicationContext(), "Authorization token conversion failed. " + response.message(),
						Toast.LENGTH_LONG).show();
			}
		} catch (IOException ioe) {
			logger.error("ER [LoginProgressActivity.registerInBackground]> IO Excetion on authorization request call. " + ioe
					.getMessage());
		}
	}

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
//	public static class NeoComAuthApi extends DefaultApi20 {
//
//		private static final String AUTHORIZE_URL = "https://login.eveonline.com/oauth/authorize";
//		//		private static final String REQUEST_TOKEN_RESOURCE = "api.twitter.com/oauth/request_token";
//		private static final String ACCESS_TOKEN_RESOURCE = "https://login.eveonline.com/oauth/token";
//		//		private static final String ACCESS_TOKEN_RESOURCE = "http://localhost:8080/persistPerson";
//		//		private static final String VERIFICATION_ENDPOINT = "https://login.eveonline.com/oauth/verify";
//
//		protected NeoComAuthApi () {
//		}
//
//		private static class InstanceHolder {
//			private static final NeoComAuthApi INSTANCE = new NeoComAuthApi();
//		}
//
//		public static NeoComAuthApi instance () {
//			return NeoComAuthApi.InstanceHolder.INSTANCE;
//		}
//
//		@Override
//		public String getAccessTokenEndpoint () {
//			return ACCESS_TOKEN_RESOURCE;
//		}
//
//		@Override
//		protected String getAuthorizationBaseUrl () {
//			return AUTHORIZE_URL;
//		}
//	}
//
//	public static class NeoComAuthService {
//		public static String CLIENT_ID = "396e0b6cbed2488284ed4ae426133c90";
//		public static String SECRET_KEY = "gf8X16xbdaO6soJWCdYHPFfftczZyvfo63z6WUjO";
//		public static String CALLBACK = "eveauth-neotest://authentication";
//		public static final String CONTENT_TYPE = "application/json";
//		public static final String PECK = "Mzk2ZTBiNmNiZWQyNDg4Mjg0ZWQ0YWU0MjYxMzNjOTA6Z2Y4WDE2eGJkYU82c29KV0NkWUhQRmZmdGN6Wnl2Zm82M3o2V1VqTw==";
//
//		private OAuth20Service service;
//		private OAuth2AccessToken accessToken;
//		private String authorizationCode;
//
//		public void createService () {
//			service = new ServiceBuilder(CLIENT_ID)
//					.apiKey(CLIENT_ID)
//					.apiSecret(SECRET_KEY)
//					.callback(CALLBACK)
//					//					.httpClient(HttpClient httpClient)
//					//					.responseType(String responseType)
//					.scope("corporationContactsRead publicData characterStatsRead characterFittingsRead characterFittingsWrite characterContactsRead characterContactsWrite characterLocationRead characterNavigationWrite characterWalletRead characterAssetsRead characterCalendarRead characterFactionalWarfareRead characterIndustryJobsRead characterKillsRead characterMailRead characterMarketOrdersRead characterMedalsRead characterNotificationsRead characterResearchRead characterSkillsRead characterAccountRead characterContractsRead characterBookmarksRead characterChatChannelsRead characterClonesRead characterOpportunitiesRead characterLoyaltyPointsRead corporationWalletRead corporationAssetsRead corporationMedalsRead corporationFactionalWarfareRead corporationIndustryJobsRead corporationKillsRead corporationMembersRead corporationMarketOrdersRead corporationStructuresRead corporationShareholdersRead corporationContractsRead corporationBookmarksRead fleetRead fleetWrite structureVulnUpdate remoteClientUI esi-calendar.respond_calendar_events.v1 esi-calendar.read_calendar_events.v1 esi-location.read_location.v1 esi-location.read_ship_type.v1 esi-mail.organize_mail.v1 esi-mail.read_mail.v1 esi-mail.send_mail.v1 esi-skills.read_skills.v1 esi-skills.read_skillqueue.v1 esi-wallet.read_character_wallet.v1 esi-wallet.read_corporation_wallet.v1 esi-search.search_structures.v1 esi-clones.read_clones.v1 esi-characters.read_contacts.v1 esi-universe.read_structures.v1 esi-bookmarks.read_character_bookmarks.v1 esi-killmails.read_killmails.v1 esi-corporations.read_corporation_membership.v1 esi-assets.read_assets.v1 esi-planets.manage_planets.v1 esi-fleets.read_fleet.v1 esi-fleets.write_fleet.v1 esi-ui.open_window.v1 esi-ui.write_waypoint.v1 esi-characters.write_contacts.v1 esi-fittings.read_fittings.v1 esi-fittings.write_fittings.v1 esi-markets.structure_markets.v1 esi-corporations.read_structures.v1 esi-corporations.write_structures.v1 esi-characters.read_loyalty.v1 esi-characters.read_opportunities.v1 esi-characters.read_chat_channels.v1 esi-characters.read_medals.v1 esi-characters.read_standings.v1 esi-characters.read_agents_research.v1 esi-industry.read_character_jobs.v1 esi-markets.read_character_orders.v1 esi-characters.read_blueprints.v1 esi-characters.read_corporation_roles.v1 esi-location.read_online.v1 esi-contracts.read_character_contracts.v1 esi-clones.read_implants.v1 esi-characters.read_fatigue.v1 esi-killmails.read_corporation_killmails.v1 esi-corporations.track_members.v1 esi-wallet.read_corporation_wallets.v1 esi-characters.read_notifications.v1 esi-corporations.read_divisions.v1 esi-corporations.read_contacts.v1 esi-assets.read_corporation_assets.v1 esi-corporations.read_titles.v1 esi-corporations.read_blueprints.v1 esi-bookmarks.read_corporation_bookmarks.v1 esi-contracts.read_corporation_contracts.v1 esi-corporations.read_standings.v1 esi-corporations.read_starbases.v1 esi-industry.read_corporation_jobs.v1 esi-markets.read_corporation_orders.v1 esi-corporations.read_container_logs.v1 esi-industry.read_character_mining.v1 esi-industry.read_corporation_mining.v1 esi-planets.read_customs_offices.v1 esi-corporations.read_facilities.v1 esi-corporations.read_medals.v1 esi-characters.read_titles.v1 esi-alliances.read_contacts.v1 esi-characters.read_fw_stats.v1 esi-corporations.read_fw_stats.v1")
//					//					.userAgent(String userAgent)
//					.state("NEOCOM-VERIFICATION-STATE")
//					.build(NeoComAuthApi.instance());
//		}
//
//		public String getAuthorizationUrl () {
//			String authUrl = service.getAuthorizationUrl();
//			return authUrl;
//		}
//
//		public OAuth2AccessToken getAccessToken (final String authorizationCode) throws InterruptedException, ExecutionException, IOException {
//			this.authorizationCode = authorizationCode;
//			//			accessToken = service.getAccessToken(authorizationCode);
//			return accessToken;
//		}
//
//	}


//	public static class VerifyCharacterResponse {
//		@JsonProperty("CharacterID")
//		private long characterID;
//		@JsonProperty("CharacterName")
//		private String characterName;
//		@JsonProperty("ExpiresOn")
//		private String expiresOn;
//		private long expiresMillis;
//		@JsonProperty("Scopes")
//		private String scopes;
//		@JsonProperty("TokenType")
//		private String tokenType;
//		@JsonProperty("CharacterOwnerHash")
//		private String characterOwnerHash;
//		@JsonProperty("IntellectualProperty")
//		private String intellectualProperty;
//
//		public long getCharacterID () {
//			return characterID;
//		}
//
//		public void setCharacterID (final long characterID) {
//			this.characterID = characterID;
//		}
//
//		public String getCharacterName () {
//			return characterName;
//		}
//
//		public void setCharacterName (final String characterName) {
//			this.characterName = characterName;
//		}
//
//		public String getExpiresOn () {
//			return expiresOn;
//		}
//
//		public void setExpiresOn (final String expiresOn) {
//			this.expiresOn = expiresOn;
//			// Convert the string to a data and then to the date milliseconds.
//			//			final DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-ddTHH:mm:ss");
//			//			expiresMillis = fmt.parseMillis(expiresOn);
//		}
//
//		public long getExpiresMillis () {
//			return expiresMillis;
//		}
//
//		public void setExpiresMillis (final long expiresInstant) {
//			this.expiresMillis = expiresInstant;
//		}
//
//		public String getScopes () {
//			return scopes;
//		}
//
//		public void setScopes (final String scopes) {
//			this.scopes = scopes;
//		}
//
//		public String getTokenType () {
//			return tokenType;
//		}
//
//		public void setTokenType (final String tokenType) {
//			this.tokenType = tokenType;
//		}
//
//		public String getCharacterOwnerHash () {
//			return characterOwnerHash;
//		}
//
//		public void setCharacterOwnerHash (final String characterOwnerHash) {
//			this.characterOwnerHash = characterOwnerHash;
//		}
//
//		public String getIntellectualProperty () {
//			return intellectualProperty;
//		}
//
//		public void setIntellectualProperty (final String intellectualProperty) {
//			this.intellectualProperty = intellectualProperty;
//		}
//	}
//
//	public static class TokenRequestBody {
//		@JsonProperty("grant_type")
//		public String grant_type = "authorization_code";
//		@JsonProperty("code")
//		public String code;
//
//		public String getGrant_type () {
//			return "authorization_code";
//		}
//
//		//		public TokenRequestBody setGrant_type (final String grant_type) {
//		//			this.grant_type = grant_type;
//		//			return this;
//		//		}
//
//		public String getCode () {
//			return code;
//		}
//
//		public TokenRequestBody setCode (final String code) {
//			this.code = code;
//			return this;
//		}
//	}
//
//	public static class TokenTranslationResponse {
//		@JsonProperty("access_token")
//		public String accessToken;
//		@JsonProperty("token_type")
//		public String tokenType;
//		@JsonProperty("expires_in")
//		public long expires;
//		@JsonProperty("refresh_token")
//		public String refreshToken;
//
//		public String getAccessToken () {
//			return accessToken;
//		}
//
//		public void setAccessToken (final String accessToken) {
//			this.accessToken = accessToken;
//		}
//
//		public String getTokenType () {
//			return tokenType;
//		}
//
//		public void setTokenType (final String tokenType) {
//			this.tokenType = tokenType;
//		}
//
//		public long getExpires () {
//			return expires;
//		}
//
//		public void setExpires (final long expires) {
//			this.expires = expires;
//		}
//
//		public String getRefreshToken () {
//			return refreshToken;
//		}
//
//		public void setRefreshToken (final String refreshToken) {
//			this.refreshToken = refreshToken;
//		}
//	}
