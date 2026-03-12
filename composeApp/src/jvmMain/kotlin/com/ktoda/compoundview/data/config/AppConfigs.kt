package com.ktoda.compoundview.data.config

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.ktoda.compoundview.platform.os.WindowsPlatform

@Immutable
data class AppConfigs(
    val theme: ThemeConfig = DarkTheme
)

val LocalAppConfig = staticCompositionLocalOf<AppConfigs> {
    error("No AppConfigs provided")
}

@Composable
fun ProvideAppConfig(
    appConfigs: AppConfigs,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalAppConfig provides appConfigs) {
        content()
    }
}

@Composable
fun MoveImgTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val windowsAccent = remember { WindowsPlatform.getSystemAccentColor() }

    val appConfig = remember(isDarkTheme, windowsAccent) {
        val baseTheme = if (isDarkTheme) DarkTheme else LightTheme
        // override the accent color from OS
        AppConfigs(theme = baseTheme.copy(accentClr = windowsAccent))
    }

    ProvideAppConfig(appConfig, content)
}