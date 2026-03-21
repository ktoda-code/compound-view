package com.ktoda.compoundview.app

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.ktoda.compoundview.platform.*

private object PreviewWindowChrome : WindowChrome {
    override val state = WindowFrameState()

    @Composable
    override fun DraggableArea(
        modifier: Modifier,
        content: @Composable () -> Unit
    ) {
        Box(modifier = modifier) {
            content()
        }
    }

    override fun minimize() = Unit

    override fun toggleMaximize() = Unit

    override fun close() = Unit
}

private val PreviewPlatformServices = PlatformServices(
    info = PlatformInfo(
        osName = "Preview",
        capabilities = PlatformCapabilities(
            supportsCustomWindowControls = true,
            supportsWindowDragging = true
        )
    ),
    accentColorProvider = { Color(0xFF0078D4) },
    windowChrome = PreviewWindowChrome
)

@Preview
@Composable
fun AppPreview() {
    App(platformServices = PreviewPlatformServices)
}
