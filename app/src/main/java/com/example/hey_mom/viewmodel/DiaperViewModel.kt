package com.example.hey_mom.viewmodel

import com.example.hey_mom.repository.DiaperRepository
import androidx.lifecycle.*
import com.example.hey_mom.api.models.DiaperChange
import kotlinx.coroutines.launch
import com.example.hey_mom.utils.Result

class DiaperViewModel(private val repo: DiaperRepository) : ViewModel() {

    val diaperList = MutableLiveData<List<DiaperChange>>()
    val status = MutableLiveData<String>()

    fun getDiaperEntries(babyId: Int) = viewModelScope.launch {
        when (val result = repo.getDiaperEntries(babyId)) {
            is Result.Success -> diaperList.postValue(result.data)
            is Result.Error -> status.postValue(result.message)
        }
    }

    fun addDiaperEntry(babyId: Int, time: String, condition: String, notes: String?) = viewModelScope.launch {
        when (val result = repo.addDiaperEntry(babyId, time, condition, notes)) {
            is Result.Success -> status.postValue(result.data.status)
            is Result.Error -> status.postValue(result.message)
        }
    }

    fun updateDiaperEntry(id: Int, time: String, condition: String, notes: String?) = viewModelScope.launch {
        when (val result = repo.updateDiaperEntry(id, time, condition, notes)) {
            is Result.Success -> status.postValue(result.data.status)
            is Result.Error -> status.postValue(result.message)
        }
    }
}

