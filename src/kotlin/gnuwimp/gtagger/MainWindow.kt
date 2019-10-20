/*
 * Copyright 2016 - 2019 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.gtagger

import gnuwimp.core.swing.Platform
import gnuwimp.core.swing.StatusBar
import gnuwimp.core.swing.extension.setFontForAll
import gnuwimp.gtagger.data.Data
import gnuwimp.gtagger.resource.Text
import gnuwimp.gtagger.tabs.*
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Frame
import java.awt.Toolkit
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.prefs.Preferences
import javax.swing.*
import kotlin.system.exitProcess

/**
 * Main window object
 * Show directory tree to the left
 * And four tab widgets to the right
 * A splitter is between tree and tab container widget
 */
class MainWindow(title: String = Text.APP_NAME) : JFrame(title) {
    private val pref: Preferences = Preferences.userNodeForPackage(Main.javaClass)
    private val albumOptions      = AlbumOptions(pref)
    private val albumTab          = JSplitPane()
    private val albumTable        = AlbumTable()
    private val dirPanel          = DirPanel()
    private val fileOptions       = FileOptions()
    private val fileTab           = JSplitPane()
    private val fileTable         = FileTable()
    private val main              = JPanel()
    private val splitPane         = JSplitPane()
    private val statusBar         = StatusBar()
    private val tabs              = JTabbedPane()
    private val titleOptions      = TitleOptions()
    private val titleTab          = JSplitPane()
    private val titleTable        = TitleTable()
    private val trackOptions      = TrackOptions(pref)
    private val trackTab          = JSplitPane()
    private val trackTable        = TrackTable()
    private var oldTabIndex       = 0
    private var savedState        = false

    /**
     * Get or set selected tab
     * Setting index forces a redraw
     */
    private var selectedTab: Int
        get() = oldTabIndex

        set(value) {
            oldTabIndex = value

            if (value != tabs.selectedIndex)
                tabs.selectedIndex = value

            tabs.invalidate()
            tabs.selectedComponent.invalidate()
        }

    init {
        defaultCloseOperation = DISPOSE_ON_CLOSE
        font = Platform.defFont
        iconImage = Main.icon
        contentPane = main
        Data.messageFunc = { value: String -> statusBar.message = value }

        main.layout              = BorderLayout()
        splitPane.leftComponent  = dirPanel
        splitPane.rightComponent = tabs
        trackTab.leftComponent   = trackOptions
        trackTab.rightComponent  = trackTable
        fileTab.leftComponent    = fileOptions
        fileTab.rightComponent   = fileTable
        albumTab.leftComponent   = albumOptions
        albumTab.rightComponent  = albumTable
        titleTab.leftComponent   = titleOptions
        titleTab.rightComponent  = titleTable

        tabs.border = BorderFactory.createEmptyBorder(5, 3, 5, 3)
        tabs.addTab(Text.LABEL_TAB_TRACK, null, trackTab, Text.TOOL_TAB_TRACK)
        tabs.addTab(Text.LABEL_TAB_FILE, null, fileTab, Text.TOOL_TAB_FILE)
        tabs.addTab(Text.LABEL_TAB_TITLE, null, titleTab, Text.TOOL_TAB_TITLE)
        tabs.addTab(Text.LABEL_TAB_ALBUM, null, albumTab, Text.TOOL_TAB_ALBUM)

        main.add(splitPane, BorderLayout.CENTER)
        main.add(statusBar, BorderLayout.SOUTH)

        // Make certain that every time a tab has been switched that changed data have been saved or discarded, ask user to apply changes also
        tabs.addChangeListener { changeEvent ->
            val tab = changeEvent.source as JTabbedPane

            if (selectedTab != tab.selectedIndex) {
                val currentRow = Data.selectedRow

                when {
                    Data.isAnyChangedAndSelected -> when (JOptionPane.showConfirmDialog(Main.window, Text.MESSAGE_ASK_SAVE_HTML, Text.DIALOG_SAVE, JOptionPane.YES_NO_CANCEL_OPTION)) {
                        Text.YES -> selectedTab = if (Data.saveTracks()) tab.selectedIndex else selectedTab
                        Text.NO -> {
                            Data.copyTagsFromAudio()
                            Data.sendUpdate(TrackEvent.LIST_UPDATED)
                            selectedTab = tab.selectedIndex
                        }
                        Text.CANCEL -> selectedTab = selectedTab
                    }
                    Data.isAnyChanged -> {
                        Data.copyTagsFromAudio()
                        Data.sendUpdate(TrackEvent.LIST_UPDATED)
                        selectedTab = tab.selectedIndex
                    }
                    else -> selectedTab = tab.selectedIndex
                }

                Data.selectedRow = currentRow
            }
        }

        // Quit application
        addWindowListener( object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                quit()
            }
        })
    }

    /**
     * Quit but ask to save if data has been changed
     * Save also all preferences
     */
    fun quit() {
        if (Data.isAnyChangedAndSelected &&
            JOptionPane.showConfirmDialog(Main.window, Text.MESSAGE_ASK_SAVE_HTML, Text.DIALOG_SAVE, JOptionPane.YES_NO_OPTION) == Text.YES &&
            !Data.saveTracks()) {

            JOptionPane.showMessageDialog(Main.window, Text.ERROR_SAVE2_HTML, Text.DIALOG_SAVE_FAILED, JOptionPane.ERROR_MESSAGE)
            return
        }

        windowSave()
        isVisible = false
        dispose()
        exitProcess(status = 0)
    }

    /**
     * Restore window/widget sizes and last used directory
     */
    fun windowRestore() {
        pack()
        setFontForAll(this.font)

        val w = pref.winWidth
        val h = pref.winHeight
        var x = pref.winX
        var y = pref.winY
        val sc = Toolkit.getDefaultToolkit().screenSize

        if (x > sc.getWidth() || x < -50)
            x = 0

        if (y > sc.getHeight() || y < -50)
            y = 0

        setLocation(x, y)
        setSize(w, h)
        preferredSize = Dimension(w, h)

        if (pref.winMax) {
            extendedState = Frame.MAXIMIZED_BOTH
        }

        splitPane.dividerLocation = pref.dirSplit
        trackTab.dividerLocation = pref.trackSplit
        fileTab.dividerLocation = pref.fileSplit
        titleTab.dividerLocation = pref.titleSplit
        albumTab.dividerLocation = pref.albumSplit

        validate()
        dirPanel.restore(pref.lastPath)
        Data.sendUpdate(TrackEvent.LIST_UPDATED)
    }

    /**
     * Save all widget sizes
     */
    private fun windowSave() {
        try {
            if (!savedState) {
                val size = size
                val pos  = location

                pref.lastPath   = dirPanel.currentPath
                pref.winWidth   = size.width
                pref.winHeight  = size.height
                pref.winX       = pos.x
                pref.winY       = pos.y
                pref.winMax     = (extendedState and Frame.MAXIMIZED_BOTH != 0)
                pref.dirSplit   = splitPane.dividerLocation
                pref.trackSplit = trackTab.dividerLocation
                pref.fileSplit  = fileTab.dividerLocation
                pref.titleSplit = titleTab.dividerLocation
                pref.albumSplit = albumTab.dividerLocation

                pref.flush()
                savedState = true
            }
        }
        catch (e: Exception) {
            println(e)
            e.printStackTrace()
        }
    }
}
