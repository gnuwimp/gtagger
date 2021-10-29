/*
 * Copyright © 2021 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audiotageditor

import gnuwimp.swing.ComboBox
import gnuwimp.swing.LayoutPanel
import gnuwimp.swing.Swing
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JLabel
import javax.swing.JTextField

/**
 * A panel with options for renaming track titles
 */
class TabTitleOptions : LayoutPanel(size = Swing.defFont.size / 2 + 1) {
    private val appendTextCheck     = JCheckBox(Constants.LABEL_APPEND_TEXT)
    private val appendTextInput     = JTextField()
    private val applyButton         = JButton(Constants.LABEL_APPLY_CHANGES)
    private val insertAlbumCheck    = JCheckBox(Constants.LABEL_INSERT_ALBUM)
    private val insertAlbumInput    = JTextField()
    private val insertArtistCheck   = JCheckBox(Constants.LABEL_INSERT_ARTIST)
    private val insertArtistInput   = JTextField()
    private val insertTextCheck     = JCheckBox(Constants.LABEL_INSERT_TEXT)
    private val insertTextInput     = JTextField()
    private val numberCheck         = JCheckBox(Constants.LABEL_TRACK)
    private val numberCombo         = ComboBox<String>(Constants.OPTIONS_NUMBER)
    private val numberSepLabel      = JLabel(Constants.LABEL_NUMBER_SEP)
    private val numberSepInput      = JTextField()
    private val removeLeadingCheck  = JCheckBox(Constants.LABEL_REMOVE_LEAD)
    private val removeTextCheck     = JCheckBox(Constants.LABEL_REMOVE_TEXT)
    private val removeTextInput     = JTextField()
    private val removeTrailingCheck = JCheckBox(Constants.LABEL_REMOVE_TRAIL)
    private val replaceTextCheck    = JCheckBox(Constants.LABEL_REPLACE_TEXT)
    private val replaceTextInput    = JTextField()
    private val resetButton         = JButton(Constants.LABEL_RESET)
    private val saveButton          = JButton(Constants.LABEL_SAVE)
    private val setTitleCheck       = JCheckBox(Constants.LABEL_SET_TITLE)
    private val setTitleInput       = JTextField()
    private val titleCapCheck       = JCheckBox(Constants.LABEL_TITLE)
    private val titleCapCombo       = ComboBox<String>(Constants.OPTIONS_CAP_TITLE)
    private val undoButton          = JButton(Constants.LABEL_UNDO)
    private val useFilenameCheck    = JCheckBox(Constants.LABEL_USE_FILENAME)

