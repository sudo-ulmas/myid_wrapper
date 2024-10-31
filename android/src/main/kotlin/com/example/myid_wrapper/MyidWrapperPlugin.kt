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
import io.flutter.plugin.common.PluginRegistry.Registrar
import uz.mani.maniauthlibrary.MyIdNativeClient

/** MyidWrapperPlugin */
class MyidWrapperPlugin: FlutterPlugin, MethodCallHandler,ActivityAware {
  private lateinit var channel : MethodChannel
  private var activity: Activity? = null
  private lateinit var activityListener: MyIdSdkActivityListener
  private lateinit var myIdNativeClient: MyIdNativeClient

  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      Log.d("all good", "all good");
      val instance = MyidWrapperPlugin()
      registrar.addActivityResultListener(instance.activityListener)
    }
  }


  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {

    Log.d("all good onAttToEngine", "all good onAttachedToEngine");
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "myid_wrapper")
    Log.d("1", "all good onAttachedToEngine");
    channel.setMethodCallHandler(this)
    Log.d("2", "all good onAttachedToEngine");

    myIdNativeClient = MyIdNativeClient()
    Log.d("3", "all good onAttachedToEngine");
    activityListener = MyIdSdkActivityListener(myIdNativeClient)
    Log.d("4", "all good onAttachedToEngine");
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    Log.d("all good mcl", "all good mc");
    if (call.method == "startMyId") {
      Log.d("hello", "hello");
      val config = call.arguments as HashMap<*, *>
      Log.d("starting", "myidfunction");
      startMyId(result, config["passportData"] as String, config["dateOfBirth"] as String)
    } else {
      Log.d("hi", "hi");
      result.notImplemented()
    }
  }
  private fun startMyId(result: Result, passportData: String, dateOfBirth: String) {
    activity?.let { act ->
      try {
        Log.d("all good 1", "all good 1");
        activityListener.setCurrentFlutterResult(result)
        Log.d("all good 2", "all good 2");
        myIdNativeClient.setActivity(act)
        Log.d("all good 3", "all good 3");
        myIdNativeClient.startMyid(passportData, dateOfBirth)
      } catch (e: Exception) {
        Log.d("error", "myid error");
        result.error("MyID_ERROR", "Failed to start MyID SDK: ${e.message}", null)
      }
    } ?: run {
      Log.d("error", "activity null");
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
