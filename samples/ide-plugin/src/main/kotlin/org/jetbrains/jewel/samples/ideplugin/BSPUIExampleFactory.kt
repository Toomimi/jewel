package org.jetbrains.jewel.samples.ideplugin

import com.intellij.openapi.application.EDT
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.ContentFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BSPUIExampleFactory : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val panel = SimpleToolWindowPanel(true, true)
        toolWindow.contentManager.removeAllContents(true)
        toolWindow.contentManager.addContent(ContentFactory.getInstance().createContent(panel.component, "", false))
        toolWindow.show()
    }
}

suspend fun registerToolWindow(project: Project) {
    val toolWindowManager = ToolWindowManager.getInstance(project)
    val currentToolWindow = toolWindowManager.getToolWindow("BSP")
    if (currentToolWindow == null) {
        withContext(Dispatchers.EDT) {
            toolWindowManager.registerToolWindow("BSP") {
                this.anchor = ToolWindowAnchor.RIGHT
                this.canCloseContent = false
                this.contentFactory = BSPUIExampleFactory()
            }
        }
    }
}
