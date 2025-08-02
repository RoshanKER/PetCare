package com.example.petcaring.ui.screens

import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.petcaring.data.model.Appointment
import com.example.petcaring.data.model.Pet
import com.example.petcaring.ui.AppointmentViewModel
import java.time.LocalDate
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentScreen(
    petViewModel: PetViewModel,
    appointmentViewModel: AppointmentViewModel
) {
    val appointments by appointmentViewModel.appointments.observeAsState(initial = emptyList())
    val pets = petViewModel.pets.value
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Appointments") }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())

        ) {

            AppointmentCalendar(
                appointments = appointments,
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Book Appointment")
            }

            Spacer(modifier = Modifier.height(16.dp))

            AppointmentList(
                appointments = appointments.filter { it.date == selectedDate.toEpochDay() },
                pets = pets
            )
        }

        if (showDialog) {
            AppointmentBookingDialog(
                date = selectedDate,
                pets = pets,
                onBook = {
                    appointmentViewModel.addAppointment(it)
                    showDialog = false
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Composable
fun AppointmentList(
    appointments: List<Appointment>,
    pets: List<Pet>
) {
    if (appointments.isEmpty()) {
        Text("No appointments for selected date")
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            appointments.forEach { appointment ->
                val pet = pets.find { it.id == appointment.petId }
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            "Pet: ${pet?.name ?: "Unknown"}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("Time: ${appointment.time}")
                        Text("Description: ${appointment.description}")
                    }
                }
            }
        }
    }
}