package com.example.myid_wrapper

import android.app.Activity
import android.os.Handler
import android.os.Looper
import com.example.myidlibrary.MyIdNativeClient

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

/** MyidWrapperPlugin */
class MyidWrapperPlugin: FlutterPlugin, MethodCallHandler,ActivityAware {
  private lateinit var channel : MethodChannel
  private var activity: Activity? = null
  private lateinit var activityListener: MyIdSdkActivityListener
  private lateinit var myIdNativeClient: MyIdNativeClient

  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val instance = MyidWrapperPlugin()
      registrar.addActivityResultListener(instance.activityListener)
    }
  }


  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "myid_wrapper")
    channel.setMethodCallHandler(this)

    myIdNativeClient = MyIdNativeClient(passportData = "", dateOfBirth = "")
    activityListener = MyIdSdkActivityListener(myIdNativeClient)
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    if (call.method == "startMyId") {
      startMyId(result)
    } else {
      result.notImplemented()
    }
  }
  private fun startMyId(result: Result) {
    activity?.let { act ->
      try {
        activityListener.setCurrentFlutterResult(result)
        myIdNativeClient.setActivity(act)
        myIdNativeClient.startMyIdjon()
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
