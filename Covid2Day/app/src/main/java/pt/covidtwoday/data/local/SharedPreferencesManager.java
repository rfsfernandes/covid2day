package pt.covidtwoday.data.local;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import static pt.covidtwoday.custom.Constants.PREFS;

public class SharedPreferencesManager {
  private static SharedPreferencesManager instance;

  /**
   * Creates the PreferencesManager singleton
   *
   * @return SharedPreferences instance, whether it's a new one or the one that already exists.
   */
  public static SharedPreferencesManager getInstance() {
    if (instance != null) {
      return instance;
    } else {
      instance = new SharedPreferencesManager();
      return instance;
    }
  }

  private static SharedPreferences sSharedPreferences;
  private static SharedPreferences.Editor sEditor;

  public void setFirstTime(Application application, boolean firstTime){
    if (sSharedPreferences == null || sEditor == null) {
      sSharedPreferences = application.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
      sEditor = sSharedPreferences.edit();

    }

    sEditor.putBoolean(PREFS, firstTime);
    sEditor.apply();

  }

  public boolean getFirstTime(Context context) {
    if (sSharedPreferences == null) {
      sSharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }
    return sSharedPreferences.getBoolean(PREFS, true);
  }


}
