package org.jetbrains.jewel.intui.standalone.styling

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.LinkState
import org.jetbrains.jewel.SvgLoader
import org.jetbrains.jewel.intui.core.theme.IntUiDarkTheme
import org.jetbrains.jewel.intui.core.theme.IntUiLightTheme
import org.jetbrains.jewel.intui.standalone.IntUiTheme
import org.jetbrains.jewel.styling.LinkColors
import org.jetbrains.jewel.styling.LinkIcons
import org.jetbrains.jewel.styling.LinkMetrics
import org.jetbrains.jewel.styling.LinkStyle
import org.jetbrains.jewel.styling.LinkTextStyles
import org.jetbrains.jewel.styling.PainterProvider
import org.jetbrains.jewel.styling.ResourcePainterProvider

@Immutable
data class IntUiLinkStyle(
    override val colors: IntUiLinkColors,
    override val metrics: IntUiLinkMetrics,
    override val icons: IntUiLinkIcons,
    override val textStyles: IntUiLinkTextStyles,
) : LinkStyle {

    companion object {

        @Composable
        fun light(
            svgLoader: SvgLoader,
            colors: IntUiLinkColors = IntUiLinkColors.light(),
            metrics: IntUiLinkMetrics = IntUiLinkMetrics(),
            icons: IntUiLinkIcons = intUiLinkIcons(svgLoader),
            textStyles: IntUiLinkTextStyles = IntUiLinkTextStyles.light(),
        ) = IntUiLinkStyle(colors, metrics, icons, textStyles)

        @Composable
        fun dark(
            svgLoader: SvgLoader,
            colors: IntUiLinkColors = IntUiLinkColors.dark(),
            metrics: IntUiLinkMetrics = IntUiLinkMetrics(),
            icons: IntUiLinkIcons = intUiLinkIcons(svgLoader),
            textStyles: IntUiLinkTextStyles = IntUiLinkTextStyles.dark(),
        ) = IntUiLinkStyle(colors, metrics, icons, textStyles)
    }
}

@Immutable
data class IntUiLinkColors(
    override val content: Color,
    override val contentDisabled: Color,
    override val contentFocused: Color,
    override val contentPressed: Color,
    override val contentHovered: Color,
    override val contentVisited: Color,
) : LinkColors {

    companion object {

        @Composable
        fun light(
            content: Color = IntUiLightTheme.colors.blue(2),
            contentDisabled: Color = IntUiLightTheme.colors.grey(8),
            contentFocused: Color = content,
            contentPressed: Color = content,
            contentHovered: Color = content,
            contentVisited: Color = content,
        ) = IntUiLinkColors(
            content,
            contentDisabled,
            contentFocused,
            contentPressed,
            contentHovered,
            contentVisited,
        )

        @Composable
        fun dark(
            content: Color = IntUiDarkTheme.colors.blue(9),
            contentDisabled: Color = IntUiDarkTheme.colors.grey(7),
            contentFocused: Color = content,
            contentPressed: Color = content,
            contentHovered: Color = content,
            contentVisited: Color = content,
        ) = IntUiLinkColors(
            content,
            contentDisabled,
            contentFocused,
            contentPressed,
            contentHovered,
            contentVisited,
        )
    }
}

@Immutable
data class IntUiLinkMetrics(
    override val focusHaloCornerSize: CornerSize = CornerSize(2.dp),
    override val textIconGap: Dp = 0.dp,
    override val iconSize: DpSize = DpSize(16.dp, 16.dp),
) : LinkMetrics

@Immutable
data class IntUiLinkIcons(
    override val dropdownChevron: PainterProvider<LinkState>,
    override val externalLink: PainterProvider<LinkState>,
) : LinkIcons {

    companion object {

        @Composable
        fun dropdownChevron(
            svgLoader: SvgLoader,
            basePath: String = "icons/intui/chevronDown.svg",
        ): PainterProvider<LinkState> =
            ResourcePainterProvider.stateful(basePath, svgLoader)

        @Composable
        fun externalLink(
            svgLoader: SvgLoader,
            basePath: String = "icons/intui/externalLink.svg",
        ): PainterProvider<LinkState> =
            ResourcePainterProvider.stateful(basePath, svgLoader)
    }
}

@Composable
fun intUiLinkIcons(
    svgLoader: SvgLoader,
    dropdownChevron: PainterProvider<LinkState> = IntUiLinkIcons.dropdownChevron(svgLoader),
    externalLink: PainterProvider<LinkState> = IntUiLinkIcons.externalLink(svgLoader),
) = IntUiLinkIcons(dropdownChevron, externalLink)

@Immutable
data class IntUiLinkTextStyles(
    override val normal: TextStyle,
    override val disabled: TextStyle,
    override val focused: TextStyle,
    override val pressed: TextStyle,
    override val hovered: TextStyle,
    override val visited: TextStyle,
) : LinkTextStyles {

    companion object {

        @Composable
        fun light(
            normal: TextStyle = IntUiTheme.defaultLightTextStyle.copy(textDecoration = TextDecoration.Underline),
            disabled: TextStyle = IntUiTheme.defaultLightTextStyle,
            focused: TextStyle = normal,
            pressed: TextStyle = normal,
            hovered: TextStyle = normal,
            visited: TextStyle = normal,
        ): IntUiLinkTextStyles = IntUiLinkTextStyles(normal, disabled, focused, pressed, hovered, visited)

        @Composable
        fun dark(
            normal: TextStyle = IntUiTheme.defaultDarkTextStyle.copy(textDecoration = TextDecoration.Underline),
            disabled: TextStyle = IntUiTheme.defaultDarkTextStyle,
            focused: TextStyle = normal,
            pressed: TextStyle = normal,
            hovered: TextStyle = normal,
            visited: TextStyle = normal,
        ): IntUiLinkTextStyles = IntUiLinkTextStyles(normal, disabled, focused, pressed, hovered, visited)
    }
}
