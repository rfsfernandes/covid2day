package pt.covidtwoday.model;

import com.google.gson.annotations.SerializedName;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WorldInfo {
  @PrimaryKey
  @SerializedName("cases")
  private long cases;
  @SerializedName("deaths")
  private long deaths;
  @SerializedName("recovered")
  private long recovered;
  @SerializedName("updated")
  private long updated;
  @SerializedName("active")
  private long active;

  public WorldInfo(long cases, long deaths, long recovered, long updated, long active) {
    this.cases = cases;
    this.deaths = deaths;
    this.recovered = recovered;
    this.updated = updated;
    this.active = active;
  }

  public long getCases() {
    return cases;
  }

  public void setCases(long cases) {
    this.cases = cases;
  }

  public long getDeaths() {
    return deaths;
  }

  public void setDeaths(long deaths) {
    this.deaths = deaths;
  }

  public long getRecovered() {
    return recovered;
  }

  public void setRecovered(long recovered) {
    this.recovered = recovered;
  }

  public long getUpdated() {
    return updated;
  }

  public void setUpdated(long updated) {
    this.updated = updated;
  }

  public long getActive() {
    return active;
  }

  public void setActive(long active) {
    this.active = active;
  }
}
