package com.example.gitapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

    private val _accessToken = MutableLiveData<String?>()

    val accessToken: LiveData<String?> = _accessToken

    fun setAccessToken(accessToken: String) {
        _accessToken.value = accessToken
        }
}
