package com.ktoda.compoundview.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import com.ktoda.compoundview.data.config.LocalAppConfig

@Composable
fun WindowScope.TitleBar(
    windowState: WindowState,
    onClose: () -> Unit
) {
    val appConfigs = LocalAppConfig.current
    val isMaximized = windowState.placement == WindowPlacement.Maximized

    // Shape Logic: Square if maximized, Rounded Top if floating
    val titleBarShape = if (isMaximized) RectangleShape else RoundedCornerShape(
        topStart = appConfigs.theme.radius,
        topEnd = appConfigs.theme.radius
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .clip(titleBarShape)
            .background(appConfigs.theme.frameBgClr) // Note: using .theme
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        WindowDraggableArea(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "MoveImg",
                    color = appConfigs.theme.textClr,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(Modifier.fillMaxSize())
            }
        }

        MinimizeButton(
            action = { windowState.isMinimized = true }
        )

        MaximizeButton(
            isMaximized = isMaximized,
            action = {
                if (windowState.placement == WindowPlacement.Maximized) {
                    windowState.placement = WindowPlacement.Floating
                } else {
                    windowState.placement = WindowPlacement.Maximized
                }
            }
        )

        CloseButton(
            isMaximized = isMaximized,
            action = onClose
        )
    }
}