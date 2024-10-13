package com.example.gitapplication.network

import android.util.Log
import com.example.gitapplication.pojomodel.Repository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubService {
    @GET("user/repos")
    suspend fun getRepos(
        @Query("visibility") visibility: String = "all",
        @Query("per_page") perPage: Int = 10
    ): List<Repository>
}


class GitHubClient(private val token: String) {
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
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .client(client)
            .build()


        apiService = retrofit.create(GitHubService::class.java)
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
