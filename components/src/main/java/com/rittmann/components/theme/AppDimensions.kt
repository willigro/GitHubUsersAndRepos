package com.rittmann.components.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.slaviboy.composeunits.dh

data class AppDimensions(
    // Padding
    val paddingSmall: Dp = 8.dp,
    val paddingMedium: Dp = 16.dp,
    val paddingLarge: Dp = 22.dp,

    val paddingTopBetweenComponentsSmall: Dp = 6.dp,
    val paddingTopBetweenComponentsMedium: Dp = 12.dp,

    val progressAppendingItems: Dp = 16.dp,

    // Size
    val progressSize: Dp = 100.dp,
)

internal val LocalDimensions = staticCompositionLocalOf { AppDimensions() }