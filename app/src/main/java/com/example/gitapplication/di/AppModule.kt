package com.example.gitapplication.di

import android.content.Context
import android.content.SharedPreferences
import com.example.gitapplication.network.GitHubClient
import com.example.gitapplication.network.GitHubClientFactory
import com.example.gitapplication.network.GitHubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val PREFS_NAME = "pref_name"

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideGitHubClient(factory: GitHubClientFactory): GitHubClient {
        return factory.createGitHubClient()
    }

    @Provides
    @Singleton
    fun provideGitHubRepository(gitHubClient: GitHubClient): GitHubRepository {
        return GitHubRepository(gitHubClient)
    }
}
