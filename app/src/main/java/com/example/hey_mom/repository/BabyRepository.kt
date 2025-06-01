package com.example.hey_mom.repository

import com.example.hey_mom.api.ApiService
import com.example.hey_mom.api.models.ApiResponse
import com.example.hey_mom.api.models.Baby
import com.example.hey_mom.utils.Result


class BabyRepository(private val api: ApiService) {
    suspend fun addBaby(baby: Baby, userId: Int, relation: String): Result<ApiResponse> {
        return try {
            val response = api.addBaby(
                baby.name, baby.dob, baby.gender, baby.weight_kg, baby.height_cm,
                baby.blood_group, baby.known_allergies, baby.medical_conditions,
                userId, relation
            )
            if (response.isSuccessful) Result.Success(response.body()!!)
            else Result.Error("Add Baby Failed: ${response.message()}")
        } catch (e: Exception) {
            Result.Error("Add Baby Exception: ${e.localizedMessage}")
        }
    }

    suspend fun getBabies(userId: Int): Result<List<Baby>> {
        return try {
            val response = api.getBabies(userId)
            if (response.isSuccessful) Result.Success(response.body() ?: emptyList())
            else Result.Error("Get Babies Failed: ${response.message()}")
        } catch (e: Exception) {
            Result.Error("Get Babies Exception: ${e.localizedMessage}")
        }
    }

    suspend fun updateBaby(baby: Baby): Result<ApiResponse> {
        return try {
            val response = api.updateBaby(
                baby.baby_id, baby.name, baby.dob, baby.gender,
                baby.weight_kg, baby.height_cm, baby.blood_group,
                baby.known_allergies, baby.medical_conditions
            )
            if (response.isSuccessful) Result.Success(response.body()!!)
            else Result.Error("Update Failed: ${response.message()}")
        } catch (e: Exception) {
            Result.Error("Update Exception: ${e.localizedMessage}")
        }
    }
    suspend fun assignAllVaccines(babyId: Int): Result<ApiResponse> {
        return try {
            val response = api.assignAllVaccinesToBaby(babyId)
            if (response.isSuccessful) Result.Success(response.body()!!)
            else Result.Error("Assign vaccines failed: ${response.message()}")
        } catch (e: Exception) {
            Result.Error("Assign vaccines exception: ${e.localizedMessage}")
        }
    }
}
