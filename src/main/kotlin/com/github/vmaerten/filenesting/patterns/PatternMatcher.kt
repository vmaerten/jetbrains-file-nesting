package com.github.vmaerten.filenesting.patterns

import java.util.concurrent.ConcurrentHashMap

/**
 * Utility object for matching file names against glob-like patterns.
 * Supports wildcards (*) for matching any sequence of characters.
 *
 * Thread-safe: uses [ConcurrentHashMap] for caching compiled regex patterns.
 */
object PatternMatcher {

    private val regexCache = ConcurrentHashMap<String, Regex>()

    /**
     * Check if a filename matches a glob-like pattern.
     *
     * Supported patterns:
     * - "*" matches any sequence of characters
     * - "." is treated literally
     * - Case-insensitive matching
     *
     * Examples:
     * - "*.json" matches "package.json", "tsconfig.json"
     * - ".eslint*" matches ".eslintrc", ".eslintrc.js", ".eslintignore"
     * - "tsconfig.*" matches "tsconfig.json", "tsconfig.node.json"
     *
     * @param pattern The glob-like pattern
     * @param filename The filename to match
     * @return true if the filename matches the pattern
     */
    fun matches(pattern: String, filename: String): Boolean {
        val regex = regexCache.getOrPut(pattern) {
            patternToRegex(pattern)
        }
        return regex.matches(filename)
    }

    /**
     * Find all filenames that match a given pattern.
     *
     * @param pattern The glob-like pattern
     * @param filenames List of filenames to filter
     * @return List of filenames that match the pattern
     */
    fun findMatches(pattern: String, filenames: List<String>): List<String> {
        return filenames.filter { matches(pattern, it) }
    }

    /**
     * Check if a filename matches any of the given patterns.
     *
     * @param patterns List of glob-like patterns
     * @param filename The filename to match
     * @return true if the filename matches any pattern
     */
    fun matchesAny(patterns: List<String>, filename: String): Boolean {
        return patterns.any { matches(it, filename) }
    }

    private fun patternToRegex(pattern: String): Regex {
        val regexPattern = buildString {
            append("^")
            for (char in pattern) {
                when (char) {
                    '*' -> append(".*")
                    '.' -> append("\\.")
                    '?' -> append(".")
                    '[', ']', '(', ')', '{', '}', '^', '$', '|', '\\', '+' -> {
                        append("\\")
                        append(char)
                    }
                    else -> append(char)
                }
            }
            append("$")
        }
        return regexPattern.toRegex(RegexOption.IGNORE_CASE)
    }

    /**
     * Clear the regex cache. Useful for testing or when patterns change.
     */
    fun clearCache() {
        regexCache.clear()
    }
}
