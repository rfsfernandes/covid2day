package pt.covidtwoday.ui.fragments.map;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pt.covidtwoday.R;
import pt.covidtwoday.custom.CovidTwoDayApp;
import pt.covidtwoday.custom.map_info_window.CustomInfoWindowAdapter;
import pt.covidtwoday.custom.permissions.PermissionsCallBack;
import pt.covidtwoday.custom.permissions.PermissionsHandler;
import pt.covidtwoday.model.CountryData;
import pt.covidtwoday.model.WorldInfo;
import pt.covidtwoday.model.viewmodels.MapViewModel;
import pt.covidtwoday.ui.activities.AdsRewardedActivity;

import static android.app.Activity.RESULT_OK;
import static pt.covidtwoday.custom.Constants.REQUEST_CODE_ADS;
import static pt.covidtwoday.custom.Constants.RESULT_NO_ADS;

public class MapFragment extends Fragment implements OnMapReadyCallback,
    GoogleMap.OnMapLoadedCallback {

  //  Place ViewBinding here
  @BindView(R.id.mapView)
  MapView mapView;
  @BindView(R.id.progressBarMap)
  ProgressBar progressBarMap;
  @BindView(R.id.adView)
  AdView adView;
  @BindView(R.id.cardViewSheetWorld)
  CardView cardViewSheetWorld;

  @BindView(R.id.textViewWorldCases)
  TextView textViewWorldCases;
  @BindView(R.id.textViewWorldDeaths)
  TextView textViewWorldDeaths;
  @BindView(R.id.textViewWorldActive)
  TextView textViewWorldActive;
  @BindView(R.id.textViewWorldRecovered)
  TextView textViewWorldRecovered;
  @BindView(R.id.textViewUpdatedAt)
  TextView textViewUpdatedAt;
//  Place static constants here

//  Place constants here

  //  Place viarables here
  private MapViewModel mMapViewModel;
  private Unbinder mUnbinder;
  private GoogleMap mGoogleMap;
  private BottomSheetBehavior behavior;
  private View rootView;
  private boolean toShowWorld = false;
  private CovidTwoDayApp mApp;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    mMapViewModel =
        ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MapViewModel.class);
    rootView = inflater.inflate(R.layout.fragment_map, container, false);
    mUnbinder = ButterKnife.bind(this, rootView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);

    handleProgress(true);

//    MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
//      @Override
//      public void onInitializationComplete(InitializationStatus initializationStatus) {
//      }
//    });
    AdRequest adRequest = new AdRequest.Builder().build();
    mApp = (CovidTwoDayApp) getActivity().getApplication();
    adView.loadAd(adRequest);
    behavior = BottomSheetBehavior.from(cardViewSheetWorld);
    behavior.setHideable(true);

    return rootView;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

  }

  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
    mUnbinder.unbind();
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mGoogleMap = googleMap;
    LatLng latLng = new LatLng(40.999683, -35.025366);
    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    if (getActivity() != null && getContext() != null) {
      mGoogleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getContext()));
      initViewModel();
      mMapViewModel.getCountriesData();
    }
  }

  private void initViewModel() {
    mMapViewModel.countryDataLiveData.observe(Objects.requireNonNull(getActivity()), countryDataList -> {
      new Thread(() -> {
        for (CountryData countryData :
            countryDataList) {

          // Add a marker in Sydney and move the camera
          LatLng country = new LatLng(countryData.getCountryInfo().getLatitude(),
              countryData.getCountryInfo().getLongitude());

          MarkerOptions markerOptions = new MarkerOptions();
          markerOptions.position(country);
          markerOptions.title(countryData.getCountry());
          markerOptions.snippet(new Gson().toJson(countryData));

          if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
              Marker marker = MapFragment.this.mGoogleMap.addMarker(markerOptions);
              if (getContext() != null) {
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.placeholder)));
              }

            });
          }
        }
        if (getActivity() != null) {
          getActivity().runOnUiThread(() -> handleProgress(false));
        }
      }).start();
    });


    mMapViewModel.worldLiveData.observe(getActivity(), worldInfo -> {
      if (toShowWorld) {
        showWorldInfo(worldInfo);
      }

    });

    mMapViewModel.errorLiveData.observe(getActivity(), error -> {
      handleProgress(false);
      new AlertDialog.Builder(getActivity())
          .setTitle(getResources().getString(R.string.splash_popup_title))
          .setMessage(getResources().getString(R.string.splash_popup_message))
          .setPositiveButton(getResources().getString(R.string.ok), (dialogInterface, i) -> dialogInterface.dismiss())
          .create().show();
    });


  }

  private void showWorldInfo(WorldInfo worldInfo) {
    textViewWorldCases.setText(String.valueOf(worldInfo.getCases()));
    textViewWorldDeaths.setText(String.valueOf(worldInfo.getDeaths()));
    textViewWorldActive.setText(String.valueOf(worldInfo.getActive()));
    textViewWorldRecovered.setText(String.valueOf(worldInfo.getRecovered()));

    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(worldInfo.getUpdated());

    if (Locale.getDefault().toString().contains("us") || Locale.getDefault().toString().contains(
        "US") || Locale.getDefault().toString().contains("en") || Locale.getDefault().toString().contains("EN")) {
      textViewUpdatedAt.setText(String.format(Locale.getDefault(), "Updated at: %d/%d/%d",
          calendar.get(Calendar.MONTH) + 1,
          calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR)));
    } else {
      textViewUpdatedAt.setText(String.format(Locale.getDefault(), "Updated at: %d/%d/%d",
          calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1,
          calendar.get(Calendar.YEAR)));
    }


    if (behavior != null) {
      behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    handleProgress(false);
  }

  private void handleProgress(boolean show) {
    if (progressBarMap != null) {
      progressBarMap.setIndeterminate(show);
      progressBarMap.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
      toShowWorld = show;
    }

  }

  @OnClick(R.id.linearLayoutWorldData)
  void getWorldInfo() {
    if (getActivity() != null) {



      if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED && behavior != null) {
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
      } else {
        handleProgress(true);
        mApp.setTokenAmount(mApp.getTokenAmount() - 1);
        if (mApp.getTokenAmount() <= 0) {
          startActivityForResult(new Intent(getActivity(), AdsRewardedActivity.class),
              REQUEST_CODE_ADS);
        } else {
          mMapViewModel.getWorldData();
        }

      }

    }

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(requestCode == REQUEST_CODE_ADS){
      if(resultCode == RESULT_OK){
        mMapViewModel.getWorldData();
      }else if(resultCode == RESULT_NO_ADS){
        if(getContext() != null){
          new AlertDialog.Builder(getContext())
              .setTitle(getResources().getString(R.string.no_ads_title))
              .setMessage(getResources().getString(R.string.no_ads_message))
              .setPositiveButton(getResources().getString(R.string.ok), (dialogInterface, i) -> {
                mMapViewModel.getWorldData();
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
                mMapViewModel.getWorldData();
                dialogInterface.dismiss();
              }).create().show();
        }

      }
    }

  }

  @Override
  public void onMapLoaded() {

  }
}