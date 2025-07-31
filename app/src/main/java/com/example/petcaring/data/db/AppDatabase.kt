package com.example.petcaring.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.petcaring.data.dao.AppointmentDao
import com.example.petcaring.data.dao.UserDao
import com.example.petcaring.data.dao.PetDao
import com.example.petcaring.data.model.User
import com.example.petcaring.data.model.Pet
import com.example.petcaring.data.model.Appointment

@Database(entities = [User::class, Pet::class, Appointment::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun petDao(): PetDao
    abstract fun appointmentDao(): AppointmentDao


    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pet_caring_db"
                ).build().also { instance = it }
            }
        }
    }
}
