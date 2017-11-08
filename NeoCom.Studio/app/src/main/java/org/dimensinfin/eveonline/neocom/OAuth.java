package org.dimensinfin.eveonline.neocom;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpStatusCodes;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Vector;

public final class OAuth {
    private static final String TAG = "EveSSO";
    private static final Vector assetScopes=new Vector<String>();
    static{
        assetScopes.add("characterAssetsRead");
        assetScopes.add("characterFittingsRead");
    }

    private static final HttpTransport HTTP = new NetHttpTransport();

    private static final JsonFactory JSON = new JacksonFactory();
    private static final ObjectMapper MAPPER;
    static {
        MAPPER = new ObjectMapper();
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private final AuthorizationCodeFlow flow;
    private final String userID;

    private final Context context;


    public OAuth (final Context context, final String userID) {
        this.context = context;
        this.userID = userID;
        try {
            this.flow = new AuthorizationCodeFlow.Builder(
                    BearerToken.authorizationHeaderAccessMethod(),
                    HTTP,
                    JSON,
                    new GenericUrl(OAuthPreferences.getTokenUrl(context)),
                    new ClientParametersAuthentication(OAuthPreferences.getClientID(context), OAuthPreferences.getClientSecret(context)),
                    OAuthPreferences.getClientID(context),
                    OAuthPreferences.getAuthorizeUrl(context)).
                    setDataStoreFactory(new MemoryDataStoreFactory()).
                    setScopes(assetScopes).
                    build();
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public String start() {
        final Credential credential = loadCredentials();
        if (null == credential) {
            return
                    this.flow.
                    newAuthorizationUrl().
                    setRedirectUri(OAuthPreferences.getRedirectUrl(this.context)).
                    build();
        }
        return null;
    }

    public boolean authorize(final Uri authData) {
        final String authCode = authData.getQueryParameter("code");
        if (StringUtils.isBlank(authCode)) {
            return false;
        }

        try {
            final TokenResponse tokenResponse = this.flow.newTokenRequest(authCode).execute();
            final Credential credential = this.flow.createAndStoreCredential(tokenResponse, this.userID);
            return null != credential;
        }
        catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage());
            return false;
        }
    }

    public OAuthVerified verify() {
        return get(OAuthPreferences.getVerifyUrl(this.context), OAuthVerified.class);
    }

    public void clear() {
        try {
            this.flow.getCredentialDataStore().delete(this.userID);
        }
        catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    public String get(final String url) {
        final Credential credential = loadCredentials();
        if (null == credential) {
            throw new IllegalStateException("No credentials found.");
        }

        try {
            final HttpRequestFactory factory = HTTP.createRequestFactory(credential);
            final HttpResponse response = factory.buildGetRequest(new GenericUrl(url)).execute();
            if (response.getStatusCode() != HttpStatusCodes.STATUS_CODE_OK) {
                Log.w(TAG, response.getStatusMessage());
                return null;
            }
            return response.parseAsString();
        }
        catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage());
            return null;
        }
    }

    public <T> T get(final String url, final Class<T> tClass) {
        final String response = get(url);
        if (null == response) {
            return null;
        }
        try {
            return MAPPER.readValue(response, tClass);
        }
        catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage());
            return null;
        }
    }

    private Credential loadCredentials() {
        try {
            return this.flow.loadCredential(this.userID);
        }
        catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage());
            return null;
        }
    }
}
