package com.example.racefeeds.ui.screens.Checkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.racefeeds.ui.screens.Cart.CartViewModel
import com.example.racefeeds.ui.screens.firebase.AuthViewModel

@Composable
fun CheckoutScreen(
    checkoutViewModel: CheckoutViewModel,
    navController: NavController,
    innerPadding: PaddingValues,
    cartViewModel: CartViewModel,
    authViewModel: AuthViewModel,
) {
    val uiState by checkoutViewModel.uiState.collectAsState()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    var showLoginPrompt by remember { mutableStateOf(false) }

    val isCheckoutEnabled = uiState.location.isNotBlank() && uiState.paymentOption != null

    LaunchedEffect(uiState.isOrderPlaced) {
        if (uiState.isOrderPlaced) {
            navController.navigate("order_success") {
                popUpTo("checkout") { inclusive = true }
            }
            checkoutViewModel.resetOrderState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(8.dp)
    ) {
        Text("Checkout", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.location,
            onValueChange = checkoutViewModel::onLocationChanged,
            label = { Text("Enter Location") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Select Payment Option:")

        Row(modifier = Modifier.fillMaxWidth()) {
            val selectedOption = uiState.paymentOption

            RadioButton(
                selected = selectedOption == PaymentOption.MPESA, onClick = {
                    checkoutViewModel.onPaymentOptionSelected(
                        if (selectedOption == PaymentOption.MPESA) null else PaymentOption.MPESA
                    )
                })
            Text("M-Pesa", modifier = Modifier.padding(start = 4.dp))

            Spacer(modifier = Modifier.width(16.dp))

            RadioButton(
                selected = selectedOption == PaymentOption.ON_DELIVERY, onClick = {
                    checkoutViewModel.onPaymentOptionSelected(
                        if (selectedOption == PaymentOption.ON_DELIVERY) null else PaymentOption.ON_DELIVERY
                    )
                })
            Text("On Delivery", modifier = Modifier.padding(start = 4.dp))
        }

        if (uiState.paymentOption == PaymentOption.MPESA) {
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "M-Pesa Payment Instructions",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Paybill Number: 123456")
                    Text("Account Number: RACEFEEDS")
                    Text("Amount: Ksh ${uiState.totalPrice}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Open your M-Pesa app and complete the payment. Then tap 'Place Order'.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Total: Ksh ${uiState.totalPrice}", style = MaterialTheme.typography.titleLarge)

        uiState.errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (!isAuthenticated) {
                    showLoginPrompt = true
                    return@Button
                }

                if (uiState.location.isBlank()) {
                    checkoutViewModel.showError("Please enter your location")
                } else if (isCheckoutEnabled) {
                    checkoutViewModel.placeOrder()
                } else {
                    checkoutViewModel.clearError()
                }
            }, enabled = !uiState.isProcessingPayment, modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isProcessingPayment) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                Text("Place Order")
            }
        }

        if (showLoginPrompt) {
            AlertDialog(
                onDismissRequest = { showLoginPrompt = false },
                title = { Text("Login Required") },
                text = { Text("You must be signed in to place an order.") },
                confirmButton = {
                    TextButton(onClick = {
                        showLoginPrompt = false
                        navController.navigate("login?returnTo=checkout") {
                            popUpTo("checkout") { inclusive = true }
                        }
                    }) {
                        Text("Sign In")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLoginPrompt = false }) {
                        Text("Cancel")
                    }
                })
        }
    }

    LaunchedEffect(Unit) {
        checkoutViewModel.setCartItems(cartViewModel.cartItems.value)
    }
}