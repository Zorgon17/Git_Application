package com.example.gitapplication.pojomodel

import com.example.gitapplication.pojomodel.Repository.RepositoryItem


class Repository2 : ArrayList<RepositoryItem>(){
    data class RepositoryItem2(
        val id: Int? = 1,
        val name: String? = "kdle",
        val language: String? = "fkmkmf",
        val description: String? = "mfkvfkl"
    )
}