package pt.covidtwoday.ui.fragments.charts;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pt.covidtwoday.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartsFragment extends Fragment {

  @BindView(R.id.chartView)
  AnyChartView mChartView;

  private Unbinder mUnbinder;
  private List<DataEntry> dataEntries;
  private TYPE mTYPE;
  private AnyChartView.OnRenderedListener mOnRenderedListener;

  public void setOnRenderedListener(AnyChartView.OnRenderedListener onRenderedListener) {
    mOnRenderedListener = onRenderedListener;
  }

  public void setDataEntries(List<DataEntry> dataEntries) {
    this.dataEntries = dataEntries;
  }

  public TYPE getTYPE() {
    return mTYPE;
  }

  public void setTYPE(TYPE TYPE) {
    mTYPE = TYPE;
  }

  public ChartsFragment() {
    // Required empty public constructor
  }

  public enum TYPE{
    CASES,
    PERCENTAGES
  }

  public static ChartsFragment getInstance(AnyChartView.OnRenderedListener onRenderedListener,
                                           List<DataEntry> dataEntries, TYPE type){
    ChartsFragment chartsFragment = new ChartsFragment();
    chartsFragment.setDataEntries(dataEntries);
    chartsFragment.setTYPE(type);
    chartsFragment.setOnRenderedListener(onRenderedListener);
    return chartsFragment;
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_charts, container, false);
    mUnbinder = ButterKnife.bind(this,view);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mChartView.setChart(makeCartesianCases(dataEntries));
    mChartView.setOnRenderedListener(mOnRenderedListener);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mUnbinder.unbind();
  }

  private Cartesian makeCartesianCases(List<DataEntry> dataEntries){
    Cartesian cartesian = AnyChart.line();
    cartesian.animation(true);
    cartesian.crosshair().enabled(false);

    switch (mTYPE){
      case CASES:
        cartesian.title(getResources().getString(R.string.evoluction_of_cases_and_deaths));
        break;
      case PERCENTAGES:
        cartesian.title(getResources().getString(R.string.evolution_of_cases_and_deaths_percentage));
        break;
    }



    Set set = Set.instantiate();
    set.data(dataEntries);
    Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'cases' }");
    Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'death' }");

    Line series1 = cartesian.line(series2Mapping);
    switch (mTYPE){
      case CASES:
        series1.name(getResources().getString(R.string.cases));
        break;
      case PERCENTAGES:
        series1.name(getResources().getString(R.string.cases) + " %");
        break;
    }

    series1.hovered().markers().enabled(true);
    series1.color("#6AB09A");
    series1.hovered().markers()
        .type(MarkerType.CIRCLE)
        .size(4d);
    series1.tooltip()
        .position("right")
        .anchor(Anchor.LEFT_CENTER)
        .offsetX(5d)
        .offsetY(5d);

    Line series2 = cartesian.line(series1Mapping);
    switch (mTYPE){
      case CASES:
        series2.name(getResources().getString(R.string.deaths));
        break;
      case PERCENTAGES:
        series2.name(getResources().getString(R.string.cases) + " %");
        break;
    }

    series2.hovered().markers().enabled(true);
    series2.color("#DF3C58");
    series2.hovered().markers()
        .type(MarkerType.CIRCLE)
        .size(4d);
    series2.tooltip()
        .position("right")
        .anchor(Anchor.RIGHT_CENTER)
        .offsetX(5d)
        .offsetY(5d);

    cartesian.legend().enabled(true);
    cartesian.legend().fontSize(13d);
    cartesian.legend().padding(0d, 0d, 10d, 0d);
    return cartesian;
  }


}
