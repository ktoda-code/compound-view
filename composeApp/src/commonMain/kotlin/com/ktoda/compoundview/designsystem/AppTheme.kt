package com.ktoda.compoundview.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.ktoda.compoundview.platform.HostWindowTheme
import com.ktoda.compoundview.platform.PlatformServices
import kotlinx.coroutines.delay

@Immutable
data class AppConfig(
    val theme: ThemeConfig = DarkTheme
)

val LocalAppConfig = staticCompositionLocalOf<AppConfig> {
    error("No AppConfig provided")
}

@Composable
fun ProvideAppConfig(
    appConfig: AppConfig,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalAppConfig provides appConfig) {
        content()
    }
}

private val DefaultAccentColor = Color(0xFF0078D4)

@Composable
fun CompoundViewTheme(
    platformServices: PlatformServices,
    isDarkTheme: Boolean? = null,
    content: @Composable () -> Unit
) {
    val accentColor = remember(platformServices.accentColorProvider) {
        platformServices.accentColorProvider.systemAccentColor() ?: DefaultAccentColor
    }
    val platformIsDarkTheme by produceState<Boolean?>(
        initialValue = platformServices.systemThemeProvider.isDarkTheme(),
        key1 = platformServices.systemThemeProvider,
        key2 = platformServices.info.osName
    ) {
        if (platformServices.info.osName != "Windows") {
            return@produceState
        }

        while (true) {
            delay(1000)
            value = platformServices.systemThemeProvider.isDarkTheme()
        }
    }
    val resolvedIsDarkTheme = isDarkTheme ?: platformIsDarkTheme ?: isSystemInDarkTheme()

    val appConfig = remember(resolvedIsDarkTheme, accentColor) {
        val baseTheme = if (resolvedIsDarkTheme) DarkTheme else LightTheme
        AppConfig(theme = baseTheme.copy(accentClr = accentColor))
    }

    SideEffect {
        platformServices.hostWindowStyler?.apply(
            HostWindowTheme(
                isDarkTheme = resolvedIsDarkTheme,
                backgroundColor = appConfig.theme.frameBgClr
            )
        )
    }

    ProvideAppConfig(appConfig, content)
}
