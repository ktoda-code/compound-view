package com.ktoda.compoundview.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Immutable
data class PlatformCapabilities(
    val supportsTray: Boolean = false,
    val supportsNotifications: Boolean = false,
    val supportsCustomWindowControls: Boolean = false,
    val supportsWindowDragging: Boolean = false,
    val supportsSystemAccentColor: Boolean = false
)

@Immutable
data class PlatformInfo(
    val osName: String,
    val capabilities: PlatformCapabilities
)

@Immutable
data class WindowFrameState(
    val isMaximized: Boolean = false
)

fun interface AccentColorProvider {
    fun systemAccentColor(): Color?
}

fun interface SystemThemeProvider {
    fun isDarkTheme(): Boolean?
}

@Immutable
data class HostWindowTheme(
    val isDarkTheme: Boolean,
    val backgroundColor: Color
)

fun interface HostWindowStyler {
    fun apply(theme: HostWindowTheme)
}

interface WindowChrome {
    val state: WindowFrameState

    @Composable
    fun DraggableArea(
        modifier: Modifier,
        content: @Composable () -> Unit
    )

    fun minimize()

    fun toggleMaximize()

    fun close()
}

data class PlatformServices(
    val info: PlatformInfo,
    val accentColorProvider: AccentColorProvider,
    val systemThemeProvider: SystemThemeProvider = SystemThemeProvider { null },
    val hostWindowStyler: HostWindowStyler? = null,
    val windowChrome: WindowChrome? = null
)
