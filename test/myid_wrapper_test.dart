import 'package:flutter_test/flutter_test.dart';
import 'package:myid_wrapper/myid_wrapper.dart';
import 'package:myid_wrapper/myid_wrapper_platform_interface.dart';
import 'package:myid_wrapper/myid_wrapper_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockMyidWrapperPlatform
    with MockPlatformInterfaceMixin
    implements MyidWrapperPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final MyidWrapperPlatform initialPlatform = MyidWrapperPlatform.instance;

  test('$MethodChannelMyidWrapper is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelMyidWrapper>());
  });

  test('getPlatformVersion', () async {
    MyidWrapper myidWrapperPlugin = MyidWrapper();
    MockMyidWrapperPlatform fakePlatform = MockMyidWrapperPlatform();
    MyidWrapperPlatform.instance = fakePlatform;

    expect(await myidWrapperPlugin.getPlatformVersion(), '42');
  });
}
