package com.ktoda.moveimg.app

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.ktoda.moveimg.data.config.MoveImgTheme

fun main() = application {
    // 1. Define initial state
    val windowState = WindowState(
        size = DpSize(1200.dp, 800.dp),
        position = WindowPosition.Aligned(Alignment.Center),
    )

    // 2. Create the Window
    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Move Images",
        undecorated = true,
        transparent = false
    ) {
        // 3. Apply Theme
        MoveImgTheme {
            // 4. Launch App
            // No Surface here. No shape logic here.
            // We pass the state down so the UI handles its own clipping.
            App(windowState)
        }
    }
}