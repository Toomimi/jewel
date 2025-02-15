package org.jetbrains.jewel

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle

@Immutable
interface IntelliJThemeDefinition {

    val isDark: Boolean
    val globalColors: GlobalColors
    val globalMetrics: GlobalMetrics
    val defaultTextStyle: TextStyle

    val colorPalette: IntelliJThemeColorPalette
    val iconData: IntelliJThemeIconData
}
