package com.rosewhat.servicework

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class MyIntentService2 : IntentService(NAME) {


    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        // true - система убьет сервис - перезапущен + intent сохранен, false - система убьет и все
        setIntentRedelivery(true)

    }

    // работает в другом потоке + автоматически останавливается
    override fun onHandleIntent(intent: Intent?) {
        log("onHandleIntent")
        val page = intent?.getIntExtra(PAGE, 0) ?: 0
        for (i in 0 until 100) {
            Thread.sleep(1000)
            log("$i $page")
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")

    }

    private fun log(message: String) {
        Log.d(LOG_SERVICE_TAG, "MyForegroundService $message")
    }


    companion object {
        private const val LOG_SERVICE_TAG = "SERVICE_TAG"
        private const val PAGE = "page"
        private const val NOTIFICATION_ID = 1

        private const val NAME = "MyIntentService"
        fun newInstance(context: Context, page: Int): Intent {
            return Intent(context, MyIntentService2::class.java).apply {
                putExtra(PAGE, page)
            }
        }
    }
}