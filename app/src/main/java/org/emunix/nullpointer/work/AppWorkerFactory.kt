package org.emunix.nullpointer.work

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import org.emunix.nullpointer.core.api.di.AppProvider
import org.emunix.nullpointer.uploader.work.UploadWorker

class AppWorkerFactory(private val appProvider: AppProvider) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        if (Class.forName(workerClassName).isAssignableFrom(UploadWorker::class.java)) {
            return UploadWorker(workerParameters, appProvider)
        } else {
            throw IllegalStateException("Unknown worker class name: $workerClassName")
        }
    }
}