/*
 * Copyright 2021 - 2025 gnuwimp@gmail.com
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
 * Table for album data.
 */
class TabAlbumTable : LayoutPanel(size = Swing.defFont.size / 2 + 1) {
    private val colSelect       = 0
    private val colTrack        = 1
    private val colArtist       = 2
    private val colAlbum        = 3
    private val colTitle        = 4
    private val colGenre        = 5
    private val colAlbumArtist  = 6
    private val table           = DataTable()
    private val filterButton    = JButton(Constants.LABEL_FILTER)
    private val selectButton    = JButton(Constants.LABEL_SELECT_ALL)
    private val unselectButton  = JButton(Constants.LABEL_SELECT_NONE)

    init {
        val scroll = JScrollPane()

        scroll.viewport.view = table
        add(scroll, x = 1, y = 1, w = -1, h = -6)
        add(filterButton, x = -63, y = -5, w = 20, h = 4)
        add(selectButton, x = -42, y = -5, w = 20, h = 4)
        add(unselectButton, x = -21, y = -5, w = 20, h = 4)

        filterButton.toolTipText   = Constants.TOOL_SELECT_ALBUM
        selectButton.toolTipText   = Constants.TOOL_SELECT_ALL
        unselectButton.toolTipText = Constants.TOOL_SELECT_NONE

        //----------------------------------------------------------------------
        // Search and select tracks
        filterButton.addActionListener {
            val answer = JOptionPane.showInputDialog(Main.window, Constants.MESSAGE_ASK_FILTER_ALBUM, Constants.DIALOG_FILTER, JOptionPane.YES_NO_OPTION)

            if (answer.isNullOrBlank() == false) {
                Data.filterOnAlbums(answer)
            }
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
        // Create data model for table, show album info, one row per track
        table.model = object : AbstractTableModel() {
            override fun getColumnClass(column: Int) = when (column) {
                colSelect -> java.lang.Boolean::class.java
                else -> java.lang.String::class.java
            }

            //------------------------------------------------------------------
            override fun getColumnCount(): Int = 7

            //------------------------------------------------------------------
            override fun getColumnName(column: Int): String = when (column) {
                colSelect -> Constants.LABEL_SELECT
                colTrack -> Constants.LABEL_TRACK
                colArtist -> Constants.LABEL_ARTIST
                colAlbum -> Constants.LABEL_ALBUM
                colTitle -> Constants.LABEL_TITLE
                colGenre -> Constants.LABEL_GENRE
                colAlbumArtist -> Constants.LABEL_ALBUM_ARTIST
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
                        colArtist -> return track.artist
                        colAlbum -> return track.album
                        colTitle -> return track.title
                        colGenre -> return track.genre
                        colAlbumArtist -> return track.albumArtist
                    }
                }

                return "!"
            }

            //------------------------------------------------------------------
            override fun isCellEditable(row: Int, column: Int): Boolean = true
//            when(column) {
//                colSelect -> true
//                colArtist -> true
//                colAlbum -> true
//                colArtist -> true
//                colTitle -> true
//                else -> false
//            }

            //------------------------------------------------------------------
            override fun setValueAt(value: Any?, row: Int, column: Int) {
                val track = Data.getTrack(row)

                if (track != null) {
                    if (column == colSelect) {
                        track.isSelected = value as Boolean
                        Data.sendUpdate(TrackEvent.ITEM_DIRTY)
                    }
                    else {
                        val value2 = value as String

                        if (column == colTrack && value2.numOrZero in 1..9999 && value2 != track.track) {
                            track.track = value2
                            Data.sendUpdate(TrackEvent.ITEM_DIRTY)
                        }
                        else if (column == colArtist && value2 != track.artist) {
                            track.artist = value2
                            Data.sendUpdate(TrackEvent.ITEM_DIRTY)
                        }
                        else if (column == colAlbum && value2 != track.album) {
                            track.album = value2
                            Data.sendUpdate(TrackEvent.ITEM_DIRTY)
                        }
                        else if (column == colTitle && value2 != track.title) {
                            track.title = value2
                            Data.sendUpdate(TrackEvent.ITEM_DIRTY)
                        }
                        else if (column == colGenre && value2 != track.genre) {
                            track.genre = value2
                            Data.sendUpdate(TrackEvent.ITEM_DIRTY)
                        }
                        else if (column == colAlbumArtist && value2 != track.albumArtist) {
                            track.albumArtist = value2
                            Data.sendUpdate(TrackEvent.ITEM_DIRTY)
                        }
                    }
                }
            }
        }

        table.tableHeader.toolTipText = Constants.TOOL_TABLE_HEAD
        table.setColumnWidth(colSelect, min = 50, pref = 50, max = 100)
        table.setColumnWidth(colTrack, min = 50, pref = 50, max = 100)
        table.setColumnWidth(colArtist, min = 50, pref = 300, max = 10000)
        table.setColumnWidth(colAlbum, min = 50, pref = 300, max = 10000)
        table.setColumnWidth(colTitle, min = 50, pref = 300, max = 10000)
        table.setColumnWidth(colGenre, min = 50, pref = 200, max = 10000)
        table.setColumnWidth(colAlbumArtist, min = 50, pref = 50, max = 10000)
        table.setShowGrid(false)
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        table.setColumnAlign(colTrack, SwingConstants.CENTER)


        //----------------------------------------------------------------------
        // Enable table header for receiving mouse clicks so data can be sorted
        table.tableHeader.addMouseListener(object : TableHeader() {
            override fun mouseClicked(event: MouseEvent?) {
                when (columnIndex(event)) {
                    colSelect -> Data.sortTracks(Data.Sort.SELECTED, isControlDown(event))
                    colTrack -> Data.sortTracks(Data.Sort.TRACK, isControlDown(event))
                    colArtist -> Data.sortTracks(Data.Sort.ARTIST, isControlDown(event))
                    colAlbum -> Data.sortTracks(Data.Sort.ALBUM, isControlDown(event))
                    colTitle -> Data.sortTracks(Data.Sort.TITLE, isControlDown(event))
                    colGenre -> Data.sortTracks(Data.Sort.GENRE, isControlDown(event))
                    colAlbumArtist -> Data.sortTracks(Data.Sort.ALBUM_ARTIST, isControlDown(event))
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

        Data.addListener(object : TrackListener {
            //------------------------------------------------------------------
            // Listener callback for data changes
            override fun update(event: TrackEvent) {
                when (event) {
                    TrackEvent.ITEM_DIRTY -> {
                        repaint()
                    }
                    TrackEvent.LIST_UPDATED -> {
                        table.fireModel()
                        selectButton.isEnabled   = Data.tracks.isNotEmpty()
                        unselectButton.isEnabled = Data.tracks.isNotEmpty()
                        filterButton.isEnabled   = Data.tracks.isNotEmpty()
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
