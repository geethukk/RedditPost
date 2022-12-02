package com.yourparkingspace.network

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken

internal inline fun <reified T> Gson.fromJson(json: JsonElement): T? = try {
    fromJson<T>(json, object : TypeToken<T>() {}.type)
} catch (e: Exception) {
    null
}