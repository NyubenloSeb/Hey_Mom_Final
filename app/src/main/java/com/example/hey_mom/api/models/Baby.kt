package com.example.hey_mom.api.models

data class Baby(
    val baby_id: Int,
    val name: String,
    val dob: String,
    val gender: String,
    val weight_kg: Float,
    val height_cm: Float,
    val blood_group: String,
    val known_allergies: String,
    val medical_conditions: String
)
