/*
 * Copyright 2016 - 2019 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.gtagger

import gnuwimp.core.swing.*
import gnuwimp.core.swing.dialog.TextDialog
import gnuwimp.gtagger.data.Data
import gnuwimp.gtagger.resource.Text
import java.io.File
import javax.swing.JButton

/**
 * Show a directory tree and some buttons
 * Load tracks when directory changes
 */
class DirPanel : LayoutPanel(size = Platform.defFont.size / 2), DirListener {
    private var path         = ""
    private val dirTree      = DirTree(dirListener = this)
    private val logButton    = JButton(Text.LABEL_SHOW_LOG)
    private val reloadButton = JButton(Text.LABEL_RELOAD_DIR)
    private val aboutButton  = JButton(Text.LABEL_ABOUT)
    private val quitButton   = JButton(Text.LABEL_QUIT)

    /**
     * Get current directory
     */
    val currentPath: String get() = path

    init {
        add(widget = dirTree, x = 1, y = 1, w = -1, h = -21)
        add(logButton, x = 1, y = -20, w = -1, h = 4)
        add(widget = reloadButton, x = 1, y = -15, w = -1, h = 4)
        add(aboutButton, x = 1, y = -10, w = -1, h = 4)
        add(quitButton, x = 1, y = -5, w = -1, h = 4)

        logButton.toolTipText    = Text.TOOL_SHOW_LOG
        reloadButton.toolTipText = Text.TOOL_RELOAD
        aboutButton.toolTipText  = Text.TOOL_ABOUT
        quitButton.toolTipText   = Text.TOOL_QUIT

        // Show about dialog box
        aboutButton.addActionListener {
            AboutHandler(Text.DIALOG_ABOUT, Text.APP_ABOUT).show(parent = Main.window, width = Platform.defFont.size * 50, height = Platform.defFont.size * 30)
        }

        // Show log dialog
        logButton.addActionListener {
            val dialog = TextDialog(text = Platform.logMessage, showLastLine = true, title = Text.DIALOG_LOG, parent = Main.window)
            dialog.isVisible = true
        }

        // Quit application
        quitButton.addActionListener {
            Main.window.quit()
        }

        // Reload current directory
        reloadButton.addActionListener {
            if (Data.saveChangedAskFirst(displayErrorDialogOnSave = false, abortOnFailedSave = true))
                Data.loadTracks(newPath = Data.path)
        }
    }

    /**
     * Restore path in tree so it is selected
     */
    fun restore(path: String) {
        if (File(path).isDirectory)
            dirTree.restore(path)
    }

    /**
     * Callback for path changes in directory tree
     * If data has been changed ask for saving
     * Path must be changed
     */
    override fun pathChanged(path: String) {
        this.path = path

        if (Data.saveChangedAskFirst(displayErrorDialogOnSave = true, abortOnFailedSave = false))
            Data.loadTracks(newPath = path)
    }
}
