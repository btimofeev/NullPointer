package org.emunix.nullpointer.core.impl.data

import androidx.room.TypeConverter
import java.util.Date

internal class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long): Date = Date(value)

    @TypeConverter
    fun dateToTimestamp(date: Date): Long = date.time
}
