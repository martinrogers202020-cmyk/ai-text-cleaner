package com.aitextcleaner.data.network

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAiApiService {
    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    suspend fun createCompletion(@Body request: OpenAiRequest): OpenAiResponse
}

data class OpenAiRequest(
    val model: String,
    val messages: List<OpenAiMessage>
)

data class OpenAiMessage(
    val role: String,
    val content: String
)

data class OpenAiResponse(
    val id: String = "",
    val choices: List<OpenAiChoice> = emptyList()
)

data class OpenAiChoice(
    val message: OpenAiMessage = OpenAiMessage(role = "assistant", content = "")
)
