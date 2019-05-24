package org.dimensinfin.eveonline.neocom.app.adapters;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.dimensinfin.eveonline.neocom.auth.NeoComOAuth20;
import org.dimensinfin.eveonline.neocom.auth.NeoComRetrofitHTTP;
import org.dimensinfin.eveonline.neocom.datamngmt.MockInterceptor;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

/**
 * Created by Adam on 15/01/2018.
 */
public class NeoComRetrofitMock extends NeoComRetrofitHTTP {
	public NeoComRetrofitMock() {
		super();
	}

	protected Retrofit build() {
		OkHttpClient.Builder retrofitClient =
				new OkHttpClient.Builder()
						.addInterceptor(new MockInterceptor())
						.addInterceptor(chain -> {
							Request.Builder builder = chain.request().newBuilder()
									                          .addHeader("User-Agent", this.agent);
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
				                          .build();
		return new Retrofit.Builder()
				       .baseUrl(this.esiDataServerLocation)
				       .addConverterFactory(GSON_CONVERTER_FACTORY)
				       .client(httpClient)
				       .build();
	}

	// - B U I L D E R
	public static class Builder {
		protected NeoComRetrofitMock onConstruction;

		public Builder() {
			this.onConstruction = new NeoComRetrofitMock();
		}

		public Builder withNeoComOAuth20( final NeoComOAuth20 neoComOAuth20 ) {
			this.onConstruction.neoComOAuth20 = neoComOAuth20;
			return this;
		}

		public Builder withEsiServerLocation( final String esiDataServerLocation ) {
			this.onConstruction.esiDataServerLocation = esiDataServerLocation;
			return this;
		}

		public Builder withAgent( final String agent ) {
			this.onConstruction.agent = agent;
			return this;
		}

		public Builder withCacheDataFile( final File cacheDataFile ) {
			this.onConstruction.cacheDataFile = cacheDataFile;
			return this;
		}

		public Builder withCacheSize( final long cacheSize ) {
			this.onConstruction.cacheSize = cacheSize;
			return this;
		}

		public Builder withTimeout( final long timeout ) {
			this.onConstruction.timeout = timeout;
			return this;
		}

		public Retrofit build() {
			return this.onConstruction.build();
		}
	}
}
