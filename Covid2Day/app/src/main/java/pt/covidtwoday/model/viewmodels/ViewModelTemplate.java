package pt.covidtwoday.model.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


public class ViewModelTemplate extends AndroidViewModel {
	 private final Context mContext;

	 public MutableLiveData<Object> objectLiveData = new MutableLiveData<>();

	 /**
		* This méthod creates a new instance of the ViewModel.
		*
    * Use getApplicationContext
		* @param application
		*/

	 public ViewModelTemplate(@NonNull Application application) {
			super(application);
			this.mContext = application.getApplicationContext();
	 }


}