import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'myid_wrapper_platform_interface.dart';

/// An implementation of [MyidWrapperPlatform] that uses method channels.
class MethodChannelMyidWrapper extends MyidWrapperPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('myid_wrapper');

  @override
  Future<String?> startMyId(String passportData, String dateOfBirth) async {
    try {
      final version = await methodChannel.invokeMethod<String>('startMyId', {
        'passportData': passportData,
        'dateOfBirth': dateOfBirth,
      });
      return version;
    } catch (e) {
    }
  }
}
