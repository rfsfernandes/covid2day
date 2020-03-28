package pt.covidtwoday.model.viewmodels;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import pt.covidtwoday.data.Repository;
import pt.covidtwoday.data.RequestCallBack;
import pt.covidtwoday.model.CountryData;

public class SplashScreenViewModel extends AndroidViewModel {

  private final Context mContext;
  private Repository mRepository;

  public MutableLiveData<List<String>> contryListMutableLiveData = new MutableLiveData<>();
  public MutableLiveData<String> errorMutableLiveData = new MutableLiveData<>();

  /**
   * This méthod creates a new instance of the ViewModel.
   *
   * Use getApplicationContext
   * @param application
   */

  public SplashScreenViewModel(@NonNull Application application) {
    super(application);
    this.mContext = application.getApplicationContext();
    mRepository = Repository.getInstance(application);
  }

  public void getAllCountriesWithCases(){
    mRepository.getAllCountries(new RequestCallBack<List<CountryData>>() {
      @Override
      public void responseSuccessful(List<CountryData> responseObject) {
        List<String> tempCountry = new ArrayList<>();
        for (CountryData countryData : responseObject) {
          if (countryData.getCases() > 0){

            if(countryData.getCountry().equals("UK")){
              tempCountry.add("United Kingdom");
            }else{
              tempCountry.add(countryData.getCountry());
            }

          }
        }

        contryListMutableLiveData.setValue(tempCountry);

      }

      @Override
      public void responseFail(String error) {
        errorMutableLiveData.setValue(error);
      }
    });
  }

}