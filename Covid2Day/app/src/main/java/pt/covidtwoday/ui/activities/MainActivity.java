package pt.covidtwoday.ui.activities;

import android.content.res.Configuration;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import pt.covidtwoday.R;
import pt.covidtwoday.custom.utils.UtilsClass;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    BottomNavigationView navView = findViewById(R.id.nav_view);
    // Passing each menu ID as a set of Ids because each
    // menu should be considered as top level destinations.

    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

    NavigationUI.setupWithNavController(navView, navController);

  }

  @Override
  protected void onResume() {
    super.onResume();

    int nightModeFlags =
        getResources().getConfiguration().uiMode &
            Configuration.UI_MODE_NIGHT_MASK;

    switch (nightModeFlags){
      case Configuration.UI_MODE_NIGHT_YES:
        UtilsClass.getInstance().setStatusBarDark(this, false);
        break;
      case Configuration.UI_MODE_NIGHT_NO:
        UtilsClass.getInstance().setStatusBarDark(this, true);
        break;
    }

  }
}
