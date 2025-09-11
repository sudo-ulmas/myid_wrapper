import 'myid_wrapper_platform_interface.dart';

class MyidWrapper {
  Future<String?> startMyId(String passportData, String dateOfBirth, bool isResident) {
    return MyidWrapperPlatform.instance
        .startMyId(passportData, dateOfBirth,isResident);
  }
}
