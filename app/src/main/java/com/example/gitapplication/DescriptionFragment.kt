package com.example.gitapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gitapplication.databinding.FragmentDescriptionBinding

class DescriptionFragment : Fragment(R.layout.fragment_description) {

    private var binding: FragmentDescriptionBinding? = null
    private val args: DescriptionFragmentArgs by navArgs()
    private val viewModel: DescriptionViewModel by viewModels()
    private lateinit var accessToken: String // Assuming you get this from your Auth ViewModel or elsewhere

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDescriptionBinding.bind(view)

        // Assuming you have set accessToken somewhere in your app
        accessToken = "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"

        // Initialize the ViewModel with the access token
        viewModel.init(accessToken)

        // Set UI elements from arguments
        setupUI()

        // Fetch the README content
        viewModel.takeReadMe(args.owner, args.repositoryName)

        // Observe the README content LiveData
        viewModel.readmeContent.observe(viewLifecycleOwner, Observer { content ->
            binding?.readmeTextView?.text = content ?: "ReadMe нет:((("
        })

        // Set up the Toolbar
        setupToolbar()
    }

    private fun setupUI() {
        binding?.link?.text = args.link
        binding?.lessence?.text = "Описание репозитория"
        binding?.starCount?.text = args.amountOfStars
        binding?.forkCount?.text = args.amountOfForks
        binding?.watcherCount?.text = args.amountOfWatchers

        // Link click listener
        binding?.link?.setOnClickListener {
            openLink(args.link)
        }
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = binding?.appbar?.toolbar ?: return
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = args.repositoryName
        binding?.appbar?.actionButton?.setOnClickListener {
            findNavController().popBackStack(R.id.AuthFragment, false)
        }
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
