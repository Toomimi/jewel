package org.jetbrains.jewel

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.input.InputMode

class MenuManager(
    val onDismissRequest: (InputMode) -> Boolean,
    private val parentMenuManager: MenuManager? = null,
) {

    private var isHovered: Boolean = false

    /**
     * Called when the hovered state of the menu changes.
     * This is used to abort parent menu closing in unforced mode
     * when submenu closed by click parent menu's item.
     *
     * @param hovered true if the menu is hovered, false otherwise.
     */
    internal fun onHoveredChange(hovered: Boolean) {
        isHovered = hovered
    }

    /**
     * Close all menus in the hierarchy.
     *
     * @param mode the input mode, menus close by pointer or keyboard event.
     * @param force true to force close all menus ignore parent hover state, false otherwise.
     */
    fun closeAll(mode: InputMode, force: Boolean) {
        // We ignore the pointer event if the menu is hovered in unforced mode.
        if (!force && mode == InputMode.Touch && isHovered) return

        if (onDismissRequest(mode)) {
            parentMenuManager?.closeAll(mode, force)
        }
    }

    fun close(mode: InputMode) = onDismissRequest(mode)

    fun isRootMenu(): Boolean = parentMenuManager == null

    fun isSubmenu(): Boolean = parentMenuManager != null

    fun submenuManager(onDismissRequest: (InputMode) -> Boolean) =
        MenuManager(onDismissRequest = onDismissRequest, parentMenuManager = this)
}

val LocalMenuManager = staticCompositionLocalOf<MenuManager> {
    error("No MenuManager provided")
}
