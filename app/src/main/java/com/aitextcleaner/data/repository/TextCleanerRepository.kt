package com.aitextcleaner.data.repository

import com.aitextcleaner.data.network.OpenAiApiClient
import com.aitextcleaner.viewmodel.CleaningMode

class TextCleanerRepository(
    private val apiClient: OpenAiApiClient
) {
    suspend fun cleanText(input: String, mode: CleaningMode): Result<String> {
        return apiClient.createChatCompletion(
            systemPrompt = mode.toSystemPrompt(),
            userMessage = input
        )
    }
}

private fun CleaningMode.toSystemPrompt(): String {
    return when (this) {
        CleaningMode.FIX_GRAMMAR ->
            "Fix grammar and spelling only. Return only the cleaned text."
        CleaningMode.MAKE_POLITE ->
            "Rewrite politely. Return only the cleaned text."
        CleaningMode.SIMPLIFY ->
            "Simplify for a B1 level reader. Return only the cleaned text."
        CleaningMode.MAKE_STRONGER ->
            "Rewrite to sound more confident and strong. Return only the cleaned text."
    }
}
