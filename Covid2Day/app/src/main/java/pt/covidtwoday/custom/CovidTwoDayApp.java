package pt.covidtwoday.custom;

import android.app.Application;


import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;

import pt.covidtwoday.R;
import pt.covidtwoday.model.CountryData;

public class CovidTwoDayApp extends Application {

  static List<String> countriesNameList = new ArrayList<>();
  static List<CountryData> countryDataList = new ArrayList<>();
  static int tokenAmount = 6;

  @Override
  public void onCreate() {
    super.onCreate();
    MobileAds.initialize(this, getResources().getString(R.string.add_app_id));
  }

  public List<CountryData> getCountryDataList() {
    return countryDataList;
  }

  public void setCountryDataList(List<CountryData> countryDataList) {
    CovidTwoDayApp.countryDataList = countryDataList;
  }

  public void setCountriesNameList(List<String> countriesNameList){
    CovidTwoDayApp.countriesNameList = countriesNameList;
  }

  public List<String> getCountriesNameList(){
    return CovidTwoDayApp.countriesNameList;
  }

  public int getTokenAmount(){
    return tokenAmount;
  }

  public void setTokenAmount(int tokenAmount) {
    CovidTwoDayApp.tokenAmount = tokenAmount;
  }

}
