package com.example.gitapplication.network

import com.example.gitapplication.pojomodel.Repository
import com.example.gitapplication.pojomodel.UserResponse

interface RepositoryOfRepos {
    suspend fun getRepositories(visibility: String): List<Repository>
    suspend fun getReadMe(owner: String, repoName: String): String?
    suspend fun checkToken(): UserResponse?
}