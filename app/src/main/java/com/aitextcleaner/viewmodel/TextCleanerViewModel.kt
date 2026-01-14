package com.aitextcleaner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aitextcleaner.BuildConfig
import com.aitextcleaner.data.network.OpenAiApiClient
import com.aitextcleaner.data.repository.TextCleanerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class CleaningMode(val label: String) {
    FIX_GRAMMAR("Fix Grammar"),
    MAKE_POLITE("Make Polite"),
    SIMPLIFY("Simplify"),
    MAKE_STRONGER("Make Stronger")
}

data class TextCleanerFormState(
    val input: String = "",
    val mode: CleaningMode = CleaningMode.FIX_GRAMMAR
)

sealed interface TextCleanerUiState {
    data object Idle : TextCleanerUiState
    data object Loading : TextCleanerUiState
    data class Success(val cleanedText: String) : TextCleanerUiState
    data class Error(val message: String) : TextCleanerUiState
}

class TextCleanerViewModel : ViewModel() {
    private val repository = TextCleanerRepository(
        OpenAiApiClient(apiKey = BuildConfig.OPENAI_API_KEY)
    )

    private val _formState = MutableStateFlow(TextCleanerFormState())
    val formState: StateFlow<TextCleanerFormState> = _formState.asStateFlow()

    private val _uiState = MutableStateFlow<TextCleanerUiState>(TextCleanerUiState.Idle)
    val uiState: StateFlow<TextCleanerUiState> = _uiState.asStateFlow()

    fun onInputChange(value: String) {
        _formState.update { it.copy(input = value) }
        if (_uiState.value is TextCleanerUiState.Error) {
            _uiState.value = TextCleanerUiState.Idle
        }
    }

    fun onModeSelected(mode: CleaningMode) {
        _formState.update { it.copy(mode = mode) }
        if (_uiState.value is TextCleanerUiState.Error) {
            _uiState.value = TextCleanerUiState.Idle
        }
    }

    fun cleanText(input: String, mode: CleaningMode) {
        val trimmedInput = input.trim()
        if (trimmedInput.isBlank()) {
            _uiState.value = TextCleanerUiState.Error("Please enter text to clean.")
            return
        }
        if (BuildConfig.OPENAI_API_KEY.isBlank()) {
            _uiState.value = TextCleanerUiState.Error("Missing API key")
            return
        }
        _uiState.value = TextCleanerUiState.Loading
        viewModelScope.launch {
            val result = try {
                repository.cleanText(trimmedInput, mode)
            } catch (exception: Exception) {
                Result.failure(exception)
            }
            _uiState.value = result.fold(
                onSuccess = { TextCleanerUiState.Success(it) },
                onFailure = { TextCleanerUiState.Error(it.message ?: "Something went wrong.") }
            )
        }
    }
}
