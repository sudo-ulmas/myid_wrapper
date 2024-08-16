import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'myid_wrapper_method_channel.dart';

abstract class MyidWrapperPlatform extends PlatformInterface {
  /// Constructs a MyidWrapperPlatform.
  MyidWrapperPlatform() : super(token: _token);

  static final Object _token = Object();

  static MyidWrapperPlatform _instance = MethodChannelMyidWrapper();

  /// The default instance of [MyidWrapperPlatform] to use.
  ///
  /// Defaults to [MethodChannelMyidWrapper].
  static MyidWrapperPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [MyidWrapperPlatform] when
  /// they register themselves.
  static set instance(MyidWrapperPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
