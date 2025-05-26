package com.example.hey_mom.api.models

data class GrowthEntry(
    val entry_id: Int,
    val recorded_on: String,
    val weight_kg: Float,
    val height_cm: Float,
    val head_circumference_cm: Float,
    val notes: String
)
