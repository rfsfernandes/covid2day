package pt.covidtwoday.ui.fragments.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;

import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pt.covidtwoday.R;
import pt.covidtwoday.model.viewmodels.HistoryViewModel;
import pt.covidtwoday.ui.fragments.charts.ChartsFragment;

public class HistoryFragment extends Fragment {


  //  Place ViewBinding here

  @BindView(R.id.frameLayoutChartsCases)
  FrameLayout frameLayoutChartsCases;
  @BindView(R.id.frameLayoutChartsPercentages)
  FrameLayout frameLayoutChartsPercentages;
  @BindView(R.id.progressBarHistory)
  ProgressBar progressBarHistory;
  //   Place static constants here

  //  Place constants here

  //  Place viarables here
  private HistoryViewModel mHistoryViewModel;
  private Unbinder mUnbinder;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    mHistoryViewModel =
        ViewModelProviders.of(this).get(HistoryViewModel.class);
    View root = inflater.inflate(R.layout.fragment_history, container, false);
    mUnbinder = ButterKnife.bind(this, root);
    initViewModel();
    mHistoryViewModel.getHistoricalDataFromCountry("China");
    handleProgress(true);
    return root;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  public void onResume() {
    super.onResume();

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
            
          });


      mHistoryViewModel.errorMutatableLiveData.observe(getViewLifecycleOwner(), error -> {
        Snackbar.make(getView(), error, BaseTransientBottomBar.LENGTH_LONG).show();
      });
    }
  }

  private void handleProgress(boolean show){
    progressBarHistory.setIndeterminate(show);
    progressBarHistory.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
  }

  private class CustomEntry extends ValueDataEntry {

    public CustomEntry(String x, Number cases, Number death) {
      super(x, cases);
      setValue("death", death);
      setValue("cases", cases);
//      setValue(x,death);
    }
  }

}