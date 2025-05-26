package com.example.hey_mom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hey_mom.repository.FeedingRepository

class FeedingViewModelFactory(
    private val repository: FeedingRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeedingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FeedingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
