package org.dimensinfin.eveonline.neocom;

import android.annotation.TargetApi;
import android.support.v7.app.AppCompatActivity;

import net.troja.eve.esi.ApiClient;
import net.troja.eve.esi.auth.OAuth;
import net.troja.eve.esi.auth.SsoScopes;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Adam on 11/11/2017.
 */

public class NeoComBaseActivity extends AppCompatActivity {
	// Preloading of classes
	//	@TargetApi(26)
	private static final Instant i1 = null;
	//	private static final Instant i1= new Instant();

	private static final String state = "neocom-eveesi-state";
	private static final String clientId = "396e0b6cbed2488284ed4ae426133c90";
	private static final String clientSecret = "gf8X16xbdaO6soJWCdYHPFfftczZyvfo63z6WUjO";
	private static final String redirectUri = "eveauth-neotest://authentication";
	private static final HashSet scopes = new HashSet();

	static {
		scopes.add("characterAssetsRead");
		scopes.add("characterIndustryJobsRead");
		scopes.add("characterAccountRead");
	}

	private static final String scopesstring = "characterAssetsRead characterIndustryJobsRead characterAccountRead";

	/** This is the new eve authentication flow to be used. */
	private OAuth auth = null;

	protected String startESIAuthentication () {
		final ApiClient client = new ApiClient();
		auth = (OAuth) client.getAuthentication("evesso");
		auth.setClientId(clientId);
		auth.setClientSecret(clientSecret);
//		final Set<String> scopes = new HashSet<>(Arrays.asList(SsoScopes.ALL));
		final String authorizationUri = auth.getAuthorizationUri(redirectUri, scopes, state);
		return authorizationUri;
	}
}
