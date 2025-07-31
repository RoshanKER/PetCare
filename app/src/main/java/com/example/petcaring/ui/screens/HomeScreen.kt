package com.example.petcaring.ui.screens

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.petcaring.R
import com.example.petcaring.data.model.Appointment
import com.example.petcaring.data.model.Pet
import com.example.petcaring.ui.AppointmentViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    petViewModel: PetViewModel = viewModel(),
    appointmentViewModel: AppointmentViewModel = viewModel(),
    loggedInUserId: Int  // <-- pass this from wherever login is done
) {
    // Set userId and load pets after login
    LaunchedEffect(loggedInUserId) {
        petViewModel.userId = loggedInUserId
        petViewModel.loadPets()
    }

    val pets by petViewModel.pets
    val appointments by appointmentViewModel.appointments.observeAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var selectedPetForHistory by remember { mutableStateOf<Pet?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Pet")
            }
        },
        topBar = {
            TopAppBar(title = { Text("Your Pets") })
        }
    ) { paddingValues ->
        if (pets.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No pets found. Add your first pet!")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                items(pets) { pet ->
                    PetCard(
                        pet = pet,
                        onClick = { selectedPetForHistory = pet },
                        onDelete = { petViewModel.deletePet(it) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        if (showDialog) {
            AddPetDialog(
                onDismiss = { showDialog = false },
                onAdd = { pet ->
                    petViewModel.addPet(pet)
                    showDialog = false
                },
                ownerId = petViewModel.userId
            )
        }

        selectedPetForHistory?.let { pet ->
            PetHistoryDialog(
                pet = pet,
                onDismiss = { selectedPetForHistory = null },
                appointments = appointments
            )
        }
    }
}

@Composable
fun AddPetDialog(
    onDismiss: () -> Unit,
    onAdd: (Pet) -> Unit,
    ownerId: Int
) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var breed by remember { mutableStateOf(TextFieldValue("")) }
    var type by remember { mutableStateOf(TextFieldValue("")) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val contentResolver = context.contentResolver

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            imageUri = it
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    if (name.text.isNotBlank() && breed.text.isNotBlank() && type.text.isNotBlank()) {
                        onAdd(
                            Pet(
                                ownerId = ownerId,
                                name = name.text,
                                breed = breed.text,
                                type = type.text,
                                imageUrl = imageUri?.toString() ?: ""
                            )
                        )
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Add Pet") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Pet Name") })
                OutlinedTextField(value = breed, onValueChange = { breed = it }, label = { Text("Breed") })
                OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Type (e.g. Dog, Cat)") })

                Button(onClick = { launcher.launch("image/*") }) {
                    Text("Select Image")
                }

                imageUri?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(4.dp)
                    )
                }
            }
        }
    )
}

@Composable
fun PetCard(
    pet: Pet,
    onClick: () -> Unit,
    onDelete: (Pet) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (pet.imageUrl.isNotBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(pet.imageUrl),
                    contentDescription = "Pet Image",
                    modifier = Modifier
                        .size(100.dp)
                        .aspectRatio(1f)
                        .padding(end = 16.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Pets,
                    contentDescription = "Default Pet Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(100.dp)
                        .aspectRatio(1f)
                        .padding(end = 16.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text("Name: ${pet.name}", style = MaterialTheme.typography.titleMedium)
                Text("Type: ${pet.type}", style = MaterialTheme.typography.bodyMedium)
                Text("Breed: ${pet.breed}", style = MaterialTheme.typography.bodySmall)
            }

            IconButton(onClick = { onDelete(pet) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Pet",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PetHistoryDialog(
    pet: Pet,
    onDismiss: () -> Unit,
    appointments: List<Appointment>
) {
    val today = LocalDate.now().toEpochDay()
    val petAppointments = appointments.filter { it.petId == pet.id }
    val upcoming = petAppointments.filter { it.date >= today }.sortedBy { it.date }
    val past = petAppointments.filter { it.date < today }.sortedByDescending { it.date }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        title = { Text("Medical History: ${pet.name}") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (upcoming.isNotEmpty()) {
                    Text("Upcoming Appointments", style = MaterialTheme.typography.titleSmall)
                    upcoming.forEach {
                        Text("- ${LocalDate.ofEpochDay(it.date)} at ${it.time}: ${it.description}")
                    }
                } else {
                    Text("No upcoming appointments")
                }

                Spacer(Modifier.height(12.dp))

                if (past.isNotEmpty()) {
                    Text("Past Appointments", style = MaterialTheme.typography.titleSmall)
                    past.forEach {
                        Text("- ${LocalDate.ofEpochDay(it.date)} at ${it.time}: ${it.description}")
                    }
                } else {
                    Text("No past appointments")
                }
            }
        }
    )
}
