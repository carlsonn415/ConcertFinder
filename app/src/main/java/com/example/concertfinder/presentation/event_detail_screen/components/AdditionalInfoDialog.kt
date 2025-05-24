package com.example.concertfinder.presentation.event_detail_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.concertfinder.R

@Composable
fun AdditionalInfoDialog(
    infoMap: MutableMap<String, String>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Additional info popup
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
            usePlatformDefaultWidth = false
        ),
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp)
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Box(
                modifier = modifier.padding(dimensionResource(R.dimen.padding_medium)),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = modifier.verticalScroll(rememberScrollState())
                ) {
                    infoMap.forEach {
                        if (it.value.isNotEmpty()) {
                            Row {
                                TextBlock(
                                    title = it.key,
                                    text = it.value,
                                    modifier = modifier.padding(start = dimensionResource(R.dimen.padding_medium))
                                )
                                Spacer(modifier = modifier.weight(1f))
                            }
                        }
                    }
                    Spacer(modifier = modifier.height(32.dp))
                }
                // Close button
                Row(
                    modifier = modifier.background(color = MaterialTheme.colorScheme.surfaceContainer),
                ) {
                    Spacer(modifier = modifier.weight(1f))
                    Text(
                        text = stringResource(R.string.close),
                        style = MaterialTheme.typography.labelLarge,
                        textDecoration = TextDecoration.Underline,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = modifier
                            .clickable {
                                onDismiss()
                            }
                            .padding(
                                horizontal = dimensionResource(R.dimen.padding_medium),
                                vertical = dimensionResource(R.dimen.padding_small)
                            )
                    )
                }
            }
        }
    }
}