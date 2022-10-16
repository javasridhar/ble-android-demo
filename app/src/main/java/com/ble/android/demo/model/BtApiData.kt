package com.ble.android.demo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ble_raw_data")
data class BtApiData(@PrimaryKey(autoGenerate = true) val id: Long? = null, val rawData: String, val fcmToken: String, val isNotificationSent: Boolean = false)