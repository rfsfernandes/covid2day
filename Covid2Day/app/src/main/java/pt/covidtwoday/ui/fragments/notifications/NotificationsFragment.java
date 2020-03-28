package pt.covidtwoday.ui.fragments.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pt.covidtwoday.R;
import pt.covidtwoday.model.viewmodels.NotificationsViewModel;

public class NotificationsFragment extends Fragment {


  //  Place ViewBinding here

  //   Place static constants here

  //  Place constants here

  //  Place viarables here
  private NotificationsViewModel notificationsViewModel;
  private Unbinder mUnbinder;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    notificationsViewModel =
        ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(NotificationsViewModel.class);
    View root = inflater.inflate(R.layout.fragment_notifications, container, false);
    mUnbinder = ButterKnife.bind(this, root);
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
}