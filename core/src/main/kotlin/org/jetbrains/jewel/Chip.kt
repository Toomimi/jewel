package org.jetbrains.jewel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.isUnspecified
import androidx.compose.ui.semantics.Role
import org.jetbrains.jewel.CommonStateBitMask.Active
import org.jetbrains.jewel.CommonStateBitMask.Enabled
import org.jetbrains.jewel.CommonStateBitMask.Focused
import org.jetbrains.jewel.CommonStateBitMask.Hovered
import org.jetbrains.jewel.CommonStateBitMask.Pressed
import org.jetbrains.jewel.CommonStateBitMask.Selected
import org.jetbrains.jewel.foundation.Stroke
import org.jetbrains.jewel.foundation.border
import org.jetbrains.jewel.styling.ChipStyle

@Composable
fun Chip(
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    enabled: Boolean = true,
    selected: Boolean = false,
    style: ChipStyle = IntelliJTheme.chipStyle,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    ChipImpl(
        interactionSource = interactionSource,
        enabled = enabled,
        selected = selected,
        style = style,
        modifier = modifier.clickable(
            onClick = onClick,
            enabled = enabled,
            role = Role.Button,
            interactionSource = interactionSource,
            indication = null,
        ),
        content = content,
    )
}

@Composable
fun ToggleableChip(
    checked: Boolean,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    enabled: Boolean = true,
    style: ChipStyle = IntelliJTheme.chipStyle,
    content: @Composable () -> Unit,
) {
    ChipImpl(
        interactionSource = interactionSource,
        enabled = enabled,
        selected = checked,
        style = style,
        modifier = modifier.toggleable(
            onValueChange = onClick,
            enabled = enabled,
            role = Role.Checkbox,
            interactionSource = interactionSource,
            indication = null,
            value = checked,
        ),
        content = content,
    )
}

@Composable
fun RadioButtonChip(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    enabled: Boolean = true,
    style: ChipStyle = IntelliJTheme.chipStyle,
    content: @Composable () -> Unit,
) {
    ChipImpl(
        interactionSource,
        enabled,
        selected,
        style,
        modifier.selectable(
            onClick = onClick,
            enabled = enabled,
            role = Role.RadioButton,
            interactionSource = interactionSource,
            indication = null,
            selected = selected,
        ),
        content,
    )
}

@Composable
private fun ChipImpl(
    interactionSource: MutableInteractionSource,
    enabled: Boolean,
    selected: Boolean,
    style: ChipStyle,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    var chipState by remember(interactionSource) {
        mutableStateOf(ChipState.of(enabled = enabled, selected = selected))
    }
    remember(enabled, selected) {
        chipState = chipState.copy(enabled = enabled, selected = selected)
    }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> chipState = chipState.copy(pressed = true)
                is PressInteraction.Cancel, is PressInteraction.Release -> chipState = chipState.copy(pressed = false)
                is HoverInteraction.Enter -> chipState = chipState.copy(hovered = true)
                is HoverInteraction.Exit -> chipState = chipState.copy(hovered = false)
                is FocusInteraction.Focus -> chipState = chipState.copy(focused = true)
                is FocusInteraction.Unfocus -> chipState = chipState.copy(focused = false)
            }
        }
    }

    val shape = RoundedCornerShape(style.metrics.cornerSize)
    val colors = style.colors
    val borderColor by colors.borderFor(chipState)

    Row(
        modifier = modifier
            .background(colors.backgroundFor(chipState).value, shape)
            .border(Stroke.Alignment.Center, style.metrics.borderWidth, borderColor, shape)
            .focusOutline(chipState, shape)
            .padding(style.metrics.padding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        CompositionLocalProvider(
            LocalContentColor provides (
                colors
                    .contentFor(state = chipState)
                    .value
                    .takeIf { !it.isUnspecified }
                    ?: LocalContentColor.current
                ),
        ) {
            content()
        }
    }
}

@Immutable
@JvmInline
value class ChipState(val state: ULong) : FocusableComponentState, SelectableComponentState {

    @Stable
    override val isActive: Boolean
        get() = state and Active != 0UL

    @Stable
    override val isEnabled: Boolean
        get() = state and Enabled != 0UL

    @Stable
    override val isFocused: Boolean
        get() = state and Focused != 0UL

    @Stable
    override val isSelected: Boolean
        get() = state and Selected != 0UL

    @Stable
    override val isHovered: Boolean
        get() = state and Hovered != 0UL

    @Stable
    override val isPressed: Boolean
        get() = state and Pressed != 0UL

    fun copy(
        enabled: Boolean = isEnabled,
        focused: Boolean = isFocused,
        selected: Boolean = isSelected,
        pressed: Boolean = isPressed,
        hovered: Boolean = isHovered,
        active: Boolean = isActive,
    ): ChipState = of(
        enabled = enabled,
        focused = focused,
        pressed = pressed,
        hovered = hovered,
        active = active,
        selected = selected,
    )

    override fun toString() =
        "ChipState(isEnabled=$isEnabled, isFocused=$isFocused, isSelected=$isSelected, " +
            "isHovered=$isHovered, isPressed=$isPressed, isActive=$isActive)"

    companion object {

        fun of(
            enabled: Boolean = true,
            focused: Boolean = false,
            selected: Boolean = false,
            pressed: Boolean = false,
            hovered: Boolean = false,
            active: Boolean = false,
        ) = ChipState(
            state = (if (enabled) Enabled else 0UL) or
                (if (focused) Focused else 0UL) or
                (if (selected) Selected else 0UL) or
                (if (hovered) Hovered else 0UL) or
                (if (pressed) Pressed else 0UL) or
                (if (active) Active else 0UL),
        )
    }
}
