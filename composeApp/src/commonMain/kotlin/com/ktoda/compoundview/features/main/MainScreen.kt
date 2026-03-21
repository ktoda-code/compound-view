package com.ktoda.compoundview.features.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ktoda.compoundview.designsystem.LocalAppConfig

@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {
    val appConfig = LocalAppConfig.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(appConfig.theme.frameBgClr)
    ) {
        Column(Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Main Screen Content",
                    color = appConfig.theme.textClr
                )
            }
        }
    }
}
