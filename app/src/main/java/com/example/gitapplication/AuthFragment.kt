package com.example.gitapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gitapplication.databinding.FragmentAuthBinding


class AuthFragment : Fragment(R.layout.fragment_auth) {
    

    private val viewModel: AuthViewModel by viewModels()
    private var binding: FragmentAuthBinding? = null

    /**
     * Вызывается для создания компонентов внутри фрагмента
     */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Используем View Binding для связывания верстки с кодом
        binding = FragmentAuthBinding.bind(view)

        // Устанавливаем click listener для кнопки
        binding!!.buttonAuth.setOnClickListener {
            val accessToken = binding!!.textInputEditTextAuth.text.toString()
            viewModel.setAccessToken(accessToken)

            findNavController().navigate(AuthFragmentDirections.actionAuthToRepos())
        }

    }

    /**
     * Чистим binding, чтобы не было утечки двнных при пересоздании экрана
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}