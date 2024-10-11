package com.example.gitapplication.network

import android.annotation.SuppressLint
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gitapplication.pojomodel.Repository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Этот код представляет собой реализацию сетевого клиента для взаимодействия с GitHub API с
 * помощью Retrofit
 *
 * Он позволяет получить список репозиториев пользователя, авторизовавшись с помощью
 * персонального токена.
 */

/**
 * Определяет базовый URL для всех запросов к GitHub API
 */
const val BASE_URL = "https://api.github.com/"

/**
 * Интерфейс предоставляет Retrofit Api сайтов для запросов.
 * В данном случае используем GitHub RestApi.
 */
interface GitHubService {
    @GET("user/repos")
    suspend fun getRepos(
        @Query("visibility") visibility: String = "all",
        @Query("per_page") perPage: Int = 10
    ): List<Repository>
}

/**
 * Создаем "service" для вызова методов GitHub RestApi.
 */
@SuppressLint("RestrictedApi")
class GitHubClient(private val token: String) {

    private val service: GitHubService

    init {
        // Создание клиента OkHttpClient
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(token))
            .build()

        // Билдим Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        
        service = retrofit.create(GitHubService::class.java)
    }

    /**
     * Создаем AuthInterceptor для гибкой настройки токена аутентификации
     * в процессе отправления запроса.
     *
     * chain используется для получения заголовка авторизации
     */
    class AuthInterceptor(private val token: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val request: Request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "token $token")
                .build()
            return chain.proceed(request)
        }
    }

    suspend fun getFirstTenRepositories(
        visibility: String = "all",
        runningFragment: Fragment
    ): List<Repository> {
        return try {
            val repos = service.getRepos(visibility, perPage = 10)
            repos
        } catch (e: Exception) {
            // Обработка ошибок
            withContext(Dispatchers.Main) {
                Toast.makeText(runningFragment.requireContext(), "Ошибка подключения", Toast.LENGTH_LONG).show()
            }
            emptyList()
        }
    }

    // Вернуться когда сделаю ViewHolder
    fun loadRepositories(runningFragment: Fragment, myTextView: TextView) {
        GlobalScope.launch {
            val repos = getFirstTenRepositories(runningFragment = runningFragment)

            withContext(Dispatchers.Main) { // Обработка результата на главном потоке
                if (repos.isNotEmpty()) {
//                    val repoNames = repos.joinToString("\n") { "${it.name} - ${it.description}" }
//                    myTextView.text = repoNames
                } else {
                    Toast.makeText(runningFragment.requireContext(), "Не удалось загрузить репозитории", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}