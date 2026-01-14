package com.aitextcleaner.data.network

import java.io.IOException
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OpenAiApiClient(
    private val apiKey: String,
    private val baseUrl: String = "https://api.openai.com/"
) {
    private val service: OpenAiApiService by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $apiKey")
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenAiApiService::class.java)
    }

    suspend fun createChatCompletion(systemPrompt: String, userMessage: String): Result<String> {
        val request = OpenAiRequest(
            model = "gpt-4o-mini",
            messages = listOf(
                OpenAiMessage(role = "system", content = systemPrompt),
                OpenAiMessage(role = "user", content = userMessage)
            )
        )
        return try {
            val response = service.createCompletion(request)
            val cleaned = response.choices.firstOrNull()?.message?.content?.trim().orEmpty()
            if (cleaned.isBlank()) {
                Result.failure(IllegalStateException("OpenAI returned an empty response. Please try again."))
            } else {
                Result.success(cleaned)
            }
        } catch (exception: HttpException) {
            Result.failure(IllegalStateException(friendlyHttpError(exception.code())))
        } catch (exception: IOException) {
            Result.failure(IllegalStateException("Network error. Check your connection and try again."))
        } catch (exception: Exception) {
            Result.failure(IllegalStateException("Unexpected error. Please try again."))
        }
    }

    private fun friendlyHttpError(code: Int): String {
        return when (code) {
            401, 403 -> "Authentication failed. Check your OpenAI API key."
            429 -> "Rate limit exceeded. Please try again later."
            in 500..599 -> "OpenAI service is having issues. Please try again later."
            else -> "Request failed with status code $code. Please try again."
        }
    }
}
