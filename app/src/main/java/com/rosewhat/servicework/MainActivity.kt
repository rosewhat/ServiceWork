package com.rosewhat.servicework

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobWorkItem
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.rosewhat.servicework.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var page = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.simpleService.setOnClickListener {
            // остановка сервиса из вне сервиса
            stopService(MyForegroundService.newInstance(this))
            startService(MyService.newInstance(this, 25))
        }
        binding.foregroundService.setOnClickListener {
            // в течении 5 секунд покажем уведомление
            ContextCompat.startForegroundService(
                this,
                MyForegroundService.newInstance(this)
            )
        }
        binding.intentService.setOnClickListener {
            ContextCompat.startForegroundService(
                this,
                MyIntentService.newInstance(this)
            )
        }
        binding.jobScheduler.setOnClickListener {
            val componentName = ComponentName(this, MyJobService::class.java)
            // требования
            val jobInfo = JobInfo.Builder(MyJobService.JOB_ID, componentName)
                // зарядка
                .setRequiresCharging(true)
                // wi fi
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                // выключили и снова вкл телефон
                // .setPersisted(true)
                .build()

            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent = MyJobService.newIntent(page++)
                // складываем в очередь все сервисы
                jobScheduler.enqueue(jobInfo, JobWorkItem(intent))
            } else {
                startService(MyIntentService2.newInstance(this, page++))
            }
        }
        binding.jobIntentService.setOnClickListener {
            MyJobIntentService.enqueue(this, page++)
        }


    }


}