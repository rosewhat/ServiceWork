package com.rosewhat.servicework

import android.content.Context
import android.util.Log
import androidx.work.*

class MyWorker(
    context: Context,
    private val workerParameters: WorkerParameters
) : Worker(
    context,
    workerParameters
) {
    override fun doWork(): Result {
        log("onHandleIntent")
        val page = workerParameters.inputData.getInt(PAGE, 0)
        for (i in 0 until 100) {
            Thread.sleep(1000)
            log("$i $page")
        }
        return Result.success()
        //Result.success() - все хорошо, сервис закончил свою работу
    }

    private fun log(message: String) {
        Log.d(LOG_SERVICE_TAG, "MyForegroundService $message")
    }

    companion object {
        private const val LOG_SERVICE_TAG = "SERVICE_TAG"
        private const val PAGE = "page"
        const val WORK_NAME = "work"


        fun makeRequest(page: Int): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<MyWorker>()
                .setInputData(workDataOf(PAGE to page))
                .setConstraints(makeConstraints())
                .build()
        }

        private fun makeConstraints(): Constraints {
            return Constraints.Builder()
                .setRequiresCharging(true)
                .build()
        }
    }
}