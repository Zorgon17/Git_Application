package com.example.gitapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gitapplication.databinding.FragmentAuthBinding

class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val viewModel: AuthViewModel by viewModels()
    private var binding: FragmentAuthBinding? = null
    private lateinit var prefsEditor: SharedPreferences.Editor
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Инициализируем SharedPreferences здесь
        sharedPref = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefsEditor = sharedPref.edit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Используем View Binding для связывания разметки с кодом
        binding = FragmentAuthBinding.bind(view)

        val textInputEditTextAuth = binding!!.textInputEditTextAuth

        // Восстанавливаем токен из SharedPreferences, если он есть
        val restoredToken = sharedPref.getString(TOKEN_PREF_NAME, "")
        textInputEditTextAuth.setText(restoredToken)

        // Сохраняем текст в SharedPreferences при изменении текста
        textInputEditTextAuth.doOnTextChanged { text, _, _, _ ->
            prefsEditor.putString(TOKEN_PREF_NAME, text.toString())
        }

        // Устанавливаем обработчик кликов для кнопки
        binding!!.buttonAuth.setOnClickListener {
            val accessToken = textInputEditTextAuth.text.toString()
            viewModel.setAccessToken(accessToken)
            findNavController().navigate(AuthFragmentDirections.actionAuthToRepos(token = accessToken))
        }
    }

    override fun onPause() {
        super.onPause()
        // Применяем изменения в SharedPreferences
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
