package com.ktoda.compoundview.platform

import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import com.sun.jna.platform.win32.Advapi32Util
import com.sun.jna.platform.win32.WinReg

private val WindowsCapabilities = PlatformCapabilities(
    supportsCustomWindowControls = true,
    supportsWindowDragging = true,
    supportsSystemAccentColor = true
)

private val LinuxCapabilities = PlatformCapabilities(
    supportsCustomWindowControls = true,
    supportsWindowDragging = true
)

private val DesktopCapabilities = PlatformCapabilities(
    supportsCustomWindowControls = true,
    supportsWindowDragging = true
)

private val WindowsAccentColorProvider = AccentColorProvider(::readWindowsAccentColor)
private val WindowsSystemThemeProvider = SystemThemeProvider(::readWindowsDarkTheme)
private val NoAccentColorProvider = AccentColorProvider { null }
private val NoSystemThemeProvider = SystemThemeProvider { null }

@Composable
fun WindowScope.rememberDesktopPlatformServices(
    windowState: WindowState,
    onCloseRequest: () -> Unit
): PlatformServices {
    val platformInfo = remember { detectDesktopPlatformInfo() }

    return remember(window, windowState, onCloseRequest, platformInfo) {
        PlatformServices(
            info = platformInfo,
            accentColorProvider = when (platformInfo.osName) {
                "Windows" -> WindowsAccentColorProvider
                else -> NoAccentColorProvider
            },
            systemThemeProvider = when (platformInfo.osName) {
                "Windows" -> WindowsSystemThemeProvider
                else -> NoSystemThemeProvider
            },
            hostWindowStyler = when (platformInfo.osName) {
                "Windows" -> WindowsHostWindowStyler(window)
                else -> null
            },
            windowChrome = DesktopWindowChrome(
                windowState = windowState,
                onCloseRequest = onCloseRequest,
                draggableArea = { modifier, content ->
                    WindowDraggableArea(modifier = modifier) {
                        content()
                    }
                }
            )
        )
    }
}

private class DesktopWindowChrome(
    private val windowState: WindowState,
    private val onCloseRequest: () -> Unit,
    private val draggableArea: DraggableAreaContent
) : WindowChrome {
    override val state: WindowFrameState
        get() = WindowFrameState(
            isMaximized = windowState.placement == WindowPlacement.Maximized
        )

    @Composable
    override fun DraggableArea(
        modifier: Modifier,
        content: @Composable () -> Unit
    ) {
        draggableArea(modifier, content)
    }

    override fun minimize() {
        windowState.isMinimized = true
    }

    override fun toggleMaximize() {
        windowState.placement = if (windowState.placement == WindowPlacement.Maximized) {
            WindowPlacement.Floating
        } else {
            WindowPlacement.Maximized
        }
    }

    override fun close() {
        onCloseRequest()
    }
}

private typealias DraggableAreaContent = @Composable (Modifier, @Composable () -> Unit) -> Unit

private fun detectDesktopPlatformInfo(): PlatformInfo {
    val osName = System.getProperty("os.name").orEmpty()
    val normalizedName = osName.lowercase()

    return when {
        "win" in normalizedName -> PlatformInfo("Windows", WindowsCapabilities)
        "linux" in normalizedName -> PlatformInfo("Linux", LinuxCapabilities)
        else -> PlatformInfo(osName.ifBlank { "Desktop" }, DesktopCapabilities)
    }
}

private fun readWindowsAccentColor(): Color? {
    return try {
        val colorInt = Advapi32Util.registryGetIntValue(
            WinReg.HKEY_CURRENT_USER,
            "Software\\Microsoft\\Windows\\DWM",
            "AccentColor"
        )
        val alpha = (colorInt shr 24) and 0xFF
        val blue = (colorInt shr 16) and 0xFF
        val green = (colorInt shr 8) and 0xFF
        val red = colorInt and 0xFF
        val resolvedAlpha = if (alpha == 0) 255 else alpha

        Color(red = red, green = green, blue = blue, alpha = resolvedAlpha)
    } catch (_: Exception) {
        null
    }
}

internal fun readWindowsDarkTheme(): Boolean? {
    return try {
        val appsUseLightTheme = Advapi32Util.registryGetIntValue(
            WinReg.HKEY_CURRENT_USER,
            "Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize",
            "AppsUseLightTheme"
        )
        appsUseLightTheme == 0
    } catch (_: Exception) {
        null
    }
}
