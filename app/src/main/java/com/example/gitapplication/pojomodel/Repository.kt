package com.example.gitapplication.pojomodel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Repository(
    @SerialName("id")
    val id: Int?,
    @SerialName("name")
    val name: String?,
    @SerialName("language")
    val language: String?,
    @SerialName("description")
    val description: String?,
)