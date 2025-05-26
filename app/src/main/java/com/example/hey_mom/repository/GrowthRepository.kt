package com.example.hey_mom.repository


import com.example.hey_mom.api.ApiService
import com.example.hey_mom.api.models.ApiResponse
import com.example.hey_mom.api.models.GrowthEntry
import com.example.hey_mom.utils.Result

class GrowthRepository(private val api: ApiService) {
    suspend fun addGrowth(
        babyId: Int, date: String, weight: Float, height: Float, head: Float, notes: String
    ): Result<ApiResponse> {
        return try {
            val response = api.addGrowth(babyId, date, weight, height, head, notes)
            if (response.isSuccessful) Result.Success(response.body()!!)
            else Result.Error("Add Growth Failed: ${response.message()}")
        } catch (e: Exception) {
            Result.Error("Add Growth Exception: ${e.localizedMessage}")
        }
    }

    suspend fun getGrowth(babyId: Int): Result<List<GrowthEntry>> {
        return try {
            val response = api.getGrowth(babyId)
            if (response.isSuccessful) Result.Success(response.body() ?: emptyList())
            else Result.Error("Get Growth Failed: ${response.message()}")
        } catch (e: Exception) {
            Result.Error("Get Growth Exception: ${e.localizedMessage}")
        }
    }
}

