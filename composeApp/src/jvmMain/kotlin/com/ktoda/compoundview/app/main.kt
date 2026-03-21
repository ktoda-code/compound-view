package com.ktoda.compoundview.app

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.ktoda.compoundview.data.config.MoveImgTheme

fun main() = application {
    val windowState = WindowState(
        size = DpSize(1200.dp, 800.dp),
        position = WindowPosition.Aligned(Alignment.Center),
    )

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "CompoundView"
    ) {
        MoveImgTheme {
            App(
                windowState = windowState,
                onClose = ::exitApplication
            )
        }
    }
}