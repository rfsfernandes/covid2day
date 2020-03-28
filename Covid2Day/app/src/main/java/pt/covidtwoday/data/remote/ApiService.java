package pt.covidtwoday.data.remote;

import java.util.List;

import pt.covidtwoday.model.CountryData;
import pt.covidtwoday.model.CountryHistory;
import pt.covidtwoday.model.WorldInfo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

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