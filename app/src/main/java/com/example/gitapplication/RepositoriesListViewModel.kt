package com.example.gitapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitapplication.network.GitHubClient
import com.example.gitapplication.pojomodel.Repository
import kotlinx.coroutines.launch

class RepositoriesViewModel : ViewModel() {

    private val _repositories = MutableLiveData<List<Repository>>()
    val repositories: LiveData<List<Repository>> get() = _repositories

    private lateinit var gitHubClient: GitHubClient

    fun init(accessToken: String) {
        gitHubClient = GitHubClient(accessToken)
        fetchRepositories()
    }

    private fun fetchRepositories() {
        viewModelScope.launch {
            val repos = gitHubClient.getFirstTenRepositories("all")
            _repositories.value = repos
        }
    }
}
