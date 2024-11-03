package com.example.gitapplication.network

import android.content.SharedPreferences
import javax.inject.Inject

class GitHubClientFactory @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun createGitHubClient(): GitHubClient {
        val token = sharedPreferences.getString(TOKEN_PREF_NAME, "null")
            ?: throw IllegalArgumentException("Token not found")
        return GitHubClient(token)
    }

    fun updateGitHubClient(): GitHubClient {
        // Создайте новый клиент с текущим токеном
        val token = sharedPreferences.getString(TOKEN_PREF_NAME, "null")
            ?: throw IllegalArgumentException("Token not found")
        return GitHubClient(token)
    }

    companion object {
        const val TOKEN_PREF_NAME = "token_pref_name"
    }
}