    init {
        val lw = 24
        var yp = 1

        add(useFilenameCheck, x = 1, y = yp, w = lw, h = 4)

        yp += 5
        add(removeLeadingCheck, x = 1, y = yp, w = lw, h = 4)

        yp += 5
        add(removeTrailingCheck, x = 1, y = yp, w = lw, h = 4)

        yp += 5
        add(setTitleCheck, x = 1, y = yp, w = lw, h = 4)
        add(setTitleInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(removeTextCheck, x = 1, y = yp, w = lw, h = 4)
        add(removeTextInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(replaceTextCheck, x = 1, y = yp, w = lw, h = 4)
        add(replaceTextInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(insertAlbumCheck, x = 1, y = yp, w = lw, h = 4)
        add(insertAlbumInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(insertArtistCheck, x = 1, y = yp, w = lw, h = 4)
        add(insertArtistInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(insertTextCheck, x = 1, y = yp, w = lw, h = 4)
        add(insertTextInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(appendTextCheck, x = 1, y = yp, w = lw, h = 4)
        add(appendTextInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(titleCapCheck, x = 1, y = yp, w = lw, h = 4)
        add(titleCapCombo, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(numberCheck, x = 1, y = yp, w = lw, h = 4)
        add(numberCombo, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(numberSepLabel, x = 1, y = yp, w = lw, h = 4)
        add(numberSepInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp = -20
        add(applyButton, x = 1, y = yp, w = -1, h = 4)

        yp += 5
        add(undoButton, x = 1, y = yp, w = -1, h = 4)

        yp += 5
        add(resetButton, x = 1, y = yp, w = -1, h = 4)

        yp += 5
        add(saveButton, x = 1, y = yp, w = -1, h = 4)

        appendTextCheck.toolTipText     = Constants.TOOL_APPEND_TEXT
        applyButton.toolTipText         = Constants.TOOL_APPLY_CHANGES
        insertAlbumCheck.toolTipText    = Constants.TOOL_INSERT_ALBUM
        insertArtistCheck.toolTipText   = Constants.TOOL_INSERT_ARTIST
        insertTextCheck.toolTipText     = Constants.TOOL_INSERT_TEXT
        numberCheck.toolTipText         = Constants.TOOL_NUMBER
        numberSepLabel.toolTipText      = Constants.TOOL_NUMBER_SEP
        removeLeadingCheck.toolTipText  = Constants.TOOL_REMOVE_LEAD
        removeTextCheck.toolTipText     = Constants.TOOL_REMOVE_TEXT
        removeTrailingCheck.toolTipText = Constants.TOOL_REMOVE_TRAIL
        replaceTextCheck.toolTipText    = Constants.TOOL_REPLACE_TEXT
        resetButton.toolTipText         = Constants.TOOL_RESET
        saveButton.toolTipText          = Constants.TOOL_SAVE
        setTitleCheck.toolTipText       = Constants.TOOL_SET_TITLE
        titleCapCheck.toolTipText       = Constants.TOOL_NAME_CAP
        undoButton.toolTipText          = Constants.TOOL_UNDO
        useFilenameCheck.toolTipText    = Constants.TOOL_USE_FILENAME

        reset()

        //----------------------------------------------------------------------
        // Update tags but do NOT save changes
        applyButton.addActionListener {
            Data.renameTitles(values())
        }

        //----------------------------------------------------------------------
        // Reset all options to the default values
        resetButton.addActionListener {
            reset()
        }

        //----------------------------------------------------------------------
        // Save all changed tracks
        saveButton.addActionListener {
            Data.saveTracks()
        }

        //----------------------------------------------------------------------
        // Reset all changes to the tracks
        undoButton.addActionListener {
            Data.copyTagsFromAudio()
            Data.sendUpdate(TrackEvent.LIST_UPDATED)
        }

        //----------------------------------------------------------------------
        // Listener callback for data changes
        Data.addListener(object : TrackListener {
            override fun update(event: TrackEvent) {
                when (event) {
                    TrackEvent.ITEM_DIRTY -> {
                        saveButton.isEnabled = Data.isAnyChangedAndSelected
                    }
                    TrackEvent.LIST_UPDATED -> {
                        applyButton.isEnabled = Data.tracks.isNotEmpty()
                        undoButton.isEnabled  = Data.tracks.isNotEmpty()
                        saveButton.isEnabled  = Data.isAnyChangedAndSelected
                    }
                    TrackEvent.ITEM_IMAGE -> {
                    }
                    TrackEvent.ITEM_SELECTED -> {
                    }
                }
            }
        })
    }

    //----------------------------------------------------------------------
    // Reset album options to default values
    private fun reset() {
        useFilenameCheck.isSelected    = false
        removeLeadingCheck.isSelected  = false
        removeTrailingCheck.isSelected = false
        setTitleCheck.isSelected       = false
        setTitleInput.text             = ""
        removeTextCheck.isSelected     = false
        removeTextInput.text           = ""
        replaceTextCheck.isSelected    = false
        replaceTextInput.text          = ""
        insertAlbumCheck.isSelected    = false
        insertAlbumInput.text          = Constants.DEF_INSERT_ALBUM
        insertArtistCheck.isSelected   = false
        insertArtistInput.text         = Constants.DEF_INSERT_ARTIST
        insertTextCheck.isSelected     = false
        insertTextInput.text           = ""
        appendTextCheck.isSelected     = false
        appendTextInput.text           = ""
        titleCapCheck.isSelected       = false
        titleCapCombo.selectedIndex    = 0
        numberCheck.isSelected         = false
        numberCombo.selectedIndex      = 0
        numberSepInput.text            = Constants.DEF_NUMBER_SEP
    }

    //----------------------------------------------------------------------
    // Return a string map with options set.
    private fun values(): Map<String, String> {
        val map = mutableMapOf<String, String>()

        if (useFilenameCheck.isSelected == true)    map["filename"]      = "true"
        if (removeLeadingCheck.isSelected == true)  map["leading"]       = "true"
        if (removeTrailingCheck.isSelected == true) map["trailing"]      = "true"
        if (setTitleCheck.isSelected == true)       map["set"]           = setTitleInput.text
        if (removeTextCheck.isSelected == true)     map["remove"]        = removeTextInput.text
        if (replaceTextCheck.isSelected == true)    map["replace"]       = replaceTextInput.text
        if (insertAlbumCheck.isSelected == true)    map["insert_album"]  = insertAlbumInput.text
        if (insertArtistCheck.isSelected == true)   map["insert_artist"] = insertArtistInput.text
        if (insertTextCheck.isSelected == true)     map["insert"]        = insertTextInput.text
        if (appendTextCheck.isSelected == true)     map["append"]        = appendTextInput.text

        if (titleCapCheck.isSelected == true) {
            map["cap_name"] = when (titleCapCombo.selectedIndex) {
                0 -> "words"
                1 -> "lower"
                2 -> "upper"
                else -> ""
            }
        }

        if (numberCheck.isSelected == true) {
            map["number"] = when (numberCombo.selectedIndex) {
                0 -> "insert_1"
                1 -> "insert_2"
                2 -> "insert_3"
                3 -> "insert_4"
                4 -> "append_1"
                5 -> "append_2"
                6 -> "append_3"
                7 -> "append_4"
                else -> ""
            }
        }

        map["number_sep"] = numberSepInput.text

        return map
    }
}
