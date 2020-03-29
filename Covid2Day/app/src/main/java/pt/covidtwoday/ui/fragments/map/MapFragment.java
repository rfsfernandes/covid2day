package pt.covidtwoday.ui.fragments.map;

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

public class MapFragment extends Fragment implements OnMapReadyCallback {

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

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    mMapViewModel =
        ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MapViewModel.class);
    rootView = inflater.inflate(R.layout.fragment_map, container, false);
    mUnbinder = ButterKnife.bind(this, rootView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);

    handleProgress(true);

    MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
      @Override
      public void onInitializationComplete(InitializationStatus initializationStatus) {
      }
    });
    AdRequest adRequest = new AdRequest.Builder().build();
    adView.loadAd(adRequest);
    behavior = BottomSheetBehavior.from(cardViewSheetWorld);
    behavior.setHideable(true);

    return rootView;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initViewModel();
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
    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    if (getActivity() != null && getContext() != null) {
      googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getContext()));

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
              marker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.placeholder)));
            });
          }
        }
        if (getActivity() != null) {
          getActivity().runOnUiThread(() -> handleProgress(false));
        }
      }).start();
    });


    mMapViewModel.worldLiveData.observe(getActivity(), worldInfo -> {
      if(toShowWorld){
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

    if(Locale.getDefault().toString().contains("us") || Locale.getDefault().toString().contains(
        "US") || Locale.getDefault().toString().contains("en") || Locale.getDefault().toString().contains("EN") ){
      textViewUpdatedAt.setText(String.format(Locale.getDefault(),"Updated at: %d/%d/%d",
          calendar.get(Calendar.MONTH) + 1,
          calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR)));
    }else{
      textViewUpdatedAt.setText(String.format(Locale.getDefault(),"Updated at: %d/%d/%d",
          calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1,
          calendar.get(Calendar.YEAR)));
    }


    if(behavior != null) {
      behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    handleProgress(false);
  }

  private void handleProgress(boolean show) {
    progressBarMap.setIndeterminate(show);
    progressBarMap.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    toShowWorld = show;
  }

  @OnClick(R.id.linearLayoutWorldData)
  void getWorldInfo() {
    if(behavior.getState() == BottomSheetBehavior.STATE_EXPANDED && behavior != null){
      behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }else{
      handleProgress(true);
      mMapViewModel.getWorldData();
    }
  }
}