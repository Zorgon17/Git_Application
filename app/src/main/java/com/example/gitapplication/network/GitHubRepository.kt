package com.example.gitapplication.network

import com.example.gitapplication.pojomodel.Repository
import com.example.gitapplication.pojomodel.UserResponse
import javax.inject.Inject

class GitHubRepository @Inject constructor(private var gitHubClient: GitHubClient) : RepositoryOfRepos {

    override suspend fun getRepositories(visibility: String): List<Repository> {
        return gitHubClient.getFirstTenRepositories(visibility)
    }

    override suspend fun getReadMe(owner: String, repoName: String): String? {
        return gitHubClient.getReadMe(owner, repoName)
    }

    // Метод для установки нового клиента
    fun setGitHubClient(client: GitHubClient) {
        this.gitHubClient = client
    }

    override suspend fun checkToken(): UserResponse? {
        return gitHubClient.checkToken()
    }
}
