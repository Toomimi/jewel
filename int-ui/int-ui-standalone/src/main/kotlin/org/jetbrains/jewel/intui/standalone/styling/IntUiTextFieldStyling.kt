package org.jetbrains.jewel.intui.standalone.styling

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.intui.core.theme.IntUiDarkTheme
import org.jetbrains.jewel.intui.core.theme.IntUiLightTheme
import org.jetbrains.jewel.intui.standalone.IntUiTheme
import org.jetbrains.jewel.styling.InputFieldMetrics
import org.jetbrains.jewel.styling.TextFieldColors
import org.jetbrains.jewel.styling.TextFieldStyle

@Stable
data class IntUiTextFieldStyle(
    override val colors: IntUiTextFieldColors,
    override val metrics: IntUiTextFieldMetrics,
    override val textStyle: TextStyle,
) : TextFieldStyle {

    companion object {

        @Composable
        fun light(
            colors: IntUiTextFieldColors = IntUiTextFieldColors.light(),
            metrics: IntUiTextFieldMetrics = IntUiTextFieldMetrics(),
            textStyle: TextStyle = IntUiTheme.defaultLightTextStyle,
        ) = IntUiTextFieldStyle(colors, metrics, textStyle)

        @Composable
        fun dark(
            colors: IntUiTextFieldColors = IntUiTextFieldColors.dark(),
            metrics: IntUiTextFieldMetrics = IntUiTextFieldMetrics(),
            textStyle: TextStyle = IntUiTheme.defaultDarkTextStyle,
        ) = IntUiTextFieldStyle(colors, metrics, textStyle)
    }
}

@Immutable
data class IntUiTextFieldColors(
    override val background: Color,
    override val backgroundDisabled: Color,
    override val backgroundFocused: Color,
    override val backgroundPressed: Color,
    override val backgroundHovered: Color,
    override val content: Color,
    override val contentDisabled: Color,
    override val contentFocused: Color,
    override val contentPressed: Color,
    override val contentHovered: Color,
    override val border: Color,
    override val borderDisabled: Color,
    override val borderFocused: Color,
    override val borderPressed: Color,
    override val borderHovered: Color,
    override val caret: Color,
    override val caretDisabled: Color,
    override val caretFocused: Color,
    override val caretPressed: Color,
    override val caretHovered: Color,
    override val placeholder: Color,
) : TextFieldColors {

    companion object {

        @Composable
        fun light(
            background: Color = IntUiLightTheme.colors.grey(14),
            backgroundDisabled: Color = IntUiLightTheme.colors.grey(13),
            backgroundFocused: Color = background,
            backgroundPressed: Color = background,
            backgroundHovered: Color = background,
            content: Color = IntUiLightTheme.colors.grey(1),
            contentDisabled: Color = IntUiLightTheme.colors.grey(8),
            contentFocused: Color = content,
            contentPressed: Color = content,
            contentHovered: Color = content,
            border: Color = IntUiLightTheme.colors.grey(9),
            borderDisabled: Color = IntUiLightTheme.colors.grey(11),
            borderFocused: Color = IntUiLightTheme.colors.blue(4),
            borderPressed: Color = border,
            borderHovered: Color = border,
            caret: Color = IntUiLightTheme.colors.grey(1),
            caretDisabled: Color = caret,
            caretFocused: Color = caret,
            caretPressed: Color = caret,
            caretHovered: Color = caret,
            placeholder: Color = IntUiLightTheme.colors.grey(8),
        ) = IntUiTextFieldColors(
            background,
            backgroundDisabled,
            backgroundFocused,
            backgroundPressed,
            backgroundHovered,
            content,
            contentDisabled,
            contentFocused,
            contentPressed,
            contentHovered,
            border,
            borderDisabled,
            borderFocused,
            borderPressed,
            borderHovered,
            caret,
            caretDisabled,
            caretFocused,
            caretPressed,
            caretHovered,
            placeholder,
        )

        @Composable
        fun dark(
            background: Color = IntUiDarkTheme.colors.grey(2),
            backgroundDisabled: Color = background,
            backgroundFocused: Color = background,
            backgroundPressed: Color = background,
            backgroundHovered: Color = background,
            content: Color = IntUiDarkTheme.colors.grey(12),
            contentDisabled: Color = IntUiDarkTheme.colors.grey(7),
            contentFocused: Color = content,
            contentPressed: Color = content,
            contentHovered: Color = content,
            border: Color = IntUiDarkTheme.colors.grey(5),
            borderDisabled: Color = border,
            borderFocused: Color = IntUiDarkTheme.colors.blue(6),
            borderPressed: Color = border,
            borderHovered: Color = border,
            caret: Color = IntUiDarkTheme.colors.grey(12),
            caretDisabled: Color = caret,
            caretFocused: Color = caret,
            caretPressed: Color = caret,
            caretHovered: Color = caret,
            placeholder: Color = IntUiDarkTheme.colors.grey(7),
        ) = IntUiTextFieldColors(
            background,
            backgroundDisabled,
            backgroundFocused,
            backgroundPressed,
            backgroundHovered,
            content,
            contentDisabled,
            contentFocused,
            contentPressed,
            contentHovered,
            border,
            borderDisabled,
            borderFocused,
            borderPressed,
            borderHovered,
            caret,
            caretDisabled,
            caretFocused,
            caretPressed,
            caretHovered,
            placeholder,
        )
    }
}

@Stable
data class IntUiTextFieldMetrics(
    override val cornerSize: CornerSize = CornerSize(4.dp),
    override val contentPadding: PaddingValues = PaddingValues(horizontal = 9.dp, vertical = 6.dp),
    override val minSize: DpSize = DpSize(144.dp, 28.dp),
    override val borderWidth: Dp = 1.dp,
) : InputFieldMetrics
