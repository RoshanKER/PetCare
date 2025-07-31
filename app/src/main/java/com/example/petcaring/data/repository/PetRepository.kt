package com.example.petcaring.data.repository

import android.content.Context
import com.example.petcaring.data.db.AppDatabase
import com.example.petcaring.data.model.Pet

class PetRepository(context: Context) {
    private val petDao = AppDatabase.getInstance(context).petDao()

    // Suspend version to fetch manually
    suspend fun getPetsForUser(ownerId: Int): List<Pet> = petDao.getPetsForUserNow(ownerId)

    suspend fun addPet(pet: Pet) = petDao.insertPet(pet)

    suspend fun deletePet(pet: Pet) = petDao.deletePet(pet)
}
