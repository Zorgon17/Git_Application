package com.example.gitapplication.pojomodel

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val login: String
)
