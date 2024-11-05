package com.example.gitapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitapplication.network.GitHubClient
import com.example.gitapplication.network.GitHubClientFactory
import com.example.gitapplication.network.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: GitHubRepository,
    private val gitHubClientFactory: GitHubClientFactory // Внедряем фабрику
) : ViewModel() {

    private val _authFragmentUiStateInside = MutableStateFlow<AuthState>(AuthState.Idle)
    val authFragmentUiState: StateFlow<AuthState> = _authFragmentUiStateInside

    fun checkToken(accessToken: String) {
        if (accessToken.isBlank()) {
            _authFragmentUiStateInside.value = AuthState.Error("The token cannot be empty")
            return
        } else {
            viewModelScope.launch {
                _authFragmentUiStateInside.value = AuthState.Loading
                // Обновляем клиент перед проверкой токена
                val newClient = gitHubClientFactory.updateGitHubClient()
                repository.setGitHubClient(newClient) // Устанавливаем новый клиент

                val userResponse = repository.checkToken()

                if (userResponse != null) {
                    _authFragmentUiStateInside.value = AuthState.Success(userResponse.login)
                } else {
                    _authFragmentUiStateInside.value = AuthState.Error("Invalid token")
                }
            }
        }
    }

    fun resetState() {
        _authFragmentUiStateInside.value = AuthState.Idle
    }
    sealed class AuthState {
        object Idle : AuthState()              // Начальное состояние
        object Loading : AuthState()           // Когда идет загрузка
        data class Success(val login: String) : AuthState() // Успех с именем пользователя
        data class Error(val message: String) : AuthState() // Ошибка с сообщением
    }

}

