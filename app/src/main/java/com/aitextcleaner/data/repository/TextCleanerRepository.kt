package com.aitextcleaner.data.repository

import com.aitextcleaner.data.network.OpenAiApiClient

class TextCleanerRepository(
    private val apiClient: OpenAiApiClient
) {
    suspend fun cleanText(input: String, mode: String): String {
        return apiClient.cleanText(input, mode)
    }
}
