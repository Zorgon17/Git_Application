package com.example.gitapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.gitapplication.databinding.FragmentAuthBinding
import com.example.gitapplication.network.GitHubClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {

    private var binding: FragmentAuthBinding? = null
    private lateinit var prefsEditor: SharedPreferences.Editor
    private lateinit var sharedPref: SharedPreferences

    @Inject
    lateinit var gitHubClient: GitHubClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefsEditor = sharedPref.edit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAuthBinding.bind(view)
        val textInputEditTextAuth = binding!!.textInputEditTextAuth

        val restoredToken = sharedPref.getString(TOKEN_PREF_NAME, "")
        textInputEditTextAuth.setText(restoredToken)

        textInputEditTextAuth.doOnTextChanged { text, _, _, _ ->
            prefsEditor.putString(TOKEN_PREF_NAME, text.toString())
        }

        binding!!.buttonAuth.setOnClickListener {
            val accessToken = textInputEditTextAuth.text.toString()
            if (accessToken.isBlank()) {
                binding!!.textInputEditTextAuth.error = "Токен не может быть пустым"
            } else {
                // Здесь создаем новый экземпляр GitHubClient с актуальным токеном
                val gitHubClientWithToken = GitHubClient(accessToken)

                lifecycleScope.launch {
                    val userResponse = gitHubClientWithToken.checkToken()

                    if (userResponse != null) {
                        binding!!.textInputEditTextAuth.error = null
                        Toast.makeText(requireContext(), "Привет, дорогой ${userResponse.login}", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(AuthFragmentDirections.actionAuthToRepos())
                    } else {
                        binding!!.textInputEditTextAuth.error = "Неверный токен"
                    }
                }
            }
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
