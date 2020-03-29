package pt.covidtwoday.ui.fragments.history;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Iterator;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import pt.covidtwoday.R;
import pt.covidtwoday.model.CountryHistory;

public class ListPagerFragment extends Fragment {

  private CountryHistory mCountryHistory;
  private Unbinder mUnbinder;

  public CountryHistory getCountryHistory() {
    return mCountryHistory;
  }

  public void setCountryHistory(CountryHistory countryHistory) {
    mCountryHistory = countryHistory;
  }

  public ListPagerFragment() {
    // Required empty public constructor
  }

  public static ListPagerFragment newInstance(CountryHistory countryHistory) {
    ListPagerFragment fragment = new ListPagerFragment();
    fragment.setCountryHistory(countryHistory);
    return fragment;
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_list_pager, container, false);
    mUnbinder = ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    new Thread(new Runnable() {
      @Override
      public void run() {
        Iterator deathsIterator = mCountryHistory.getTimeline().getDeaths().entrySet().iterator();
        while (deathsIterator.hasNext()){

        }
      }
    }).start();

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mUnbinder.unbind();
  }
}
