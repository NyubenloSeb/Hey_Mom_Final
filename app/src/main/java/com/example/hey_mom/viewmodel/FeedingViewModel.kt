package com.example.hey_mom.viewmodel

import com.example.hey_mom.repository.FeedingRepository
import androidx.lifecycle.*
import com.example.hey_mom.api.models.FeedingEntry
import kotlinx.coroutines.launch
import com.example.hey_mom.utils.Result

class FeedingViewModel(private val repo: FeedingRepository) : ViewModel() {

    val feedingList = MutableLiveData<List<FeedingEntry>>()
    val status = MutableLiveData<String>()

    fun getFeedingEntries(babyId: Int) = viewModelScope.launch {
        when (val result = repo.getFeedingEntries(babyId)) {
            is Result.Success -> feedingList.postValue(result.data)
            is Result.Error -> status.postValue(result.message)
        }
    }

    fun addFeedingEntry(babyId: Int, time: String, type: String, quantity_ml: Int, notes: String?) =
        viewModelScope.launch {
            when (val result = repo.addFeedingEntry(babyId, time, type, quantity_ml, notes)) {
                is Result.Success -> status.postValue(result.data.status)
                is Result.Error -> status.postValue(result.message)
            }
        }

    fun updateFeedingEntry(feedingId: Int, time: String, type: String, quantity_ml: Int, notes: String?) =
        viewModelScope.launch {
            when (val result = repo.updateFeedingEntry(feedingId, time, type, quantity_ml, notes)) {
                is Result.Success -> status.postValue(result.data.status)
                is Result.Error -> status.postValue(result.message)
            }
        }
}

