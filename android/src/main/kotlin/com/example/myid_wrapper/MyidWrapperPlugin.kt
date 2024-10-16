package com.example.myid_wrapper

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.myidlibrary.MyIdNativeClient

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** MyidWrapperPlugin */
class MyidWrapperPlugin: FlutterPlugin, MethodCallHandler,ActivityAware {
  private lateinit var channel : MethodChannel
  private var activity: Activity? = null
  private lateinit var activityListener: MyIdSdkActivityListener
  private lateinit var myIdNativeClient: MyIdNativeClient



  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    Log.d("MyIDWrapperPlugin", "Plugin initialized")
    println("hello")
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "uz.mani.myid_wrapper")
    channel.setMethodCallHandler(this)

    myIdNativeClient = MyIdNativeClient()
    activityListener = MyIdSdkActivityListener(myIdNativeClient)
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    println("Method called: ${call.method}")
    if (call.method == "startMyId") {
      val config = call.arguments as HashMap<*, *>
      startMyId(result, config["passportData"] as String, config["dateOfBirth"] as String)
    } else {
      result.notImplemented()
    }
  }
  private fun startMyId(result: Result, passportData: String, dateOfBirth: String) {
    activity?.let { act ->
      try {
        activityListener.setCurrentFlutterResult(result)
        myIdNativeClient.setActivity(act)
        myIdNativeClient.startMyIdxon(passportData, dateOfBirth)
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
    activity = binding.activity
    binding.addActivityResultListener(activityListener)
    println("Activity attached")
  }

  override fun onDetachedFromActivityForConfigChanges() {
    println("detachcha")
  }
  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    println("recreato")
    onAttachedToActivity(binding)
  }

  override fun onDetachedFromActivity() {
    println("detacho")
  }


}
