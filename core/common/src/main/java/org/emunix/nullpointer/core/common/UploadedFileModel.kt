package org.emunix.nullpointer.core.common

import java.util.Date

data class UploadedFileModel(
    val name: String,
    val size: Long,
    val url: String,
    val uploadDate: Date,
)