package com.aitextcleaner.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SettingsUiState(
    val useSystemTheme: Boolean = true,
    val enableAds: Boolean = true
)

class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun toggleSystemTheme(enabled: Boolean) {
        _uiState.update { it.copy(useSystemTheme = enabled) }
    }

    fun toggleAds(enabled: Boolean) {
        _uiState.update { it.copy(enableAds = enabled) }
    }
}
