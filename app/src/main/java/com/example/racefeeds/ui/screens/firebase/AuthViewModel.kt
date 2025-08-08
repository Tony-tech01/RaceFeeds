package com.example.racefeeds.ui.screens.firebase

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _isAuthenticated = MutableStateFlow(firebaseAuth.currentUser != null)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> get() = _errorMessage

    init {
        val user = firebaseAuth.currentUser
        if (user != null) {
            android.util.Log.d("AuthViewModel", "User already signed in: ${user.email}")
        } else {
            android.util.Log.d("AuthViewModel", "No user signed in")
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                firebaseAuth.signInWithEmailAndPassword(email, password).await()
                _isAuthenticated.value = true
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Login failed"
                _isAuthenticated.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createAccount(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                _isAuthenticated.value = true
                createUserProfile(result.user?.uid ?: "", email)
            } catch (e: FirebaseAuthException) {
                _errorMessage.value = when (e.errorCode) {
                    "ERROR_EMAIL_ALREADY_IN_USE" -> "Email is already registered"
                    "ERROR_INVALID_EMAIL" -> "Invalid email format"
                    "ERROR_WEAK_PASSWORD" -> "Password is too weak"
                    else -> "Signup failed. Please try again"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun createUserProfile(uid: String, email: String) {
        val userDoc = Firebase.firestore.collection("users").document(uid)
        userDoc.set(
            mapOf(
                "email" to email,
                "createdAt" to FieldValue.serverTimestamp()
            )
        )
    }

    fun signOut() {
        firebaseAuth.signOut()
        _isAuthenticated.value = false
    }

    fun setErrorMessage(message: String?) {
        _errorMessage.value = message
    }

    fun clearError() {
        _errorMessage.value = null
    }
}