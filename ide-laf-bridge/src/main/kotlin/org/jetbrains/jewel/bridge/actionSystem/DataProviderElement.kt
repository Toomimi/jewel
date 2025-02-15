package org.jetbrains.jewel.bridge.actionSystem

import androidx.compose.ui.node.ModifierNodeElement

internal class DataProviderElement(val dataProvider: (dataId: String) -> Any?) : ModifierNodeElement<DataProviderNode>() {

    override fun create(): DataProviderNode = DataProviderNode(dataProvider)

    override fun update(node: DataProviderNode) {
        node.dataProvider = dataProvider
        node.updateParent()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DataProviderElement

        return dataProvider == other.dataProvider
    }

    override fun hashCode(): Int = dataProvider.hashCode()
}
