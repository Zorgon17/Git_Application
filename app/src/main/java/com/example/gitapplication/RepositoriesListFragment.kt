package com.example.gitapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gitapplication.adapter.OnItemClickListener
import com.example.gitapplication.adapter.RepoAdapter
import com.example.gitapplication.network.GitHubClient
import com.example.gitapplication.pojomodel.Repository
import kotlinx.coroutines.launch


class RepositoriesListFragment : Fragment(R.layout.recyclerview_fragment), OnItemClickListener {

    private lateinit var gitHubClient: GitHubClient

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.recyclerview_fragment, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val repoRecycler: RecyclerView = view.findViewById(R.id.repositoryRecyclerView)
        val repoAdapter = RepoAdapter(this)


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

    override fun onItemClick(repository: Repository) {
        Toast.makeText(context, "Вы открыли репозиторий: ${repository.name}", Toast.LENGTH_SHORT).show()
        findNavController().navigate(RepositoriesListFragmentDirections.actionReposToDescr())

    }
}