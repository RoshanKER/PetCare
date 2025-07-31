package com.example.petcaring.ui.screens

import android.app.TimePickerDialog
import android.os.Build
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.petcaring.data.model.Appointment
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.petcaring.data.model.Pet
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentCalendar(
    appointments: List<Appointment>,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    var currentMonth by remember { mutableStateOf(YearMonth.from(selectedDate)) }

    val startOfMonth = currentMonth.atDay(1)
    val daysBefore = startOfMonth.dayOfWeek.value % 7
    val daysInMonth = currentMonth.lengthOfMonth()
    val calendarDays = List(daysBefore) { null } + List(daysInMonth) {
        currentMonth.atDay(it + 1)
    }

    val appointmentsByDate = appointments.groupBy { LocalDate.ofEpochDay(it.date) }

    Column {
        // Month Header
        Row(
            Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Previous")
            }

            Text(
                text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                style = MaterialTheme.typography.titleMedium
            )

            IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next")
            }
        }

        // Week Days
        Row(Modifier.fillMaxWidth()) {
            DayOfWeek.values().forEach {
                Text(
                    text = it.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                    Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        // Calendar Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height(300.dp),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(calendarDays) { date ->
                val isSelected = date == selectedDate
                val hasAppointments = appointmentsByDate.containsKey(date)

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(2.dp)
                        .clickable(enabled = date != null) { date?.let(onDateSelected) }
                        .background(
                            color = when {
                                isSelected -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.surface
                            },
                            shape = MaterialTheme.shapes.small
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = date?.dayOfMonth?.toString() ?: "",
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )

                        if (hasAppointments) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .size(6.dp)
//                                    .clip(CircleShape)
                                    .background(Color.Green)
                            )
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentBookingDialog(
    date: LocalDate,
    pets: List<Pet>,
    onBook: (Appointment) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedPetId by remember { mutableStateOf(pets.firstOrNull()?.id ?: -1) }
    var time by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val context = LocalContext.current
    val showTimePicker = remember { mutableStateOf(false) }

    if (showTimePicker.value) {
        TimePickerDialog(
            context,
            { _, hour: Int, minute: Int ->
                val isPM = hour >= 12
                val formattedHour = if (hour % 12 == 0) 12 else hour % 12
                val amPm = if (isPM) "PM" else "AM"
                time = String.format("%02d:%02d %s", formattedHour, minute, amPm)
                showTimePicker.value = false
            },
            12, 0, false
        ).show()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Book Appointment") },
        text = {
            Column {
                // ✅ Show selected date
                Text(
                    text = "Date: ${date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))}",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

// 🔄 Pet selection dropdown (fixed)
                var expanded by remember { mutableStateOf(false) }
                val selectedPet = pets.find { it.id == selectedPetId }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = selectedPet?.name ?: "",
                        onValueChange = {},
                        label = { Text("Select Pet") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        pets.forEach { pet ->
                            DropdownMenuItem(
                                text = { Text(pet.name) },
                                onClick = {
                                    selectedPetId = pet.id
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // ✅ Time selector button
                Button(
                    onClick = { showTimePicker.value = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (time.isNotBlank()) "Time: $time" else "Select Time")
                }

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedPetId != -1 && time.isNotBlank()) {
                        onBook(
                            Appointment(
                                petId = selectedPetId,
                                date = date.toEpochDay(),
                                time = time,
                                description = description
                            )
                        )
                    }
                }
            ) {
                Text("Book")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun DropdownMenuBox(
    items: List<Pet>,
    selectedId: Int,
    onItemSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedPet = items.find { it.id == selectedId }

    Box {
        OutlinedTextField(
            value = selectedPet?.name ?: "",
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            label = { Text("Select Pet") }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { pet ->
                DropdownMenuItem(
                    text = { Text(pet.name) },
                    onClick = {
                        onItemSelected(pet.id)
                        expanded = false
                    }
                )
            }
        }
    }
}
