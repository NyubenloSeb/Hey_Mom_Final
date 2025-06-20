package com.example.hey_mom.repository

import com.example.hey_mom.api.ApiService
import com.example.hey_mom.api.models.LocationService
import com.example.hey_mom.utils.Result

class LocationRepository(private val api: ApiService) {
    suspend fun getAllLocations(): Result<List<LocationService>> {
        return try {
            val response = api.getAllLocations()
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Failed to load locations")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unexpected error")
        }
    }
}
