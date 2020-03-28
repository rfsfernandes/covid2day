package pt.covidtwoday.model.viewmodels;

import android.app.Application;
import android.content.Context;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import pt.covidtwoday.data.Repository;
import pt.covidtwoday.data.RequestCallBack;
import pt.covidtwoday.model.CountryData;
import pt.covidtwoday.model.WorldInfo;

public class MapViewModel extends AndroidViewModel {
  private final Context mContext;
  private Repository mRepository;

  public MutableLiveData<WorldInfo> worldLiveData = new MutableLiveData<>();
  public MutableLiveData<List<CountryData>> countryDataLiveData = new MutableLiveData<>();
  public MutableLiveData<String> errorLiveData = new MutableLiveData<>();

  /**
   * This méthod creates a new instance of the ViewModel.
   *
   * Use getApplicationContext
   * @param application
   */

  public MapViewModel(@NonNull Application application) {
    super(application);
    this.mContext = application.getApplicationContext();
    mRepository = Repository.getInstance(application);
  }

  public void getWorldData(){
    mRepository.getWorldNumbers(new RequestCallBack<WorldInfo>() {
      @Override
      public void responseSuccessful(WorldInfo responseObject) {
        worldLiveData.setValue(responseObject);
      }

      @Override
      public void responseFail(String error) {
        errorLiveData.setValue(error);
      }
    });
  }

  public void getCountriesData(){
    mRepository.getAllCountries(new RequestCallBack<List<CountryData>>() {
      @Override
      public void responseSuccessful(List<CountryData> responseObject) {
        countryDataLiveData.setValue(responseObject);
      }

      @Override
      public void responseFail(String error) {
        errorLiveData.setValue(error);
      }
    });
  }


}