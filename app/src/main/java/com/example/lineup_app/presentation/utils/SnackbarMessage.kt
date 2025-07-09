package com.example.lineup_app.presentation.utils

import androidx.compose.material3.SnackbarDuration


data class SnackbarMessage(
    val message: String,
    val actionLabel: String? = null,
    val onAction: (() -> Unit)? = null,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val id: Long = System.currentTimeMillis() // Unique ID for LaunchedEffect key
)
