package pt.covidtwoday.ui.fragments.informations;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pt.covidtwoday.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformationsFragment extends Fragment {
  @BindView(R.id.textViewIcons)
  TextView textViewIcons;
  @BindView(R.id.textViewAppInfo)
  TextView textViewAppInfo;
  @BindView(R.id.textViewDevelopInfo)
  TextView textViewDevelopInfo;
  @BindView(R.id.textViewStayHome)
  TextView textViewStayHome;

  private Unbinder mUnbinder;


  public InformationsFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_informations, container, false);
    mUnbinder = ButterKnife.bind(this, view);

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    textViewIcons.setText(HtmlCompat.fromHtml(getResources().getString(R.string.icon_made), HtmlCompat.FROM_HTML_MODE_LEGACY));
    textViewIcons.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());

    textViewAppInfo.setText(HtmlCompat.fromHtml(getResources().getString(R.string.app_info),
        HtmlCompat.FROM_HTML_MODE_LEGACY));
    textViewAppInfo.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());

    textViewDevelopInfo.setText(HtmlCompat.fromHtml(getResources().getString(R.string.developer_info),
        HtmlCompat.FROM_HTML_MODE_LEGACY));
    textViewDevelopInfo.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());

    textViewStayHome.setText(HtmlCompat.fromHtml(getResources().getString(R.string.appeal),
        HtmlCompat.FROM_HTML_MODE_LEGACY));
    textViewStayHome.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mUnbinder.unbind();
  }
}
