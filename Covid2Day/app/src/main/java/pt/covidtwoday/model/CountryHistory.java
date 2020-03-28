package pt.covidtwoday.model;

public class CountryHistory {
  private String country;
  private Timeline timeline;

  public CountryHistory(String country, Timeline timeline) {
    this.country = country;
    this.timeline = timeline;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public Timeline getTimeline() {
    return timeline;
  }

  public void setTimeline(Timeline timeline) {
    this.timeline = timeline;
  }
}
