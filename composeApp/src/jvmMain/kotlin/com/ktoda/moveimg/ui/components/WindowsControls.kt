package com.ktoda.moveimg.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import com.ktoda.moveimg.data.config.LocalAppConfig

@Composable
fun MinimizeButton(action: () -> Unit) {
    WindowBaseButton(onClick = action) { color ->
        MinimizeIcon(color)
    }
}

@Composable
fun MaximizeButton(isMaximized: Boolean, action: () -> Unit) {
    WindowBaseButton(onClick = action) { color ->
        if (isMaximized) {
            RestoreIcon(color)
        } else {
            MaximizeIcon(color)
        }
    }
}

@Composable
fun CloseButton(isMaximized: Boolean, action: () -> Unit) {
    WindowBaseButton(
        onClick = action,
        isCloseButton = true,
        isWindowMaximized = isMaximized
    ) { color ->
        CloseIcon(color)
    }
}

// Hover states, Background colors, Clicks, and Shapes
@Composable
private fun WindowBaseButton(
    onClick: () -> Unit,
    isCloseButton: Boolean = false,
    isWindowMaximized: Boolean = false,
    iconContent: @Composable (iconColor: Color) -> Unit
) {
    val appConfigs = LocalAppConfig.current
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val buttonShape = if (isCloseButton && !isWindowMaximized) {
        RoundedCornerShape(topEnd = appConfigs.theme.radius)
    } else {
        RectangleShape
    }

    val backgroundColor = when {
        isCloseButton && isHovered -> Color(0xFFE81123) // Windows Red
        isHovered -> appConfigs.theme.textClr.copy(alpha = 0.05f)
        else -> Color.Transparent
    }

    val iconColor = if (isCloseButton && isHovered) Color.White else appConfigs.theme.textClr

    Box(
        modifier = Modifier
            .width(46.dp)
            .fillMaxHeight()
            .clip(buttonShape)
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null, // We handle the color change manually via 'backgroundColor'
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // A fixed size box = consistent scaling
        Box(Modifier.size(10.dp)) {
            iconContent(iconColor)
        }
    }
}

// How the buttons look
@Composable
private fun MinimizeIcon(color: Color) {
    Canvas(Modifier.fillMaxSize()) {
        drawLine(
            color = color,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = 1.5f
        )
    }
}

@Composable
private fun MaximizeIcon(color: Color) {
    Canvas(Modifier.fillMaxSize()) {
        drawRoundRect(
            color = color,
            topLeft = Offset.Zero,
            size = size,
            cornerRadius = CornerRadius(2.dp.toPx()),
            style = Stroke(width = 1.5f)
        )
    }
}

@Composable
private fun RestoreIcon(color: Color) {
    val appConfigs = LocalAppConfig.current

    Canvas(Modifier.fillMaxSize()) {
        val strokeWidth = 1.5f
        val cornerRadius = CornerRadius(2.dp.toPx())
        val iconSize = size.width * 0.70f
        val offsetDist = size.width * 0.30f

        // back square
        drawRoundRect(
            color = color,
            topLeft = Offset(offsetDist, 0f),
            size = Size(iconSize, iconSize),
            cornerRadius = cornerRadius,
            style = Stroke(width = strokeWidth)
        )
        // eraser (the "Punch out" mask)
        drawRoundRect(
            color = appConfigs.theme.panelBgClr, // Reads from config
            topLeft = Offset(0f, offsetDist),
            size = Size(iconSize, iconSize),
            cornerRadius = cornerRadius
        )
        // front square
        drawRoundRect(
            color = color,
            topLeft = Offset(0f, offsetDist),
            size = Size(iconSize, iconSize),
            cornerRadius = cornerRadius,
            style = Stroke(width = strokeWidth)
        )
    }
}

@Composable
private fun CloseIcon(color: Color) {
    Canvas(Modifier.fillMaxSize()) {
        val stroke = 1.5f
        drawLine(color = color, start = Offset.Zero, end = Offset(size.width, size.height), strokeWidth = stroke)
        drawLine(color = color, start = Offset(0f, size.height), end = Offset(size.width, 0f), strokeWidth = stroke)
    }
}