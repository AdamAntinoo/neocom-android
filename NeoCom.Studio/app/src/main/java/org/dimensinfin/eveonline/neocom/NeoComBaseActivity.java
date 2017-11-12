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
	private static final Instant i1=null;
//	private static final Instant i1= new Instant();

	private static final String state = "neocom-eveesi-state";
	private static final String clientId = "dbc9c2b1d18d49d8adacd23436c5281d";
	private static final String clientSecret = "QqnTLCqLQxZYHgHUuobkNA9g950vXVYDMg8ETTXM";
	private static final String redirectUri = "http://localhost/neocom";

	/** This is the new eve authentication flow to be used. */
	private OAuth auth = null;

	protected String startESIAuthentication () {
		final ApiClient client = new ApiClient();
		auth = (OAuth) client.getAuthentication("evesso");
		auth.setClientId(clientId);
		auth.setClientSecret(clientSecret);
		final Set<String> scopes = new HashSet<>(Arrays.asList(SsoScopes.ALL));
		final String authorizationUri = auth.getAuthorizationUri(redirectUri, scopes, state);
		return authorizationUri;
	}
}
