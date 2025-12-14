package com.github.valentin.filenesting

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.PsiFileNode
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile

/**
 * A custom tree node that represents a parent file with nested child files.
 *
 * This node wraps a [PsiFileNode] (the parent file) and displays its nested children
 * as expandable child nodes in the Project View.
 *
 * @param project The current project
 * @param parentNode The parent file node that will be displayed
 * @param nestedChildren The child file nodes to nest under the parent
 * @param settings The view settings for the Project View
 */
class NestingGroupNode(
    project: Project,
    private val parentNode: PsiFileNode,
    private val nestedChildren: List<PsiFileNode>,
    settings: ViewSettings
) : AbstractTreeNode<PsiFile>(project, parentNode.value) {

    private val viewSettings: ViewSettings = settings

    override fun getChildren(): Collection<AbstractTreeNode<*>> {
        return nestedChildren
    }

    override fun update(presentation: PresentationData) {
        // Delegate to the parent node's presentation
        parentNode.update(presentation)

        // Add indicator showing number of nested files
        if (nestedChildren.isNotEmpty()) {
            val currentLocation = presentation.locationString ?: ""
            val nestedCount = "+${nestedChildren.size}"
            presentation.locationString = if (currentLocation.isNotEmpty()) {
                "$currentLocation $nestedCount"
            } else {
                nestedCount
            }
        }
    }

    fun containsFile(file: VirtualFile): Boolean {
        // Check if this node contains the given file
        val parentFile = parentNode.virtualFile
        if (parentFile == file) {
            return true
        }
        return nestedChildren.any { it.virtualFile == file }
    }

    override fun canNavigate(): Boolean = parentNode.canNavigate()

    override fun canNavigateToSource(): Boolean = parentNode.canNavigateToSource()

    override fun navigate(requestFocus: Boolean) {
        parentNode.navigate(requestFocus)
    }

    override fun getVirtualFile(): VirtualFile? = parentNode.virtualFile

    /**
     * Returns the underlying parent file node.
     */
    fun getParentFileNode(): PsiFileNode = parentNode

    /**
     * Returns the nested child file nodes.
     */
    fun getNestedChildren(): List<PsiFileNode> = nestedChildren

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NestingGroupNode) return false
        return parentNode.virtualFile == other.parentNode.virtualFile
    }

    override fun hashCode(): Int {
        return parentNode.virtualFile?.hashCode() ?: 0
    }
}
