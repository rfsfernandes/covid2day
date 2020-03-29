package pt.covidtwoday.ui.fragments.live_data;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.Unbinder;
import pt.covidtwoday.R;
import pt.covidtwoday.custom.CovidTwoDayApp;
import pt.covidtwoday.model.CountryData;
import pt.covidtwoday.model.viewmodels.LiveDataViewModel;
import pt.covidtwoday.ui.activities.AdsRewardedActivity;

import static android.app.Activity.RESULT_OK;
import static pt.covidtwoday.custom.Constants.REQUEST_CODE_ADS;
import static pt.covidtwoday.custom.Constants.RESULT_NO_ADS;

public class LiveDataFragment extends Fragment {

  //  Place ViewBinding here
  @BindView(R.id.autoCompleteTextView)
  AutoCompleteTextView mAutoCompleteTextView;
  @BindView(R.id.cardViewSheet)
  CardView cardViewSheet;
  @BindView(R.id.coordinatorLayoutSheet)
  CoordinatorLayout coordinatorLayoutSheet;
  @BindView(R.id.progressBar)
  ProgressBar progressBar;
  @BindView(R.id.textViewCases)
  TextView textViewCases;
  @BindView(R.id.textViewCasesToday)
  TextView textViewCasesToday;
  @BindView(R.id.textViewOverallDeaths)
  TextView textViewOverallDeaths;
  @BindView(R.id.textViewDeathsToday)
  TextView textViewDeathsToday;
  @BindView(R.id.textViewRecovered)
  TextView textViewRecovered;
  @BindView(R.id.textViewActiveCases)
  TextView textViewActiveCases;
  @BindView(R.id.textViewCriticalCases)
  TextView textViewCriticalCases;
  @BindView(R.id.textViewCasesPerMillion)
  TextView textViewCasesPerMillion;
  @BindView(R.id.textViewDeathsPerMillion)
  TextView textViewDeathsPerMillion;
  @BindView(R.id.textViewCountryName)
  TextView textViewCountryName;
  @BindView(R.id.imageViewCountryFlag)
  ImageView imageViewCountryFlag;


  //  Place static constants here

  //  Place constants here

  //  Place viarables here
  private LiveDataViewModel mLiveDataViewModel;

  private Unbinder mUnbinder;
  private BottomSheetBehavior behavior;
  private CovidTwoDayApp mApp;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    mLiveDataViewModel =
        ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(LiveDataViewModel.class);

    View root = inflater.inflate(R.layout.fragment_live_data, container, false);
    mUnbinder = ButterKnife.bind(this, root);
    behavior = BottomSheetBehavior.from(cardViewSheet);
    behavior.setHideable(true);
    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

    return root;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mApp = (CovidTwoDayApp) Objects.requireNonNull(getActivity()).getApplication();

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, mApp.getCountriesNameList());

    mAutoCompleteTextView.setAdapter(adapter);

    mAutoCompleteTextView.setOnItemClickListener((adapterView, view1, i, l) -> {
      mApp.setTokenAmount(mApp.getTokenAmount() - 1);
      if (mApp.getTokenAmount() <= 0) {
        startActivityForResult(new Intent(getActivity(), AdsRewardedActivity.class),
            REQUEST_CODE_ADS);
      } else {
        performSearch(mAutoCompleteTextView.getText().toString());
      }

    });

    initViewModel();
  }

  private void initViewModel(){
    mLiveDataViewModel.mCountryDataMutableLiveData.observe(Objects.requireNonNull(getActivity()), this::showBottomSheet);

    mLiveDataViewModel.errorLiveData.observe(getActivity(), errorMessage -> {
      handleProgress(false);
      new AlertDialog.Builder(getActivity())
          .setTitle(getResources().getString(R.string.splash_popup_title))
          .setMessage(getResources().getString(R.string.splash_popup_message))
          .setPositiveButton(getResources().getString(R.string.ok), (dialogInterface, i) -> dialogInterface.dismiss())
          .create().show();
    });

  }

  private void showBottomSheet(CountryData countryData) {
    if(getContext() != null){
      Glide.with(getContext()).load(countryData.getCountryInfo().getFlag()).into(imageViewCountryFlag);
      textViewCountryName.setText(countryData.getCountry());
      textViewActiveCases.setText(String.valueOf(countryData.getActive()));
      textViewCases.setText(String.valueOf(countryData.getCases()));
      textViewCasesToday.setText(String.valueOf(countryData.getTodayCases()));
      textViewOverallDeaths.setText(String.valueOf(countryData.getDeaths()));
      textViewRecovered.setText(String.valueOf(countryData.getRecovered()));
      textViewCasesPerMillion.setText(String.valueOf(countryData.getCasesPerOneMillion()));
      textViewDeathsPerMillion.setText(String.valueOf(countryData.getDeathsPerOneMillion()));
      textViewDeathsToday.setText(String.valueOf(countryData.getTodayDeaths()));
      textViewCriticalCases.setText(String.valueOf(countryData.getCritical()));
      mAutoCompleteTextView.clearFocus();
      handleProgress(false);
      behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

  }

  public static void hideKeyboard(Activity activity) {
    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

    View view = activity.getCurrentFocus();

    if (view == null) {
      view = new View(activity);
    }
    if (imm != null) {
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
  }


  @Override
  public void onDestroy() {
    super.onDestroy();
    if(mUnbinder != null){
      mUnbinder.unbind();
    }

  }

  @OnEditorAction(R.id.autoCompleteTextView)
  boolean onEditorAction(EditText editText, int actionId, KeyEvent keyEvent){
    if (actionId == EditorInfo.IME_ACTION_DONE) {

      mApp.setTokenAmount(mApp.getTokenAmount() - 1);
      if (mApp.getTokenAmount() <= 0) {
        startActivityForResult(new Intent(getActivity(), AdsRewardedActivity.class),
            REQUEST_CODE_ADS);
      } else {
        performSearch(mAutoCompleteTextView.getText().toString());
      }

      return true;
    }
    return false;
  }

  private void performSearch(String country){
    hideKeyboard(Objects.requireNonNull(getActivity()));
    handleProgress(true);
    mLiveDataViewModel.getCountryData(country);
  }

  private void handleProgress(boolean show){
    progressBar.setIndeterminate(show);
    progressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(requestCode == REQUEST_CODE_ADS){
      if(resultCode == RESULT_OK){
        performSearch(mAutoCompleteTextView.getText().toString());
      }else if(resultCode == RESULT_NO_ADS){
        if(getContext() != null){
          new AlertDialog.Builder(getContext())
              .setTitle(getResources().getString(R.string.no_ads_title))
              .setMessage(getResources().getString(R.string.no_ads_message))
              .setPositiveButton(getResources().getString(R.string.ok), (dialogInterface, i) -> {
                performSearch(mAutoCompleteTextView.getText().toString());
                dialogInterface.dismiss();
              }).create().show();
        }

      }else{
        if(getContext() != null){
          new AlertDialog.Builder(getContext())
              .setTitle(getResources().getString(R.string.ad_closed))
              .setMessage(getResources().getString(R.string.ad_closed_message))
              .setPositiveButton(getResources().getString(R.string.ok), (dialogInterface, i) -> {
                startActivityForResult(new Intent(getActivity(), AdsRewardedActivity.class),
                    REQUEST_CODE_ADS);
              })
              .setNegativeButton(getResources().getString(R.string.cancel), (dialogInterface, i) -> {
                mApp.setTokenAmount(3);
                performSearch(mAutoCompleteTextView.getText().toString());
                dialogInterface.dismiss();
              }).create().show();
        }

      }
    }

  }

}