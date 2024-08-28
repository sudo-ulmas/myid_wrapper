package com.example.myid_wrapper

data class Response(
    val code: String? = null,
    val comparison: Double? = null,
    val base64: String? = null
) {

    fun toMap(): HashMap<String, String> {
        val map: HashMap<String, String> = HashMap()

        code?.let { map["code"] = it }
        comparison?.let { map["comparison"] = "$it" }
        base64?.let { map["base64"] = it }

        return map
    }
}