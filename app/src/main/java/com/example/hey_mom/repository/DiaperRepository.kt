package com.example.hey_mom.repository

import com.example.hey_mom.api.ApiService
import com.example.hey_mom.api.models.ApiResponse
import com.example.hey_mom.api.models.DiaperChange
import com.example.hey_mom.utils.Result

class DiaperRepository(private val api: ApiService) {
    suspend fun addDiaperEntry(babyId: Int, time: String, condition: String, notes: String?): Result<ApiResponse> {
        return try {
            val response = api.addDiaperEntry(babyId, time, condition, notes)
            if (response.isSuccessful) Result.Success(response.body()!!)
            else Result.Error("Add Diaper Failed: ${response.message()}")
        } catch (e: Exception) {
            Result.Error("Add Diaper Exception: ${e.localizedMessage}")
        }
    }

    suspend fun getDiaperEntries(babyId: Int): Result<List<DiaperChange>> {
        return try {
            val response = api.getDiaperEntries(babyId)
            if (response.isSuccessful) Result.Success(response.body() ?: emptyList())
            else Result.Error("Get Diaper Failed: ${response.message()}")
        } catch (e: Exception) {
            Result.Error("Get Diaper Exception: ${e.localizedMessage}")
        }
    }

    suspend fun updateDiaperEntry(id: Int, time: String, condition: String, notes: String?): Result<ApiResponse> {
        return try {
            val response = api.updateDiaperEntry(id, time, condition, notes)
            if (response.isSuccessful) Result.Success(response.body()!!)
            else Result.Error("Update Diaper Failed: ${response.message()}")
        } catch (e: Exception) {
            Result.Error("Update Diaper Exception: ${e.localizedMessage}")
        }
    }
}
