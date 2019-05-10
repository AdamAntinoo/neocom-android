package org.dimensinfin.eveonline.neocom.app;

import java.io.File;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import org.dimensinfin.eveonline.neocom.auth.NeoComOAuth20;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManager;
import org.dimensinfin.eveonline.neocom.datamngmt.MockInterceptor;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import okhttp3.Cache;
import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Adam on 15/01/2018.
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class NeoComRetrofitMock {
	public static class GSONDateTimeDeserializer implements com.google.gson.JsonDeserializer<DateTime> {

		@Override
		public DateTime deserialize (
				com.google.gson.JsonElement element,
				Type arg1,
				com.google.gson.JsonDeserializationContext arg2) throws JsonParseException {
			String date = element.getAsString();
			return DateTime.parse(date);
		}
	}

	public static class GSONLocalDateDeserializer implements com.google.gson.JsonDeserializer<LocalDate> {

		private static final DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");

		@Override
		public LocalDate deserialize (
				com.google.gson.JsonElement element,
				Type arg1,
				com.google.gson.JsonDeserializationContext arg2) throws JsonParseException {
			String date = element.getAsString();
			return LocalDate.parse(date, format);
		}
	}

	private static final Converter.Factory GSON_CONVERTER_FACTORY =
			GsonConverterFactory.create(
					new GsonBuilder()
							.registerTypeAdapter(DateTime.class, new GSONDateTimeDeserializer())
							.registerTypeAdapter(LocalDate.class, new GSONLocalDateDeserializer())
							.create());
	private static String refreshToken = "";

	private static String getRefreshToken () {
		return refreshToken;
	}

	public static void setRefeshToken (final String token) {
		refreshToken = token;
	}

	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger logger = LoggerFactory.getLogger(NeoComRetrofitMock.class);

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeoComRetrofitMock() {
		super();
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public static Retrofit build (final String refresh, final NeoComOAuth20 auth, final String agent, final File cache
			, final long cacheSize
			, final long timeout) {
		NeoComRetrofitMock.setRefeshToken(refresh);
		return build(auth, agent, cache, cacheSize, timeout);
	}

	public static Retrofit build (final NeoComOAuth20 auth, final String agent, final File cache
			, final long cacheSize
			, final long timeout) {

		OkHttpClient.Builder retrofitClient =
				new OkHttpClient.Builder()
						.addInterceptor(new MockInterceptor());

		if ( timeout != -1 ) {
			retrofitClient.readTimeout(timeout, TimeUnit.MILLISECONDS);
		}

		if ( null != cache ) {
			retrofitClient.cache(new Cache(cache, cacheSize));
		}

		OkHttpClient httpClient = retrofitClient
				.certificatePinner(
						new CertificatePinner.Builder()
								.add("login.eveonline.com", "sha256/075pvb1KMqiPud6f347Lhzb0ALOY+dX5G7u+Yx+b8U4=")
								.add("login.eveonline.com", "sha256/YLh1dUR9y6Kja30RrAn7JKnbQG/uEtLMkBgFF2Fuihg=")
								.add("login.eveonline.com", "sha256/Vjs8r4z+80wjNcr1YKepWQboSIRi63WsWXhIMN+eWys=")
								.build())
				.build();
		return
				new Retrofit.Builder()
						.baseUrl(GlobalDataManager.getResourceString("R.esi.data.server.location.mock"))
						.addConverterFactory(GSON_CONVERTER_FACTORY)
						.client(httpClient)
						.build();
	}
}
