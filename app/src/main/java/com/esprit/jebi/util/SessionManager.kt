package com.esprit.jebi.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val securityManager: SecurityManager
) {
    private val _isSessionValid = MutableStateFlow(false)
    val isSessionValid: StateFlow<Boolean> = _isSessionValid.asStateFlow()

    private val SESSION_TIMEOUT_MS = 5 * 60 * 1000 // 5 minutes

    fun startSession() {
        securityManager.saveLastActivity(System.currentTimeMillis())
        _isSessionValid.value = true
    }

    fun checkSession() {
        val lastActivity = securityManager.getLastActivity()
        val currentTime = System.currentTimeMillis()
        
        if (currentTime - lastActivity > SESSION_TIMEOUT_MS) {
            logout()
        } else {
            // Refresh session
            securityManager.saveLastActivity(currentTime)
            _isSessionValid.value = true
        }
    }

    fun logout() {
        securityManager.clearToken()
        _isSessionValid.value = false
    }
    
    fun updateActivity() {
        if (_isSessionValid.value) {
            securityManager.saveLastActivity(System.currentTimeMillis())
        }
    }
}
