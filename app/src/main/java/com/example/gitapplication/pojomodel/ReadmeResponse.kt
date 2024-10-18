import android.util.Log
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.io.encoding.Base64

@Serializable
data class ReadmeResponse(
    @SerialName("content")
    val content: String,  // Base64 encoded content
    @SerialName("encoding")
    val encoding: String
) {
    // Ленивая декодировка контента
    @OptIn(kotlin.io.encoding.ExperimentalEncodingApi::class)
    val decodedContent: String by lazy {
        // Логгируем входную строку
        Log.d("ReadmeResponse", "Входящая строка до обработки: $content")

        // Убираем все символы новой строки
        val cleanedContent = content.replace("\n", "")

        // Логгируем очищенную строку
        Log.d("ReadmeResponse", "Строка после удаления \\n: $cleanedContent")

        if (encoding == "base64") {
            try {
                // Пытаемся декодировать Base64 строку
                val decodedBytes = Base64.decode(cleanedContent)
                val decodedString = decodedBytes.decodeToString()

                // Логгируем результат декодирования
                Log.d("ReadmeResponse", "Декодированная строка: $decodedString")
                decodedString
            } catch (e: Exception) {
                // Логгируем ошибку декодирования
                Log.e("ReadmeResponse", "Ошибка декодирования: ${e.message}")
                "Ошибка декодирования"
            }
        } else {
            content
        }
    }
}
