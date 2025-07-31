package com.example.petcaring.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.petcaring.data.model.Pet

@Dao
interface PetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: Pet)
    @Delete
    suspend fun deletePet(pet: Pet)

    @Query("SELECT * FROM pets WHERE ownerId = :ownerId")
    fun getPetsForUser(ownerId: Int): LiveData<List<Pet>>

    @Query("SELECT * FROM pets WHERE ownerId = :ownerId")
    suspend fun getPetsForUserNow(ownerId: Int): List<Pet>  // <-- add this

}
