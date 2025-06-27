package com.example.hey_mom.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hey_mom.repository.UserRepository
import kotlinx.coroutines.launch
import com.example.hey_mom.utils.Result

class UserViewModel(private val repo: UserRepository) : ViewModel() {

    val status = MutableLiveData<String>()

    fun updateProfile(userId: Int, name: String, email: String, contact: String) {
        viewModelScope.launch {
            when (val result = repo.updateProfile(userId, name, email, contact)) {
                is Result.Success -> status.postValue(result.data)
                is Result.Error -> status.postValue(result.message)
            }
        }
    }
}

class UserViewModelFactory(private val repo: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

