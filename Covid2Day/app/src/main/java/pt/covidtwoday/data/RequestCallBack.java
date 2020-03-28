package pt.covidtwoday.data;

public interface RequestCallBack<T> {
  void responseSuccessful(T responseObject);
  void responseFail(String error);
}
