package com.example.racefeeds.ui.screens.firebase

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class AuthViewModel(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ViewModel() {

    private val _isAuthenticated = MutableStateFlow(firebaseAuth.currentUser != null)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    private val _userEmail = MutableStateFlow(firebaseAuth.currentUser?.email)
    val userEmail: StateFlow<String?> = _userEmail

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> get() = _errorMessage

    private val _currentUser = MutableStateFlow<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    private val _userId = MutableStateFlow(firebaseAuth.currentUser?.uid)
    val userId: StateFlow<String?> = _userId

    init {
        firebaseAuth.addAuthStateListener {
            val user = it.currentUser
            _currentUser.value = user
            _userId.value = user?.uid
        }
    }

    init {
        val user = firebaseAuth.currentUser
        if (user != null) {
            _userEmail.value = user.email
            checkAdminStatus(user.uid)
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val user = firebaseAuth.currentUser
                _isAuthenticated.value = true
                _userEmail.value = user?.email
                _currentUser.value = user
                _userId.value = user?.uid
                user?.uid?.let { checkAdminStatus(it) }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Login failed"
                _isAuthenticated.value = false
                _userId.value = null
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
                val user = result.user ?: throw Exception("User creation failed")
                val uid = user.uid

                _isAuthenticated.value = true
                _userEmail.value = user.email
                _currentUser.value = user
                _userId.value = uid

                createUserProfile(uid, email)
                checkAdminStatus(uid)
            } catch (e: FirebaseAuthException) {
                _errorMessage.value = when (e.errorCode) {
                    "ERROR_EMAIL_ALREADY_IN_USE" -> "Email is already registered"
                    "ERROR_INVALID_EMAIL" -> "Invalid email format"
                    "ERROR_WEAK_PASSWORD" -> "Password is too weak"
                    else -> "Signup failed. Please try again"
                }
                _userId.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Signup failed"
                _userId.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun createUserProfile(uid: String, email: String) {
        val userDoc = firestore.collection("users").document(uid)
        userDoc.set(
            mapOf(
                "email" to email,
                "createdAt" to FieldValue.serverTimestamp(),
                "isAdmin" to false
            )
        )
    }

    private fun checkAdminStatus(uid: String) {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("users").document(uid).get().await()
                val isAdminFlag = snapshot.getBoolean("isAdmin") ?: false
                val role = snapshot.getString("role")?.lowercase()
                val isAdmin = isAdminFlag || role == "admin"

                _isAdmin.value = isAdmin
                Log.d("AuthViewModel", "Admin check — isAdmin: $isAdminFlag, role: $role, result: $isAdmin")
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Failed to check admin status", e)
                _isAdmin.value = false
            }
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
        _isAuthenticated.value = false
        _userEmail.value = null
        _isAdmin.value = false
    }

    fun setErrorMessage(message: String?) {
        _errorMessage.value = message
    }

    fun clearError() {
        _errorMessage.value = null
    }
}