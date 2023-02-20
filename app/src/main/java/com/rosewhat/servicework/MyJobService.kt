package com.rosewhat.servicework

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Build
import android.util.Log
import kotlinx.coroutines.*

class MyJobService : JobService() {

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
    }

    // boolean - работа выполняется? true - система выполняется, если синхронно , то false
    override fun onStartJob(params: JobParameters?): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            scope.launch {
                // из очереди берется 1 сервис
                var workItem = params?.dequeueWork()
                while (workItem != null) {
                    val page = workItem.intent.getIntExtra(PAGE, 0)

                    for (i in 0 until 100) {
                        delay(1000)
                        log("Timer $i $page")
                    }
                    // сервис в очереди завершился, могут работать другие
                    params?.completeWork(workItem)
                    // достать след сервис из очереди
                    workItem = params?.dequeueWork()

                }
                // завершение работы, сервис перезапущен будет в будущем + onStopJob - вызываться не будет
                jobFinished(params, false)
            }
        }
        return true
    }


    // ограничения + система убила сервис
    override fun onStopJob(params: JobParameters?): Boolean {
        // перезапустить сервис?
        return true
    }


    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()

    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyForegroundService $message")
    }

    companion object {
        const val JOB_ID = 111

        private const val PAGE = "page"

        fun newIntent(page: Int): Intent {
            return Intent().apply {
                putExtra(PAGE, page)
            }
        }
    }


}