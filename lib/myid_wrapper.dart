
import 'myid_wrapper_platform_interface.dart';

class MyidWrapper {
  Future<String?> getPlatformVersion() {
    return MyidWrapperPlatform.instance.getPlatformVersion();
  }
}
