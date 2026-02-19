package com.ktoda.moveimg.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ktoda.moveimg.data.config.AppConfigs
import com.ktoda.moveimg.data.config.ProvideAppConfig
import com.ktoda.moveimg.ui.screens.MainScreen

@Preview
@Composable
fun AppPreview() {
    ProvideAppConfig(AppConfigs()) {
        MainScreen()
    }
}