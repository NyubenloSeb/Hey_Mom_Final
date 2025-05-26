package com.example.hey_mom.viewmodel

import androidx.lifecycle.*
import com.example.hey_mom.api.models.GrowthEntry
import com.example.hey_mom.repository.GrowthRepository
import kotlinx.coroutines.launch
import com.example.hey_mom.utils.Result


class GrowthViewModel(private val repo: GrowthRepository) : ViewModel() {

    val growthList = MutableLiveData<List<GrowthEntry>>()
    val status = MutableLiveData<String>()

    fun getGrowthEntries(babyId: Int) = viewModelScope.launch {
        when (val result = repo.getGrowth(babyId)) {
            is Result.Success -> growthList.postValue(result.data)
            is Result.Error -> status.postValue(result.message)
        }
    }

    fun addGrowthEntry(babyId: Int, date: String, weight: Float, height: Float, head: Float, notes: String) =
        viewModelScope.launch {
            when (val result = repo.addGrowth(babyId, date, weight, height, head, notes)) {
                is Result.Success -> status.postValue(result.data.status)
                is Result.Error -> status.postValue(result.message)
            }
        }
}

