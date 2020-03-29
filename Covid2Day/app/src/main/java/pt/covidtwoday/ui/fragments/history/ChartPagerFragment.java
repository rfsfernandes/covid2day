package pt.covidtwoday.ui.fragments.history;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pt.covidtwoday.R;
import pt.covidtwoday.model.CountryHistory;
import pt.covidtwoday.ui.fragments.charts.ChartsFragment;


public class ChartPagerFragment extends Fragment implements AnyChartView.OnRenderedListener {

  @BindView(R.id.progressBarHistory)
  ProgressBar progressBarHistory;

  private CountryHistory countryHistory;
  private FragmentManager mFragmentManager;
  private AnyChartView.OnRenderedListener mOnRenderedListener;




  private Unbinder mUnbinder;

  public ChartPagerFragment() {
    // Required empty public constructor
  }


  public void setCountryHistory(CountryHistory countryHistory) {
    this.countryHistory = countryHistory;
  }

  public void setFragmentManager(FragmentManager fragmentManager) {
    mFragmentManager = fragmentManager;
  }

  public void setOnRenderedListener(AnyChartView.OnRenderedListener onRenderedListener) {
    mOnRenderedListener = onRenderedListener;
  }
  public static ChartPagerFragment newInstance(FragmentManager fragmentManager,
                                               CountryHistory countryHistory,
                                               AnyChartView.OnRenderedListener onRenderedListener) {
    ChartPagerFragment fragment = new ChartPagerFragment();
    fragment.setCountryHistory(countryHistory);
    fragment.setFragmentManager(fragmentManager);
    fragment.setOnRenderedListener(onRenderedListener);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_chart_pager, container, false);
    mUnbinder = ButterKnife.bind(this,view);
    handleProgress(true);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (countryHistory != null) {

      new Thread(new Runnable() {
        @Override
        public void run() {

          double lastDeathNumber = 0;
          double lastCaseNumber = 0;
          Iterator casesIterator = countryHistory.getTimeline().getCases().entrySet().iterator();
          Iterator deathsIterator = countryHistory.getTimeline().getDeaths().entrySet().iterator();

          List<DataEntry> dataEntries = new ArrayList<>();
          List<DataEntry> dataEntriesPercentages = new ArrayList<>();
          while (casesIterator.hasNext()) {
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

            dataEntries.add(new CustomEntry(date,
                actualNumberOfDeaths, actualNumberOfCases));

            dataEntriesPercentages.add(new CustomEntry(date,
                deathPercentage, casePercentage));

          }

          if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
              @Override
              public void run() {
                mFragmentManager.beginTransaction().replace(R.id.frameLayoutChartsCases,
                    ChartsFragment.getInstance(ChartPagerFragment.this,dataEntries, ChartsFragment.TYPE.CASES)).commit();

                mFragmentManager.beginTransaction().replace(R.id.frameLayoutChartsPercentages,
                    ChartsFragment.getInstance(ChartPagerFragment.this,
                        dataEntriesPercentages, ChartsFragment.TYPE.PERCENTAGES)).commit();

                handleProgress(false);

              }
            });
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

  private void handleProgress(boolean show){
    if(progressBarHistory != null){
      progressBarHistory.setIndeterminate(show);
      progressBarHistory.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

  }

  int counter = 0;

  @Override
  public void onRendered() {
    counter++;

    if(counter == 2){
      mOnRenderedListener.onRendered();
    }
  }

  private class CustomEntry extends ValueDataEntry {

    public CustomEntry(String x, Number cases, Number death) {
      super(x, cases);
      setValue("death", death);
      setValue("cases", cases);

    }
  }


}
