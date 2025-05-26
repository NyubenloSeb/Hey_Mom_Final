package com.example.hey_mom.viewmodel

import androidx.lifecycle.*
import com.example.hey_mom.api.models.BabyVaccineStatus
import com.example.hey_mom.api.models.Vaccine
import com.example.hey_mom.repository.VaccineRepository
import com.example.hey_mom.utils.Result
import kotlinx.coroutines.launch

class VaccineViewModel(private val repo: VaccineRepository) : ViewModel() {

    val vaccineList = MutableLiveData<List<Vaccine>>()
    val babyVaccineList = MutableLiveData<List<BabyVaccineStatus>>()
    val status = MutableLiveData<String>()

    fun getAllVaccines() = viewModelScope.launch {
        when (val result = repo.getAllVaccines()) {
            is Result.Success -> vaccineList.postValue(result.data)
            is Result.Error -> status.postValue(result.message)
        }
    }

    fun getBabyVaccines(babyId: Int) = viewModelScope.launch {
        when (val result = repo.getBabyVaccines(babyId)) {
            is Result.Success -> babyVaccineList.postValue(result.data)
            is Result.Error -> status.postValue(result.message)
        }
    }

    fun assignVaccine(babyId: Int, vaccineId: Int) = viewModelScope.launch {
        when (val result = repo.assignVaccine(babyId, vaccineId)) {
            is Result.Success -> status.postValue(result.data.status)
            is Result.Error -> status.postValue(result.message)
        }
    }

    fun updateVaccineStatus(
        babyId: Int, vaccineId: Int, statusStr: String, date: String, notes: String?
    ) = viewModelScope.launch {
        when (val result = repo.updateVaccineStatus(babyId, vaccineId, statusStr, date, notes)) {
            is Result.Success -> status.postValue(result.data.status)
            is Result.Error -> status.postValue(result.message)
        }
    }
}
