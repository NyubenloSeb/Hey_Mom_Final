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
        when (val addResult = repo.addBaby(baby, userId, relation)) {
            is Result.Success -> {
                status.postValue(addResult.data.status)

                if (addResult.data.status.equals("success", ignoreCase = true)) {
                    // Fetch babies to get the new baby's ID
                    when (val babiesResult = repo.getBabies(userId)) {
                        is Result.Success -> {
                            val newBaby = babiesResult.data.maxByOrNull { it.baby_id }
                            newBaby?.let {
                                // Assign all vaccines to this baby
                                when (val assignResult = repo.assignAllVaccines(it.baby_id)) {
                                    is Result.Success -> status.postValue("Vaccines assigned successfully")
                                    is Result.Error -> status.postValue(assignResult.message)
                                }
                            }
                        }
                        is Result.Error -> status.postValue(babiesResult.message)
                    }
                }
            }
            is Result.Error -> status.postValue(addResult.message)
        }
    }


    fun updateBaby(baby: Baby) = viewModelScope.launch {
        when (val result = repo.updateBaby(baby)) {
            is Result.Success -> status.postValue(result.data.status)
            is Result.Error -> status.postValue(result.message)
        }
    }
}
