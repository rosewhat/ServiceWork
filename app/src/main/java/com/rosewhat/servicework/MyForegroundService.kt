package com.rosewhat.servicework

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class MyForegroundService : Service() {

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scope.launch {
            for (i in 0 until 100) {
                delay(1000)
                log(i.toString())
            }
            // остановка сервиса
            stopSelf()
        }
        return START_STICKY
        //START_REDELIVER_INTENT - перезапустит сервис , intent передаст значения
        //START_NOT_STICKY - система не будет перезапускать сервис
        //START_STICKY - система убьет сервис он будет пересоздан, intent - больше не даст значения
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        log("onDestroy")

    }


    override fun onBind(p0: Intent?): IBinder? {
        TODO()
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
        fun newInstance(context: Context): Intent {
            return Intent(context, MyForegroundService::class.java)
        }
    }
}