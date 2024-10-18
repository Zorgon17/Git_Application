package com.example.gitapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitapplication.network.GitHubClient
import kotlinx.coroutines.launch

class DescriptionViewModel : ViewModel() {

    private val _readmeContent = MutableLiveData<String?>()
    val readmeContent: LiveData<String?> get() = _readmeContent

    private lateinit var gitHubClient: GitHubClient

    fun init(accessToken: String) {
        gitHubClient = GitHubClient(accessToken)
    }

    fun takeReadMe(owner: String, repositoryName: String) {
        viewModelScope.launch {
            val content = gitHubClient.getReadMe(owner, repositoryName)
            _readmeContent.value = content
        }
    }
}
