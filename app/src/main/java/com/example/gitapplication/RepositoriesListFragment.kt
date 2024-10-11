package com.example.gitapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gitapplication.adapter.RepoAdapter
import com.example.gitapplication.pojomodel.EventGenerator

class RepositoriesListFragment : Fragment(R.layout.recyclerview_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repoRecycler: RecyclerView = view.findViewById(R.id.repositoryRecyclerView)
        val repoAdapter = RepoAdapter()

        repoRecycler.layoutManager = LinearLayoutManager(context)
        repoRecycler.adapter = repoAdapter

        repoAdapter.data = EventGenerator.generateEvents(100)
    }
}