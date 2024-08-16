import Flutter
import UIKit
import MyIDNative

public class MyidWrapperPlugin: NSObject, FlutterPlugin, MyIdNativeClientDelegate {
    var  flResult: FlutterResult?
   public func onSuccess(code: String) {
       if let result = flResult {
           print("hello world")
           result(code)
       }
   }
   
   
   public func onError(exception: String) {
       if let result = flResult {
           print("nma gap nma nma gap")
           result(exception)
       }
   }
   
   public func onUserExited() {
       
       if let result = flResult {
           result("usha gap")
       }
   }
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "myid_wrapper", binaryMessenger: registrar.messenger())
    let instance = MyidWrapperPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    switch call.method {
    case "getPlatformVersion":
        flResult = result
        let myIdClient = MyIdNativeClient(passportData: "AD5644300", dateOfBirth: "13.08.1997")
        myIdClient.delegate = self
        myIdClient.startMyIdjon()
    default:
      result(FlutterMethodNotImplemented)
    }
  }
}
