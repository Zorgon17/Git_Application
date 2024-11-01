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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val viewModel: AuthViewModel by viewModels()

    private var binding: FragmentAuthBinding? = null
    private lateinit var prefsEditor: SharedPreferences.Editor
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefsEditor = sharedPref.edit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //биндим кнопку
        binding = FragmentAuthBinding.bind(view)
        val textInputEditTextAuth = binding!!.textInputEditTextAuth

        //проверяем наличие токена в SharedPreferences
        val restoredToken = sharedPref.getString(TOKEN_PREF_NAME, "")
        textInputEditTextAuth.setText(restoredToken)

        // Сохраняем токен при каждом изменении
        // (если пользователь при авторизации свернет приложение, введенное значение не удалиться)
        textInputEditTextAuth.doOnTextChanged { text, _, _, _ ->
            prefsEditor.putString(TOKEN_PREF_NAME, text.toString())
        }

        // Наблюдаем за состоянием аутентификации
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authFragmentUiState.collect { state ->
                    when (state) {
                        is AuthViewModel.AuthState.Idle -> {
                            // Начальное состояние, загружается экран
                        }
                        is AuthViewModel.AuthState.Loading -> {
                            // Отображаем индикатор загрузки
                        }
                        is AuthViewModel.AuthState.Success -> {
                            Toast.makeText(requireContext(), "Привет, дорогой ${state.login}", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(AuthFragmentDirections.actionAuthToRepos())
                        }
                        is AuthViewModel.AuthState.Error -> {
                            binding!!.textInputEditTextAuth.error = state.message
                        }
                    }
                }
            }
        }
        // Запускаем проверку токена при нажатии на кнопку
        binding!!.buttonAuth.setOnClickListener {
            val accessToken = textInputEditTextAuth.text.toString()
            viewModel.checkToken(accessToken)
        }
    }

    override fun onPause() {
        super.onPause()
        prefsEditor.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        const val PREFS_NAME: String = "pref_name"
        const val TOKEN_PREF_NAME: String = "token_pref_name"
    }
}
