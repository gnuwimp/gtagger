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
 * Table for track file names
 */
class FileTable : LayoutPanel(size = Platform.defFont.size / 2) {
    private val colSelect      = 0
    private val colTrack       = 1
    private val colFile        = 2
    private val colFormat      = 3
    private val colSize        = 4
    private val table          = DataTable()
    private val filterButton   = JButton(Text.LABEL_FILTER)
    private val selectButton   = JButton(Text.LABEL_SELECT_ALL)
    private val unselectButton = JButton(Text.LABEL_SELECT_NONE)

    init {
        val scroll = JScrollPane()

        scroll.viewport.view = table
        add(scroll, x = 1, y = 1, w = -1, h = -6)
        add(filterButton, x = -63, y = -5, w = 20, h = 4)
        add(selectButton, x = -42, y = -5, w = 20, h = 4)
        add(unselectButton, x = -21, y = -5, w = 20, h = 4)

        filterButton.toolTipText   = Text.TOOL_SELECT_FILE
        selectButton.toolTipText   = Text.TOOL_SELECT_ALL
        unselectButton.toolTipText = Text.TOOL_SELECT_NONE

        // Select all tracks
        selectButton.addActionListener {
            Data.selectAll(true)
        }

        // Unselect all tracks
        unselectButton.addActionListener {
            Data.selectAll(false)
        }

        // Search and select tracks
        filterButton.addActionListener {
            val answer = JOptionPane.showInputDialog(Main.window, Text.MESSAGE_ASK_FILTER_FILE, Text.DIALOG_FILTER, JOptionPane.YES_NO_OPTION)

            if (!answer.isNullOrBlank())
                Data.filterOnFiles(answer)
        }

        // Create data model for table, show a list of file names
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
                colFormat -> Text.LABEL_FORMAT
                colSize -> Text.LABEL_SIZE
                else -> "!"
            }

            override fun getRowCount(): Int = Data.tracks.size

            override fun getValueAt(row: Int, column: Int): Any {
                val track = Data.getTrack(row)

                if (track != null) {
                    when (column) {
                        colSelect -> return track.isSelected
                        colTrack -> return track.track
                        colFile ->  return track.fileNameWithExtension
                        colFormat -> return track.formatInfo
                        colSize -> return track.fileSizeInfo
                    }
                }

                return "!"
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
        table.setColumnWidth(colSelect, min = 50, pref = 50, max = 100)
        table.setColumnWidth(colTrack, min = 50, pref = 50, max = 100)
        table.setColumnWidth(colFile, min = 150, pref = 500, max = 10000)
        table.setColumnWidth(colFormat, min = 150, pref = 250, max = 100)
        table.setColumnWidth(colSize, min = 100, pref = 100, max = 100)
        table.setShowGrid(false)
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        table.setColumnAlign(colTrack, SwingConstants.CENTER)
        table.setColumnAlign(colSize, SwingConstants.RIGHT)


        // Enable table header for receiving mouse clicks so data can be sorted
        table.tableHeader.addMouseListener(object : TableHeaderCallback() {
            override fun mouseClicked(event: MouseEvent?) {
                when (columnIndex(event)) {
                    colSelect -> Data.sortTracks(Data.Sort.SELECTED, isControlDown(event))
                    colTrack -> Data.sortTracks(Data.Sort.TRACK, isControlDown(event))
                    colFile -> Data.sortTracks(Data.Sort.FILE, isControlDown(event))
                    colFormat -> Data.sortTracks(Data.Sort.FORMAT, isControlDown(event))
                    colSize -> Data.sortTracks(Data.Sort.SIZE, isControlDown(event))
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
                    TrackEvent.ITEM_DIRTY -> repaint()
                    TrackEvent.LIST_UPDATED -> {
                        table.fireModel()

                        selectButton.isEnabled = Data.tracks.isNotEmpty()
                        unselectButton.isEnabled = Data.tracks.isNotEmpty()
                        filterButton.isEnabled = Data.tracks.isNotEmpty()
                    }
                    TrackEvent.ITEM_SELECTED -> table.selectRow = Data.selectedRow
                    TrackEvent.ITEM_IMAGE -> Unit
                }
            }
        })
    }
}
