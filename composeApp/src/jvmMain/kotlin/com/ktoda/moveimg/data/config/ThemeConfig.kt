package com.ktoda.moveimg.data.config

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class ThemeConfig(
    val frameBgClr: Color,
    val panelBgClr: Color,
    val textClr: Color,
    val borderClr: Color,
    val accentClr: Color,
    val radius: Dp = 8.dp,
    val frameShape: RoundedCornerShape = RoundedCornerShape(radius)
)

internal val DarkTheme = ThemeConfig(
    frameBgClr = Color(0xFF202020),
    panelBgClr = Color(0xFF2B2B2B),
    textClr = Color(0xFFFFFFFF),
    borderClr = Color(0x1AFFFFFF),
    accentClr = Color(0xFF0078D4)
)

internal val LightTheme = ThemeConfig(
    frameBgClr = Color(0xFFF3F3F3),
    panelBgClr = Color(0xFFFFFFFF),
    textClr = Color(0xFF1A1A1A),
    borderClr = Color(0x0F000000),
    accentClr = Color(0xFF0078D4)
)