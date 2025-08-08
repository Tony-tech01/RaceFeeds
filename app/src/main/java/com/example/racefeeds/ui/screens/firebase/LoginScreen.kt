package com.example.racefeeds.ui.screens.firebase

import android.R.attr.enabled
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    returnTo: String
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val errorMessage = authViewModel.errorMessage.value


    LaunchedEffect(isAuthenticated) {
        Log.d("LoginCrash", "isAuthenticated changed: $isAuthenticated")
        Log.d("LoginCrash", "returnTo: $returnTo")

        try {
            if (isAuthenticated) {
                Log.d("LoginCrash", "Navigating to $returnTo")
                navController.navigate(returnTo) {
                    popUpTo("login") { inclusive = true }
                }
            }
        } catch (e: Exception) {
            Log.e("LoginCrash", "Navigation crash", e)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    authViewModel.signIn(email.trim(), password)
                }
            })
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                Log.d("LoginCrash", "Login button clicked")
                Log.d("LoginCrash", "Email: '$email', Password: '$password'")

                try {
                    if (email.isBlank() || password.isBlank()) {
                        Log.w("LoginCrash", "Empty fields detected")
                        authViewModel.setErrorMessage("Email and password must not be empty")
                        return@Button
                    }

                    Log.d("LoginCrash", "Calling signIn()")
                    authViewModel.signIn(email.trim(), password)
                } catch (e: Exception) {
                    Log.e("LoginCrash", "Exception during signIn", e)
                }

            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Sign In")
            }
        }

        if (!errorMessage.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(errorMessage, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Don't have an account? Sign up",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.Underline),
            modifier = Modifier
                .clickable {
                    navController.navigate("signup?returnTo=$returnTo")
                }
                .padding(top = 8.dp)
        )
    }
}