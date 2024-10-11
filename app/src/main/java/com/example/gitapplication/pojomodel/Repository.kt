package com.example.gitapplication.pojomodel

import com.example.gitapplication.pojomodel.Repository.RepositoryItem
import com.google.gson.annotations.SerializedName

/**
 * Model класс, который используется как модель JSON
 */
// возможно придется переделать
class Repository : ArrayList<RepositoryItem>(){
    data class RepositoryItem(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("language")
        val language: String?,
        @SerializedName("description")
        val description: String?
    )
}