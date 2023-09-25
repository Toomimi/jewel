package org.jetbrains.jewel.samples.ideplugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class Startup : ProjectActivity {
    override suspend fun execute(project: Project) {
        registerToolWindow(project)
    }
}
