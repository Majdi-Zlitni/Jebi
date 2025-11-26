package com.esprit.jebi.util

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecurityManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("auth_token", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    fun clearToken() {
        sharedPreferences.edit().remove("auth_token").apply()
    }
    
    fun saveLastActivity(timestamp: Long) {
        sharedPreferences.edit().putLong("last_activity", timestamp).apply()
    }
    
    fun getLastActivity(): Long {
        return sharedPreferences.getLong("last_activity", 0)
    }

    fun saveCredentials(email: String, passwordHash: String) {
        sharedPreferences.edit()
            .putString("user_email", email)
            .putString("user_password_hash", passwordHash)
            .apply()
    }

    fun getStoredEmail(): String? {
        return sharedPreferences.getString("user_email", null)
    }

    fun getStoredPasswordHash(): String? {
        return sharedPreferences.getString("user_password_hash", null)
    }

    fun setEmailVerified(verified: Boolean) {
        sharedPreferences.edit().putBoolean("is_email_verified", verified).apply()
    }

    fun isEmailVerified(): Boolean {
        return sharedPreferences.getBoolean("is_email_verified", false)
    }

    fun clearCredentials() {
        sharedPreferences.edit()
            .remove("user_email")
            .remove("user_password_hash")
            .remove("is_email_verified")
            .remove("auth_token")
            .apply()
    }
}
