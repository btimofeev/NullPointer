package org.emunix.nullpointer.uploader.impl.data.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadApi {

    @Multipart
    @POST("/")
    suspend fun upload(
        @Part part: MultipartBody.Part
    ): Response<String>
}