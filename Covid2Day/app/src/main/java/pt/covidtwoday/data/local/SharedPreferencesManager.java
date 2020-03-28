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

}
