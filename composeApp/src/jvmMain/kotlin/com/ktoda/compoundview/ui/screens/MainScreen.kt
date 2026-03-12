package com.ktoda.compoundview.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import com.ktoda.compoundview.data.config.LocalAppConfig

@Composable
fun MainScreen(windowState: WindowState? = null) {
    val appConfigs = LocalAppConfig.current
    val isMaximized = windowState?.placement == WindowPlacement.Maximized

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(
                if (isMaximized) RectangleShape
                else RoundedCornerShape(
                    bottomStart = appConfigs.theme.radius,
                    bottomEnd = appConfigs.theme.radius
                )
            )
            .background(appConfigs.theme.frameBgClr)
    ) {
        Column(Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Main Screen Content", color = appConfigs.theme.textClr)
            }
        }
    }
}
