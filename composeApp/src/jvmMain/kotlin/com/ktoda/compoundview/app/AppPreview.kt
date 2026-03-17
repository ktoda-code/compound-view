package com.ktoda.compoundview.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ktoda.compoundview.data.config.AppConfigs
import com.ktoda.compoundview.data.config.ProvideAppConfig
import com.ktoda.compoundview.ui.screens.MainScreen

@Preview
@Composable
fun AppPreview() {
    ProvideAppConfig(AppConfigs()) {
        MainScreen()
    }
}