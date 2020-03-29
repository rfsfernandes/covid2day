package pt.covidtwoday.ui.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import butterknife.Unbinder;
import pt.covidtwoday.R;
import pt.covidtwoday.custom.CovidTwoDayApp;
import pt.covidtwoday.custom.utils.UtilsClass;
import pt.covidtwoday.model.viewmodels.SplashScreenViewModel;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.material.snackbar.Snackbar;

public class SplashScreenActivity extends AppCompatActivity {

  private SplashScreenViewModel mSplashScreenViewModel;
  private int counter = 0;
  private boolean canGoToMain = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    int nightModeFlags =
        getResources().getConfiguration().uiMode &
            Configuration.UI_MODE_NIGHT_MASK;

    switch (nightModeFlags) {
      case Configuration.UI_MODE_NIGHT_YES:
        UtilsClass.getInstance().setStatusBarDark(this, false);
        break;
      case Configuration.UI_MODE_NIGHT_NO:
        UtilsClass.getInstance().setStatusBarDark(this, true);
        break;

    }
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash_screen);
    mSplashScreenViewModel = ViewModelProviders.of(this).get(SplashScreenViewModel.class);
    initViewModel();
  }

  public void initViewModel() {

    mSplashScreenViewModel.contryListMutableLiveData.observe(this, stringList -> {
      canGoToMain = true;

      CovidTwoDayApp covidTwoDayApp = (CovidTwoDayApp) this.getApplication();

      covidTwoDayApp.setCountriesNameList(stringList);
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          if (canGoToMain) {
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            finish();
          }
        }
      }, 1000);
      counter = 0;
    });

    mSplashScreenViewModel.errorMutableLiveData.observe(this, error -> {
      canGoToMain = false;
      counter++;
      if (counter == 10) {
        new AlertDialog.Builder(this)
            .setTitle(getResources().getString(R.string.splash_popup_title))
            .setMessage(getResources().getString(R.string.splash_popup_message))
            .setPositiveButton(getResources().getString(R.string.ok), (dialogInterface, i) -> {
              dialogInterface.dismiss();

              new Handler().postDelayed(SplashScreenActivity.this::finishAndRemoveTask, 500);

            })
            .create().show();
      } else {
        mSplashScreenViewModel.getAllCountriesWithCases();
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    mSplashScreenViewModel.getAllCountriesWithCases();
//    startActivity(new Intent(this, AdsRewardedActivity.class));


  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

  }
}
