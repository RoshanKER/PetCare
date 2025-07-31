package com.example.petcaring.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.petcaring.data.db.AppDatabase
import com.example.petcaring.data.model.Appointment
import com.example.petcaring.data.repository.AppointmentRepository
import com.example.petcaring.data.repository.PetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AppointmentViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = AppointmentRepository(application)
    val appointments: LiveData<List<Appointment>> = repo.getAllAppointments()

    fun addAppointment(appointment: Appointment) {
        viewModelScope.launch {
            repo.insertAppointment(appointment)
        }
    }
}
