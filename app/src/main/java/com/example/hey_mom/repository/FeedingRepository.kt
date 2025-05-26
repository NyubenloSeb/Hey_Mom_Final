package com.example.hey_mom.repository

import com.example.hey_mom.api.ApiService
import com.example.hey_mom.api.models.ApiResponse
import com.example.hey_mom.api.models.FeedingEntry
import com.example.hey_mom.utils.Result

class FeedingRepository(private val api: ApiService) {
    suspend fun addFeedingEntry(babyId: Int, time: String, type: String, qty: Int, notes: String?): Result<ApiResponse> {
        return try {
            val response = api.addFeedingEntry(babyId, time, type, qty, notes)
            if (response.isSuccessful) Result.Success(response.body()!!)
            else Result.Error("Add Feeding Failed: ${response.message()}")
        } catch (e: Exception) {
            Result.Error("Add Feeding Exception: ${e.localizedMessage}")
        }
    }

    suspend fun getFeedingEntries(babyId: Int): Result<List<FeedingEntry>> {
        return try {
            val response = api.getFeedingEntries(babyId)
            if (response.isSuccessful) Result.Success(response.body() ?: emptyList())
            else Result.Error("Get Feeding Failed: ${response.message()}")
        } catch (e: Exception) {
            Result.Error("Get Feeding Exception: ${e.localizedMessage}")
        }
    }

    suspend fun updateFeedingEntry(feedingId: Int, time: String, type: String, qty: Int, notes: String?): Result<ApiResponse> {
        return try {
            val response = api.updateFeedingEntry(feedingId, time, type, qty, notes)
            if (response.isSuccessful) Result.Success(response.body()!!)
            else Result.Error("Update Feeding Failed: ${response.message()}")
        } catch (e: Exception) {
            Result.Error("Update Feeding Exception: ${e.localizedMessage}")
        }
    }
}

