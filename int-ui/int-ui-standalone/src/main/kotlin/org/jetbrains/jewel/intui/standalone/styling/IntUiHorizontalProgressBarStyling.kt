package org.jetbrains.jewel.intui.standalone.styling

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.intui.core.theme.IntUiDarkTheme
import org.jetbrains.jewel.intui.core.theme.IntUiLightTheme
import org.jetbrains.jewel.styling.HorizontalProgressBarColors
import org.jetbrains.jewel.styling.HorizontalProgressBarMetrics
import org.jetbrains.jewel.styling.HorizontalProgressBarStyle
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Immutable
data class IntUiHorizontalProgressBarStyle(
    override val colors: IntUiHorizontalProgressBarColors,
    override val metrics: IntUiHorizontalProgressBarMetrics,
    override val indeterminateCycleDuration: Duration,
) : HorizontalProgressBarStyle {

    companion object {

        @Composable
        fun light(
            colors: IntUiHorizontalProgressBarColors = IntUiHorizontalProgressBarColors.light(),
            metrics: IntUiHorizontalProgressBarMetrics = IntUiHorizontalProgressBarMetrics(),
            indeterminateCycleDuration: Duration = 800.milliseconds,
        ) = IntUiHorizontalProgressBarStyle(colors, metrics, indeterminateCycleDuration)

        @Composable
        fun dark(
            colors: IntUiHorizontalProgressBarColors = IntUiHorizontalProgressBarColors.dark(),
            metrics: IntUiHorizontalProgressBarMetrics = IntUiHorizontalProgressBarMetrics(),
            indeterminateCycleDuration: Duration = 800.milliseconds,
        ) = IntUiHorizontalProgressBarStyle(colors, metrics, indeterminateCycleDuration)
    }
}

@Immutable
data class IntUiHorizontalProgressBarColors(
    override val track: Color,
    override val progress: Color,
    override val indeterminateBase: Color,
    override val indeterminateHighlight: Color,
) : HorizontalProgressBarColors {

    companion object {

        @Composable
        fun light(
            track: Color = IntUiLightTheme.colors.grey(11),
            progress: Color = IntUiLightTheme.colors.blue(4),
            indeterminateBase: Color = IntUiLightTheme.colors.blue(9),
            indeterminateHighlight: Color = IntUiLightTheme.colors.blue(4),
        ) = IntUiHorizontalProgressBarColors(track, progress, indeterminateBase, indeterminateHighlight)

        @Composable
        fun dark(
            track: Color = IntUiDarkTheme.colors.grey(4),
            progress: Color = IntUiDarkTheme.colors.blue(7),
            indeterminateBase: Color = IntUiDarkTheme.colors.blue(9),
            indeterminateHighlight: Color = IntUiDarkTheme.colors.blue(5),
        ) = IntUiHorizontalProgressBarColors(track, progress, indeterminateBase, indeterminateHighlight)
    }
}

@Immutable
data class IntUiHorizontalProgressBarMetrics(
    override val cornerSize: CornerSize = CornerSize(100),
    override val minHeight: Dp = 4.dp,
    override val indeterminateHighlightWidth: Dp = 140.dp,
) : HorizontalProgressBarMetrics
