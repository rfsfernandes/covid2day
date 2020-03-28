package pt.covidtwoday.model.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import pt.covidtwoday.data.Repository;
import pt.covidtwoday.data.RequestCallBack;
import pt.covidtwoday.model.CountryData;

public class HomeViewModel extends AndroidViewModel {
  private final Context mContext;
  private final Repository mRepository;

  public MutableLiveData<CountryData> mCountryDataMutableLiveData = new MutableLiveData<>();
  public MutableLiveData<String> errorLiveData = new MutableLiveData<>();

  /**
   * This méthod creates a new instance of the ViewModel.
   *
   * Use getApplicationContext
   * @param application
   */

  public HomeViewModel(@NonNull Application application) {
    super(application);
    this.mContext = application.getApplicationContext();
    this.mRepository = Repository.getInstance(application);
  }

  public void getCountryData(String country){
    mRepository.getNumbersFromCountry(country, new RequestCallBack<CountryData>() {
      @Override
      public void responseSuccessful(CountryData responseObject) {
        mCountryDataMutableLiveData.setValue(responseObject);
      }

      @Override
      public void responseFail(String error) {
        errorLiveData.setValue(error);
      }
    });
  }

}