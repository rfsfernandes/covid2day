package pt.covidtwoday.ui.fragments.history.adapter;

import android.content.Context;

import com.anychart.AnyChartView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import pt.covidtwoday.R;
import pt.covidtwoday.model.CountryHistory;
import pt.covidtwoday.ui.fragments.history.ChartPagerFragment;
import pt.covidtwoday.ui.fragments.history.ListPagerFragment;

public class HistoryPagerAdapter extends FragmentStatePagerAdapter {

  CountryHistory mCountryHistory;
  Context mContext;
  FragmentManager mFragmentManager;
  AnyChartView.OnRenderedListener mOnRenderedListener;

  public Context getContext() {
    return mContext;
  }

  public void setContext(Context context) {
    mContext = context;
  }

  public void setOnRenderedListener(AnyChartView.OnRenderedListener onRenderedListener) {
    mOnRenderedListener = onRenderedListener;
  }

  public void setCountryHistory(CountryHistory countryHistory) {
    mCountryHistory = countryHistory;
  }

  public HistoryPagerAdapter(@NonNull FragmentManager fm, int behavior) {
    super(fm, behavior);
    mFragmentManager = fm;
  }


  @NonNull
  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0:
        return ChartPagerFragment.newInstance(mFragmentManager, mCountryHistory, mOnRenderedListener);

      case 1:
        return ListPagerFragment.newInstance(mCountryHistory);

      default:
        return ChartPagerFragment.newInstance(mFragmentManager, mCountryHistory, mOnRenderedListener);

    }

  }

  @Override
  public int getCount() {

    return 2;
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {

    switch (position) {
      case 0:
        return getContext().getResources().getString(R.string.charts);
      case 1:
        return getContext().getResources().getString(R.string.list);
      default:
        return getContext().getResources().getString(R.string.charts);
    }


  }
}
