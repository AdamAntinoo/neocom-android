package org.dimensinfin.eveonline.neocom;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.store.MemoryDataStoreFactory;

import net.troja.eve.esi.ApiClient;
import net.troja.eve.esi.auth.OAuth;
import net.troja.eve.esi.auth.SsoScopes;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Adam on 11/11/2017.
 */

public class NeoComBaseActivity extends AppCompatActivity {
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
