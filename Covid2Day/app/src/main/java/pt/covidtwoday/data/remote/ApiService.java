package pt.covidtwoday.data.remote;

import java.util.List;

import pt.covidtwoday.model.CountryData;
import pt.covidtwoday.model.CountryHistory;
import pt.covidtwoday.model.WorldInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
  /**
   * User, for example:
   *
   * @POST("/api/v1/login") Call<ResponseObject> login(@Body User user, @Header("Language") String language);
   * <p>
   * To login using a given url.
   */

  @GET("all")
  Call<WorldInfo> getWorldNumbers();

  @GET("countries/{country}")
  Call<CountryData> getNumbersFromCountry(@Path("country") String country);

  @GET("countries")
  Call<List<CountryData>> getAllCountries();

  @GET("v2/historical/{country}")
  Call<CountryHistory> getHistoricalDataFromCountry(@Path("country") String country);

}