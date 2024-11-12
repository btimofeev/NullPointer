package org.emunix.nullpointer.uploader.data.repository

import okhttp3.MultipartBody.Part
import okhttp3.RequestBody
import org.emunix.nullpointer.core.api.domain.ResponseIsEmptyException
import org.emunix.nullpointer.core.api.domain.UploadFailedException
import org.emunix.nullpointer.uploader.data.api.UploadApi
import org.emunix.nullpointer.uploader.domain.UploadRepository
import org.emunix.nullpointer.uploader.domain.model.UploadResponse
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class UploadRepositoryImpl @Inject constructor(
    private val uploadApi: UploadApi,
) : UploadRepository {

    override suspend fun upload(
        file: File,
    ): UploadResponse {
        val response = sendFile(file)
        if (response.isSuccessful) {
            val body = response.body() ?: throw ResponseIsEmptyException()
            return UploadResponse(
                url = body,
                token = response.headers().get(HEADER_X_TOKEN).orEmpty(),
            )
        } else {
            throw UploadFailedException()
        }
    }

    private suspend fun sendFile(file: File): Response<String> {
        return uploadApi.upload(
            part = Part
                .createFormData(
                    "file",
                    file.name,
                    RequestBody.create(
                        null,
                        file
                    )
                )
        )
    }

    private companion object {

        private const val HEADER_X_TOKEN = "X-Token"
    }
}