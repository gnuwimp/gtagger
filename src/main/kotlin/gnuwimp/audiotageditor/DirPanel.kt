/*
 * Copyright © 2021 - 2024 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audiotageditor

import gnuwimp.swing.*
import javax.swing.JButton

/**
 * Show a directory tree and some buttons
 * Load tracks when directory changes
 */
class DirPanel : LayoutPanel(size = Swing.defFont.size / 2), DirListener {
    private var path         = ""
    private val dirTree      = DirTree(dirListener = this)
    private val logButton    = JButton(Constants.LABEL_SHOW_LOG)
    private val reloadButton = JButton(Constants.LABEL_RELOAD_DIR)
    private val aboutButton  = JButton(Constants.LABEL_ABOUT)
    private val quitButton   = JButton(Constants.LABEL_QUIT)

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

        logButton.toolTipText    = Constants.TOOL_SHOW_LOG
        reloadButton.toolTipText = Constants.TOOL_RELOAD
        aboutButton.toolTipText  = Constants.TOOL_ABOUT
        quitButton.toolTipText   = Constants.TOOL_QUIT

        //----------------------------------------------------------------------
        // Show about dialog box
        aboutButton.addActionListener {
            AboutHandler(Constants.DIALOG_ABOUT, Constants.aboutApp()).show(parent = Main.window, width = Swing.defFont.size * 60, height = Swing.defFont.size * 50)
        }

        //----------------------------------------------------------------------
        // Show log dialog
        logButton.addActionListener {
            val dialog = TextDialog(text = Swing.logMessage, showLastLine = true, title = Constants.DIALOG_LOG, parent = Main.window)
            dialog.isVisible = true
        }

        //----------------------------------------------------------------------
        // Quit application
        quitButton.addActionListener {
            Main.window.quit()
        }

        //----------------------------------------------------------------------
        // Reload current directory
        reloadButton.addActionListener {
            if (Data.saveChangedAskFirst(displayErrorDialogOnSave = false, abortOnFailedSave = true)) {
                Data.loadTracks(newPath = Data.path)
            }
        }
    }

    /**
     * Restore path in tree so it is selected.
     */
    fun restore(path: String) {
        dirTree.restore(path)
    }

    /**
     * Callback for path changes in directory tree.
     * If data has been changed ask for saving.
     * Path must be changed.
     */
    override fun pathChanged(path: String) {
        this.path = path

        if (Data.saveChangedAskFirst(displayErrorDialogOnSave = true, abortOnFailedSave = false) == true) {
            Data.loadTracks(newPath = path)
        }
    }
}
