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
 * Create a table with track data.
 */
class TabTrackTable : LayoutPanel(size = Swing.defFont.size / 2 + 1) {
    private val colSelect      = 0
    private val colTrack       = 1
    private val colFile        = 2
    private val colFormat      = 3
    private val colBitrate     = 4
    private val colTime        = 5
    private val table          = DataTable()
    private val clearButton    = JButton(Constants.LABEL_DELETE_TAGS)
    private val deleteButton   = JButton(Constants.LABEL_DELETE_TRACKS)
    private val downButton     = JButton(Constants.LABEL_MOVE_DOWN)
    private val selectButton   = JButton(Constants.LABEL_SELECT_ALL)
    private val unselectButton = JButton(Constants.LABEL_SELECT_NONE)
    private val upButton       = JButton(Constants.LABEL_MOVE_UP)

    init {
        val scroll = JScrollPane()

        scroll.viewport.view = table
        add(scroll, x = 1, y = 1, w = -1, h = -6)
        add(deleteButton, x = -127, y = -5, w = 20, h = 4)
        add(clearButton, x = -106, y = -5, w = 20, h = 4)
        add(selectButton, x = -84, y = -5, w = 20, h = 4)
        add(unselectButton, x = -63, y = -5, w = 20, h = 4)
        add(downButton, x = -42, y = -5, w = 20, h = 4)
        add(upButton, x = -21, y = -5, w = 20, h = 4)

        clearButton.toolTipText    = Constants.TOOL_DELETE_TAGS
        deleteButton.toolTipText   = Constants.TOOL_DELETE_TRACKS
        downButton.toolTipText     = Constants.TOOL_MOVE_DOWN
        selectButton.toolTipText   = Constants.TOOL_SELECT_ALL
        unselectButton.toolTipText = Constants.TOOL_SELECT_NONE
        upButton.toolTipText       = Constants.TOOL_MOVE_UP
        clearButton.isEnabled      = false
        deleteButton.isEnabled     = false
        downButton.isEnabled       = false
        upButton.isEnabled         = false

        //----------------------------------------------------------------------
        // Remove all tags from all selected tracks
        clearButton.addActionListener {
            if (JOptionPane.showConfirmDialog(Main.window, Constants.MESSAGE_ASK_CLEAR_HTML, Constants.DIALOG_CLEAR, JOptionPane.YES_NO_OPTION) == Constants.YES) {
                Data.removeTagsForAll()
            }
        }

        //----------------------------------------------------------------------
        deleteButton.addActionListener {
            if (JOptionPane.showConfirmDialog(Main.window, Constants.MESSAGE_ASK_DELETE_HTML, Constants.DIALOG_DELETE, JOptionPane.YES_NO_OPTION) == 0) {
                Data.deleteTracks()
            }
        }

        //----------------------------------------------------------------------
        // Move current selected row down in track list
        downButton.addActionListener {
            Data.moveRowDown()
        }

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
        // Move current selected row down up track list
        upButton.addActionListener {
            Data.moveRowUp()
        }

        //----------------------------------------------------------------------
        // Create data model for table, show list of file names and file properties
        table.model = object : AbstractTableModel() {
            override fun getColumnClass(column: Int) = when (column) {
                colSelect -> java.lang.Boolean::class.java
                else -> java.lang.String::class.java
            }

            //------------------------------------------------------------------
            override fun getColumnCount(): Int = 6

            //------------------------------------------------------------------
            override fun getColumnName(column: Int): String = when (column) {
                colSelect -> Constants.LABEL_SELECT
                colTrack -> Constants.LABEL_TRACK
                colFile -> Constants.LABEL_FILE
                colFormat -> Constants.LABEL_FORMAT
                colBitrate -> Constants.LABEL_BITRATE
                colTime -> Constants.LABEL_TIME
                else -> "!"
            }

            //------------------------------------------------------------------
            override fun getRowCount(): Int = Data.tracks.size

            //------------------------------------------------------------------
            override fun getValueAt(row: Int, column: Int): Any {
                val track = Data.getTrack(row)

                if (track != null) {
                    when (column) {
                        colSelect -> return track.isSelected
                        colTrack -> return track.track
                        colFile -> return track.fileName
                        colFormat -> return track.formatInfo
                        colBitrate -> return track.bitrateInfo
                        colTime -> return track.timeInfo
                    }
                }

                return ""
            }

            //------------------------------------------------------------------
            override fun isCellEditable(row: Int, column: Int): Boolean = when(column) {
                colSelect -> true
                colTrack -> true
                colFile -> true
                else -> false
            }

            //------------------------------------------------------------------
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
        table.setColumnWidth(colSelect, min = 50, pref = 50, max = 50)
        table.setColumnWidth(colTrack, min = 50, pref = 50, max = 50)
        table.setColumnWidth(colFile, min = 150, pref = 500, max = 10000)
        table.setColumnWidth(colFormat, min = 75, pref = 75, max = 100)
        table.setColumnWidth(colBitrate, min = 75, pref = 75, max = 100)
        table.setColumnWidth(colTime, min = 75, pref = 75, max = 100)
        table.setShowGrid(false)
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        table.setColumnAlign(colTrack, SwingConstants.CENTER)
        table.setColumnAlign(colBitrate, SwingConstants.RIGHT)
        table.setColumnAlign(colTime, SwingConstants.RIGHT)

        //----------------------------------------------------------------------
        // Enable table header for receiving mouse clicks so data can be sorted
        table.tableHeader.addMouseListener(object : TableHeader() {
            override fun mouseClicked(event: MouseEvent?) {
                when (columnIndex(event)) {
                    colSelect -> Data.sortTracks(Data.Sort.SELECTED, isControlDown(event))
                    colTrack -> Data.sortTracks(Data.Sort.TRACK, isControlDown(event))
                    colFile -> Data.sortTracks(Data.Sort.FILE, isControlDown(event))
                    colFormat -> Data.sortTracks(Data.Sort.FORMAT, isControlDown(event))
                    colBitrate -> Data.sortTracks(Data.Sort.BITRATE, isControlDown(event))
                    colTime -> Data.sortTracks(Data.Sort.TIME, isControlDown(event))
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
                        clearButton.isEnabled  = Data.countSelected > 0
                        deleteButton.isEnabled = Data.countSelected > 0
                        repaint()
                    }
                    TrackEvent.LIST_UPDATED -> {
                        table.fireModel()
                        clearButton.isEnabled  = Data.countSelected > 0
                        deleteButton.isEnabled = Data.countSelected > 0
                        upButton.isEnabled     = Data.selectedRow > 0
                        downButton.isEnabled   = (Data.selectedRow > -1 && Data.selectedRow < Data.tracks.size - 1)
                    }
                    TrackEvent.ITEM_SELECTED -> {
                        table.selectRow        = Data.selectedRow
                        deleteButton.isEnabled = Data.countSelected > 0
                        upButton.isEnabled     = Data.selectedRow > 0
                        downButton.isEnabled   = (Data.selectedRow > -1 && Data.selectedRow < Data.tracks.size - 1)
                    }
                    TrackEvent.ITEM_IMAGE -> {
                    }
                }
             }
        })
    }
}
