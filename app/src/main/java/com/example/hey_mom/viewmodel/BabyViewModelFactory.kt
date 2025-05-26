package com.example.hey_mom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hey_mom.repository.BabyRepository

class BabyViewModelFactory(private val repository: BabyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BabyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BabyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
