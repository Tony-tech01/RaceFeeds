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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.racefeeds.ui.screens.Cart.CartViewModel


@Composable
fun CheckoutScreen(
    checkoutViewModel: CheckoutViewModel,
    navController: NavController,
    innerPadding: PaddingValues,
    cartViewModel: CartViewModel,
) {
    val uiState by checkoutViewModel.uiState.collectAsState()
    val isCheckoutEnabled =
        uiState.location.isNotBlank() && (uiState.paymentOption == PaymentOption.ON_DELIVERY || uiState.phoneNumber.matches(
            Regex("^07\\d{8}$")
        ))

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
            RadioButton(
                selected = uiState.paymentOption == PaymentOption.MPESA,
                onClick = { checkoutViewModel.onPaymentOptionSelected(PaymentOption.MPESA) })
            Text("Pay Now", modifier = Modifier.padding(start = 4.dp))

            Spacer(modifier = Modifier.width(16.dp))

            RadioButton(
                selected = uiState.paymentOption == PaymentOption.ON_DELIVERY,
                onClick = { checkoutViewModel.onPaymentOptionSelected(PaymentOption.ON_DELIVERY) })
            Text("On Delivery", modifier = Modifier.padding(start = 4.dp))
        }

        if (uiState.paymentOption == PaymentOption.MPESA) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = uiState.phoneNumber,
                onValueChange = checkoutViewModel::onPhoneNumberChanged,
                label = { Text("Enter Phone Number (M-Pesa)") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )
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
                if (uiState.location.isBlank()) {
                    checkoutViewModel.showError("Please enter your location")
                } else if (uiState.paymentOption == PaymentOption.MPESA && uiState.phoneNumber.isBlank()) {
                    checkoutViewModel.showError("Please enter your phone number")
                } else if (isCheckoutEnabled) {
                    checkoutViewModel.placeOrder()
                } else {
                    checkoutViewModel.clearError()
                }
            },
            enabled = !uiState.isProcessingPayment,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isProcessingPayment) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                Text("Place Order")
            }
        }

        if (uiState.showConfirmationDialog) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Payment Sent") },
                text = {
                    if (uiState.isWaitingForPayment) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Waiting for payment confirmation...")
                        }
                    } else {
                        Text("Payment confirmed!")
                    }
                },
                confirmButton = {
                    if (!uiState.isWaitingForPayment) {
                        TextButton(onClick = {
                            checkoutViewModel.dismissDialog()
                            checkoutViewModel.confirmOrder()
                        }) {
                            Text("Continue")
                        }
                    }
                }
            )
        }


    }

    LaunchedEffect(Unit) {
        checkoutViewModel.setCartItems(cartViewModel.cartItems.value)
    }


}

