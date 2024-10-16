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
import androidx.navigation.fragment.navArgs
import com.example.gitapplication.databinding.FragmentDescriptionBinding

class DescriptionFragment : Fragment(R.layout.fragment_description) {

    private val viewModel: DescriptionViewModel by viewModels()
    private var binding: FragmentDescriptionBinding? = null
    private val args: DescriptionFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_description, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.appbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        toolbar.setNavigationIcon(R.drawable.ic_back_stack)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Используем View Binding для связывания верстки с кодом
        binding = FragmentDescriptionBinding.bind(view)

        // Устанавливаем заголовок из имени репозитория
        (activity as AppCompatActivity).supportActionBar?.title = args.repositoryName

        // Устанавливаем текстовые значения из аргументов
        binding?.link?.text = args.link
        binding?.lessence?.text = "Описание репозитория"
        binding?.starCount?.text = args.amountOfStars
        binding?.forkCount?.text = args.amountOfForks
        binding?.watcherCount?.text = args.amountOfWatchers

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
