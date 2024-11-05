package com.example.gitapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.gitapplication.databinding.FragmentAuthBinding
import com.example.gitapplication.network.GitHubClientFactory
import com.example.gitapplication.utils.isInternetAvailable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val viewModel: AuthViewModel by viewModels()
    private var binding: FragmentAuthBinding? = null
    private lateinit var prefsEditor: SharedPreferences.Editor
    private lateinit var sharedPref: SharedPreferences

    @Inject
    lateinit var gitHubClientFactory: GitHubClientFactory  // Внедрение фабрики

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefsEditor = sharedPref.edit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAuthBinding.bind(view)
        val textInputEditTextAuth = binding!!.textInputEditTextAuth

        // Получаем токен из SharedPreferences
        val restoredToken = sharedPref.getString(TOKEN_PREF_NAME, "")
        textInputEditTextAuth.setText(restoredToken)

        // Сохраняем токен при каждом изменении
        textInputEditTextAuth.doOnTextChanged { text, _, _, _ ->
            prefsEditor.putString(TOKEN_PREF_NAME, text.toString()).apply()
            // GitHubClient автоматически обновится через фабрику при обращении к нему
        }

        // Наблюдаем за состоянием аутентификации
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authFragmentUiState.collect { state ->
                    when (state) {
                        is AuthViewModel.AuthState.Idle -> { /* Начальное состояние */
                        }

                        is AuthViewModel.AuthState.Loading -> {
                            binding!!.buttonAuth.showLoading()
                        }

                        is AuthViewModel.AuthState.Success -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.hi_dear, state.login),
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(AuthFragmentDirections.actionAuthToRepos())
                        }

                        is AuthViewModel.AuthState.Error -> {
                            binding!!.textInputEditTextAuth.error = state.message
                        }
                    }
                }
            }
        }

        // Обработка нажатия кнопки для аутентификации
        binding!!.buttonAuth.setOnClickListener {
            val accessToken = textInputEditTextAuth.text.toString()
            prefsEditor.putString(TOKEN_PREF_NAME, accessToken).apply()
            if (isInternetAvailable(requireContext())) {
                viewModel.checkToken(accessToken)  // Если интернет есть, выполняем checkToken
            } else {
                findNavController().navigate(AuthFragmentDirections.actionAuthToError())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        viewModel.resetState()
    }

    companion object {
        const val PREFS_NAME: String = "pref_name"
        const val TOKEN_PREF_NAME: String = "token_pref_name"
    }
}
