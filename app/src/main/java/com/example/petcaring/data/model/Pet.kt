package com.example.petcaring.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class Pet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ownerId: Int, // Link to the user
    val name: String,
    val breed: String,
    val type: String,
    val imageUrl: String = "" // Optional
)
