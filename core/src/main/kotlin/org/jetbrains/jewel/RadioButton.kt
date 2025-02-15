package org.jetbrains.jewel

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.res.ResourceLoader
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import org.jetbrains.jewel.CommonStateBitMask.Active
import org.jetbrains.jewel.CommonStateBitMask.Enabled
import org.jetbrains.jewel.CommonStateBitMask.Focused
import org.jetbrains.jewel.CommonStateBitMask.Hovered
import org.jetbrains.jewel.CommonStateBitMask.Pressed
import org.jetbrains.jewel.foundation.Stroke
import org.jetbrains.jewel.styling.RadioButtonStyle

@Composable
fun RadioButton(
    selected: Boolean,
    resourceLoader: ResourceLoader,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    outline: Outline = Outline.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    style: RadioButtonStyle = IntelliJTheme.radioButtonStyle,
    textStyle: TextStyle = IntelliJTheme.defaultTextStyle,
) {
    RadioButtonImpl(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        outline = outline,
        resourceLoader = resourceLoader,
        interactionSource = interactionSource,
        style = style,
        textStyle = textStyle,
        content = null,
    )
}

@Composable
fun RadioButtonRow(
    text: String,
    selected: Boolean,
    resourceLoader: ResourceLoader,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    outline: Outline = Outline.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    style: RadioButtonStyle = IntelliJTheme.radioButtonStyle,
    textStyle: TextStyle = IntelliJTheme.defaultTextStyle,
) {
    RadioButtonImpl(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        outline = outline,
        resourceLoader = resourceLoader,
        interactionSource = interactionSource,
        style = style,
        textStyle = textStyle,
    ) {
        Text(text)
    }
}

@Composable
fun RadioButtonRow(
    selected: Boolean,
    resourceLoader: ResourceLoader,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    outline: Outline = Outline.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    style: RadioButtonStyle = IntelliJTheme.radioButtonStyle,
    textStyle: TextStyle = IntelliJTheme.defaultTextStyle,
    content: @Composable RowScope.() -> Unit,
) {
    RadioButtonImpl(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        outline = outline,
        interactionSource = interactionSource,
        style = style,
        textStyle = textStyle,
        resourceLoader = resourceLoader,
        content = content,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun RadioButtonImpl(
    selected: Boolean,
    resourceLoader: ResourceLoader,
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    outline: Outline,
    interactionSource: MutableInteractionSource,
    style: RadioButtonStyle,
    textStyle: TextStyle,
    content: (@Composable RowScope.() -> Unit)?,
) {
    var radioButtonState by remember(interactionSource) {
        mutableStateOf(RadioButtonState.of(selected = selected, enabled = enabled))
    }
    remember(selected, enabled) {
        radioButtonState = radioButtonState.copy(selected = selected, enabled = enabled)
    }
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> radioButtonState = radioButtonState.copy(pressed = true)
                is PressInteraction.Cancel, is PressInteraction.Release ->
                    radioButtonState =
                        radioButtonState.copy(pressed = false)

                is HoverInteraction.Enter -> radioButtonState = radioButtonState.copy(hovered = true)
                is HoverInteraction.Exit -> radioButtonState = radioButtonState.copy(hovered = false)
                is FocusInteraction.Focus -> radioButtonState = radioButtonState.copy(focused = true)
                is FocusInteraction.Unfocus -> radioButtonState = radioButtonState.copy(focused = false)
            }
        }
    }

    val wrapperModifier = modifier.selectable(
        selected = selected,
        onClick = onClick,
        enabled = enabled,
        role = Role.RadioButton,
        interactionSource = interactionSource,
        indication = null,
    )

    val colors = style.colors
    val metrics = style.metrics
    val radioButtonModifier = Modifier
        .size(metrics.radioButtonSize)
        .outline(radioButtonState, outline, outlineShape = CircleShape, alignment = Stroke.Alignment.Inside)
    val radioButtonPainter by style.icons.radioButton.getPainter(resourceLoader, radioButtonState)

    if (content == null) {
        RadioButtonImage(wrapperModifier, radioButtonPainter, radioButtonModifier)
    } else {
        Row(
            wrapperModifier,
            horizontalArrangement = Arrangement.spacedBy(metrics.iconContentGap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButtonImage(Modifier, radioButtonPainter, radioButtonModifier)

            val contentColor by colors.contentFor(radioButtonState)
            CompositionLocalProvider(
                LocalTextStyle provides textStyle.copy(color = contentColor.takeOrElse { textStyle.color }),
                LocalContentColor provides contentColor.takeOrElse { textStyle.color },
            ) {
                content()
            }
        }
    }
}

@Composable
private fun RadioButtonImage(outerModifier: Modifier, radioButtonPainter: Painter, radioButtonModifier: Modifier) {
    // TODO tint icon painter
    Box(outerModifier) {
        Image(radioButtonPainter, contentDescription = null, modifier = radioButtonModifier)
    }
}

@Immutable
@JvmInline
value class RadioButtonState(val state: ULong) : SelectableComponentState {

    @Stable
    override val isActive: Boolean
        get() = state and Active != 0UL

    @Stable
    override val isSelected: Boolean
        get() = state and Selected != 0UL

    @Stable
    override val isEnabled: Boolean
        get() = state and Enabled != 0UL

    @Stable
    override val isFocused: Boolean
        get() = state and Focused != 0UL

    @Stable
    override val isHovered: Boolean
        get() = state and Hovered != 0UL

    @Stable
    override val isPressed: Boolean
        get() = state and Pressed != 0UL

    fun copy(
        selected: Boolean = isSelected,
        enabled: Boolean = isEnabled,
        focused: Boolean = isFocused,
        pressed: Boolean = isPressed,
        hovered: Boolean = isHovered,
        active: Boolean = isActive,
    ) = of(
        selected = selected,
        enabled = enabled,
        focused = focused,
        pressed = pressed,
        hovered = hovered,
        active = active,
    )

    override fun toString() =
        "${javaClass.simpleName}(isSelected=$isSelected, isEnabled=$isEnabled, isFocused=$isFocused, " +
            "isHovered=$isHovered, isPressed=$isPressed, isActive=$isActive)"

    companion object {

        private const val SELECTED_BIT_OFFSET = CommonStateBitMask.FIRST_AVAILABLE_OFFSET

        private val Selected = 1UL shl SELECTED_BIT_OFFSET

        fun of(
            selected: Boolean,
            enabled: Boolean = true,
            focused: Boolean = false,
            pressed: Boolean = false,
            hovered: Boolean = false,
            active: Boolean = false,
        ) = RadioButtonState(
            (if (selected) Selected else 0UL) or
                (if (enabled) Enabled else 0UL) or
                (if (focused) Focused else 0UL) or
                (if (pressed) Pressed else 0UL) or
                (if (hovered) Hovered else 0UL) or
                (if (active) Active else 0UL),
        )
    }
}
