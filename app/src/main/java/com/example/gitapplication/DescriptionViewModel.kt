package com.example.gitapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitapplication.network.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DescriptionViewModel @Inject constructor(private val repository: GitHubRepository) : ViewModel() {

    private val _descriptionFragmentUiState = MutableStateFlow<DescriptionState> (DescriptionState.Loading)
    val descriptionFragmentUiState: StateFlow<DescriptionState> = _descriptionFragmentUiState


    fun loadReadme(owner: String, repositoryName: String) {
        _descriptionFragmentUiState.value = DescriptionState.Loading
        viewModelScope.launch {
            try {
                val content = repository.getReadMe(owner, repositoryName)
                _descriptionFragmentUiState.value = DescriptionState.Success (content ?: "This application does not have a ReadMe")
            } catch (e: Exception) {
                _descriptionFragmentUiState.value = DescriptionState.Error ("Error loading the ReadMe: ${e.message}")
            }
        }
    }


    sealed class DescriptionState {
        data object OutOfInternet: DescriptionState()
        data object Loading : DescriptionState() // Когда идет загрузка
        data class Success(val content: String) : DescriptionState() // Успех
        data class Error(val message: String) : DescriptionState() // Ошибка с сообщением
    }

}