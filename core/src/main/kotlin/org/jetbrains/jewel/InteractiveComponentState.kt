package org.jetbrains.jewel

import androidx.compose.runtime.Stable

interface InteractiveComponentState {

    @Stable
    val isActive: Boolean

    @Stable
    val isEnabled: Boolean

    @Stable
    val isHovered: Boolean

    @Stable
    val isPressed: Boolean
}
