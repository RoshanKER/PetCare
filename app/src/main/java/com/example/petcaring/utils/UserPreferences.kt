package com.example.petcaring.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")

object UserPreferences {
    private val USER_EMAIL_KEY = stringPreferencesKey("user_email")

    suspend fun saveUserEmail(context: Context, email: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_EMAIL_KEY] = email
        }
    }

    suspend fun getUserEmail(context: Context): String? {
        return context.dataStore.data.map { prefs ->
            prefs[USER_EMAIL_KEY]
        }.first()
    }

    suspend fun clearUserEmail(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.remove(USER_EMAIL_KEY)
        }
    }
}
