package com.aitextcleaner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aitextcleaner.data.network.OpenAiApiClient
import com.aitextcleaner.data.repository.TextCleanerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class CleaningMode(val label: String) {
    CLEAN("Clean"),
    SIMPLIFY("Simplify"),
    SUMMARIZE("Summarize")
}

data class TextCleanerUiState(
    val input: String = "",
    val output: String = "",
    val mode: CleaningMode = CleaningMode.CLEAN,
    val isLoading: Boolean = false,
    val error: String? = null
)

class TextCleanerViewModel : ViewModel() {
    private val repository = TextCleanerRepository(
        OpenAiApiClient(apiKey = "YOUR_OPENAI_API_KEY")
    )

    private val _uiState = MutableStateFlow(TextCleanerUiState())
    val uiState: StateFlow<TextCleanerUiState> = _uiState.asStateFlow()

    fun onInputChange(value: String) {
        _uiState.update { it.copy(input = value) }
    }

    fun onModeSelected(mode: CleaningMode) {
        _uiState.update { it.copy(mode = mode) }
    }

    fun submit() {
        val input = _uiState.value.input.trim()
        if (input.isBlank()) {
            _uiState.update { it.copy(error = "Please enter text to clean.") }
            return
        }
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val output = repository.cleanText(input, _uiState.value.mode.label)
            _uiState.update { it.copy(output = output, isLoading = false) }
        }
    }
}
