package com.ble.android.demo.model

import androidx.annotation.Nullable

data class BtApiData(val id: Long? = null, val rawData: String, val fcmToken: String, val isNotificationSent: Boolean = false)