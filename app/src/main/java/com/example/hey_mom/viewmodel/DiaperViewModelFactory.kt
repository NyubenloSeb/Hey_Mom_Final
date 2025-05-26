package com.example.hey_mom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hey_mom.repository.DiaperRepository

class DiaperViewModelFactory(private val repository: DiaperRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiaperViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DiaperViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
