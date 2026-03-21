package com.ktoda.compoundview.platform

import androidx.compose.ui.graphics.toArgb
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinDef.HWND
import com.sun.jna.ptr.IntByReference
import com.sun.jna.win32.StdCallLibrary
import com.sun.jna.win32.W32APIOptions
import java.awt.Color as AwtColor
import java.awt.Component
import java.awt.Container
import java.awt.Window
import java.awt.event.ContainerAdapter
import java.awt.event.ContainerEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.Collections
import java.util.WeakHashMap
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.RootPaneContainer
import javax.swing.SwingUtilities
import javax.swing.UIManager

internal class WindowsHostWindowStyler(
    private val window: Window
) : HostWindowStyler {
    private var lastAppliedTheme: HostWindowTheme? = null
    private var lastAppliedBackground: AwtColor? = null
    private val observedContainers = Collections.newSetFromMap(WeakHashMap<Container, Boolean>())
    private val backgroundSyncListener = object : ContainerAdapter() {
        override fun componentAdded(event: ContainerEvent) {
            val backgroundColor = lastAppliedBackground ?: return
            applyBackgroundRecursively(event.child, backgroundColor)
            (event.child as? Container)?.let(::installBackgroundSync)
        }
    }

    init {
        window.addWindowListener(object : WindowAdapter() {
            override fun windowOpened(event: WindowEvent) {
                lastAppliedTheme?.let(::applyNow)
            }
        })
    }

    override fun apply(theme: HostWindowTheme) {
        lastAppliedTheme = theme
        SwingUtilities.invokeLater {
            applyNow(theme)
        }
    }

    private fun applyNow(theme: HostWindowTheme) {
        val awtColor = AwtColor(theme.backgroundColor.toArgb(), true)
        lastAppliedBackground = awtColor

        applySwingDefaults(awtColor)
        SwingUtilities.updateComponentTreeUI(window)
        installBackgroundSync(window)
        applyRootPaneBackground(window, awtColor)
        applyBackgroundRecursively(window, awtColor)

        val hwnd = window.hwndOrNull()
        if (hwnd != null) {
            DwmApi.INSTANCE.setWindowAttribute(
                hwnd = hwnd,
                attribute = DWMWA_USE_IMMERSIVE_DARK_MODE,
                value = theme.isDarkTheme
            ) || DwmApi.INSTANCE.setWindowAttribute(
                hwnd = hwnd,
                attribute = DWMWA_USE_IMMERSIVE_DARK_MODE_BEFORE_20H1,
                value = theme.isDarkTheme
            )
        }

        window.invalidate()
        window.repaint()
    }

    private fun applySwingDefaults(backgroundColor: AwtColor) {
        UIManager.put("control", backgroundColor)
        UIManager.put("Panel.background", backgroundColor)
        UIManager.put("window", backgroundColor)
        UIManager.put("Viewport.background", backgroundColor)
    }

    private fun installBackgroundSync(container: Container) {
        if (!observedContainers.add(container)) {
            return
        }

        container.addContainerListener(backgroundSyncListener)
        container.components.filterIsInstance<Container>().forEach(::installBackgroundSync)
    }

    private fun applyBackgroundRecursively(
        component: Component,
        backgroundColor: AwtColor
    ) {
        component.background = backgroundColor

        (component as? JComponent)
            ?.takeIf { it.javaClass.name.startsWith("androidx.compose.ui.") }
            ?.isOpaque = true

        if (component is RootPaneContainer) {
            component.rootPane.background = backgroundColor
            component.rootPane.isOpaque = true
            component.layeredPane.background = backgroundColor
            component.contentPane.background = backgroundColor
            (component.contentPane as? JComponent)?.isOpaque = true
        }

        if (component is Container) {
            component.components.forEach { child ->
                applyBackgroundRecursively(child, backgroundColor)
            }
        }
    }

    private fun applyRootPaneBackground(
        window: Window,
        backgroundColor: AwtColor
    ) {
        val rootPaneContainer = window as? RootPaneContainer ?: return
        val contentPane = rootPaneContainer.contentPane

        rootPaneContainer.rootPane.background = backgroundColor
        rootPaneContainer.rootPane.isOpaque = true
        rootPaneContainer.layeredPane.background = backgroundColor
        contentPane.background = backgroundColor
        (contentPane as? JComponent)?.isOpaque = true

        // ComposeWindow uses an opaque JPanel under the layered pane as the real backing surface.
        (rootPaneContainer.layeredPane.components.firstOrNull { child ->
            child is JPanel && child.isOpaque
        } as? JPanel)?.apply {
            background = backgroundColor
            isOpaque = true
        }
    }
}

private fun Window.hwndOrNull(): HWND? {
    if (!isDisplayable) {
        return null
    }

    val pointer = Native.getComponentPointer(this)
    if (Pointer.nativeValue(pointer) == 0L) {
        return null
    }

    return HWND().apply {
        setPointer(pointer)
    }
}

private interface DwmApi : StdCallLibrary {
    fun DwmSetWindowAttribute(
        hwnd: HWND,
        dwAttribute: Int,
        pvAttribute: Pointer,
        cbAttribute: Int
    ): Int

    companion object {
        val INSTANCE: DwmApi = Native.load(
            "dwmapi",
            DwmApi::class.java,
            W32APIOptions.DEFAULT_OPTIONS
        )
    }
}

private fun DwmApi.setWindowAttribute(
    hwnd: HWND,
    attribute: Int,
    value: Boolean
): Boolean {
    val attributeValue = IntByReference(if (value) 1 else 0)
    return DwmSetWindowAttribute(
        hwnd,
        attribute,
        attributeValue.pointer,
        Int.SIZE_BYTES
    ) == S_OK
}

private const val DWMWA_USE_IMMERSIVE_DARK_MODE_BEFORE_20H1 = 19
private const val DWMWA_USE_IMMERSIVE_DARK_MODE = 20
private const val S_OK = 0
