/*
 * Copyright 2016 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.swing

import java.awt.BorderLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import javax.swing.*
import javax.swing.tree.*

/**
 * Copy closed icon to leaf icon (make them same)
 */
fun JTree.copyClosedIconToLeafIcon() {
    (this.cellRenderer as DefaultTreeCellRenderer).leafIcon = (this.cellRenderer as DefaultTreeCellRenderer).closedIcon
}

//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------

/**
 * Store file data for a tree node
 */
data class DirData(val file: File) {
    /**
     * Get directory name or file path
     */
    override fun toString(): String {
        return file.name.ifEmpty { file.path }
    }
}

//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------

/**
 * Interface for callbacks when directory path has been changed
 */
interface DirListener {
    /**
     * Callback method to override
     */
    fun pathChanged(path: String)
}

/**
 * Mouse right click listener for popup menu
 */
class DirPopupListener(val menu: JPopupMenu) : MouseAdapter() {
    /**
     * Show popup menu
     */
    override fun mousePressed(mouseEvent: MouseEvent) {
        if (SwingUtilities.isRightMouseButton(mouseEvent) == true) {
            menu.show(mouseEvent.component, mouseEvent.x, mouseEvent.y)
        }
    }
}

//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------

/**
 * A directory tree panel
 * With a popup mouse menu with a few options
 */
class DirTree(val dirListener: DirListener) : JPanel(), ActionListener {
    private val topNode:   DefaultMutableTreeNode
    private val treeModel: DefaultTreeModel

    private val tree         = JTree()
    private val menu         = JPopupMenu()
    private var selectedNode = DefaultMutableTreeNode()

    init {
        val scroll = JScrollPane(tree)

        layout = BorderLayout()
        add(scroll, BorderLayout.CENTER)

        menu.addItems(listOf("Reload selected directory", "Rename directory", "Create directory", "Delete directory"), this)

        if (Swing.isUnix == true) {
            topNode = DefaultMutableTreeNode(DirData(File("/")))
        }
        else {
            topNode = DefaultMutableTreeNode("My Computer")

            for (file in File.listRoots()) {
                topNode.add(DefaultMutableTreeNode(DirData(file)))
            }
        }

        treeModel                         = DefaultTreeModel(topNode)
        tree.model                        = treeModel
        tree.selectionModel.selectionMode = TreeSelectionModel.SINGLE_TREE_SELECTION
        tree.showsRootHandles             = true
        tree.isEditable                   = false

        tree.addMouseListener(DirPopupListener(menu))
        tree.copyClosedIconToLeafIcon()

        tree.addTreeSelectionListener {
            val node = tree.lastSelectedPathComponent as DefaultMutableTreeNode?

            if (node != null && node !== topNode) {
                selectedNode = node
                addDir(selectedNode)
                dirListener.pathChanged((selectedNode.userObject as DirData).file.path)
                tree.expandPath(TreePath(selectedNode.path))
            }
        }
    }

    /**
     * Callback for popup menu
     * It will catch eventual exceptions and display it in an error dialog
     */
    override fun actionPerformed(actionEvent: ActionEvent?) {
        try {
            if (actionEvent == null) {
                return
            }
            else if (actionEvent.actionCommand == "Reload selected directory") {
                addDir(selectedNode)
                treeModel.reload(selectedNode)
                dirListener.pathChanged((selectedNode.userObject as DirData).file.path)
            }
            else if (actionEvent.actionCommand == "Rename directory") {
                val newDirName = JOptionPane.showInputDialog(this, "Enter new directory name", "Rename Directory", JOptionPane.QUESTION_MESSAGE)
                val dir        = selectedNode.userObject as DirData

                if (!newDirName.isNullOrBlank() && newDirName != dir.file.name) {
                    val from = dir.file
                    val to   = File(from.parent + File.separator + newDirName)

                    if (from.renameTo(to) == false) {
                        throw Exception("error: can't rename directory")
                    }
                    else {
                        selectedNode.userObject = DirData(to)
                        treeModel.reload(selectedNode)
                        dirListener.pathChanged(to.path)
                    }
                }
            }
            else if (actionEvent.actionCommand == "Create directory") {
                val newDirName = JOptionPane.showInputDialog(this, "Enter new directory name", "Create Directory", JOptionPane.QUESTION_MESSAGE)

                if (newDirName.isNullOrEmpty() == false) {
                    val currDir = (selectedNode.userObject as DirData).file
                    val newDir  = File(currDir.path + File.separator + newDirName)

                    if (newDir.mkdir() == false) {
                        throw Exception("error: can't create directory")
                    }
                    else {
                        selectedNode.removeAllChildren()
                        addDir(selectedNode)
                        treeModel.reload(selectedNode)
                    }
                }
            }
            else if (actionEvent.actionCommand == "Delete directory") {
                val answer = JOptionPane.showConfirmDialog(this, "Delete this directory (must be empty)?", "Delete Directory", JOptionPane.YES_NO_OPTION)

                if (answer == JOptionPane.YES_OPTION) {
                    if ((selectedNode.userObject as DirData).file.delete() == false) {
                        throw Exception("error: can't delete directory")
                    }
                    else {
                        val parentNode = selectedNode.parent as DefaultMutableTreeNode

                        addDir(parentNode)
                        treeModel.reload(parentNode)
                        dirListener.pathChanged((parentNode.userObject as DirData).file.path)
                        tree.selectionPath = TreePath(parentNode.path)
                    }
                }
            }
        }
        catch (e: Exception) {
            JOptionPane.showMessageDialog(this, e, "File Error", JOptionPane.ERROR_MESSAGE)
        }
    }

    /**
     * Add child directories to parent directory
     */
    private fun addDir(node: DefaultMutableTreeNode) {
        node.removeAllChildren()

        val dirNode = node.userObject as DirData

        if (dirNode.file.isDirectory == true) {
            val list = dirNode.file.listFiles()

            if (list != null) {
                for (file in list.sorted()) {
                    if (file.isDirectory == true) {
                        node.add(DefaultMutableTreeNode(DirData(file)))
                    }
                }
            }
        }
    }

    /**
     * Create sub path
     */
    private fun makePath(paths: List<String>, index: Int): String {
        var path = ""

        for (i in 0..index) {
            if (Swing.isUnix == true) {
                when (i) {
                    0    -> path = "/"
                    1    -> path += paths[i]
                    else -> path += "/" + paths[i]
                }
            }
            else {
                if (i > 1) {
                    path += File.separator
                }

                path += paths[i]

                if (i == 0) {
                    path += File.separator
                }
            }
        }

        return path
    }

    /**
     * Restore and select path
     */
    fun restore(path: String) {
        val paths    = File(path).canonicalPath.split(("\\" + File.separator).toRegex()).dropLastWhile(String::isEmpty)
        val node     = restorePath(paths, if (Swing.isUnix) 1 else 0, topNode)
        val treePath = TreePath(node.path)

        tree.selectionPath = treePath
        tree.scrollPathToVisible(treePath)
    }

    /**
     * Restore child subPaths recursively
     * And return last used tree node
     */
    private fun restorePath(subPaths: List<String>, index: Int, node: DefaultMutableTreeNode): DefaultMutableTreeNode {
        if (index >= subPaths.size)
            return node

        if (node.isLeaf == true) {
            addDir(node)
        }

        val path = makePath(subPaths, index)

        for (childNode in node.children()) {
            val dir = (childNode as DefaultMutableTreeNode).userObject as DirData

            if (path == dir.file.canonicalPath) {
                return restorePath(subPaths, index + 1, childNode)
            }
        }

        return node
    }
}
