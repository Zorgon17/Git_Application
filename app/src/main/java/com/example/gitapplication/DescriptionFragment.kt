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
import com.example.gitapplication.databinding.FragmentDescriptionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DescriptionFragment : Fragment(R.layout.fragment_description) {

    private val args: DescriptionFragmentArgs by navArgs()
    private val viewModel: DescriptionViewModel by viewModels()

    private var binding: FragmentDescriptionBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Используем View Binding для связывания верстки с кодом
        binding = FragmentDescriptionBinding.bind(view)

        // Устанавливаем текстовые значения из аргументов
        val owner: String = args.owner
        val repositoryName: String = args.repositoryName
        binding?.link?.text = args.link
        binding?.lessence?.text = "Описание репозитория"
        binding?.starCount?.text = args.amountOfStars
        binding?.forkCount?.text = args.amountOfForks
        binding?.watcherCount?.text = args.amountOfWatchers

        // Настройка Toolbar
        val toolbar: Toolbar = binding?.appbar?.toolbar ?: return
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        // Установка заголовка
        (activity as AppCompatActivity).supportActionBar?.title = repositoryName

        // Включаем кнопку "Назад" в ActionBar
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Устанавливаем слушатель на кнопку "Назад"
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        //непосредственно биндим кнопку и appbar при нажатии сбрасываем весь стек навигации к AuthFragment
        binding?.appbar?.actionButton?.setOnClickListener {
            findNavController().popBackStack(R.id.AuthFragment, false)
        }

        // Устанавливаем слушатель клика для текстового поля ссылки
        binding?.link?.setOnClickListener {
            val url = args.link
            openLink(url) // Открываем ссылку
        }

        // Загружаем README
        viewModel.loadReadme(owner, repositoryName)

        // Наблюдаем за состоянием
        lifecycleScope.launch {
            viewModel.descriptionFragmentUiState.collect { state ->
                when (state) {
                    is DescriptionViewModel.DescriptionState.Loading -> {
                        binding?.readmeTextView?.text = "Загрузка..."
                    }
                    is DescriptionViewModel.DescriptionState.Success -> {
                        binding?.readmeTextView?.text = state.content
                    }
                    is DescriptionViewModel.DescriptionState.Error -> {
                        binding?.readmeTextView?.text = state.message
                    }
                }
            }
        }
    }

    /**
     * Функция, делающая ссылку в приложении кликабельной
     */
    private fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url) // Установка ссылки
        startActivity(intent) // Открытие ссылки в браузере
    }

    /**
     * Чистим binding, чтобы не было утечки данных при пересоздании экрана
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
