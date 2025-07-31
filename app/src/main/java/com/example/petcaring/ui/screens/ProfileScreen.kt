package com.example.petcaring.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petcaring.ui.auth.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(authViewModel: AuthViewModel = viewModel()) {
    val user by authViewModel.loggedInUser.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var postCode by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        authViewModel.loadLoggedInUser() // Ensure user is loaded from DataStore
    }

    LaunchedEffect(user?.id) {
        user?.let {
            firstName = it.firstName
            lastName = it.lastName
            email = it.email
            address = it.address
            postCode = it.postCode
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Profile") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Image(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile Icon",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
            }

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = {},
                label = { Text("Email") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = postCode,
                onValueChange = { postCode = it },
                label = { Text("Post Code") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    user?.let {
                        val updatedUser = it.copy(
                            firstName = firstName,
                            lastName = lastName,
                            address = address,
                            postCode = postCode
                        )
                        authViewModel.updateUser(
                            updatedUser,
                            onSuccess = {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Profile updated successfully")
                                }
                            },
                            onError = {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Failed to update profile")
                                }
                            }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }


        }
    }
}
