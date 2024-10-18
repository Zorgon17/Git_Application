package com.example.gitapplication

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitapplication.adapter.OnItemClickListener
import com.example.gitapplication.adapter.RepoAdapter
import com.example.gitapplication.databinding.RecyclerviewFragmentBinding
import com.example.gitapplication.pojomodel.Repository
import kotlinx.coroutines.launch

class RepositoriesListFragment : Fragment(R.layout.recyclerview_fragment), OnItemClickListener {

    private var binding: RecyclerviewFragmentBinding? = null
    private val viewModel: RepositoriesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация binding
        binding = RecyclerviewFragmentBinding.bind(view)

        // Настройка Toolbar
        setupToolbar()

        // Настройка RecyclerView
        val repoRecycler = binding!!.repositoryRecyclerView
        val repoAdapter = RepoAdapter(this)
        repoRecycler.layoutManager = LinearLayoutManager(context)
        repoRecycler.adapter = repoAdapter

        // Initialize ViewModel
        val accessToken = "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" // Replace with the actual access token
        viewModel.init(accessToken)

        // Observe repository data
        viewModel.repositories.observe(viewLifecycleOwner, Observer { repos ->
            repoAdapter.data = repos
        })
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = binding?.appbar?.toolbar ?: return
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        // Установка заголовка
        (activity as AppCompatActivity).supportActionBar?.title = "Repositories"

        // Кнопка для сброса стека навигации
        binding?.appbar?.actionButton?.setOnClickListener {
            findNavController().popBackStack(R.id.AuthFragment, false)
        }
    }

    override fun onItemClick(repository: Repository) {
        Toast.makeText(context, "Вы открыли репозиторий: ${repository.name}", Toast.LENGTH_SHORT).show()

        val action = RepositoriesListFragmentDirections.actionReposToDescr(
            repositoryName = repository.name.toString(),
            link = repository.repositoryUrl.toString(),
            amountOfStars = repository.countOfStars.toString(),
            amountOfForks = repository.countOfForks.toString(),
            amountOfWatchers = repository.countOfWatchers.toString(),
            owner = repository.owner?.login.toString()
        )

        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null // Освобождаем binding
    }
}
