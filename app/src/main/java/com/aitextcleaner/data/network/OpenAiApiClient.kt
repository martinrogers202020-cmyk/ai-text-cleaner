package com.aitextcleaner.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OpenAiApiClient(
    private val apiKey: String,
    private val baseUrl: String = "https://api.openai.com/"
) {
    val service: OpenAiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenAiApiService::class.java)
    }

    suspend fun cleanText(input: String, mode: String): String {
        // Placeholder for OpenAI API integration.
        return "[$mode] $input"
    }
}
