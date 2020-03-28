package pt.covidtwoday.model;

import com.google.gson.annotations.SerializedName;

public class CountryInfo {
  @SerializedName("id")
  private long id;
  @SerializedName("lat")
  private double latitude;
  @SerializedName("long")
  private double longitude;
  @SerializedName("flag")
  private String flag;
  @SerializedName("iso3")
  private String iso3;
  @SerializedName("iso2")
  private String iso2;

  public CountryInfo(long id, double latitude, double longitude, String flag, String iso3, String iso2) {
    this.id = id;
    this.latitude = latitude;
    this.longitude = longitude;
    this.flag = flag;
    this.iso3 = iso3;
    this.iso2 = iso2;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public String getFlag() {
    return flag;
  }

  public void setFlag(String flag) {
    this.flag = flag;
  }

  public String getIso3() {
    return iso3;
  }

  public void setIso3(String iso3) {
    this.iso3 = iso3;
  }

  public String getIso2() {
    return iso2;
  }

  public void setIso2(String iso2) {
    this.iso2 = iso2;
  }
}
