package pt.covidtwoday.model;

public class HistoryListModel {
  private String date;
  private double cases;
  private double deaths;
  private double casesPercentage;
  private double deathsPercentage;

  public HistoryListModel(String date, double cases, double deaths, double casesPercentage, double deathsPercentage) {
    this.date = date;
    this.cases = cases;
    this.deaths = deaths;
    this.casesPercentage = casesPercentage;
    this.deathsPercentage = deathsPercentage;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public double getCases() {
    return cases;
  }

  public void setCases(double cases) {
    this.cases = cases;
  }

  public double getDeaths() {
    return deaths;
  }

  public void setDeaths(double deaths) {
    this.deaths = deaths;
  }

  public double getCasesPercentage() {
    return casesPercentage;
  }

  public void setCasesPercentage(double casesPercentage) {
    this.casesPercentage = casesPercentage;
  }

  public double getDeathsPercentage() {
    return deathsPercentage;
  }

  public void setDeathsPercentage(double deathsPercentage) {
    this.deathsPercentage = deathsPercentage;
  }
}
