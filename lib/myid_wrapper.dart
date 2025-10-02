import 'myid_wrapper_platform_interface.dart';

class MyidWrapper {
  Future<String?> startMyId(String sessionId, String locale, bool isResident) {
    return MyidWrapperPlatform.instance.startMyId(locale, sessionId, isResident);
  }
}
