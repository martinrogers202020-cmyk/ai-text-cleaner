package com.aitextcleaner.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aitextcleaner.R
import com.aitextcleaner.ui.components.PremiumUpsellBottomSheet
import com.aitextcleaner.viewmodel.CleaningMode
import com.aitextcleaner.viewmodel.SettingsViewModel
import com.aitextcleaner.viewmodel.TextCleanerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: TextCleanerViewModel,
    settingsViewModel: SettingsViewModel,
    onNavigateToResult: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val settingsState by settingsViewModel.uiState.collectAsState()
    val maxChars = 2000

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
                onValueChange = { value ->
                    if (value.length <= maxChars) {
                        viewModel.onInputChange(value)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = stringResource(id = R.string.input_hint)) },
                minLines = 6,
                supportingText = {
                    Text(text = stringResource(id = R.string.character_count, uiState.input.length, maxChars))
                }
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
                enabled = !uiState.isLoading && uiState.input.isNotBlank()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(id = R.string.clean_text))
                } else {
                    Text(text = stringResource(id = R.string.clean_text))
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            BannerAdPlaceholder(
                height = 64.dp,
                onGoProClick = { settingsViewModel.setPremiumUpsellVisible(true) }
            )
        }
    }

    if (settingsState.showPremiumUpsell) {
        PremiumUpsellBottomSheet(
            onDismiss = { settingsViewModel.setPremiumUpsellVisible(false) },
            onGoPro = { settingsViewModel.setPremiumUpsellVisible(false) },
            onWatchAd = { settingsViewModel.setPremiumUpsellVisible(false) }
        )
    }
}

@Composable
private fun ModeRow(
    selectedMode: CleaningMode,
    onModeSelected: (CleaningMode) -> Unit
) {
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        SegmentedButton(
            selected = selectedMode == CleaningMode.FIX_GRAMMAR,
            onClick = { onModeSelected(CleaningMode.FIX_GRAMMAR) },
            shape = androidx.compose.material3.SegmentedButtonDefaults.itemShape(index = 0, count = 4)
        ) {
            Text(text = stringResource(id = R.string.fix_grammar))
        }
        SegmentedButton(
            selected = selectedMode == CleaningMode.MAKE_POLITE,
            onClick = { onModeSelected(CleaningMode.MAKE_POLITE) },
            shape = androidx.compose.material3.SegmentedButtonDefaults.itemShape(index = 1, count = 4)
        ) {
            Text(text = stringResource(id = R.string.make_polite))
        }
        SegmentedButton(
            selected = selectedMode == CleaningMode.SIMPLIFY,
            onClick = { onModeSelected(CleaningMode.SIMPLIFY) },
            shape = androidx.compose.material3.SegmentedButtonDefaults.itemShape(index = 2, count = 4)
        ) {
            Text(text = stringResource(id = R.string.simplify))
        }
        SegmentedButton(
            selected = selectedMode == CleaningMode.MAKE_STRONGER,
            onClick = { onModeSelected(CleaningMode.MAKE_STRONGER) },
            shape = androidx.compose.material3.SegmentedButtonDefaults.itemShape(index = 3, count = 4)
        ) {
            Text(text = stringResource(id = R.string.make_stronger))
        }
    }
}

@Composable
private fun BannerAdPlaceholder(
    height: Dp,
    onGoProClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = height)
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 2.dp,
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(id = R.string.banner_placeholder),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        OutlinedButton(onClick = onGoProClick, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.go_pro))
        }
    }
}
