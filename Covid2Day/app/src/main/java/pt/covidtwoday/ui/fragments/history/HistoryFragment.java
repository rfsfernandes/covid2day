package pt.covidtwoday.ui.fragments.history;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.ProgressBar;

import com.anychart.AnyChartView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.Unbinder;
import pt.covidtwoday.R;
import pt.covidtwoday.custom.CovidTwoDayApp;
import pt.covidtwoday.model.viewmodels.HistoryViewModel;
import pt.covidtwoday.ui.activities.AdsRewardedActivity;
import pt.covidtwoday.ui.fragments.history.adapter.HistoryPagerAdapter;

import static android.app.Activity.RESULT_OK;
import static pt.covidtwoday.custom.Constants.REQUEST_CODE_ADS;
import static pt.covidtwoday.custom.Constants.RESULT_NO_ADS;

public class HistoryFragment extends Fragment implements AnyChartView.OnRenderedListener {

  //  Place ViewBinding here
  @BindView(R.id.viewPagerHistory)
  ViewPager viewPagerHistory;
  @BindView(R.id.tabLayout)
  TabLayout tabLayout;
  @BindView(R.id.cardViewSheet)
  CardView cardViewSheet;
  @BindView(R.id.autoCompleteTextView)
  AutoCompleteTextView autoCompleteTextView;
  @BindView(R.id.progressBar)
  ProgressBar progressBar;
  //   Place static constants here

  //  Place constants here
  BottomSheetBehavior mSheetBehavior;

  //  Place viarables here
  private HistoryViewModel mHistoryViewModel;
  private Unbinder mUnbinder;
  private CovidTwoDayApp mApp;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    mHistoryViewModel =
        ViewModelProviders.of(this).get(HistoryViewModel.class);
    View root = inflater.inflate(R.layout.fragment_history, container, false);
    mUnbinder = ButterKnife.bind(this, root);
    initViewModel();
    mSheetBehavior = BottomSheetBehavior.from(cardViewSheet);
    mSheetBehavior.setHideable(true);
    return root;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    CovidTwoDayApp covidTwoDayApp = (CovidTwoDayApp) Objects.requireNonNull(getActivity()).getApplication();

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, covidTwoDayApp.getCountriesNameList());

    mApp = (CovidTwoDayApp) Objects.requireNonNull(getActivity()).getApplication();


    autoCompleteTextView.setAdapter(adapter);

    autoCompleteTextView.setOnItemClickListener((adapterView, view1, i, l) -> {
      mApp.setTokenAmount(mApp.getTokenAmount() - 1);
      if (mApp.getTokenAmount() <= 0) {
        startActivityForResult(new Intent(getActivity(), AdsRewardedActivity.class),
            REQUEST_CODE_ADS);
      } else {
        performSearch(autoCompleteTextView.getText().toString());
      }

    });

    initViewModel();

  }

  @Override
  public void onResume() {
    super.onResume();
    mSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mUnbinder.unbind();
  }

  private void initViewModel() {
    if (getActivity() != null) {

      mHistoryViewModel.mCountryHistoryMutableLiveData.observe(getViewLifecycleOwner(),
          countryHistory -> {

            HistoryPagerAdapter historyPagerAdapter =
                new HistoryPagerAdapter(getParentFragmentManager(),
                    FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            historyPagerAdapter.setCountryHistory(countryHistory);
            historyPagerAdapter.setContext(getContext());
            historyPagerAdapter.setOnRenderedListener(this);
            tabLayout.setupWithViewPager(viewPagerHistory);
            viewPagerHistory.setAdapter(historyPagerAdapter);
            mSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

          });


      mHistoryViewModel.errorMutatableLiveData.observe(getViewLifecycleOwner(), error -> {
        handleProgress(false);
        Snackbar.make(getView(), error, BaseTransientBottomBar.LENGTH_LONG).show();
      });
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

  private void performSearch(String country) {
    hideKeyboard(Objects.requireNonNull(getActivity()));
    handleProgress(true);
    mHistoryViewModel.getHistoricalDataFromCountry(country);
  }

  @OnEditorAction(R.id.autoCompleteTextView)
  boolean onEditorAction(EditText editText, int actionId, KeyEvent keyEvent){
    if (actionId == EditorInfo.IME_ACTION_DONE) {
      mApp.setTokenAmount(mApp.getTokenAmount() - 1);
      if (mApp.getTokenAmount() <= 0) {
        startActivityForResult(new Intent(getActivity(), AdsRewardedActivity.class),
            REQUEST_CODE_ADS);
      } else {
        performSearch(autoCompleteTextView.getText().toString());
      }
      return true;
    }
    return false;
  }

  private void handleProgress(boolean show) {
    progressBar.setIndeterminate(show);
    progressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
  }

  @Override
  public void onRendered() {
    handleProgress(false);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(requestCode == REQUEST_CODE_ADS){
      if(resultCode == RESULT_OK){
        performSearch(autoCompleteTextView.getText().toString());
      }else if(resultCode == RESULT_NO_ADS){
        if(getContext() != null){
          new AlertDialog.Builder(getContext())
              .setTitle(getResources().getString(R.string.no_ads_title))
              .setMessage(getResources().getString(R.string.no_ads_message))
              .setPositiveButton(getResources().getString(R.string.ok), (dialogInterface, i) -> {
                performSearch(autoCompleteTextView.getText().toString());
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
                performSearch(autoCompleteTextView.getText().toString());
                dialogInterface.dismiss();
              }).create().show();
        }

      }
    }

  }

}