package com.example.hey_mom.viewmodel

import com.example.hey_mom.repository.SleepRepository
import androidx.lifecycle.*
import com.example.hey_mom.api.models.SleepEntry
import kotlinx.coroutines.launch
import com.example.hey_mom.utils.Result

class SleepViewModel(private val repo: SleepRepository) : ViewModel() {

    val sleepList = MutableLiveData<List<SleepEntry>>()
    val status = MutableLiveData<String>()

    fun getSleepEntries(babyId: Int) = viewModelScope.launch {
        when (val result = repo.getSleepEntries(babyId)) {
            is Result.Success -> sleepList.postValue(result.data)
            is Result.Error -> status.postValue(result.message)
        }
    }

    fun addSleepEntry(babyId: Int, start: String, end: String, notes: String?) = viewModelScope.launch {
        when (val result = repo.addSleepEntry(babyId, start, end, notes)) {
            is Result.Success -> status.postValue(result.data.status)
            is Result.Error -> status.postValue(result.message)
        }
    }

    fun updateSleepEntry(sleepId: Int, start: String, end: String, notes: String?) = viewModelScope.launch {
        when (val result = repo.updateSleepEntry(sleepId, start, end, notes)) {
            is Result.Success -> status.postValue(result.data.status)
            is Result.Error -> status.postValue(result.message)
        }
    }
}
