package com.example.hey_mom.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("HeyMomPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_CONTACT = "user_contact"
    }

    /**
     * Save user session after successful login
     */
    fun saveUserSession(userId: String, userName: String, userEmail: String, userContact: String = "") {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, userName)
            putString(KEY_USER_EMAIL, userEmail)
            putString(KEY_USER_CONTACT, userContact) // ✅ Save contact info
            apply()
        }
    }

    /**
     * Save updated user profile details
     */
    fun saveUser(name: String, email: String, contact: String) {
        prefs.edit().apply {
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_CONTACT, contact)
            apply()
        }
    }

    /**
     * Check if user is logged in
     */
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    /**
     * Get user ID
     */
    fun getUserId(): Int {
        return prefs.getString(KEY_USER_ID, "0")?.toInt() ?: 0
    }

    /**
     * Get user name
     */
    fun getUserName(): String? {
        return prefs.getString(KEY_USER_NAME, null)
    }

    /**
     * Get user email
     */
    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }

    /**
     * Get user contact info
     */
    fun getUserContact(): String? {
        return prefs.getString(KEY_USER_CONTACT, "")
    }

    /**
     * Clear user session (logout)
     */
    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
