package com.example.petcaring.ui.screens

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcaring.data.model.Pet
import com.example.petcaring.data.repository.PetRepository
import kotlinx.coroutines.launch

class PetViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = PetRepository(application)

    var userId: Int = 0

    private val _pets = mutableStateOf<List<Pet>>(emptyList())
    val pets: State<List<Pet>> = _pets

    // Must call this after userId is set
    fun loadPets() {
        viewModelScope.launch {
            _pets.value = repo.getPetsForUser(userId) as List<Pet>
        }
    }

    fun addPet(pet: Pet) {
        viewModelScope.launch {
            repo.addPet(pet)
            loadPets()
        }
    }

    fun deletePet(pet: Pet) {
        viewModelScope.launch {
            repo.deletePet(pet)
            loadPets()
        }
    }
}
