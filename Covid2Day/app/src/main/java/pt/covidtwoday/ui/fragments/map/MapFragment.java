package pt.covidtwoday.ui.fragments.map;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pt.covidtwoday.R;
import pt.covidtwoday.custom.CovidTwoDayApp;
import pt.covidtwoday.custom.map_info_window.CustomInfoWindowAdapter;
import pt.covidtwoday.custom.permissions.PermissionsCallBack;
import pt.covidtwoday.custom.permissions.PermissionsHandler;
import pt.covidtwoday.model.CountryData;
import pt.covidtwoday.model.viewmodels.MapViewModel;

public class MapFragment extends Fragment implements OnMapReadyCallback {

//  Place ViewBinding here
  @BindView(R.id.mapView)
  MapView mapView;
  @BindView(R.id.progressBarMap)
  ProgressBar progressBarMap;
  @BindView(R.id.adView)
  AdView adView;

//  Place static constants here

//  Place constants here

//  Place viarables here
  private MapViewModel mMapViewModel;
  private Unbinder mUnbinder;
  private GoogleMap mGoogleMap;


  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    mMapViewModel =
        ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MapViewModel.class);
    View root = inflater.inflate(R.layout.fragment_map, container, false);
    mUnbinder = ButterKnife.bind(this, root);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
    initViewModel();
    handleProgress(true);
    MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
      @Override
      public void onInitializationComplete(InitializationStatus initializationStatus) {
      }
    });
    AdRequest adRequest = new AdRequest.Builder().build();
    adView.loadAd(adRequest);
    return root;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

  }

  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
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
    if(getActivity() != null && getContext() != null){
      googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getContext()));

      mMapViewModel.getCountriesData();

    }


  }

  private void initViewModel(){
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

          if(getActivity()!=null){
            getActivity().runOnUiThread(() -> {
              Marker marker = MapFragment.this.mGoogleMap.addMarker(markerOptions);
              marker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.placeholder)));
            });
          }
        }
        if(getActivity() != null){
          getActivity().runOnUiThread(() -> handleProgress(false));
        }
      }).start();
    });

  }

  private void handleProgress(boolean show){
    progressBarMap.setIndeterminate(show);
    progressBarMap.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
  }

}