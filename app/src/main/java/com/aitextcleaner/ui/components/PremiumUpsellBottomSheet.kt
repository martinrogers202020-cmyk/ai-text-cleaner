package com.aitextcleaner.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aitextcleaner.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumUpsellBottomSheet(
    onDismiss: () -> Unit,
    onGoPro: () -> Unit,
    onWatchAd: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.premium_title),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = stringResource(id = R.string.premium_subtitle),
                style = MaterialTheme.typography.bodyMedium
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = stringResource(id = R.string.premium_benefit_no_ads), fontSize = 15.sp)
                Text(text = stringResource(id = R.string.premium_benefit_faster), fontSize = 15.sp)
                Text(text = stringResource(id = R.string.premium_benefit_extra_modes), fontSize = 15.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onGoPro, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.go_pro))
            }
            OutlinedButton(onClick = onWatchAd, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.watch_ad_unlock))
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
