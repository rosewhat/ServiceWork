package com.rosewhat.servicework

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class MyIntentService : IntentService(NAME) {


    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        // true - система убьет сервис - перезапущен + intent сохранен, false - система убьет и все
        setIntentRedelivery(true)
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())

    }

    // работает в другом потоке + автоматически останавливается
    override fun onHandleIntent(intent: Intent?) {
        log("onHandleIntent")
        for (i in 0 until 100) {
            Thread.sleep(1000)
            log(i.toString())
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")

    }

    private fun log(message: String) {
        Log.d(LOG_SERVICE_TAG, "MyForegroundService $message")
    }

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createNotification(): Notification = NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("title")
        .setContentText("text")
        .setSmallIcon(R.drawable.ic_launcher_background)
        .build()


    companion object {
        private const val LOG_SERVICE_TAG = "SERVICE_TAG"
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 1

        private const val NAME = "MyIntentService"
        fun newInstance(context: Context): Intent {
            return Intent(context, MyIntentService::class.java)
        }
    }
}