package com.aitextcleaner.data.repository

import com.aitextcleaner.data.network.OpenAiApiClient
import com.aitextcleaner.viewmodel.CleaningMode

class TextCleanerRepository(
    private val apiClient: OpenAiApiClient
) {
    suspend fun cleanText(input: String, mode: CleaningMode): Result<String> {
        return apiClient.cleanText(input, mode)
    }
}
