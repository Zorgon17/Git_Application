package com.example.gitapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitapplication.network.GitHubRepository
import com.example.gitapplication.pojomodel.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoriesListViewModel @Inject constructor(private val repository: GitHubRepository) :
    ViewModel() {
    private val _repoListUiStateInside =
        MutableStateFlow<RepositoriesListState>(RepositoriesListState.Loading)
    val repoListUiState: StateFlow<RepositoriesListState> = _repoListUiStateInside

    init {
        loadRepositories()
    }

    private fun loadRepositories() {
        _repoListUiStateInside.value = RepositoriesListState.Loading // Устанавливаем состояние загрузки
        viewModelScope.launch {
            try {
                val repos = repository.getRepositories("all") // Получаем репозитории
                when {
                    repos.isNullOrEmpty() -> {
                        _repoListUiStateInside.value = RepositoriesListState.Empty // Состояние пустого списка
                    }
                    else -> {
                        _repoListUiStateInside.value = RepositoriesListState.Success(repositories = repos)
                    }
                }
            } catch (e: Exception) {
                _repoListUiStateInside.value = RepositoriesListState.Error(message = "Error loading repositories: ${e.localizedMessage}")
            }
        }
    }



    sealed class RepositoriesListState {
        data object Empty: RepositoriesListState()
        data object Loading : RepositoriesListState()           // Когда идет загрузка
        data class Success(val repositories: List<Repository>) : RepositoriesListState() // Успех
        data class Error(val message: String) : RepositoriesListState() // Ошибка с сообщением
    }

}