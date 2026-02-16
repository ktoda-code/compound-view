package com.ktoda.moveimg.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import com.ktoda.moveimg.ui.components.TitleBar
import com.ktoda.moveimg.ui.screens.MainScreen

@Composable
fun WindowScope.App(
    windowState: WindowState,
    onClose: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {

        TitleBar(
            windowState = windowState,
            onClose = onClose
        )
        MainScreen(windowState)
    }
}