package com.example.hey_mom.viewmodel

import androidx.lifecycle.*
import com.example.hey_mom.api.models.LocationService
import com.example.hey_mom.repository.LocationRepository
import com.example.hey_mom.utils.Result
import kotlinx.coroutines.launch

class LocationViewModel(private val repo: LocationRepository) : ViewModel() {

    private val _clinics = MutableLiveData<List<LocationService>>()
    val clinics: LiveData<List<LocationService>> get() = _clinics

    private val _daycareCenters = MutableLiveData<List<LocationService>>()
    val daycareCenters: LiveData<List<LocationService>> get() = _daycareCenters

    val status = MutableLiveData<String>()

    fun fetchLocations() {
        viewModelScope.launch {
            when (val result = repo.getAllLocations()) {
                is Result.Success -> {
                    val all = result.data
                    _clinics.postValue(all.filter {  it.service_type.trim().equals("Clinic", ignoreCase = true) })
                    _daycareCenters.postValue(all.filter { it.service_type.trim().equals("Day Care Center",ignoreCase = true) })
                }
                is Result.Error -> status.postValue(result.message)
            }
        }
    }
}

class LocationViewModelFactory(private val repo: LocationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocationViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
