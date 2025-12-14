package com.github.valentin.filenesting

import com.github.valentin.filenesting.patterns.DefaultPatterns
import com.github.valentin.filenesting.patterns.NestingPattern
import com.github.valentin.filenesting.patterns.PatternMatcher
import com.github.valentin.filenesting.settings.FileNestingSettings
import com.intellij.ide.projectView.TreeStructureProvider
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode
import com.intellij.ide.projectView.impl.nodes.PsiFileNode
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.application.ApplicationManager

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
        val nestingSettings = ApplicationManager.getApplication().getService(FileNestingSettings::class.java)
        if (!nestingSettings.state.enabled) {
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

        // Process each file to find parent-child relationships
        val result = mutableListOf<AbstractTreeNode<*>>()

        for (node in fileNodes) {
            val filename = node.virtualFile?.name ?: continue

            // Skip files that are already nested under another parent
            if (filename in nestedFilenames) {
                continue
            }

            // Check if this file is a parent according to our patterns
            val matchingPattern = findMatchingParentPattern(filename)

            if (matchingPattern != null) {
                // Find all children for this parent
                val childFilenames = findChildFilenames(matchingPattern, filename, allFilenames)
                val childNodes = childFilenames.mapNotNull { filenameToNode[it] }

                // Mark children as nested
                nestedFilenames.addAll(childFilenames)

                if (childNodes.isNotEmpty()) {
                    // Create a nesting group node
                    result.add(NestingGroupNode(project, node, childNodes, settings))
                } else {
                    // No children found, add the node as-is
                    result.add(node)
                }
            } else {
                // Not a parent file, add as-is (unless it becomes nested later)
                result.add(node)
            }
        }

        // Remove any files that got nested after they were added to result
        val finalFileNodes = result.filter { node ->
            when (node) {
                is PsiFileNode -> node.virtualFile?.name !in nestedFilenames
                is NestingGroupNode -> true
                else -> true
            }
        }

        return otherNodes + finalFileNodes
    }

    /**
     * Find a pattern where the given filename matches the parent pattern.
     */
    private fun findMatchingParentPattern(filename: String): NestingPattern? {
        return DefaultPatterns.patterns.find { pattern ->
            PatternMatcher.matches(pattern.parent, filename)
        }
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
