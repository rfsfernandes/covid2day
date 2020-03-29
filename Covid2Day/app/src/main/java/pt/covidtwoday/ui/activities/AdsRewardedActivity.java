package pt.covidtwoday.ui.activities;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import pt.covidtwoday.R;
import pt.covidtwoday.custom.CovidTwoDayApp;
import pt.covidtwoday.custom.utils.UtilsClass;

import static pt.covidtwoday.custom.Constants.RESULT_NO_ADS;

public class AdsRewardedActivity extends AppCompatActivity implements RewardedVideoAdListener {
  private RewardedVideoAd mRewardedVideoAd;
  ProgressBar progressBarAds;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    int nightModeFlags =
        getResources().getConfiguration().uiMode &
            Configuration.UI_MODE_NIGHT_MASK;

    switch (nightModeFlags){
      case Configuration.UI_MODE_NIGHT_YES:
        UtilsClass.getInstance().setStatusBarDark(this, false);
        break;
      case Configuration.UI_MODE_NIGHT_NO:
        UtilsClass.getInstance().setStatusBarDark(this, true);
        break;
    }

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ads_rewarded);
    progressBarAds = findViewById(R.id.progressBarAds);
    progressBarAds.setIndeterminate(true);

    mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);

    mRewardedVideoAd.setRewardedVideoAdListener(this);
    loadRewardedVideoAd();

    if(mRewardedVideoAd.isLoaded()){
      mRewardedVideoAd.show();
    }

  }

  private void loadRewardedVideoAd() {
    mRewardedVideoAd.loadAd(getResources().getString(R.string.reward_ad),
        new AdRequest.Builder().build());
  }

  @Override
  public void onRewardedVideoAdLoaded() {
//    Toast.makeText(this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
    mRewardedVideoAd.show();
    progressBarAds.setIndeterminate(false);
    progressBarAds.setVisibility(View.INVISIBLE);
  }

  @Override
  public void onRewardedVideoAdOpened() {
  }

  @Override
  public void onRewardedVideoStarted() {
    mRewardedVideoAd.resume(this);

  }

  @Override
  public void onRewardedVideoAdClosed() {
    setResult(RESULT_CANCELED);
    finish();
  }

  @Override
  public void onRewarded(RewardItem rewardItem) {
    CovidTwoDayApp covidTwoDayApp = (CovidTwoDayApp) this.getApplication();
    covidTwoDayApp.setTokenAmount(rewardItem.getAmount());
    mRewardedVideoAd.destroy(this);
    setResult(RESULT_OK);
    finish();
  }

  @Override
  public void onRewardedVideoAdLeftApplication() {
    mRewardedVideoAd.pause(this);
  }

  @Override
  public void onRewardedVideoAdFailedToLoad(int i) {
    if(i == 3){
      CovidTwoDayApp covidTwoDayApp = (CovidTwoDayApp) this.getApplication();
      covidTwoDayApp.setTokenAmount(6);
      mRewardedVideoAd.destroy(this);
      setResult(RESULT_NO_ADS);
      finish();
    }else{
      loadRewardedVideoAd();
    }
  }

  @Override
  public void onRewardedVideoCompleted() {
//    Toast.makeText(this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
  }
}
