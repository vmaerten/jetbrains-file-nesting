package com.github.vmaerten.filenesting.patterns

/**
 * Represents a file nesting pattern.
 *
 * @param parent The parent file pattern (e.g., "package.json", "README*")
 * @param children List of child file patterns to nest under the parent (e.g., ".eslint*", "tsconfig.*")
 */
data class NestingPattern(
    val parent: String,
    val children: List<String>
)
