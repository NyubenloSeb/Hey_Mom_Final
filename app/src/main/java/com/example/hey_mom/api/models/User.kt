package com.example.hey_mom.api.models

data class User(
    val user_id: Int,
    val name: String,
    val email: String,
    val user_type: String,
    val contact_info: String?,
    val last_login: String?
)

