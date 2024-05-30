package org.emunix.nullpointer.core.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "history")
data class UploadedFileDbModel(
    @PrimaryKey
    val url: String,
    val name: String,
    val size: Long,
    @ColumnInfo(name = "upload_date")
    val uploadDate: Date,
)
