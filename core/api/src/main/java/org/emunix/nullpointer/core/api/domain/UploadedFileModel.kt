package org.emunix.nullpointer.core.api.domain

import java.util.Date

data class UploadedFileModel(
    val name: String,
    val size: Long,
    val url: String,
    val uploadDate: Date,
    val token: String,
)