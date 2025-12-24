package com.github.vmaerten.filenesting

import com.github.vmaerten.filenesting.patterns.DefaultPatterns
import com.github.vmaerten.filenesting.patterns.NestingPattern
import com.github.vmaerten.filenesting.patterns.PatternMatcher
import com.github.vmaerten.filenesting.settings.FileNestingSettings
import com.intellij.ide.projectView.TreeStructureProvider
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode
import com.intellij.ide.projectView.impl.nodes.PsiFileNode
import com.intellij.ide.util.treeView.AbstractTreeNode

/**
 * A [TreeStructureProvider] that groups related files under their parent files
 * in the Project View.
 *
 * This provider modifies the tree structure to nest configuration files and
 * related files under their "parent" files (e.g., eslint config files under package.json).
 *
 * The nesting patterns are defined in [DefaultPatterns].
 */
class FileNestingTreeStructureProvider : TreeStructureProvider {

    override fun modify(
        parent: AbstractTreeNode<*>,
        children: Collection<AbstractTreeNode<*>>,
        settings: ViewSettings
    ): Collection<AbstractTreeNode<*>> {
        // Check if file nesting is enabled
        if (!FileNestingSettings.getInstance().state.enabled) {
            return children
        }

        // Only process directory nodes
        if (parent !is PsiDirectoryNode) {
            return children
        }

        val project = parent.project ?: return children

        // Separate file nodes from other nodes (directories, etc.)
        val fileNodes = children.filterIsInstance<PsiFileNode>()
        val otherNodes = children.filter { it !is PsiFileNode }

        if (fileNodes.isEmpty()) {
            return children
        }

        // Build a map of filename -> node for quick lookup
        val filenameToNode = fileNodes.associateBy { it.virtualFile?.name ?: "" }
        val allFilenames = filenameToNode.keys.toList()

        // Track which files have been nested (to avoid showing them at the top level)
        val nestedFilenames = mutableSetOf<String>()
        // Track which files have become parents (to avoid duplicates)
        val parentFilenames = mutableSetOf<String>()
        // Collect parent groups
        val parentGroups = mutableListOf<NestingGroupNode>()

        // Process patterns in order of definition - first pattern wins
        for (pattern in DefaultPatterns.patterns) {
            // Find all files matching this pattern's parent
            for ((filename, node) in filenameToNode) {
                // Skip if already nested under another parent or already a parent
                if (filename in nestedFilenames || filename in parentFilenames) {
                    continue
                }

                // Check if this file matches the current pattern's parent
                if (!PatternMatcher.matches(pattern.parent, filename)) {
                    continue
                }

                // Find children (excluding already nested files)
                val childFilenames = findChildFilenames(pattern, filename, allFilenames)
                    .filter { it !in nestedFilenames && it != filename }
                val childNodes = childFilenames.mapNotNull { filenameToNode[it] }

                if (childNodes.isNotEmpty()) {
                    // Mark children as nested
                    nestedFilenames.addAll(childFilenames)
                    parentFilenames.add(filename)
                    parentGroups.add(NestingGroupNode(project, node, childNodes, settings))
                }
            }
        }

        // Collect standalone files (not nested and not a parent with children)
        val standaloneFiles = fileNodes.filter {
            val name = it.virtualFile?.name ?: ""
            name !in nestedFilenames && name !in parentFilenames
        }

        return otherNodes + parentGroups + standaloneFiles
    }

    /**
     * Find all filenames that should be nested under the given parent.
     *
     * This handles both:
     * - Static patterns (e.g., ".eslintrc" under "package.json")
     * - Capture patterns (e.g., "$(capture).js" under "*.ts" where capture is the basename)
     */
    private fun findChildFilenames(
        pattern: NestingPattern,
        parentFilename: String,
        allFilenames: List<String>
    ): List<String> {
        val result = mutableListOf<String>()

        // Extract the capture value (basename without extension) if applicable
        val capture = extractCapture(pattern.parent, parentFilename)

        for (childPattern in pattern.children) {
            if (childPattern.contains("\$(capture)")) {
                // Replace $(capture) with the actual captured value
                if (capture != null) {
                    val resolvedPattern = childPattern.replace("\$(capture)", capture)
                    result.addAll(PatternMatcher.findMatches(resolvedPattern, allFilenames))
                }
            } else {
                // Static pattern
                result.addAll(PatternMatcher.findMatches(childPattern, allFilenames))
            }
        }

        // Don't include the parent itself as a child
        return result.filter { it != parentFilename }
    }

    /**
     * Extract the "capture" value from a filename based on the parent pattern.
     *
     * For example:
     * - Pattern "*.ts", filename "app.ts" → capture = "app"
     * - Pattern "package.json", filename "package.json" → capture = null (exact match)
     */
    private fun extractCapture(pattern: String, filename: String): String? {
        // For patterns with wildcards, extract the matching portion
        if (pattern.contains("*")) {
            // Convert pattern to regex with capture group
            val regexPattern = buildString {
                append("^")
                var i = 0
                while (i < pattern.length) {
                    when (val char = pattern[i]) {
                        '*' -> append("(.*)")
                        '.' -> append("\\.")
                        else -> append(char)
                    }
                    i++
                }
                append("$")
            }

            val regex = regexPattern.toRegex(RegexOption.IGNORE_CASE)
            val match = regex.find(filename)

            if (match != null && match.groupValues.size > 1) {
                // Return the first capture group (the part matched by *)
                return match.groupValues[1]
            }
        }

        // For exact matches or no wildcard, try to get basename
        val dotIndex = filename.lastIndexOf('.')
        return if (dotIndex > 0) {
            filename.substring(0, dotIndex)
        } else {
            filename
        }
    }
}
