package com.example.gitapplication.screens.emptyscreen

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gitapplication.R
import com.example.gitapplication.databinding.EmptyRepositoryFragmentBinding

class EmptyFragment : Fragment(R.layout.empty_repository_fragment) {

    private var binding: EmptyRepositoryFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EmptyRepositoryFragmentBinding.bind(view)

        val buttonRetry = binding!!.buttonRetry

        // Настройка Toolbar
        val toolbar: Toolbar = binding?.appbar?.toolbar ?: return
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "Repositories"

        // Навигация к AuthFragment
        binding?.appbar?.actionButton?.setOnClickListener {
            findNavController().popBackStack(R.id.AuthFragment, false)
        }

        buttonRetry.setOnClickListener {
            findNavController().navigate(EmptyFragmentDirections.actionEmptyToRepos())
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}