package com.example.gitapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gitapplication.databinding.CombinedDescriptionFragmentBinding
import com.example.gitapplication.databinding.FragmentTopWithDescriptionBinding
import com.example.gitapplication.databinding.ReadmeFragmentBinding
import com.example.gitapplication.screens.errorscreen.ErrorFragment
import com.example.gitapplication.utils.isInternetAvailable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DescriptionFragment : Fragment(R.layout.combined_description_fragment) {

    private val args: DescriptionFragmentArgs by navArgs()
    private val viewModel: DescriptionViewModel by viewModels()

    private var binding: FragmentTopWithDescriptionBinding? = null
    private var combinedBinding: CombinedDescriptionFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация combinedBinding
        combinedBinding = CombinedDescriptionFragmentBinding.bind(view)
        binding = FragmentTopWithDescriptionBinding.bind(combinedBinding!!.root)

        // Устанавливаем текстовые значения из аргументов
        binding?.link?.text = args.link
        binding?.lessence?.text = getString(R.string.description_of_the_repository)
        binding?.starCount?.text = args.amountOfStars
        binding?.forkCount?.text = args.amountOfForks
        binding?.watcherCount?.text = args.amountOfWatchers

        // Настройка Toolbar
        val toolbar: Toolbar = combinedBinding?.appbar?.toolbar ?: return
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = args.repositoryName
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        combinedBinding?.appbar?.actionButton?.setOnClickListener {
            findNavController().popBackStack(R.id.AuthFragment, false)
        }

        binding?.link?.setOnClickListener {
            openLink(args.link)
        }

        // Проверяем интернет-соединение
        if (isInternetAvailable(requireContext())) {
            viewModel.loadReadme(args.owner, args.repositoryName)
        } else {
            showConnectionErrorFragment()
        }

        // Наблюдаем за состоянием
        lifecycleScope.launch {
            viewModel.descriptionFragmentUiState.collect { state ->
                when (state) {
                    is DescriptionViewModel.DescriptionState.Loading -> {
                        //индикатор загрузки
                    }
                    is DescriptionViewModel.DescriptionState.Success -> {
                        showReadmeFragment(state.content)
                    }
                    is DescriptionViewModel.DescriptionState.Error -> {
                        // ошибка
                    }
                    is DescriptionViewModel.DescriptionState.OutOfInternet -> {
                        showConnectionErrorFragment()
                    }
                }
            }
        }
    }

    private fun showReadmeFragment(content: String) {
        // Убедитесь, что вы не создаете новый binding каждый раз
        val readmeBinding = ReadmeFragmentBinding.inflate(layoutInflater)
        readmeBinding.readmeTextView.text = content
        // Замените содержимое фрагмента на ReadMe
        combinedBinding?.fragmentContainer?.removeAllViews()
        combinedBinding?.fragmentContainer?.addView(readmeBinding.root)
    }

    private fun showConnectionErrorFragment() {
        val connectionErrorFragment = ErrorFragment()
        // Заменяем текущий фрагмент на фрагмент ошибки подключения
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(combinedBinding?.fragmentContainer?.id ?: return, connectionErrorFragment)
            .commit()
    }

    private fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        combinedBinding = null // Освобождаем combinedBinding
    }
}
