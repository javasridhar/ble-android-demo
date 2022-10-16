package com.ble.android.demo.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ble.android.demo.R
import com.ble.android.demo.utils.SharedPreference
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class BleAndroidDemoFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = BleAndroidDemoFirebaseMessagingService::class.java.simpleName

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "101"
        private const val NOTIFICATION_CHANNEL_NAME = "BleAndroidDemo"
        private const val GROUP_KEY = "com.ble.android.demo"
    }

    override fun onNewToken(token: String) {
        Log.i(TAG, "FcmToken=>$token")
        saveRegistrationToPreference(token)
    }

    private fun saveRegistrationToPreference(token: String) {
        SharedPreference(this).saveFcmToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        createNotification(message)
    }

    private fun createNotification(message: RemoteMessage) {
        val notificationContentRawData: String = message.data["rawData"]!!
        val deviceName: String = notificationContentRawData.split(",")[0]
        val deviceAddress: String = notificationContentRawData.split(",")[1]
        val notificationContent = "$deviceName, $deviceAddress is successfully saved in server"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val descriptionText = "BleAndroidDemoImplementation"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Ble Android Demo")
            .setContentText(notificationContent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setGroup(GROUP_KEY)
            .setGroupSummary(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(Calendar.getInstance().timeInMillis.toInt(), notificationBuilder.build())
        }
    }
}