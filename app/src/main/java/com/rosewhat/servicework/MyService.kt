package com.rosewhat.servicework

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

class MyService : Service() {

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val start = intent?.getIntExtra(EXTRA_START, 0) ?: 0
        scope.launch {
            for (i in start until 100) {
                delay(1000)
                log(i.toString())
            }
        }
        return START_REDELIVER_INTENT
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
        Log.d(LOG_SERVICE_TAG, "MyService $message")
    }

    companion object {
        private const val LOG_SERVICE_TAG = "SERVICE_TAG"
        private const val EXTRA_START = "start"

        fun newInstance(context: Context, start: Int): Intent {
            return Intent(context, MyService::class.java).apply {
                putExtra(EXTRA_START, start)
            }
        }
    }
}