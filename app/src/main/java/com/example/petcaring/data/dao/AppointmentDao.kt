package com.example.petcaring.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.petcaring.data.model.Appointment

@Dao
interface AppointmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: Appointment)

    @Query("SELECT * FROM appointments WHERE date = :date")
    fun getAppointmentsForDate(date: Long): LiveData<List<Appointment>>

    @Query("SELECT * FROM appointments")
    fun getAllAppointments(): LiveData<List<Appointment>>
}
