package com.example.hey_mom.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import com.example.hey_mom.api.models.Baby
import com.example.hey_mom.repository.BabyRepository
import com.example.hey_mom.utils.Result

class BabyViewModel(private val repo: BabyRepository) : ViewModel() {

    val babies = MutableLiveData<List<Baby>>()
    val status = MutableLiveData<String>()

    fun getBabies(userId: Int) = viewModelScope.launch {
        when (val result = repo.getBabies(userId)) {
            is Result.Success -> babies.postValue(result.data)
            is Result.Error -> status.postValue(result.message)
        }
    }

    fun addBaby(baby: Baby, userId: Int, relation: String) = viewModelScope.launch {
        when (val result = repo.addBaby(baby, userId, relation)) {
            is Result.Success -> status.postValue(result.data.status)
            is Result.Error -> status.postValue(result.message)
        }
    }

    fun updateBaby(baby: Baby) = viewModelScope.launch {
        when (val result = repo.updateBaby(baby)) {
            is Result.Success -> status.postValue(result.data.status)
            is Result.Error -> status.postValue(result.message)
        }
    }
}
