package com.example.gitapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gitapplication.databinding.FragmentDescriptionBinding
import com.example.gitapplication.network.GitHubClient
import kotlinx.coroutines.launch

class DescriptionFragment : Fragment(R.layout.fragment_description) {

    private var binding: FragmentDescriptionBinding? = null
    private val args: DescriptionFragmentArgs by navArgs()
    private lateinit var gitHubClient: GitHubClient


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

        gitHubClient =
            GitHubClient("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")

        lifecycleScope.launch {
            val readmeContent = gitHubClient.getReadMe(owner, repositoryName)
            if (readmeContent != null) {
                binding?.readmeTextView?.text = readmeContent
            } else {
                binding?.readmeTextView?.text = "ReadMe нет:((("
            }
        }

        // Настройка Toolbar
        val toolbar: Toolbar = binding?.appbar?.toolbar ?: return
        (activity as AppCompatActivity).setSupportActionBar(toolbar)


        // Установка заголовка
        (activity as AppCompatActivity).supportActionBar?.title = repositoryName

        binding?.appbar?.actionButton?.setOnClickListener {
            findNavController().popBackStack(R.id.AuthFragment, false)
        }

        // Включаем кнопку "Назад" в ActionBar
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Устанавливаем слушатель на кнопку "Назад"
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        // Устанавливаем слушатель клика для текстового поля ссылки
        binding?.link?.setOnClickListener {
            val url = args.link
            openLink(url) // Открываем ссылку
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
