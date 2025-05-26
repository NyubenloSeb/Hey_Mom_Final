package com.example.hey_mom.repository

import com.example.hey_mom.api.ApiService
import com.example.hey_mom.api.models.ApiResponse
import com.example.hey_mom.api.models.BabyVaccineStatus
import com.example.hey_mom.api.models.Vaccine
import com.example.hey_mom.utils.Result

class VaccineRepository(private val api: ApiService) {
    suspend fun getAllVaccines(): Result<List<Vaccine>> {
        return try {
            val response = api.getAllVaccines()
            if (response.isSuccessful) Result.Success(response.body() ?: emptyList())
            else Result.Error("Get Vaccines Failed: ${response.message()}")
        } catch (e: Exception) {
            Result.Error("Get Vaccines Exception: ${e.localizedMessage}")
        }
    }

    suspend fun assignVaccine(babyId: Int, vaccineId: Int): Result<ApiResponse> {
        return try {
            val response = api.assignVaccine(babyId, vaccineId)
            if (response.isSuccessful) Result.Success(response.body()!!)
            else Result.Error("Assign Vaccine Failed: ${response.message()}")
        } catch (e: Exception) {
            Result.Error("Assign Vaccine Exception: ${e.localizedMessage}")
        }
    }

    suspend fun getBabyVaccines(babyId: Int): Result<List<BabyVaccineStatus>> {
        return try {
            val response = api.getBabyVaccines(babyId)
            if (response.isSuccessful) Result.Success(response.body() ?: emptyList())
            else Result.Error("Get Baby Vaccines Failed: ${response.message()}")
        } catch (e: Exception) {
            Result.Error("Get Baby Vaccines Exception: ${e.localizedMessage}")
        }
    }

    suspend fun updateVaccineStatus(
        babyId: Int, vaccineId: Int, status: String, administeredOn: String, notes: String?
    ): Result<ApiResponse> {
        return try {
            val response = api.updateVaccineStatus(babyId, vaccineId, status, administeredOn, notes)
            if (response.isSuccessful) Result.Success(response.body()!!)
            else Result.Error("Update Vaccine Status Failed: ${response.message()}")
        } catch (e: Exception) {
            Result.Error("Update Vaccine Exception: ${e.localizedMessage}")
        }
    }
}

