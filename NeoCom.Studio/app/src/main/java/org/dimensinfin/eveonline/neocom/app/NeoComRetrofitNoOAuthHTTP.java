package org.dimensinfin.eveonline.neocom.app;

import java.util.concurrent.TimeUnit;

import org.dimensinfin.eveonline.neocom.auth.NeoComRetrofitHTTP;
import org.dimensinfin.eveonline.neocom.datamngmt.GlobalDataManager;
import org.dimensinfin.eveonline.neocom.interfaces.IConfigurationProvider;

import okhttp3.Cache;
import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Retrofit;

/**
 * Created by Adam on 15/01/2018.
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class NeoComRetrofitNoOAuthHTTP extends NeoComRetrofitHTTP {
	//	public static class GSONDateTimeDeserializer implements com.google.gson.JsonDeserializer<DateTime> {
	//
	//		@Override
	//		public DateTime deserialize (
	//				com.google.gson.JsonElement element,
	//				Type arg1,
	//				com.google.gson.JsonDeserializationContext arg2) throws JsonParseException {
	//			String date = element.getAsString();
	//			return DateTime.parse(date);
	//		}
	//	}
	//
	//	public static class GSONLocalDateDeserializer implements com.google.gson.JsonDeserializer<LocalDate> {
	//
	//		private static final DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
	//
	//		@Override
	//		public LocalDate deserialize (
	//				com.google.gson.JsonElement element,
	//				Type arg1,
	//				com.google.gson.JsonDeserializationContext arg2) throws JsonParseException {
	//			String date = element.getAsString();
	//			return LocalDate.parse(date, format);
	//		}
	//	}
	//
	//	private static final Converter.Factory GSON_CONVERTER_FACTORY =
	//			GsonConverterFactory.create(
	//					new GsonBuilder()
	//							.registerTypeAdapter(DateTime.class, new GSONDateTimeDeserializer())
	//							.registerTypeAdapter(LocalDate.class, new GSONLocalDateDeserializer())
	//							.create());
	//	private static String refreshToken = "";
	//
	//	private static String getRefreshToken () {
	//		return refreshToken;
	//	}
	//
	//	public static void setRefeshToken (final String token) {
	//		refreshToken = token;
	//	}
	//
	//	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger logger = LoggerFactory.getLogger(NeoComRetrofitNoOAuthHTTP.class);

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	//	public NeoComRetrofitNoOAuthHTTP() {
	//		super();
	//	}

	//	public NeoComRetrofitNoOAuthHTTP() {
	//	}

	public NeoComRetrofitNoOAuthHTTP( final IConfigurationProvider configurationProvider ) {
		super(configurationProvider);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	//	public static Retrofit build (final String refresh, final NeoComOAuth20 auth, final String agent, final File cache
	//			, final long cacheSize
	//			, final long timeout) {
	//		NeoComRetrofitNoOAuthHTTP.setRefeshToken(refresh);
	//		return build(auth, agent, cache, cacheSize, timeout);
	//	}

	public Retrofit build() {
		OkHttpClient.Builder retrofitClient =
				new OkHttpClient.Builder()
						.addInterceptor(chain -> {
							Request.Builder builder = chain.request().newBuilder()
									                          .addHeader("User-Agent", this.agent);
							return chain.proceed(builder.build());
						})
						.addInterceptor(chain -> {
							if (StringUtils.isBlank(getRefreshToken())) {
								return chain.proceed(chain.request());
							}

							Response r = chain.proceed(chain.request());
							if (r.isSuccessful()) {
								return r;
							}
							return r;
						});

		if (this.timeout != -1) {
			retrofitClient.readTimeout(this.timeout, TimeUnit.MILLISECONDS);
		}

		if (null != this.cacheDataFile) {
			retrofitClient.cache(new Cache(this.cacheDataFile, this.cacheSize));
		}

		OkHttpClient httpClient = retrofitClient
				                          .certificatePinner(
						                          new CertificatePinner.Builder()
								                          .add("login.eveonline.com", "sha256/075pvb1KMqiPud6f347Lhzb0ALOY+dX5G7u+Yx+b8U4=")
								                          .add("login.eveonline.com", "sha256/YLh1dUR9y6Kja30RrAn7JKnbQG/uEtLMkBgFF2Fuihg=")
								                          .add("login.eveonline.com", "sha256/Vjs8r4z+80wjNcr1YKepWQboSIRi63WsWXhIMN+eWys=")
								                          .build())
				                          .build();
		return new Retrofit.Builder()
				       .baseUrl(GlobalDataManager.getResourceString("R.esi.data.server.location"))
				       .addConverterFactory(GSON_CONVERTER_FACTORY)
				       .client(httpClient)
				       .build();
	}
}
