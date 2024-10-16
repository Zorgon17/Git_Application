package com.example.gitapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RepositoriesListViewModel : ViewModel() {
    val title = MutableLiveData<String>()
}