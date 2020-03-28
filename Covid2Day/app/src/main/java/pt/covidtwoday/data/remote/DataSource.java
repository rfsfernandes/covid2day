package pt.covidtwoday.data.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DataSource {

	 private static final String BASE_URL = "https://corona.lmao.ninja/";

	 private static final Retrofit retrofit = new Retrofit.Builder()
					 .baseUrl(BASE_URL)
					 .addConverterFactory(GsonConverterFactory.create())
					 .build();

	 private static ApiService sApiService;

	 public static ApiService getApiService() {
			if (sApiService == null) {
				 sApiService = retrofit.create(ApiService.class);
			}
			return sApiService;
	 }

}