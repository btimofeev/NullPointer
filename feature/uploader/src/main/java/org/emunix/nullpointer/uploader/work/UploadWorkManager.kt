package org.emunix.nullpointer.uploader.work

import android.content.Context
import androidx.work.ExistingWorkPolicy.REPLACE
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST
import androidx.work.WorkInfo.State.BLOCKED
import androidx.work.WorkInfo.State.CANCELLED
import androidx.work.WorkInfo.State.ENQUEUED
import androidx.work.WorkInfo.State.FAILED
import androidx.work.WorkInfo.State.RUNNING
import androidx.work.WorkInfo.State.SUCCEEDED
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.emunix.nullpointer.uploader.domain.model.UploadStatus

internal interface UploadWorkManager {

    fun startUpload(
        fileName: String,
        uri: String,
    )

    fun observeUpload(): Flow<UploadStatus>

    fun cancelUpload()
}

internal class UploadWorkManagerImpl(
    private val context: Context,
) : UploadWorkManager {

    override fun startUpload(fileName: String, uri: String) {
        val uploadWork = OneTimeWorkRequestBuilder<UploadWorker>()
            .setInputData(
                workDataOf(
                    UPLOAD_WORK_PARAM_FILE_NAME_KEY to fileName,
                    UPLOAD_WORK_PARAM_URI_KEY to uri,
                )
            )
            .setExpedited(RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                /* uniqueWorkName = */ UPLOAD_WORK_NAME_KEY,
                /* existingWorkPolicy = */ REPLACE,
                /* work = */ uploadWork
            )
    }

    override fun observeUpload(): Flow<UploadStatus> {
        val workManager = WorkManager.getInstance(context)
        return workManager.getWorkInfosForUniqueWorkFlow(UPLOAD_WORK_NAME_KEY)
            .map { works ->
                if (works.isNotEmpty()) {
                    val workInfo = works.first()
                    when (workInfo.state) {
                        ENQUEUED,
                        BLOCKED,
                        RUNNING -> UploadStatus.Running

                        SUCCEEDED -> {
                            val url = workInfo.outputData.getString(UPLOAD_WORK_RESULT_KEY)
                            workManager.pruneWork()
                            if (url != null) UploadStatus.Success(url) else UploadStatus.Failed
                        }

                        FAILED -> {
                            workManager.pruneWork()
                            UploadStatus.Failed
                        }
                        CANCELLED -> {
                            workManager.pruneWork()
                            UploadStatus.Cancelled
                        }
                    }
                } else UploadStatus.Unavailable
            }
    }

    override fun cancelUpload() {
        WorkManager.getInstance(context).cancelUniqueWork(UPLOAD_WORK_NAME_KEY)
    }
}