package pt.covidtwoday.data;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import pt.covidtwoday.data.local.AppDatabase;
import pt.covidtwoday.data.remote.ApiService;
import pt.covidtwoday.data.remote.DataSource;
import pt.covidtwoday.model.CountryData;
import pt.covidtwoday.model.CountryHistory;
import pt.covidtwoday.model.WorldInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

  private static Repository INSTANCE;

  private Application application;
  private AppDatabase appDatabase;
  private ApiService mApiService;

  private Repository(Application application) {
    this.mApiService = DataSource.getApiService();
    this.application = application;
    appDatabase = AppDatabase.getInstance(application.getApplicationContext());
  }

  public static Repository getInstance(@NonNull Application application) {
    if (INSTANCE == null) {
      synchronized (Repository.class) {
        if (INSTANCE == null) {
          INSTANCE = new Repository(application);
        }
      }
    }
    return INSTANCE;
  }

  public void getWorldNumbers(final RequestCallBack<WorldInfo> callBack){
    Call<WorldInfo> call = mApiService.getWorldNumbers();

    call.enqueue(new Callback<WorldInfo>() {
      @Override
      public void onResponse(Call<WorldInfo> call, Response<WorldInfo> response) {
        if(response.isSuccessful()){
          WorldInfo worldInfo = response.body();
          if(worldInfo != null){
            callBack.responseSuccessful(worldInfo);
          }
        }else{
          callBack.responseFail(response.toString());
        }
      }

      @Override
      public void onFailure(Call<WorldInfo> call, Throwable t) {
        callBack.responseFail(t.getMessage());
      }
    });

  }

  public void getNumbersFromCountry(String country, final RequestCallBack<CountryData> callBack){
    Call<CountryData> call = mApiService.getNumbersFromCountry(country);

    call.enqueue(new Callback<CountryData>() {
      @Override
      public void onResponse(Call<CountryData> call, Response<CountryData> response) {
        if(response.isSuccessful()){
          CountryData countryData = response.body();

          if(countryData != null){
            callBack.responseSuccessful(countryData);
          }

        }else{
          callBack.responseFail(response.toString());
        }
      }

      @Override
      public void onFailure(Call<CountryData> call, Throwable t) {
        callBack.responseFail(t.getMessage());
      }
    });

  }

  public void getAllCountries(final RequestCallBack<List<CountryData>> callBack){
    Call<List<CountryData>> call = mApiService.getAllCountries();

    call.enqueue(new Callback<List<CountryData>>() {
      @Override
      public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
        if(response.isSuccessful()){
          List<CountryData> countryDataList = response.body();

          if(countryDataList != null){
            callBack.responseSuccessful(countryDataList);
          }

        }else{
          callBack.responseFail(response.toString());
        }
      }

      @Override
      public void onFailure(Call<List<CountryData>> call, Throwable t) {
        callBack.responseFail(t.getMessage());
      }
    });
  }

  public void getHistoricalDataFromCountry(String country,
                                           final RequestCallBack<CountryHistory> callBack){
    Call<CountryHistory> call = mApiService.getHistoricalDataFromCountry(country);

    call.enqueue(new Callback<CountryHistory>() {
      @Override
      public void onResponse(Call<CountryHistory> call, Response<CountryHistory> response) {
        if(response.isSuccessful()){
          CountryHistory countryHistory = response.body();

          if(countryHistory != null){
            callBack.responseSuccessful(countryHistory);
          }

        }else{
          callBack.responseFail(response.toString());
        }
      }

      @Override
      public void onFailure(Call<CountryHistory> call, Throwable t) {
        callBack.responseFail(t.getMessage());
      }
    });
  }

}
