package com.example.gitapplication

import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections

class AuthViewModel : ViewModel() {

    private var accessToken: String? = null

    fun setAccessToken(token: String) {
        accessToken = token
    }

    fun getAccessToken(): String? {
        return accessToken
    }

    fun getNavigationAction(): NavDirections {
        return AuthFragmentDirections.actionAuthToRepos()
    }
}

