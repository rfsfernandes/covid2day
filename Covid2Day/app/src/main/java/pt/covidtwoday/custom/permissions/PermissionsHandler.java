package pt.covidtwoday.custom.permissions;

import android.Manifest;
import android.app.Activity;
import android.app.Application;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import androidx.annotation.NonNull;
import pt.covidtwoday.data.Repository;
import pt.covidtwoday.data.local.AppDatabase;
import pt.covidtwoday.data.remote.ApiService;
import pt.covidtwoday.data.remote.DataSource;

public class PermissionsHandler {
  private static PermissionsHandler INSTANCE;

  private Application application;
  private AppDatabase appDatabase;
  private ApiService mApiService;

  private PermissionsHandler(Application application) {
    this.mApiService = DataSource.getApiService();
    this.application = application;
    appDatabase = AppDatabase.getInstance(application.getApplicationContext());
  }

  public static PermissionsHandler getInstance(@NonNull Application application) {
    if (INSTANCE == null) {
      synchronized (Repository.class) {
        if (INSTANCE == null) {
          INSTANCE = new PermissionsHandler(application);
        }
      }
    }
    return INSTANCE;
  }

  public void askPermissionLocation(Activity activity, PermissionsCallBack callBack){
    Dexter.withActivity(activity).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        .withListener(new PermissionListener() {
          @Override
          public void onPermissionGranted(PermissionGrantedResponse response) {
            callBack.onPermissionGranted();
          }

          @Override
          public void onPermissionDenied(PermissionDeniedResponse response) {
            if(response.isPermanentlyDenied()){
              callBack.onPermissionPermanentlyDenied();
            }else{
              callBack.onPermissionDenied();
            }
          }

          @Override
          public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
            token.continuePermissionRequest();
          }
        })
        .check();
  }

}
