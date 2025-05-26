package com.example.hey_mom.api.models

data class Vaccine(
    val vaccine_id: Int,
    val vaccine_name: String,
    val recommended_age_weeks: Int,
    val description: String
)

