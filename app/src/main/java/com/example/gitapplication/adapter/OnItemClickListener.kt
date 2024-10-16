package com.example.gitapplication.adapter

import com.example.gitapplication.pojomodel.Repository

interface OnItemClickListener {
    fun onItemClick(repository: Repository)
}