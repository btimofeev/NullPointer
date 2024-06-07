package org.emunix.nullpointer.uploader.work

import android.annotation.SuppressLint
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import org.emunix.nullpointer.core.api.UPLOAD_NOTIFICATION_ID
import org.emunix.nullpointer.core.api.di.AppProvider
import org.emunix.nullpointer.core.api.domain.UploadedFileModel
import org.emunix.nullpointer.uploader.di.UploadComponent

class UploadWorker(
    private val workerParams: WorkerParameters,
    private val appProvider: AppProvider,
) : CoroutineWorker(appProvider.getContext(), workerParams) {

    override suspend fun doWork(): Result {
        runCatching {
            val fileName = inputData.getString(UPLOAD_WORK_PARAM_FILE_NAME_KEY).orEmpty()
            val uri = Uri.parse(inputData.getString(UPLOAD_WORK_PARAM_URI_KEY))
            appProvider.getContext().contentResolver?.openInputStream(uri)?.use { inputStream ->
                val uploadProvider = UploadComponent.create(appProvider)
                val response = uploadProvider.getUploadRepository().upload(fileName, inputStream).getOrThrow()
                addToHistory(response)
                return Result.success(workDataOf(UPLOAD_WORK_RESULT_KEY to response.url))
            } ?: error("Cannot open inputStream from Uri: $uri")
        }.onFailure {
            return Result.failure()
        }
        return Result.failure()
    }

    @SuppressLint("InlinedApi")
    override suspend fun getForegroundInfo(): ForegroundInfo =
        ForegroundInfo(
            /* notificationId = */ UPLOAD_NOTIFICATION_ID,
            /* notification = */ appProvider.getUploadNotificationProvider().getNotification(),
            /* foregroundServiceType = */ FOREGROUND_SERVICE_TYPE_DATA_SYNC
        )

    private suspend fun addToHistory(model: UploadedFileModel) {
        appProvider.getDatabaseRepository().addToHistory(model.url, model.name, model.size, model.uploadDate)
    }
}