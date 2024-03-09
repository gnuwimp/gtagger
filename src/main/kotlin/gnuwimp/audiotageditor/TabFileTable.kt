/*
 * Copyright © 2021 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audiotageditor

import gnuwimp.swing.LayoutPanel
import gnuwimp.swing.Swing
import gnuwimp.swing.TableHeader
import gnuwimp.util.numOrZero
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.table.AbstractTableModel

/**
 * Table for track file names
 */
class TabFileTable : LayoutPanel(size = Swing.defFont.size / 2 + 1) {
    private val colSelect      = 0
    private val colTrack       = 1
    private val colFile        = 2
    private val colExtension   = 3
    private val colSize        = 4
    private val table          = DataTable()
    private val filterButton   = JButton(Constants.LABEL_FILTER)
    private val selectButton   = JButton(Constants.LABEL_SELECT_ALL)
    private val unselectButton = JButton(Constants.LABEL_SELECT_NONE)

    init {
        val scroll = JScrollPane()

        scroll.viewport.view = table
        add(scroll, x = 1, y = 1, w = -1, h = -6)
        add(filterButton, x = -63, y = -5, w = 20, h = 4)
        add(selectButton, x = -42, y = -5, w = 20, h = 4)
        add(unselectButton, x = -21, y = -5, w = 20, h = 4)

        filterButton.toolTipText   = Constants.TOOL_SELECT_FILE
        selectButton.toolTipText   = Constants.TOOL_SELECT_ALL
        unselectButton.toolTipText = Constants.TOOL_SELECT_NONE

        //----------------------------------------------------------------------
        // Select all tracks
        selectButton.addActionListener {
            Data.selectAll(true)
        }

        //----------------------------------------------------------------------
        // Unselect all tracks
        unselectButton.addActionListener {
            Data.selectAll(false)
        }

        //----------------------------------------------------------------------
        // Search and select tracks
        filterButton.addActionListener {
            val answer = JOptionPane.showInputDialog(Main.window, Constants.MESSAGE_ASK_FILTER_FILE, Constants.DIALOG_FILTER, JOptionPane.YES_NO_OPTION)

            if (answer.isNullOrBlank() == false) {
                Data.filterOnFiles(answer)
            }
        }

        //----------------------------------------------------------------------
        // Create data model for table, show a list of file names
        table.model = object : AbstractTableModel() {
            override fun getColumnClass(column: Int) = when (column) {
                colSelect -> java.lang.Boolean::class.java
                else -> java.lang.String::class.java
            }

            //----------------------------------------------------------------------
            override fun getColumnCount(): Int = 5

            //----------------------------------------------------------------------
            override fun getColumnName(column: Int): String = when (column) {
                colSelect -> Constants.LABEL_SELECT
                colTrack -> Constants.LABEL_TRACK
                colFile -> Constants.LABEL_FILE
                colExtension -> Constants.LABEL_EXTENSION
                colSize -> Constants.LABEL_SIZE
                else -> "!"
            }

            //----------------------------------------------------------------------
            override fun getRowCount(): Int = Data.tracks.size

            //----------------------------------------------------------------------
            override fun getValueAt(row: Int, column: Int): Any {
                val track = Data.getTrack(row)

                if (track != null) {
                    when (column) {
                        colSelect -> return track.isSelected
                        colTrack -> return track.track
                        colFile ->  return track.fileName
                        colExtension ->  return track.fileExt
                        colSize -> return track.fileSizeInfo
                    }
                }

                return "!"
            }

            //----------------------------------------------------------------------
            override fun isCellEditable(row: Int, column: Int): Boolean = when(column) {
                colSelect -> true
                colTrack -> true
                colFile -> true
                else -> false
            }

            //----------------------------------------------------------------------
            override fun setValueAt(value: Any?, row: Int, column: Int) {
                val track = Data.getTrack(row)

                if (track != null) {
                    if (column == colSelect) {
                        track.isSelected = value as Boolean
                        Data.sendUpdate(TrackEvent.ITEM_DIRTY)
                    }
                    else if (column == colTrack) {
                        val num = value as String

                        if (column == colTrack && num.numOrZero in 1..9999 && num != track.track) {
                            track.track = num
                            Data.sendUpdate(TrackEvent.ITEM_DIRTY)
                        }
                    }
                    else if (column == colFile) {
                        val name = value as String
                        if (track.fileName != name) {
                            track.fileName = name
                            Data.sendUpdate(TrackEvent.ITEM_DIRTY)
                        }
                    }
                }
            }
        }

        table.tableHeader.toolTipText = Constants.TOOL_TABLE_HEAD
        table.setColumnWidth(colSelect, min = 50, pref = 50, max = 100)
        table.setColumnWidth(colTrack, min = 50, pref = 50, max = 100)
        table.setColumnWidth(colFile, min = 150, pref = 500, max = 10000)
        table.setColumnWidth(colExtension, min = 50, pref = 100, max = 100)
        table.setColumnWidth(colSize, min = 50, pref = 100, max = 100)
        table.setShowGrid(false)
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        table.setColumnAlign(colTrack, SwingConstants.CENTER)
        table.setColumnAlign(colSize, SwingConstants.RIGHT)


        //----------------------------------------------------------------------
        // Enable table header for receiving mouse clicks so data can be sorted
        table.tableHeader.addMouseListener(object : TableHeader() {
            override fun mouseClicked(event: MouseEvent?) {
                when (columnIndex(event)) {
                    colSelect -> Data.sortTracks(Data.Sort.SELECTED, isControlDown(event))
                    colTrack -> Data.sortTracks(Data.Sort.TRACK, isControlDown(event))
                    colFile -> Data.sortTracks(Data.Sort.FILE, isControlDown(event))
                    colExtension -> Data.sortTracks(Data.Sort.EXTENSION, isControlDown(event))
                    colSize -> Data.sortTracks(Data.Sort.SIZE, isControlDown(event))
                }

                Data.sendUpdate(TrackEvent.LIST_UPDATED)
            }
        })

        //----------------------------------------------------------------------
        // If new row has been selected do select row in Data object
        table.selectionModel.addListSelectionListener { lse ->
            val index = (lse.source as ListSelectionModel).minSelectionIndex

            if (lse.valueIsAdjusting == false && Data.selectedRow != index) {
                Data.selectedRow = index
            }
        }

        //----------------------------------------------------------------------
        // Listener callback for data changes
        Data.addListener(object : TrackListener {
            override fun update(event: TrackEvent) {
                when (event) {
                    TrackEvent.ITEM_DIRTY -> {
                        repaint()
                    }
                    TrackEvent.LIST_UPDATED -> {
                        table.fireModel()

                        selectButton.isEnabled = Data.tracks.isNotEmpty()
                        unselectButton.isEnabled = Data.tracks.isNotEmpty()
                        filterButton.isEnabled = Data.tracks.isNotEmpty()
                    }
                    TrackEvent.ITEM_SELECTED -> {
                        table.selectRow = Data.selectedRow
                    }
                    TrackEvent.ITEM_IMAGE -> {
                    }
                }
            }
        })
    }
}
