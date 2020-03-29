package pt.covidtwoday.model.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import pt.covidtwoday.custom.utils.UtilsClass;
import pt.covidtwoday.data.Repository;
import pt.covidtwoday.data.RequestCallBack;
import pt.covidtwoday.model.CountryHistory;

public class HistoryViewModel extends AndroidViewModel {
  private final Context mContext;
  private final Repository mRepository;
  public MutableLiveData<CountryHistory> mCountryHistoryMutableLiveData = new MutableLiveData<>();
  public MutableLiveData<String> errorMutatableLiveData = new MutableLiveData<>();

  /**
   * This méthod creates a new instance of the ViewModel.
   *
   * Use getApplicationContext
   * @param application
   */

  public HistoryViewModel(@NonNull Application application) {
    super(application);
    this.mContext = application.getApplicationContext();
    mRepository = Repository.getInstance(application);
  }

  public void getHistoricalDataFromCountry(String country){
    mRepository.getHistoricalDataFromCountry(country, new RequestCallBack<CountryHistory>() {
      @Override
      public void responseSuccessful(CountryHistory responseObject) {
        new Thread(new Runnable() {
          @Override
          public void run() {
            responseObject.getTimeline().setCases(UtilsClass.getInstance().sortByValues(responseObject.getTimeline().getCases()));
            responseObject.getTimeline().setDeaths(UtilsClass.getInstance().sortByValues(responseObject.getTimeline().getDeaths()));
            mCountryHistoryMutableLiveData.postValue(responseObject);
          }
        }).start();

      }

      @Override
      public void responseFail(String error) {
        errorMutatableLiveData.setValue(error);
      }
    });
  }


}