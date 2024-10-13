package com.example.gitapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gitapplication.adapter.RepoAdapter
import com.example.gitapplication.network.GitHubClient
import kotlinx.coroutines.launch

class RepositoriesListFragment : Fragment(R.layout.recyclerview_fragment) {

    private lateinit var gitHubClient: GitHubClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repoRecycler: RecyclerView = view.findViewById(R.id.repositoryRecyclerView)
        val repoAdapter = RepoAdapter()

        /**
         * LayoutManager - ответственен за расположение элементов внутри RecyclerView
         */
        repoRecycler.layoutManager = LinearLayoutManager(context)
        repoRecycler.adapter = repoAdapter

        gitHubClient = GitHubClient("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")

        lifecycleScope.launch {
            val repos = gitHubClient.getFirstTenRepositories("all")
            repoAdapter.data = repos
        }
    }
}