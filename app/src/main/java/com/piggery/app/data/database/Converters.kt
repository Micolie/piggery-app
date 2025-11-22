package com.piggery.app.data.database

import androidx.room.TypeConverter
import com.piggery.app.data.entity.Pig

class Converters {

    @TypeConverter
    fun fromGender(value: Pig.Gender): String {
        return value.name
    }

    @TypeConverter
    fun toGender(value: String): Pig.Gender {
        return Pig.Gender.valueOf(value)
    }

    @TypeConverter
    fun fromStatus(value: Pig.Status): String {
        return value.name
    }

    @TypeConverter
    fun toStatus(value: String): Pig.Status {
        return Pig.Status.valueOf(value)
    }
}
