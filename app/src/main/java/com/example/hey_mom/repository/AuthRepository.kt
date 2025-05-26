package com.example.hey_mom.repository

import com.example.hey_mom.api.ApiClient
import com.example.hey_mom.api.ApiService

class AuthRepository {
    private val api = ApiClient.retrofit.create(ApiService::class.java)

    suspend fun login(email: String, password: String) =
        api.login(email, password)

    suspend fun register(
        name: String,
        email: String,
        password: String,
        userType: String,
        contactInfo: String?
    ): Boolean {
        val response = api.register(name, email, password, userType, contactInfo)
        return response.isSuccessful && response.body()?.status == "success"
    }
}
