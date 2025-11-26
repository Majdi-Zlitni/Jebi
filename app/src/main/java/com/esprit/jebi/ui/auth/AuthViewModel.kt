package com.esprit.jebi.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.esprit.jebi.util.AuditLogger
import com.esprit.jebi.util.SecurityManager
import com.esprit.jebi.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.security.MessageDigest
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val needsVerification: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val securityManager: SecurityManager,
    private val sessionManager: SessionManager,
    private val auditLogger: AuditLogger,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    init {
        // Check if user is already logged in via Firebase
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            if (currentUser.isEmailVerified) {
                sessionManager.startSession()
                _uiState.update { it.copy(isAuthenticated = true) }
            } else {
                // User is logged in but not verified
                _uiState.update { it.copy(needsVerification = true) }
            }
        }
    }

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                firebaseAuth.signInWithEmailAndPassword(email, pass)
                    .addOnSuccessListener { authResult ->
                        val user = authResult.user
                        if (user != null && user.isEmailVerified) {
                            // Save credentials for biometric login
                            securityManager.saveCredentials(email, hashPassword(pass))

                            sessionManager.startSession()
                            auditLogger.logEvent("User logged in: $email")
                            _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
                        } else {
                            user?.sendEmailVerification()
                            _uiState.update { it.copy(isLoading = false, needsVerification = true, error = "Please verify your email.") }
                        }
                    }
                    .addOnFailureListener { e ->
                        auditLogger.logEvent("Failed login attempt: $email")
                        _uiState.update { it.copy(isLoading = false, error = e.message ?: "Login failed") }
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun register(email: String, pass: String) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(error = "Invalid email format") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                firebaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnSuccessListener { authResult ->
                        val user = authResult.user
                        user?.sendEmailVerification()
                        auditLogger.logEvent("New user registered: $email")
                        _uiState.update { it.copy(isLoading = false, needsVerification = true) }
                    }
                    .addOnFailureListener { e ->
                        _uiState.update { it.copy(isLoading = false, error = e.message ?: "Registration failed") }
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
    
    fun signInWithGoogle(credential: AuthCredential) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                firebaseAuth.signInWithCredential(credential)
                    .addOnSuccessListener { authResult ->
                        val user = authResult.user
                        sessionManager.startSession()
                        auditLogger.logEvent("User logged in with Google: ${user?.email}")
                        _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
                    }
                    .addOnFailureListener { e ->
                        _uiState.update { it.copy(isLoading = false, error = e.message ?: "Google Sign-In failed") }
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
    
    fun verifyEmail() {
        // Check if the current user is verified (reload to get latest status)
        firebaseAuth.currentUser?.reload()?.addOnSuccessListener {
            if (firebaseAuth.currentUser?.isEmailVerified == true) {
                sessionManager.startSession()
                _uiState.update { it.copy(isAuthenticated = true, needsVerification = false) }
            } else {
                _uiState.update { it.copy(error = "Email not verified yet. Please check your inbox.") }
            }
        }
    }

    fun resendVerificationEmail() {
        firebaseAuth.currentUser?.sendEmailVerification()
            ?.addOnSuccessListener {
                _uiState.update { it.copy(error = "Verification email sent again.") }
            }
            ?.addOnFailureListener { e ->
                _uiState.update { it.copy(error = e.message ?: "Failed to resend email") }
            }
    }
    
    fun logout() {
        firebaseAuth.signOut()
        auditLogger.logEvent("User logged out")
        sessionManager.logout()
        _uiState.update { it.copy(isAuthenticated = false) }
    }

    fun hasStoredCredentials(): Boolean {
        return securityManager.getStoredEmail() != null && securityManager.getStoredPasswordHash() != null
    }

    fun loginWithBiometrics() {
        viewModelScope.launch {
            if (hasStoredCredentials()) {
                val email = securityManager.getStoredEmail() ?: "Unknown"
                val mockToken = "mock_token_bio_${System.currentTimeMillis()}"
                securityManager.saveToken(mockToken)
                sessionManager.startSession()
                auditLogger.logEvent("User logged in with Biometrics: $email")
                _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
            } else {
                _uiState.update { it.copy(error = "No credentials found. Please login with password first.") }
            }
        }
    }

    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}
