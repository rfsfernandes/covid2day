package pt.covidtwoday.data.local;

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

//  private static SharedPreferences sharedToken;
//  private static SharedPreferences.Editor sharedTokenEditor;
//
//  private void setTokenAmmount(Application application, int tokens){
//    if (sharedToken == null || sharedTokenEditor == null) {
//      sharedToken = application.getSharedPreferences(TOKEN_PREFERENCES, Context.MODE_PRIVATE);
//      sharedTokenEditor = sharedToken.edit();
//
//    }
//
//    sharedTokenEditor.putInt(TOKEN_PREFERENCES, tokens);
//    sharedTokenEditor.apply();
//
//  }
//
//  public String getTokenAmmount(Context context) {
//    if (sharedTrails == null) {
//      sharedTrails = context.getApplicationContext().getSharedPreferences(COURSES_PREFS, Context.MODE_PRIVATE);
//    }
//    return sharedTrails.getString(COURSES_TYPE_PREFS, "");
//  }


}
