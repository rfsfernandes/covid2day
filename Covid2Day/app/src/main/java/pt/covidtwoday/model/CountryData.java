package pt.covidtwoday.model;

import com.google.gson.annotations.SerializedName;

public class CountryData {
  @SerializedName("country")
  private String country;
  @SerializedName("countryInfo")
  private CountryInfo countryInfo;
  @SerializedName("cases")
  private long cases;
  @SerializedName("todayCases")
  private long todayCases;
  @SerializedName("deaths")
  private long deaths;
  @SerializedName("todayDeaths")
  private long todayDeaths;
  @SerializedName("recovered")
  private long recovered;
  @SerializedName("active")
  private long active;
  @SerializedName("critical")
  private long critical;
  @SerializedName("casesPerOneMillion")
  private double casesPerOneMillion;
  @SerializedName("deathsPerOneMillion")
  private double deathsPerOneMillion;

  public CountryData(String country, CountryInfo countryInfo, long cases, long todayCases, long deaths, long todayDeaths, long recovered, long active, long critical, double casesPerOneMillion, double deathsPerOneMillion) {
    this.country = country;
    this.countryInfo = countryInfo;
    this.cases = cases;
    this.todayCases = todayCases;
    this.deaths = deaths;
    this.todayDeaths = todayDeaths;
    this.recovered = recovered;
    this.active = active;
    this.critical = critical;
    this.casesPerOneMillion = casesPerOneMillion;
    this.deathsPerOneMillion = deathsPerOneMillion;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public CountryInfo getCountryInfo() {
    return countryInfo;
  }

  public void setCountryInfo(CountryInfo countryInfo) {
    this.countryInfo = countryInfo;
  }

  public long getCases() {
    return cases;
  }

  public void setCases(long cases) {
    this.cases = cases;
  }

  public long getTodayCases() {
    return todayCases;
  }

  public void setTodayCases(long todayCases) {
    this.todayCases = todayCases;
  }

  public long getDeaths() {
    return deaths;
  }

  public void setDeaths(long deaths) {
    this.deaths = deaths;
  }

  public long getTodayDeaths() {
    return todayDeaths;
  }

  public void setTodayDeaths(long todayDeaths) {
    this.todayDeaths = todayDeaths;
  }

  public long getRecovered() {
    return recovered;
  }

  public void setRecovered(long recovered) {
    this.recovered = recovered;
  }

  public long getActive() {
    return active;
  }

  public void setActive(long active) {
    this.active = active;
  }

  public long getCritical() {
    return critical;
  }

  public void setCritical(long critical) {
    this.critical = critical;
  }

  public double getCasesPerOneMillion() {
    return casesPerOneMillion;
  }

  public void setCasesPerOneMillion(double casesPerOneMillion) {
    this.casesPerOneMillion = casesPerOneMillion;
  }

  public double getDeathsPerOneMillion() {
    return deathsPerOneMillion;
  }

  public void setDeathsPerOneMillion(double deathsPerOneMillion) {
    this.deathsPerOneMillion = deathsPerOneMillion;
  }
}
