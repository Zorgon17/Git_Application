package com.example.gitapplication

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.gitapplication.databinding.FragmentAuthBinding
import com.example.gitapplication.databinding.FragmentDescriptionBinding

class DescriptionFragment : Fragment(R.layout.fragment_description) {

    private val viewModel: DescriptionViewModel by viewModels()
    private var binding: FragmentDescriptionBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Используем View Binding для связывания верстки с кодом
        binding = FragmentDescriptionBinding.bind(view)



    }

    /**
     * Чистим binding, чтобы не было утечки двнных при пересоздании экрана
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}