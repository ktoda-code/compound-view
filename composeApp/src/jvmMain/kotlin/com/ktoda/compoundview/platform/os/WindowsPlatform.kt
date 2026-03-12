package com.ktoda.compoundview.platform.os

import androidx.compose.ui.graphics.Color
import com.sun.jna.platform.win32.Advapi32Util
import com.sun.jna.platform.win32.WinReg

object WindowsPlatform {
    private val DefaultAccent = Color(0xFF0078D4)

    fun getSystemAccentColor(): Color {
        return try {
            val os = System.getProperty("os.name").lowercase()
            if (!os.contains("win")) return DefaultAccent

            val colorInt = Advapi32Util.registryGetIntValue(
                WinReg.HKEY_CURRENT_USER,
                "Software\\Microsoft\\Windows\\DWM",
                "AccentColor"
            )
            val a = (colorInt shr 24) and 0xFF
            val b = (colorInt shr 16) and 0xFF
            val g = (colorInt shr 8) and 0xFF
            val r = colorInt and 0xFF
            val finalAlpha = if (a == 0) 255 else a

            Color(red = r, green = g, blue = b, alpha = finalAlpha)
        } catch (_: Exception) {
            DefaultAccent
        }
    }
}