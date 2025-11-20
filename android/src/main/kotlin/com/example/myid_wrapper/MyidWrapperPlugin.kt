package com.example.myid_wrapper

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import uz.mani.maniauthlibrary.MyIdNativeClient

/** MyidWrapperPlugin */
class MyidWrapperPlugin: FlutterPlugin, MethodCallHandler,ActivityAware {
  private lateinit var channel : MethodChannel
  private var activity: Activity? = null
  private lateinit var activityListener: MyIdSdkActivityListener
  private lateinit var myIdNativeClient: MyIdNativeClient

//  companion object {
//    @JvmStatic
//    fun registerWith(registrar: Registrar) {
//      Log.d("all good", "all good");
//      val instance = MyidWrapperPlugin()
//      registrar.addActivityResultListener(instance.activityListener)
//    }
//  }


  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {

    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "myid_wrapper")
    channel.setMethodCallHandler(this)

    myIdNativeClient = MyIdNativeClient()
    activityListener = MyIdSdkActivityListener(myIdNativeClient)
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    if (call.method == "startMyId") {
      val config = call.arguments as HashMap<*, *>
      startMyId(result, config["sessionId"] as String, config["locale"] as String, config["isResident"] as Boolean)
    } else {
      result.notImplemented()
    }
  }
  private fun startMyId(result: Result, sessionId: String, locale: String, isResident: Boolean) {
    activity?.let { act ->
      try {
        activityListener.setCurrentFlutterResult(result)
        myIdNativeClient.setActivity(act)
        myIdNativeClient.startMyid(sessionId, isResident,  onSuccess = { result ->
          println("1Success: $result")
        },
          onError = { error ->
            println("1Error: $error")
          },
          onUserExited = {
            println("1User exited")
          })
      } catch (e: Exception) {
        result.error("MyID_ERROR", "Failed to start MyID SDK: ${e.message}", null)
      }
    } ?: run {
      result.error("ACTIVITY_NULL", "Activity is not attached", null)
    }
  }


  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    Log.d("all good onAttToAct", "all good onAttachedToActivity");
    activity = binding.activity
    binding.addActivityResultListener(activityListener)
  }

  override fun onDetachedFromActivityForConfigChanges() {
    Log.d("detachcha","dtch")
  }
  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    Log.d("recreat","rct")
    onAttachedToActivity(binding)
  }

  override fun onDetachedFromActivity() {
    Log.d("detach","dtch")
  }


}
