package pt.covidtwoday.custom.map_info_window;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;

import pt.covidtwoday.R;
import pt.covidtwoday.model.CountryData;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

  private final View mWindow;
  private Context mContext;

  public CustomInfoWindowAdapter(Context context) {
    mContext = context;
    mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window_map, null);
  }

  private void renderWindow(Marker marker, View view){
    if(marker.getSnippet() != null){
      CountryData countryData = new Gson().fromJson(marker.getSnippet(), CountryData.class);

      TextView textViewCountryName = view.findViewById(R.id.textViewCountryTitle);
      TextView textViewCases = view.findViewById(R.id.textViewCases);
      TextView textViewDeaths = view.findViewById(R.id.textViewDeaths);

      textViewCountryName.setText(countryData.getCountry());
      textViewCases.setText(String.valueOf(countryData.getCases()));
      textViewDeaths.setText(String.valueOf(countryData.getDeaths()));
    }

  }


  @Override
  public View getInfoWindow(Marker marker) {
    renderWindow(marker, mWindow);
    return mWindow;
  }

  @Override
  public View getInfoContents(Marker marker) {
    renderWindow(marker, mWindow);
    return mWindow;
  }

}
