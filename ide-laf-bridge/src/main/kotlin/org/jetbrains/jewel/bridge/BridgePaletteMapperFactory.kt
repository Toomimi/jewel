package org.jetbrains.jewel.bridge

import com.intellij.ide.ui.UITheme
import com.intellij.openapi.diagnostic.thisLogger
import org.jetbrains.jewel.PaletteMapper
import org.jetbrains.jewel.themes.PaletteMapperFactory

object BridgePaletteMapperFactory : PaletteMapperFactory() {

    private val logger = thisLogger()

    fun create(isDark: Boolean): PaletteMapper {
        // If we can't read the current theme, no mapping is possible
        val uiTheme = currentUiThemeOrNull() ?: return PaletteMapper.Empty
        logger.info("Parsing theme info from theme ${uiTheme.name} (id: ${uiTheme.id}, isDark: ${uiTheme.isDark})")

        val iconColorPalette = uiTheme.iconColorPalette
        val keyPalette = UITheme.getColorPalette()
        val themeColors = uiTheme.colors.orEmpty()

        return createInternal(iconColorPalette, keyPalette, themeColors, isDark)
    }

    override fun logInfo(message: String) {
        logger.info(message)
    }
}
