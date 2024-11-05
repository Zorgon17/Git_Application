package com.example.gitapplication.screens.errorscreen

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gitapplication.AuthFragmentDirections
import com.example.gitapplication.R
import com.example.gitapplication.databinding.ConnectionErrorFragmentBinding
import com.example.gitapplication.utils.isInternetAvailable

class ErrorFragment : Fragment(R.layout.connection_error_fragment) {
    private var binding: ConnectionErrorFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ConnectionErrorFragmentBinding.bind(view)

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
            if (isInternetAvailable(requireContext())) {
                findNavController().navigate(ErrorFragmentDirections.actionErrorToRepos())
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.turning_on_the_internet_please),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}