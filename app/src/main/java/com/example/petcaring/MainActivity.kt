package com.example.petcaring

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.petcaring.ui.auth.LoginScreen
import com.example.petcaring.ui.auth.SignupScreen
import com.example.petcaring.ui.theme.PetCaringTheme
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import com.example.petcaring.ui.auth.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petcaring.ui.navigation.MainScreen
import com.example.petcaring.ui.screens.PetViewModel


enum class AuthScreen {
    LOGIN, SIGNUP, HOME
}

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var isDarkMode by remember { mutableStateOf(false) }
            var currentScreen by remember { mutableStateOf(AuthScreen.LOGIN) }

            val authViewModel: AuthViewModel = viewModel()

            PetCaringTheme(darkTheme = isDarkMode) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Pet Care") },
                            actions = {
                                IconButton(onClick = { isDarkMode = !isDarkMode }) {
                                    Icon(
                                        imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                        contentDescription = "Toggle Theme"
                                    )
                                }
                            }
                        )
                    }

                    //padding for the screens
                ) { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        when (currentScreen) {
                            AuthScreen.LOGIN -> LoginScreen(
                                viewModel = authViewModel,
                                onLoginSuccess = { currentScreen = AuthScreen.HOME },
                                onNavigateToSignup = { currentScreen = AuthScreen.SIGNUP }
                            )

                            AuthScreen.SIGNUP -> SignupScreen(
                                viewModel = authViewModel,
                                onSignupSuccess = { currentScreen = AuthScreen.HOME },
                                onNavigateToLogin = { currentScreen = AuthScreen.LOGIN }
                            )

                            
                            }
                        }
                    }
                }

            }
        }

        }}