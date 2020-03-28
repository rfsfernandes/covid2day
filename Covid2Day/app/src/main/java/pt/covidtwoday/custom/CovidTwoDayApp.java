package pt.covidtwoday.custom;

import android.app.Application;



import java.util.ArrayList;
import java.util.List;

public class CovidTwoDayApp extends Application {

  static List<String> countriesNameList = new ArrayList<>();

  @Override
  public void onCreate() {
    super.onCreate();

  }

  public void setCountriesNameList(List<String> countriesNameList){
    CovidTwoDayApp.countriesNameList = countriesNameList;
  }

  public List<String> getCountriesNameList(){
    return CovidTwoDayApp.countriesNameList;
  }

}
