package pt.covidtwoday.ui.fragments.home;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnItemClick;
import butterknife.Unbinder;
import pt.covidtwoday.R;
import pt.covidtwoday.custom.CovidTwoDayApp;
import pt.covidtwoday.model.CountryData;
import pt.covidtwoday.model.viewmodels.HomeViewModel;
import pt.covidtwoday.model.viewmodels.SplashScreenViewModel;

public class HomeFragment extends Fragment {

  //  Place ViewBinding here
  @BindView(R.id.autoCompleteTextView)
  AutoCompleteTextView mAutoCompleteTextView;
  @BindView(R.id.cardViewSheet)
  CardView cardViewSheet;
  @BindView(R.id.coordinatorLayoutSheet)
  CoordinatorLayout coordinatorLayoutSheet;

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
  private HomeViewModel homeViewModel;
  private SplashScreenViewModel mSplashScreenViewModel;
  private Unbinder mUnbinder;
  private BottomSheetBehavior behavior;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    homeViewModel =
        ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(HomeViewModel.class);
    mSplashScreenViewModel = ViewModelProviders.of(getActivity()).get(SplashScreenViewModel.class);
    View root = inflater.inflate(R.layout.fragment_home, container, false);
    mUnbinder = ButterKnife.bind(this, root);
    behavior = BottomSheetBehavior.from(cardViewSheet);
    behavior.setHideable(true);
    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    return root;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    CovidTwoDayApp covidTwoDayApp = (CovidTwoDayApp) Objects.requireNonNull(getActivity()).getApplication();

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, covidTwoDayApp.getCountriesNameList());

    mAutoCompleteTextView.setAdapter(adapter);

    mAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        performSearch(mAutoCompleteTextView.getText().toString());
      }
    });

    initViewModel();
  }

  private void initViewModel(){
    homeViewModel.mCountryDataMutableLiveData.observe(Objects.requireNonNull(getActivity()), this::showBottomSheet);
  }

  private void showBottomSheet(CountryData countryData) {
    Glide.with(Objects.requireNonNull(getActivity())).load(countryData.getCountryInfo().getFlag()).into(imageViewCountryFlag);
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
    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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
    mUnbinder.unbind();
  }

  @OnEditorAction(R.id.autoCompleteTextView)
  boolean onEditorAction(EditText editText, int actionId, KeyEvent keyEvent){
    if (actionId == EditorInfo.IME_ACTION_DONE) {
      performSearch(mAutoCompleteTextView.getText().toString());
      return true;
    }
    return false;
  }

  private void performSearch(String country){
    hideKeyboard(Objects.requireNonNull(getActivity()));
    homeViewModel.getCountryData(country);
  }

}