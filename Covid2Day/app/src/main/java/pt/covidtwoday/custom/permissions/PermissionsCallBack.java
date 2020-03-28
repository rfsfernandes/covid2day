package pt.covidtwoday.custom.permissions;

public interface PermissionsCallBack {
  void onPermissionGranted();
  void onPermissionPermanentlyDenied();
  void onPermissionDenied();
}
