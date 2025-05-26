package com.example.hey_mom.repository

import com.example.hey_mom.api.ApiService
import com.example.hey_mom.api.models.ApiResponse
import com.example.hey_mom.api.models.SleepEntry
import com.example.hey_mom.utils.Result

class SleepRepository(private val api: ApiService) {
    suspend fun addSleepEntry(babyId: Int, start: String, end: String, notes: String?): Result<ApiResponse> {
        return try {
            val response = api.addSleepEntry(babyId, start, end, notes)
            if (response.isSuccessful) Result.Success(response.body()!!)
            else Result.Error("Add Sleep Failed: ${response.message()}")
        } catch (e: Exception) {
            Result.Error("Add Sleep Exception: ${e.localizedMessage}")
        }
    }

    suspend fun getSleepEntries(babyId: Int): Result<List<SleepEntry>> {
        return try {
            val response = api.getSleepEntries(babyId)
            if (response.isSuccessful) Result.Success(response.body() ?: emptyList())
            else Result.Error("Get Sleep Failed: ${response.message()}")
        } catch (e: Exception) {
            Result.Error("Get Sleep Exception: ${e.localizedMessage}")
        }
    }

    suspend fun updateSleepEntry(sleepId: Int, start: String, end: String, notes: String?): Result<ApiResponse> {
        return try {
            val response = api.updateSleepEntry(sleepId, start, end, notes)
            if (response.isSuccessful) Result.Success(response.body()!!)
            else Result.Error("Update Sleep Failed: ${response.message()}")
        } catch (e: Exception) {
            Result.Error("Update Sleep Exception: ${e.localizedMessage}")
        }
    }
}
