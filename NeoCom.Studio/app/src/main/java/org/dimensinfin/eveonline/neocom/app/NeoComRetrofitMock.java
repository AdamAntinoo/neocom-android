package org.dimensinfin.eveonline.neocom.app;

import java.util.concurrent.TimeUnit;

import org.dimensinfin.eveonline.neocom.auth.NeoComRetrofitHTTP;
import org.dimensinfin.eveonline.neocom.datamngmt.MockInterceptor;

import okhttp3.Cache;
import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by Adam on 15/01/2018.
 */
public class NeoComRetrofitMock extends NeoComRetrofitHTTP {
	public NeoComRetrofitMock() {
		super();
	}

	public Retrofit build() {
		OkHttpClient.Builder retrofitClient =
				new OkHttpClient.Builder()
						.addInterceptor(new MockInterceptor());

		if (this.timeout != -1) {
			retrofitClient.readTimeout(this.timeout, TimeUnit.MILLISECONDS);
		}

		if (null != this.cacheDataFile) {
			retrofitClient.cache(new Cache(this.cacheDataFile, this.cacheSize));
		}

		OkHttpClient httpClient = retrofitClient
//				                          .certificatePinner(
//						                          new CertificatePinner.Singletoner()
//								                          .add("login.eveonline.com", "sha256/075pvb1KMqiPud6f347Lhzb0ALOY+dX5G7u+Yx+b8U4=")
//								                          .add("login.eveonline.com", "sha256/YLh1dUR9y6Kja30RrAn7JKnbQG/uEtLMkBgFF2Fuihg=")
//								                          .add("login.eveonline.com", "sha256/Vjs8r4z+80wjNcr1YKepWQboSIRi63WsWXhIMN+eWys=")
//								                          .build())
				                          .build();
		return new Retrofit.Builder()
				       .baseUrl(this.esiDataServerLocation)
				       .addConverterFactory(GSON_CONVERTER_FACTORY)
				       .client(httpClient)
				       .build();
	}
}
