package com.example.petcaring.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", "Home", Icons.Filled.Home)
    object Appointments : BottomNavItem("appointments", "Appointments", Icons.Filled.CalendarToday)
    object Profile : BottomNavItem("profile", "Profile", Icons.Filled.Person)
}
