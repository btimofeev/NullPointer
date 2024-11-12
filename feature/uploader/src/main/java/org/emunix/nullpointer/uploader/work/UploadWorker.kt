package org.emunix.nullpointer.uploader.work

import android.annotation.SuppressLint
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import org.emunix.nullpointer.core.api.UPLOAD_NOTIFICATION_ID
import org.emunix.nullpointer.core.api.di.AppProvider
import org.emunix.nullpointer.core.api.domain.FileTypeIsForbiddenException
import org.emunix.nullpointer.core.api.domain.MaxFileSizeHasBeenExceedsException
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
                val response = uploadProvider.getCheckAndUploadFileUseCase().invoke(fileName, inputStream).getOrThrow()
                addToHistory(response)
                return Result.success(workDataOf(UPLOAD_WORK_RESULT_KEY to response.url))
            } ?: error("Cannot open inputStream from Uri: $uri")
        }.onFailure { err ->
            return Result.failure(getErrorData(err))
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
        appProvider.getDatabaseRepository().addToHistory(
            url = model.url,
            name = model.name,
            size = model.size,
            uploadDate = model.uploadDate,
            token = model.token,
        )
    }

    private fun getErrorData(err: Throwable): Data {
        val data = Data.Builder()
        when (err) {
            is FileTypeIsForbiddenException -> data.putBoolean(UPLOAD_WORK_ERROR_FILE_TYPE_IS_FORBIDDEN, true)
            is MaxFileSizeHasBeenExceedsException -> data.putBoolean(UPLOAD_WORK_ERROR_MAX_SIZE, true)
        }
        return data.build()
    }
}