package pt.covidtwoday.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class Timeline {
  @SerializedName("cases")
  private HashMap<String, Long> cases;
  @SerializedName("deaths")
  private HashMap<String, Long> deaths;

  public Timeline(HashMap<String, Long> cases, HashMap<String, Long> deaths) {
    this.cases = cases;
    this.deaths = deaths;
  }

  public HashMap<String, Long> getCases() {
    return cases;
  }

  public void setCases(HashMap<String, Long> cases) {
    this.cases = cases;
  }

  public HashMap<String, Long> getDeaths() {
    return deaths;
  }

  public void setDeaths(HashMap<String, Long> deaths) {
    this.deaths = deaths;
  }
}
