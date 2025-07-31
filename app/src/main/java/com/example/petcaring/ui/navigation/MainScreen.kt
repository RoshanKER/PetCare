package com.example.petcaring.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petcaring.ui.AppointmentViewModel
import com.example.petcaring.ui.screens.AppointmentScreen
import com.example.petcaring.ui.screens.HomeScreen
import com.example.petcaring.ui.screens.PetViewModel
import com.example.petcaring.ui.screens.ProfileScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(userId: Int) { // 👈 Pass userId
    val navController = rememberNavController()
    val petViewModel: PetViewModel = viewModel()
    val appointmentViewModel: AppointmentViewModel = viewModel()

    // ✅ Set userId once and load pets
    LaunchedEffect(userId) {
        petViewModel.userId = userId
        petViewModel.loadPets()
    }

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Appointments,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentBackStack by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStack?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentRoute == item.route,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(
                    petViewModel = petViewModel,
                    appointmentViewModel = appointmentViewModel,
                    loggedInUserId = userId // 👈 FIX: pass the userId!
                )
            }

            composable(BottomNavItem.Appointments.route) {
                AppointmentScreen(
                    petViewModel = petViewModel,
                    appointmentViewModel = appointmentViewModel
                )
            }
            composable(BottomNavItem.Profile.route) {
                ProfileScreen()
            }
        }
    }
}
