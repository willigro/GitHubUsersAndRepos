package com.rittmann.components.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.rittmann.components.theme.AppTheme
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ProgressScreen(modifier: Modifier, isLoadingState: StateFlow<Boolean>) {
    val isLoading = isLoadingState.collectAsState().value

    if (isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = AppTheme.floats.progressAlpha)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(AppTheme.dimensions.progressSize)
            )
        }
    }
}