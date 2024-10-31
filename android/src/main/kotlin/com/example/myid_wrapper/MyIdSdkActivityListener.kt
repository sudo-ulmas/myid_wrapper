package com.example.myid_wrapper

import android.content.Intent
import android.graphics.Bitmap
import android.util.Base64
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry
import uz.mani.maniauthlibrary.MyIdNativeClient
//import uz.myid.android.sdk.capture.model.MyIdGraphicFieldType
//import uz.myid.android.sdk.capture.model.MyIdImageFormat
import java.io.ByteArrayOutputStream

class MyIdSdkActivityListener(
    private val client: MyIdNativeClient
) : PluginRegistry.ActivityResultListener {

    private var flutterResult: MethodChannel.Result? = null

    fun setCurrentFlutterResult(result: MethodChannel.Result?) {
        this.flutterResult = result
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        client.handleActivityResult(resultCode,
            onSuccess = { result ->
                try {
//                    val bitmap = result.getGraphicFieldImageByType(MyIdGraphicFieldType.FACE_PORTRAIT)
                    val response = Response(
                        code = result.code,
                        comparison = result.comparison,
//                        base64 = bitmap?.toBase64()
                    )
                    println("success ${response.code} and result $flutterResult")
                    flutterResult?.success(response.code)
                } catch (e: Exception) {
                    val response = Response(
                        code = result.code,
                        comparison = result.comparison
                    )
                    flutterResult?.success(response.toMap())
                } finally {
                    flutterResult = null
                }
            },
            onError = { exception ->
                println("error ${exception.code} - ${exception.message}")
                flutterResult?.error(
                    "error",
                    "${exception.code} - ${exception.message}",
                    null
                )
                flutterResult = null
            },
            onUserExited = {

                println("exited user canceled flow")
                flutterResult?.error(
                    "cancel",
                    "User canceled flow",
                    null
                )
                flutterResult = null
            }
        )
        return true
    }

//    private fun Bitmap?.toBase64(
//        format: MyIdImageFormat = MyIdImageFormat.JPG
//    ): String? {
//        this ?: return null
//
//        return Base64.encodeToString(toByteArray(format), Base64.DEFAULT)
//    }
//
//    private fun Bitmap.toByteArray(
//        format: MyIdImageFormat,
//        quality: Int = 100
//    ): ByteArray? {
//        val compressFormat = when (format) {
//            MyIdImageFormat.JPG -> Bitmap.CompressFormat.JPEG
//            MyIdImageFormat.PNG -> Bitmap.CompressFormat.PNG
//        }
//
//        ByteArrayOutputStream().use { stream ->
//            compress(compressFormat, quality, stream)
//            return stream.toByteArray()
//        }
//    }
}