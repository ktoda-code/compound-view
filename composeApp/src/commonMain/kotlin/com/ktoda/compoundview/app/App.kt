package com.ktoda.compoundview.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ktoda.compoundview.designsystem.CompoundViewTheme
import com.ktoda.compoundview.features.main.MainScreen
import com.ktoda.compoundview.platform.PlatformServices

@Composable
fun App(
    platformServices: PlatformServices
) {
    CompoundViewTheme(platformServices = platformServices) {
        MainScreen(modifier = Modifier.fillMaxSize())
    }
}
