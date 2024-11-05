package com.example.gitapplication

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gitapplication.RepositoriesListViewModel.RepositoriesListState
import com.example.gitapplication.adapter.OnItemClickListener
import com.example.gitapplication.adapter.RepoAdapter
import com.example.gitapplication.databinding.RecyclerviewFragmentBinding
import com.example.gitapplication.pojomodel.Repository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepositoriesListFragment : Fragment(R.layout.recyclerview_fragment), OnItemClickListener {

    private val viewModel: RepositoriesListViewModel by viewModels()
    private var binding: RecyclerviewFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RecyclerviewFragmentBinding.bind(view)

        // Настройка Toolbar
        val toolbar: Toolbar = binding?.appbar?.toolbar ?: return
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "Repositories"

        // Навигация к AuthFragment
        binding?.appbar?.actionButton?.setOnClickListener {
            findNavController().popBackStack(R.id.AuthFragment, false)
        }

        // RecyclerView и адаптер
        val repoRecycler: RecyclerView = binding!!.repositoryRecyclerView
        val repoAdapter = RepoAdapter(this)
        repoRecycler.layoutManager = LinearLayoutManager(context)
        repoRecycler.adapter = repoAdapter

        // Управление состояниями загрузки
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.repoListUiState.collect { state ->
                    when (state) {
                        is RepositoriesListState.Loading -> {
                            binding?.progressBar?.visibility = View.VISIBLE
                            binding?.repositoryRecyclerView?.visibility = View.GONE
                        }
                        is RepositoriesListState.Success -> {
                            binding?.progressBar?.visibility = View.GONE
                            binding?.repositoryRecyclerView?.visibility = View.VISIBLE
                            repoAdapter.data = state.repositories
                        }
                        is RepositoriesListState.Error -> {
                            Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                        }
                        is RepositoriesListState.Empty -> {findNavController().navigate(RepositoriesListFragmentDirections.actionReposToEmpty())}
                    }
                }
            }
        }
    }

    override fun onItemClick(repository: Repository) {
        Toast.makeText(context,
            getString(R.string.you_have_opened_the_repository, repository.name), Toast.LENGTH_SHORT)
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
