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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pt.covidtwoday.R;
import pt.covidtwoday.custom.HistoryRecyclerDecoration;
import pt.covidtwoday.model.CountryHistory;
import pt.covidtwoday.model.HistoryListModel;
import pt.covidtwoday.ui.fragments.history.adapter.HistoryListAdapter;

public class ListPagerFragment extends Fragment {

  @BindView(R.id.recyclerViewHistory)
  RecyclerView recyclerViewHistory;


  private CountryHistory mCountryHistory;
  private Unbinder mUnbinder;
  private HistoryListAdapter mHistoryListAdapter;

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
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    linearLayoutManager.setStackFromEnd(true);
    linearLayoutManager.setReverseLayout(true);
    recyclerViewHistory.setLayoutManager(linearLayoutManager);
    mHistoryListAdapter = new HistoryListAdapter(getContext());
    recyclerViewHistory.setAdapter(mHistoryListAdapter);
    recyclerViewHistory.addItemDecoration(new HistoryRecyclerDecoration(getContext(),
        R.drawable.recycler_divider));
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    if(mCountryHistory != null){
      new Thread(() -> {
        double lastDeathNumber = 0;
        double lastCaseNumber = 0;

        Iterator casesIterator = mCountryHistory.getTimeline().getCases().entrySet().iterator();
        Iterator deathsIterator = mCountryHistory.getTimeline().getDeaths().entrySet().iterator();

        List<HistoryListModel> historyListModelList = new ArrayList<>();

        while (deathsIterator.hasNext()){
          Map.Entry entryDeaths = (Map.Entry) deathsIterator.next();
          Map.Entry entryCases = (Map.Entry) casesIterator.next();

          String date = entryDeaths.getKey().toString();
          String[] stringArray = date.split("/");

          if (Locale.getDefault().toString().contains("us") || Locale.getDefault().toString().contains(
              "US") || Locale.getDefault().toString().contains("en") || Locale.getDefault().toString().contains("EN")) {
            date = stringArray[0] + "/" + stringArray[1] + "/" + stringArray[2];
          } else {
            date = stringArray[1] + "/" + stringArray[0] + "/" + stringArray[2];
          }

          double actualNumberOfDeaths = Integer.parseInt(entryDeaths.getValue().toString());
          double actualNumberOfCases = Integer.parseInt(entryCases.getValue().toString());

          double actualMinusLastDeaths = actualNumberOfDeaths - lastDeathNumber;
          double actualMinusLastCases = actualNumberOfCases - lastCaseNumber;

          double actualMinusLastDeathsDivedByActual =
              actualMinusLastDeaths / actualNumberOfDeaths;
          double actualMinusLastCasesDivedByActual =
              actualMinusLastCases / actualNumberOfCases;

          double deathPercentage = actualMinusLastDeathsDivedByActual * 100;
          double casePercentage = actualMinusLastCasesDivedByActual * 100;

          lastCaseNumber = actualNumberOfCases;
          lastDeathNumber = actualNumberOfDeaths;

          historyListModelList.add(new HistoryListModel(date, actualNumberOfCases,
              actualNumberOfDeaths, casePercentage, deathPercentage));

          if(getActivity() != null ){
            getActivity().runOnUiThread(() -> mHistoryListAdapter.refreshList(historyListModelList));
          }

        }
      }).start();
    }

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mUnbinder.unbind();
  }
}
