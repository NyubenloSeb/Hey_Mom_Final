package com.example.hey_mom.api.models

data class BabyVaccineStatus(
    val vaccine_id: Int,
    val vaccine_name: String,
    val recommended_age_weeks: Int,
    val administered_on: String?,
    val status: String,
    val notes: String?
)

