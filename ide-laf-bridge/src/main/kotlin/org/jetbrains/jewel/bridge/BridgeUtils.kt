package org.jetbrains.jewel.bridge

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Typeface
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.intellij.openapi.diagnostic.Logger
import com.intellij.ui.JBColor
import com.intellij.ui.scale.JBUIScale.scale
import com.intellij.util.ui.JBDimension
import com.intellij.util.ui.JBInsets
import com.intellij.util.ui.JBValue
import org.jetbrains.jewel.IntelliJThemeIconData
import org.jetbrains.jewel.InteractiveComponentState
import org.jetbrains.jewel.SvgLoader
import org.jetbrains.jewel.styling.PainterProvider
import org.jetbrains.skiko.DependsOnJBR
import org.jetbrains.skiko.awt.font.AwtFontManager
import org.jetbrains.skiko.toSkikoTypefaceOrNull
import java.awt.Dimension
import java.awt.Insets
import javax.swing.UIManager

private val logger = Logger.getInstance("JewelBridge")

fun java.awt.Color.toComposeColor() = Color(
    red = red,
    green = green,
    blue = blue,
    alpha = alpha,
)

fun java.awt.Color?.toComposeColorOrUnspecified() = this?.toComposeColor() ?: Color.Unspecified

@Suppress("UnstableApiUsage")
fun retrieveColorOrNull(key: String) =
    JBColor.namedColor(key)
        .takeUnless { it.name == "NAMED_COLOR_FALLBACK_MARKER" }
        ?.toComposeColor()

fun retrieveColorOrUnspecified(key: String): Color {
    val color = retrieveColorOrNull(key)
    if (color == null) {
        logger.warn("Color with key \"$key\" not found, fallback to 'Color.Unspecified'")
    }
    return color ?: Color.Unspecified
}

fun retrieveColorsOrUnspecified(vararg keys: String) = keys.map { retrieveColorOrUnspecified(it) }

fun List<Color>.createVerticalBrush(
    startY: Float = 0.0f,
    endY: Float = Float.POSITIVE_INFINITY,
    tileMode: TileMode = TileMode.Clamp,
): Brush {
    if (isEmpty()) return SolidColor(Color.Transparent)
    if (size == 1) return SolidColor(first())

    // Optimization: use a cheaper SolidColor if all colors are identical
    if (all { it == first() }) SolidColor(first())

    return Brush.verticalGradient(this, startY, endY, tileMode)
}

fun retrieveIntAsDp(key: String): Dp {
    val rawValue = UIManager.get(key)
    if (rawValue is Int) rawValue.dp

    keyNotFound(key, "Int")
}

fun retrieveIntAsDpOrUnspecified(key: String) =
    try {
        retrieveIntAsDp(key)
    } catch (ignored: JewelBridgeException) {
        Dp.Unspecified
    }

fun retrieveInsetsAsPaddingValues(key: String) =
    UIManager.getInsets(key)?.toPaddingValues()
        ?: keyNotFound(key, "Insets")

/**
 * Converts a [Insets] to [PaddingValues]. If the receiver is a [JBInsets]
 * instance, this function delegates to the specific [toPaddingValues] for
 * it, which is scaling-aware.
 */
fun Insets.toPaddingValues() =
    if (this is JBInsets) {
        toPaddingValues()
    } else {
        PaddingValues(left.dp, top.dp, right.dp, bottom.dp)
    }

/**
 * Converts a [JBInsets] to [PaddingValues], in a scaling-aware way. This
 * means that the resulting [PaddingValues] will be constructed from the
 * [JBInsets.unscaled] values, treated as [Dp]. This avoids double scaling.
 */
fun JBInsets.toPaddingValues() =
    PaddingValues(unscaled.left.dp, unscaled.top.dp, unscaled.right.dp, unscaled.bottom.dp)

/**
 * Converts a [Dimension] to [DpSize]. If the receiver is a [JBDimension]
 * instance, this function delegates to the specific [toDpSize] for it,
 * which is scaling-aware.
 */
fun Dimension.toDpSize() = DpSize(width.dp, height.dp)

/**
 * Converts a [JBDimension] to [DpSize], in a scaling-aware way. This means
 * that the resulting [DpSize] will be constructed by first obtaining the
 * unscaled values. This avoids double scaling.
 */
fun JBDimension.toDpSize(): DpSize {
    val scaleFactor = scale(1f)
    return DpSize((width2d() / scaleFactor).dp, (height2d() / scaleFactor).dp)
}

fun retrieveArcAsCornerSize(key: String) =
    CornerSize(retrieveIntAsDp(key) / 2)

fun retrieveArcAsCornerSizeWithFallbacks(vararg keys: String): CornerSize {
    for (key in keys) {
        val rawValue = UIManager.get(key)
        if (rawValue is Int) {
            val cornerSize = rawValue.dp

            // Swing uses arcs, which are a diameter length, but we need a radius
            return CornerSize(cornerSize / 2)
        }
    }

    keysNotFound(keys.toList(), "Int")
}

@OptIn(DependsOnJBR::class)
private val awtFontManager = AwtFontManager()

@DependsOnJBR
suspend fun retrieveTextStyle(fontKey: String, colorKey: String? = null): TextStyle {
    val baseColor = colorKey?.let { retrieveColorOrUnspecified(colorKey) } ?: Color.Unspecified
    return retrieveTextStyle(fontKey, color = baseColor)
}

@DependsOnJBR
suspend fun retrieveTextStyle(
    key: String,
    color: Color = Color.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified,
): TextStyle {
    val font = UIManager.getFont(key) ?: keyNotFound(key, "Font")

    return with(font) {
        val typeface = toSkikoTypefaceOrNull(awtFontManager)
            ?: org.jetbrains.skia.Typeface.makeDefault()
                .also {
                    logger.warn(
                        "Unable to convert font ${font.fontName} into a Skiko typeface, " +
                            "fallback to 'Typeface.makeDefault()'",
                    )
                }

        TextStyle(
            color = color,
            fontSize = size.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily(Typeface(typeface)),
            // todo textDecoration might be defined in the awt theme
            lineHeight = lineHeight,
        )
    }
}

val JBValue.dp
    get() = unscaled.dp

internal operator fun TextUnit.minus(delta: Float) = plus(-delta)

internal operator fun TextUnit.plus(delta: Float) =
    when {
        isSp -> TextUnit(value + delta, type)
        isEm -> TextUnit(value + delta, type)
        else -> this
    }

fun <T : InteractiveComponentState> retrieveStatefulIcon(
    iconPath: String,
    svgLoader: SvgLoader,
    iconData: IntelliJThemeIconData,
    prefixTokensProvider: (state: T) -> String = { "" },
    suffixTokensProvider: (state: T) -> String = { "" },
): PainterProvider<T> =
    BridgeResourcePainterProvider.stateful(
        iconPath = iconPath,
        svgLoader,
        iconData,
        prefixTokensProvider,
        suffixTokensProvider,
    )

fun retrieveStatelessIcon(
    iconPath: String,
    svgLoader: SvgLoader,
    iconData: IntelliJThemeIconData,
): PainterProvider<Unit> =
    BridgeResourcePainterProvider.stateless(iconPath, svgLoader, iconData)
