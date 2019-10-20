/*
 * Copyright 2016 - 2019 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.gtagger.tabs

import gnuwimp.core.swing.LayoutPanel
import gnuwimp.core.swing.Platform
import gnuwimp.core.swing.TableHeaderCallback
import gnuwimp.gtagger.Main
import gnuwimp.gtagger.TrackEvent
import gnuwimp.gtagger.TrackListener
import gnuwimp.gtagger.data.Data
import gnuwimp.gtagger.resource.Text
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.table.AbstractTableModel

/**
 * Create a table with track data
 */
class TrackTable : LayoutPanel(size = Platform.defFont.size / 2) {
    private val colSelect   = 0
    private val colTrack    = 1
    private val colFile     = 2
    private val colBitrate  = 3
    private val colTime     = 4
    private val table       = DataTable()
    private val clearButton = JButton(Text.LABEL_DELETE_TAGS2)
    private val downButton  = JButton(Text.LABEL_MOVE_DOWN)
    private val upButton    = JButton(Text.LABEL_MOVE_UP)

    init {
        val scroll = JScrollPane()

        scroll.viewport.view = table
        add(scroll, x = 1, y = 1, w = -1, h = -6)
        add(clearButton, x = -63, y = -5, w = 20, h = 4)
        add(downButton, x = -42, y = -5, w = 20, h = 4)
        add(upButton, x = -21, y = -5, w = 20, h = 4)

        clearButton.toolTipText = Text.TOOL_DELETE_TAGS2
        downButton.toolTipText  = Text.TOOL_MOVE_DOWN
        upButton.toolTipText    = Text.TOOL_MOVE_UP

        clearButton.isEnabled   = false
        downButton.isEnabled    = false
        upButton.isEnabled      = false

        // Remove all tags from all selected tracks
        clearButton.addActionListener {
            if (JOptionPane.showConfirmDialog(Main.window, Text.MESSAGE_ASK_CLEAR_HTML, Text.DIALOG_CLEAR, JOptionPane.YES_NO_OPTION) == Text.YES)
                Data.removeTagsForAll()
        }

        // Move current selected row down in track list
        downButton.addActionListener {
            Data.moveRowDown()
        }

        // Move current selected row down up track list
        upButton.addActionListener {
            Data.moveRowUp()
        }

        // Create data model for table, show list of file names and file properties
        table.model = object : AbstractTableModel() {
            override fun getColumnClass(column: Int): Class<Any> = when (column) {
                colSelect -> true.javaClass
                else -> "".javaClass
            }

            override fun getColumnCount(): Int = 5

            override fun getColumnName(column: Int): String = when (column) {
                colSelect -> Text.LABEL_SELECT
                colTrack -> Text.LABEL_TRACK
                colFile -> Text.LABEL_FILE
                colBitrate -> Text.LABEL_BITRATE
                colTime -> Text.LABEL_TIME
                else -> "!"
            }

            override fun getRowCount(): Int = Data.tracks.size

            override fun getValueAt(row: Int, column: Int): Any {
                val track = Data.getTrack(row)

                if (track != null) {
                    when (column) {
                        colSelect -> return track.isSelected
                        colTrack -> return track.track
                        colFile -> return track.fileName
                        colBitrate -> return track.bitrateInfo
                        colTime -> return track.timeInfo
                    }
                }

                return ""
            }

            override fun isCellEditable(row: Int, column: Int): Boolean = when(column) {
                colSelect -> true
                else -> false
            }

            override fun setValueAt(value: Any?, row: Int, column: Int) {
                val track = Data.getTrack(row)

                if (track != null && column == colSelect) {
                    track.isSelected = value as Boolean
                    Data.sendUpdate(TrackEvent.ITEM_DIRTY)
                }
            }
        }

        table.tableHeader.toolTipText = Text.TOOL_TABLE_HEAD
        table.setColumnWidth(colSelect, min = 50, pref = 50, max = 50)
        table.setColumnWidth(colTrack, min = 50, pref = 50, max = 50)
        table.setColumnWidth(colFile, min = 150, pref = 500, max = 10000)
        table.setColumnWidth(colBitrate, min = 75, pref = 75, max = 100)
        table.setColumnWidth(colTime, min = 75, pref = 75, max = 100)
        table.setShowGrid(false)
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        table.setColumnAlign(colTrack, SwingConstants.CENTER)
        table.setColumnAlign(colBitrate, SwingConstants.RIGHT)
        table.setColumnAlign(colTime, SwingConstants.RIGHT)

        // Enable table header for receiving mouse clicks so data can be sorted
        table.tableHeader.addMouseListener(object : TableHeaderCallback() {
            override fun mouseClicked(event: MouseEvent?) {
                when (columnIndex(event)) {
                    colSelect -> Data.sortTracks(Data.Sort.SELECTED, isControlDown(event))
                    colTrack -> Data.sortTracks(Data.Sort.TRACK, isControlDown(event))
                    colFile -> Data.sortTracks(Data.Sort.FILE, isControlDown(event))
                    colBitrate -> Data.sortTracks(Data.Sort.BITRATE, isControlDown(event))
                    colTime -> Data.sortTracks(Data.Sort.TIME, isControlDown(event))
                }

                Data.sendUpdate(TrackEvent.LIST_UPDATED)
            }

        })

        // If new row has been selected do select row in Data object
        table.selectionModel.addListSelectionListener { lse ->
            val index = (lse.source as ListSelectionModel).minSelectionIndex

            if (!lse.valueIsAdjusting && Data.selectedRow != index)
                Data.selectedRow = index
        }

        // Listener callback for data changes
        Data.addListener(object : TrackListener {
            override fun update(event: TrackEvent) {
                when (event) {
                    TrackEvent.ITEM_DIRTY -> {
                        clearButton.isEnabled = Data.countSelected > 0
                        repaint()
                    }
                    TrackEvent.LIST_UPDATED -> {
                        table.fireModel()
                        clearButton.isEnabled = Data.countSelected > 0
                        upButton.isEnabled = Data.selectedRow > 0
                        downButton.isEnabled = (Data.selectedRow > -1 && Data.selectedRow < Data.tracks.size - 1)
                    }
                    TrackEvent.ITEM_SELECTED -> {
                        table.selectRow = Data.selectedRow
                        upButton.isEnabled = Data.selectedRow > 0
                        downButton.isEnabled = (Data.selectedRow > -1 && Data.selectedRow < Data.tracks.size - 1)
                    }
                    TrackEvent.ITEM_IMAGE -> Unit
                }
             }
        })
    }
}
