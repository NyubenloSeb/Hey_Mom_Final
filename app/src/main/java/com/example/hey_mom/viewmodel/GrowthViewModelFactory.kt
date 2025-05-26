package com.example.hey_mom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hey_mom.repository.GrowthRepository

class GrowthViewModelFactory(private val repository: GrowthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GrowthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GrowthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
