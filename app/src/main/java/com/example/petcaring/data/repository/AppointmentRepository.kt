package com.example.petcaring.data.repository

import android.content.Context
import com.example.petcaring.data.db.AppDatabase
import com.example.petcaring.data.model.Appointment

class AppointmentRepository(context: Context) {
    private val dao = AppDatabase.getInstance(context).appointmentDao()

    fun getAppointmentsForDate(date: Long) = dao.getAppointmentsForDate(date)
    fun getAllAppointments() = dao.getAllAppointments()

    // ✅ This is correct and already used in ViewModel
    suspend fun insertAppointment(appointment: Appointment) {
        dao.insertAppointment(appointment)
    }
}
