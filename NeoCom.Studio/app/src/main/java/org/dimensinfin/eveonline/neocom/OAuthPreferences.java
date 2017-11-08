package org.dimensinfin.eveonline.neocom;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

final class OAuthPreferences {

    private static final String TOKEN_URL = "https://login.eveonline.com/oauth/token";
    private static final String TOKEN_URL_KEY = "Eve.OAuthPreferences.TokenUrl.key";

    private static final String AUTH_URL = "https://login.eveonline.com/oauth/authorize";
    private static final String AUTH_URL_KEY = "Eve.OAuthPreferences.AuthUrl.key";

    private static final String REDIRECT_URL = "http://localhost/neocom";
    private static final String REDIRECT_URL_KEY = "Eve.OAuthPreferences.RedirectUrl.key";

    private static final String VERIFY_URL = "https://login.eveonline.com/oauth/verify";
    private static final String VERIFY_URL_KEY = "Eve.OAuthPreferences.VerifyUrl.key";

    public static final String getTokenUrl(final Context context) {
        return getString(context, TOKEN_URL_KEY, TOKEN_URL);
    }

    public static final String getAuthorizeUrl(final Context context) {
        return getString(context, AUTH_URL_KEY, AUTH_URL);
    }

    public static final String getRedirectUrl(final Context context) {
        return getString(context, REDIRECT_URL_KEY, REDIRECT_URL);
    }

    public static final String getVerifyUrl(final Context context) {
        return getString(context, VERIFY_URL_KEY, VERIFY_URL);
    }

    //FIXME
    public static String getClientID(final Context context) {
        return "dbc9c2b1d18d49d8adacd23436c5281d";
    }

    //FIXME
    public static String getClientSecret(final Context context) {
        return "QqnTLCqLQxZYHgHUuobkNA9g950vXVYDMg8ETTXM";
    }

    private static String getString(final Context context, final String key, final String defaultValue) {
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sp.getString(key, defaultValue);
    }
}
