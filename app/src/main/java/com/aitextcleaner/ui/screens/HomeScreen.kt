package com.aitextcleaner.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aitextcleaner.R
import com.aitextcleaner.viewmodel.CleaningMode
import com.aitextcleaner.viewmodel.TextCleanerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: TextCleanerViewModel,
    onNavigateToResult: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.home_title)) },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Settings,
                            contentDescription = stringResource(id = R.string.settings_title)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.home_title),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            TextField(
                value = uiState.input,
                onValueChange = viewModel::onInputChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = stringResource(id = R.string.input_hint)) },
                minLines = 5
            )
            ModeRow(
                selectedMode = uiState.mode,
                onModeSelected = viewModel::onModeSelected
            )
            if (uiState.error != null) {
                Text(
                    text = uiState.error.orEmpty(),
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp
                )
            }
            Button(
                onClick = {
                    viewModel.submit()
                    if (uiState.input.isNotBlank()) {
                        onNavigateToResult()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(id = R.string.clean))
                } else {
                    Text(text = stringResource(id = R.string.clean))
                }
            }
        }
    }
}

@Composable
private fun ModeRow(
    selectedMode: CleaningMode,
    onModeSelected: (CleaningMode) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ModeButton(
            label = stringResource(id = R.string.clean),
            isSelected = selectedMode == CleaningMode.CLEAN,
            onClick = { onModeSelected(CleaningMode.CLEAN) }
        )
        ModeButton(
            label = stringResource(id = R.string.simplify),
            isSelected = selectedMode == CleaningMode.SIMPLIFY,
            onClick = { onModeSelected(CleaningMode.SIMPLIFY) }
        )
        ModeButton(
            label = stringResource(id = R.string.summarize),
            isSelected = selectedMode == CleaningMode.SUMMARIZE,
            onClick = { onModeSelected(CleaningMode.SUMMARIZE) }
        )
    }
}

@Composable
private fun ModeButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val modifier = Modifier.weight(1f)
    if (isSelected) {
        FilledTonalButton(onClick = onClick, modifier = modifier) {
            Text(text = label)
        }
    } else {
        OutlinedButton(onClick = onClick, modifier = modifier) {
            Text(text = label)
        }
    }
}
