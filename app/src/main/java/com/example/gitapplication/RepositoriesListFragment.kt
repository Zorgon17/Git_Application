package com.example.gitapplication

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gitapplication.adapter.OnItemClickListener
import com.example.gitapplication.adapter.RepoAdapter
import com.example.gitapplication.databinding.RecyclerviewFragmentBinding
import com.example.gitapplication.network.GitHubClient
import com.example.gitapplication.pojomodel.Repository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RepositoriesListFragment : Fragment(R.layout.recyclerview_fragment), OnItemClickListener {

    @Inject
    lateinit var gitHubClient: GitHubClient
    private var binding: RecyclerviewFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация binding
        binding = RecyclerviewFragmentBinding.bind(view)

        // Настройка Toolbar
        val toolbar: Toolbar = binding?.appbar?.toolbar ?: return
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        // Установка заголовка
        (activity as AppCompatActivity).supportActionBar?.title = "Repositories"

        //непосредственно биндим кнопку и appbar при нажатии сбрасываем весь стек навигации к AuthFragment
        binding?.appbar?.actionButton?.setOnClickListener {
            findNavController().popBackStack(R.id.AuthFragment, false)
        }

        //биндим RecyclerView
        val repoRecycler: RecyclerView = binding!!.repositoryRecyclerView
        val repoAdapter = RepoAdapter(this)

        // Устанавливаем LayoutManager и адаптер
        repoRecycler.layoutManager = LinearLayoutManager(context)
        repoRecycler.adapter = repoAdapter

        lifecycleScope.launch {
            val repos = gitHubClient.getFirstTenRepositories("all")
            repoAdapter.data = repos
        }
    }

    override fun onItemClick(repository: Repository) {
        Toast.makeText(context, "Вы открыли репозиторий: ${repository.name}", Toast.LENGTH_SHORT)
            .show()

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
