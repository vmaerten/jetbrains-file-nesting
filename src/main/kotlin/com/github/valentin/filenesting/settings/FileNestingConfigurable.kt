package com.github.valentin.filenesting.settings

import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel

/**
 * Settings UI for the File Nesting plugin.
 *
 * Provides a simple toggle to enable/disable file nesting in the Project View.
 */
class FileNestingConfigurable : BoundConfigurable("File Nesting") {

    private val settings = FileNestingSettings.getInstance()

    override fun createPanel(): DialogPanel = panel {
        group("File Nesting") {
            row {
                checkBox("Enable file nesting in Project View")
                    .bindSelected(settings.state::enabled)
                    .comment("Group related configuration files under their parent files (e.g., .eslintrc under package.json)")
            }
        }

        group("About") {
            row {
                text(
                    """
                    This plugin provides VS Code-like file nesting for JetBrains IDEs.
                    <br><br>
                    Patterns are based on <a href="https://github.com/antfu/vscode-file-nesting-config">antfu/vscode-file-nesting-config</a>.
                    """.trimIndent()
                )
            }
        }

        group("Examples") {
            row {
                text(
                    """
                    <b>package.json</b> will group: .eslintrc, tsconfig.json, .prettierrc, lock files, etc.<br>
                    <b>README</b> will group: LICENSE, CHANGELOG, CONTRIBUTING, etc.<br>
                    <b>Dockerfile</b> will group: docker-compose files, .dockerignore, etc.<br>
                    <b>*.ts</b> will group: corresponding .js, .d.ts, .map files
                    """.trimIndent()
                )
            }
        }
    }

    override fun apply() {
        super.apply()
        // Refresh all open project views to apply the change immediately
        refreshAllProjectViews()
    }

    private fun refreshAllProjectViews() {
        for (project in ProjectManager.getInstance().openProjects) {
            if (!project.isDisposed) {
                ProjectView.getInstance(project).refresh()
            }
        }
    }
}
