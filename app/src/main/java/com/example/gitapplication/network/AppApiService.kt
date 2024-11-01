package com.example.gitapplication.network

import ReadmeResponse
import android.util.Log
import com.example.gitapplication.pojomodel.Repository
import com.example.gitapplication.pojomodel.UserResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

interface GitHubService {
    // запрос для получения информации о репозиториях
    @GET("user/repos")
    suspend fun getRepos(
        @Query("visibility") visibility: String = "all",
        @Query("per_page") perPage: Int = 10
    ): List<Repository>

    // запрос для получения ReadMe файла
    @GET("repos/{owner}/{nameOfRepo}/readme")
    suspend fun getReadMe(
        @Path("owner") owner: String,
        @Path("nameOfRepo") nameOfRepo: String
    ): ReadmeResponse

    // запрос для проверки токена
    @GET("user")
    suspend fun getUser(): UserResponse
}

class GitHubClient @Inject constructor(private val token: String) {
    private val apiService: GitHubService

    init {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Создаем OkHttpClient и добавляем интерсепторы
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder()
                    .addHeader("Authorization", "token $token")
                    .build())
            }
            .build()

        // Создаем объект Json с настройками, чтобы игнорировать неизвестные ключи
        val json = Json {
            ignoreUnknownKeys = true // игнорируем неописанные поля
        }

        // Создаем Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(client)
            .build()

        apiService = retrofit.create(GitHubService::class.java)
    }

    suspend fun checkToken(): UserResponse? {
        return try {
            apiService.getUser()
        } catch (e: Exception) {
            Log.e("GitHubClient", "Invalid token: ${e.message}", e)
            null
        }
    }

    suspend fun getReadMe(owner: String, nameOfRepo: String): String? {
        return try {
            val response = apiService.getReadMe(owner, nameOfRepo)
            Log.d("GitHubClient", "ReadmeResponse: $response")
            response.decodedContent
        } catch (e: Exception) {
            Log.e("GitHubClient", "Error fetching README: ${e.message}", e)
            null
        }
    }

    suspend fun getFirstTenRepositories(visibility: String): List<Repository> {
        return try {
            apiService.getRepos(visibility)
        } catch (e: Exception) {
            Log.e("GitHubClient", "Error fetching repositories: ${e.message}", e)
            emptyList()
        }
    }

    companion object {
        const val BASE_URL = "https://api.github.com/"
    }
}
