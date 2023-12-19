package br.com.dio.work.manager.worker

import android.content.Context
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import br.com.dio.work.manager.data.datasource.VideoDataSource
import br.com.dio.work.manager.ui.extensions.showBigPictureNotification
import java.util.concurrent.TimeUnit

class NotificationWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {
    override fun doWork(): Result {
        Log.i(TAG, "doWork")

        //quando worker for chamado, ir no datasource e pegar um video random
        val video = VideoDataSource.getRandomVideo()
        context.showBigPictureNotification(video)


        return Result.success()
    }

    companion object {
        private const val TAG = "NotificationWorker"
        private const val WORKER_NAME = "worker_name"

        fun start(context: Context) {
            Log.i(TAG, "starting the Worker")
            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    WORKER_NAME,
                    ExistingWorkPolicy.KEEP,
                    createRequest()
                )
        }

        private fun createRequest() =
            OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(2, TimeUnit.MINUTES)
                .build()

    }

}