package com.example.gitapplication.di

import android.content.Context
import android.content.SharedPreferences
import com.example.gitapplication.network.GitHubClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object AppModule {

    private const val PREFS_NAME = "pref_name"
    private const val TOKEN_PREF_NAME = "token_pref_name"

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    fun provideGitHubClient(sharedPreferences: SharedPreferences): GitHubClient {
        val token = sharedPreferences.getString(TOKEN_PREF_NAME, "") ?: ""
        return GitHubClient(token)
    }
}
