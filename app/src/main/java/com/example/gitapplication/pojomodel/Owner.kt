package com.example.gitapplication.pojomodel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Owner(
    @SerialName("login")
    val login: String?
)