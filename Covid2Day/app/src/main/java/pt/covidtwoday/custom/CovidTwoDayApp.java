package pt.covidtwoday.custom;

import android.app.Application;



import java.util.ArrayList;
import java.util.List;

import pt.covidtwoday.model.CountryData;

public class CovidTwoDayApp extends Application {

  static List<String> countriesNameList = new ArrayList<>();
  static List<CountryData> countryDataList = new ArrayList<>();

  @Override
  public void onCreate() {
    super.onCreate();

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

}
