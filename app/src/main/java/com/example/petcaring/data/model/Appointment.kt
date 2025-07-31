package com.example.petcaring.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "appointments")
data class Appointment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val petId: Int,
    val date: Long, // Store date as timestamp (start of day)
    val time: String,
    val description: String
)
