import 'myid_wrapper_platform_interface.dart';

class MyidWrapper {
  Future<String?> startMyId(String passportData, String dateOfBirth) {
    return MyidWrapperPlatform.instance
        .startMyId(passportData, dateOfBirth);
  }
}
