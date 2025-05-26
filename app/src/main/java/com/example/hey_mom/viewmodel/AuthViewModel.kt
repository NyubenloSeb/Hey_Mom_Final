package com.example.hey_mom.viewmodel


import androidx.lifecycle.*
import com.example.hey_mom.api.models.User

import com.example.hey_mom.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repo = AuthRepository()

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _registrationStatus = MutableLiveData<Boolean?>()
    val registrationStatus: LiveData<Boolean?> = _registrationStatus

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val res = repo.login(email, password)
            _user.value = if (res.isSuccessful) res.body()?.user else null
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        userType: String,
        contact: String?
    ) {
        viewModelScope.launch {
            try {
                val success = repo.register(name, email, password, userType, contact)
                _registrationStatus.value = success
            } catch (e: Exception) {
                e.printStackTrace()
                _registrationStatus.value = false
            }
        }
    }
}

