import Flutter
import UIKit
import MyIDNative

public class MyidWrapperPlugin: NSObject, FlutterPlugin, MyIdNativeClientDelegate {
    var  flResult: FlutterResult?
   public func onSuccess(code: String) {
       if let result = flResult {
           result(code)
       }
   }
   
   
   public func onError(exception: String) {
       if let result = flResult {
           result(exception)
       }
   }
   
   public func onUserExited() {
       
       if let result = flResult {
       }
   }
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "myid_wrapper", binaryMessenger: registrar.messenger())
    let instance = MyidWrapperPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    switch call.method {
    case "startMyId":
        if let arguments = call.arguments as? [String: Any],
         let locale = arguments["locale"] as? String,
           let sessionId = arguments["sessionId"] as? String,
           let isResident = arguments["isResident"] as? Bool {
            flResult = result
            let myIdClient = MyIdNativeClient( isResident: isResident, sessionId: sessionId, locale: locale)
            myIdClient.delegate = self
            myIdClient.startIdenditication()
        } else {
            result(FlutterError(code: "100", message: "Provided arguments to startMyId method is not valid", details: ""))
        }
    default:
      result(FlutterMethodNotImplemented)
    }
  }
}
