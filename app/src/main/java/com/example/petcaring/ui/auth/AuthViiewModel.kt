package com.example.petcaring.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcaring.data.model.User
import com.example.petcaring.data.repository.UserRepository
import com.example.petcaring.utils.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = UserRepository(application)

    private val _loginSuccess = MutableStateFlow<User?>(null)
    val loginSuccess: StateFlow<User?> = _loginSuccess

    private val _loggedInUser = MutableStateFlow<User?>(null)
    val loggedInUser: StateFlow<User?> = _loggedInUser

    fun register(user: User, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                repo.register(user)
                _loggedInUser.value = user
                UserPreferences.saveUserEmail(getApplication(), user.email)

                onSuccess()

            } catch (e: Exception) {
                onError("Registration failed: ${e.message}")
            }
        }
    }

    fun login(email: String, password: String, onSuccess: (User) -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            val user = repo.login(email, password)
            println(user)
            if (user != null) {
                _loginSuccess.value = user
                _loggedInUser.value = user
                UserPreferences.saveUserEmail(getApplication(), user.email)
                onSuccess(user)
            } else {
                onError()
            }
        }
    }

    fun loadLoggedInUser() {
        viewModelScope.launch {
            val email = UserPreferences.getUserEmail(getApplication())
            if (!email.isNullOrEmpty()) {
                val user = repo.getUserByEmail(email)
                _loggedInUser.value = user
            }
        }
    }

    fun updateUser(updatedUser: User, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            try {
                repo.updateUser(updatedUser)
                _loggedInUser.value = updatedUser
                onSuccess()
            } catch (e: Exception) {
                onError("Update failed: ${e.message}")
            }
        }
    }

    fun logout(onComplete: () -> Unit) {
        _loggedInUser.value = null
        _loginSuccess.value = null
        onComplete()
    }


}
