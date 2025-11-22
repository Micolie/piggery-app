package com.piggery.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "pigs",
    indices = [Index(value = ["tagNumber"], unique = true)]
)
data class Pig(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val tagNumber: String,

    val breed: String,

    val gender: Gender,

    val dateOfBirth: Long, // Timestamp in milliseconds

    val weight: Double, // Weight in kg

    val status: Status,

    val photoPath: String? = null,

    val notes: String? = null,

    val registeredDate: Long = System.currentTimeMillis(),

    val lastUpdated: Long = System.currentTimeMillis()
) {
    enum class Gender {
        MALE,
        FEMALE
    }

    enum class Status {
        ACTIVE,
        SOLD,
        DECEASED,
        QUARANTINE
    }

    fun getAgeInDays(): Int {
        val currentTime = System.currentTimeMillis()
        val ageInMillis = currentTime - dateOfBirth
        return (ageInMillis / (1000 * 60 * 60 * 24)).toInt()
    }
}
