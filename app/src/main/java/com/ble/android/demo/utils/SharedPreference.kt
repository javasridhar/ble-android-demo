package com.ble.android.demo.utils

import android.content.Context

class SharedPreference(private val context: Context) {

    companion object {
        const val PREFERENCE_NAME = "BleAndroidDemoPref"
        const val PREFERENCE_MODE = Context.MODE_PRIVATE
        const val FCM_TOKEN = "fcmToken"
    }

    fun saveFcmToken(token: String) {
        val sharedPreference = context.getSharedPreferences(PREFERENCE_NAME, PREFERENCE_MODE)
        sharedPreference.edit().putString(FCM_TOKEN, token).commit()
    }

    fun getFcmToken() : String {
        val sharedPreference = context.getSharedPreferences(PREFERENCE_NAME, PREFERENCE_MODE)
        return sharedPreference.getString(FCM_TOKEN, "")!!
    }
}