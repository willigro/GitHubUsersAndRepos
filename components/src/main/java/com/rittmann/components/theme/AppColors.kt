package com.rittmann.components.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


class AppColors(
    isLightColors: Boolean,
    primary: Color,
    secondary: Color,
    textPrimary: Color,
    textSecondary: Color,
    textInfo: Color,
    background: Color,
    backgroundFilter: Color,
    searchFieldBackground: Color,
    searchFieldMinimizeBackground: Color,
    primaryIcon: Color,
    secondaryIcon: Color,
) {
    var primary by mutableStateOf(primary)
        private set
    var secondary by mutableStateOf(secondary)
        private set
    var textPrimary by mutableStateOf(textPrimary)
        private set
    var textSecondary by mutableStateOf(textSecondary)
        private set
    var textInfo by mutableStateOf(textInfo)
        private set
    var isLight by mutableStateOf(isLightColors)
        internal set

    /**
     * Backgrounds
     * */
    var background by mutableStateOf(background)
        private set
    var backgroundFilter by mutableStateOf(backgroundFilter)
        private set

    /**
     * Icons
     * */
    var primaryIcon by mutableStateOf(primaryIcon)
        private set
    var secondaryIcon by mutableStateOf(secondaryIcon)
        private set

    /**
     * Specific
     * */
    var searchFieldBackground by mutableStateOf(searchFieldBackground)
        private set
    var searchFieldMinimizeBackground by mutableStateOf(searchFieldMinimizeBackground)
        private set

    fun copy(
        isLight: Boolean = this.isLight,
        primary: Color = this.primary,
        secondary: Color = this.secondary,
        textPrimary: Color = this.textPrimary,
        textSecondary: Color = this.textSecondary,
        textInfo: Color = this.textInfo,
        background: Color = this.background,
        backgroundFilter: Color = this.backgroundFilter,
        primaryIcon: Color = this.primaryIcon,
        secondaryIcon: Color = this.secondaryIcon,
        searchFieldBackground: Color = this.searchFieldBackground,
        searchFieldMinimizeBackground: Color = this.searchFieldMinimizeBackground,
    ): AppColors = AppColors(
        isLight,
        primary,
        secondary,
        textPrimary,
        textSecondary,
        textInfo,
        background,
        backgroundFilter,
        searchFieldBackground,
        searchFieldMinimizeBackground,
        primaryIcon,
        secondaryIcon,
    )

    fun updateColorsFrom(other: AppColors) {
        primary = other.primary
        textPrimary = other.textPrimary
        textSecondary = other.textSecondary
    }
}

/**
 * Default
 * */
private val colorLightPrimary = Color(0xFF444E72)
private val colorLightTextPrimary = Color(0xFFFFFFFF)
private val colorLightTextSecondary = Color(0xFF444E72)
private val colorLightTextInfo = Color(0xFF838BAA)
private val colorLightBackground = Color(0xFF002762)
private val colorLightBackgroundInfo = Color(0xFFFCFCFC)
private val colorLightPrimaryIcon = Color(0xFF444E72)
private val colorLightSecondaryIcon = Color(0xFFFFFFFF)

/**
 * Specific
 * */
private val colorLightSearchFieldBackground = Color(0xFFFFFFFF)
private val colorLightSearchFieldMinimizeBackground = Color(0xFFF1F4FF)

fun appLightColors(
    primary: Color = colorLightPrimary,
    secondary: Color = colorLightPrimary,
    textPrimary: Color = colorLightTextPrimary,
    textSecondary: Color = colorLightTextSecondary,
    textInfo: Color = colorLightTextInfo,
    background: Color = colorLightBackground,
    backgroundFilter: Color = colorLightBackgroundInfo,
    primaryIcon: Color = colorLightPrimaryIcon,
    secondaryIcon: Color = colorLightSecondaryIcon,
    searchFieldBackground: Color = colorLightSearchFieldBackground,
    searchFieldMinimizeBackground: Color = colorLightSearchFieldMinimizeBackground,
): AppColors = AppColors(
    isLightColors = true,
    primary = primary,
    secondary = secondary,
    textPrimary = textPrimary,
    textSecondary = textSecondary,
    textInfo = textInfo,
    background = background,
    backgroundFilter = backgroundFilter,
    primaryIcon = primaryIcon,
    secondaryIcon = secondaryIcon,
    searchFieldBackground = searchFieldBackground,
    searchFieldMinimizeBackground = searchFieldMinimizeBackground,
)

internal val LocalColors = staticCompositionLocalOf { appLightColors() }