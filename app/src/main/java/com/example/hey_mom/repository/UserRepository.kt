package com.example.hey_mom.repository

import com.example.hey_mom.api.ApiService
import com.example.hey_mom.api.models.ApiResponse
import com.example.hey_mom.utils.Result

class UserRepository(private val api: ApiService) {

        suspend fun updateProfile(userId: Int, name: String, email: String, contact: String): Result<String> {
            return try {
                val response = api.updateProfile(userId, name, email, contact)
                if (response.isSuccessful && response.body()?.status == "success") {
                    Result.Success(response.body()?.message ?: "Profile updated")
                } else {
                    Result.Error(response.body()?.message ?: "Error updating profile")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Exception occurred")
            }
        }
    }

