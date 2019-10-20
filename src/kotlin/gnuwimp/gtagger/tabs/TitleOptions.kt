/*
 * Copyright 2016 - 2019 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.gtagger.tabs

import gnuwimp.core.swing.ComboBoxCallback
import gnuwimp.core.swing.LayoutPanel
import gnuwimp.core.swing.Platform
import gnuwimp.gtagger.TrackEvent
import gnuwimp.gtagger.TrackListener
import gnuwimp.gtagger.data.Data
import gnuwimp.gtagger.resource.Text
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JLabel
import javax.swing.JTextField

/**
 * A panel with options for renaming track titles
 */
class TitleOptions : LayoutPanel(size = Platform.defFont.size / 2) {
    private val appendTextCheck     = JCheckBox(Text.LABEL_APPEND_TEXT)
    private val appendTextInput     = JTextField()
    private val applyButton         = JButton(Text.LABEL_APPLY_CHANGES)
    private val insertAlbumCheck    = JCheckBox(Text.LABEL_INSERT_ALBUM)
    private val insertAlbumInput    = JTextField()
    private val insertArtistCheck   = JCheckBox(Text.LABEL_INSERT_ARTIST)
    private val insertArtistInput   = JTextField()
    private val insertTextCheck     = JCheckBox(Text.LABEL_INSERT_TEXT)
    private val insertTextInput     = JTextField()
    private val numberCheck         = JCheckBox(Text.LABEL_TRACK)
    private val numberCombo         = ComboBoxCallback<String>(Text.OPTIONS_NUMBER)
    private val numberSepLabel      = JLabel(Text.LABEL_NUMBER_SEP)
    private val numberSepInput      = JTextField()
    private val removeLeadingCheck  = JCheckBox(Text.LABEL_REMOVE_LEAD)
    private val removeTextCheck     = JCheckBox(Text.LABEL_REMOVE_TEXT)
    private val removeTextInput     = JTextField()
    private val removeTrailingCheck = JCheckBox(Text.LABEL_REMOVE_TRAIL)
    private val replaceTextCheck    = JCheckBox(Text.LABEL_REPLACE_TEXT)
    private val replaceTextInput    = JTextField()
    private val resetButton         = JButton(Text.LABEL_RESET)
    private val saveButton          = JButton(Text.LABEL_SAVE)
    private val setTitleCheck       = JCheckBox(Text.LABEL_SET_TITLE)
    private val setTitleInput       = JTextField()
    private val titleCapCheck       = JCheckBox(Text.LABEL_TITLE)
    private val titleCapCombo       = ComboBoxCallback<String>(Text.OPTIONS_CAP_TITLE)
    private val undoButton          = JButton(Text.LABEL_UNDO)
    private val useFilenameCheck    = JCheckBox(Text.LABEL_USE_FILENAME)

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

        yp = -25
        add(applyButton, x = 1, y = yp, w = -1, h = 4)

        yp += 5
        add(undoButton, x = 1, y = yp, w = -1, h = 4)

        yp += 5
        add(resetButton, x = 1, y = yp, w = -1, h = 4)

        yp += 5
        add(saveButton, x = 1, y = yp, w = -1, h = 4)

        appendTextCheck.toolTipText     = Text.TOOL_APPEND_TEXT
        applyButton.toolTipText         = Text.TOOL_APPLY_CHANGES
        insertAlbumCheck.toolTipText    = Text.TOOL_INSERT_ALBUM
        insertArtistCheck.toolTipText   = Text.TOOL_INSERT_ARTIST
        insertTextCheck.toolTipText     = Text.TOOL_INSERT_TEXT
        numberCheck.toolTipText         = Text.TOOL_NUMBER
        numberSepLabel.toolTipText      = Text.TOOL_NUMBER_SEP
        removeLeadingCheck.toolTipText  = Text.TOOL_REMOVE_LEAD
        removeTextCheck.toolTipText     = Text.TOOL_REMOVE_TEXT
        removeTrailingCheck.toolTipText = Text.TOOL_REMOVE_TRAIL
        replaceTextCheck.toolTipText    = Text.TOOL_REPLACE_TEXT
        resetButton.toolTipText         = Text.TOOL_RESET
        saveButton.toolTipText          = Text.TOOL_SAVE
        setTitleCheck.toolTipText       = Text.TOOL_SET_TITLE
        titleCapCheck.toolTipText       = Text.TOOL_NAME_CAP
        undoButton.toolTipText          = Text.TOOL_UNDO
        useFilenameCheck.toolTipText    = Text.TOOL_USE_FILENAME

        reset()

        // Update tags but do NOT save changes
        applyButton.addActionListener {
            Data.renameTitles(values())
        }

        // Reset all options to the default values
        resetButton.addActionListener {
            reset()
        }

        // Save all changed tracks
        saveButton.addActionListener {
            Data.saveTracks()
        }

        // Reset all changes to the tracks
        undoButton.addActionListener {
            Data.copyTagsFromAudio()
            Data.sendUpdate(TrackEvent.LIST_UPDATED)
        }

        // Listener callback for data changes
        Data.addListener(object : TrackListener {
            override fun update(event: TrackEvent) {
                when (event) {
                    TrackEvent.ITEM_DIRTY -> saveButton.isEnabled = Data.isAnyChangedAndSelected
                    TrackEvent.LIST_UPDATED -> {
                        applyButton.isEnabled = Data.tracks.isNotEmpty()
                        undoButton.isEnabled  = Data.tracks.isNotEmpty()
                        saveButton.isEnabled  = Data.isAnyChangedAndSelected
                    }
                    TrackEvent.ITEM_IMAGE -> Unit
                    TrackEvent.ITEM_SELECTED -> Unit
                }
            }
        })
    }

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
        insertAlbumInput.text          = Text.DEF_INSERT_ALBUM
        insertArtistCheck.isSelected   = false
        insertArtistInput.text         = Text.DEF_INSERT_ARTIST
        insertTextCheck.isSelected     = false
        insertTextInput.text           = ""
        appendTextCheck.isSelected     = false
        appendTextInput.text           = ""
        titleCapCheck.isSelected       = false
        titleCapCombo.selectedIndex    = 0
        numberCheck.isSelected         = false
        numberCombo.selectedIndex      = 0
        numberSepInput.text            = Text.DEF_NUMBER_SEP
    }

    /**
     * Return a string map with options set
     */
    private fun values(): Map<String, String> {
        val map = mutableMapOf<String, String>()

        if (useFilenameCheck.isSelected)    map["filename"]      = "true"
        if (removeLeadingCheck.isSelected)  map["leading"]       = "true"
        if (removeTrailingCheck.isSelected) map["trailing"]      = "true"
        if (setTitleCheck.isSelected)       map["set"]           = setTitleInput.text
        if (removeTextCheck.isSelected)     map["remove"]        = removeTextInput.text
        if (replaceTextCheck.isSelected)    map["replace"]       = replaceTextInput.text
        if (insertAlbumCheck.isSelected)    map["insert_album"]  = insertAlbumInput.text
        if (insertArtistCheck.isSelected)   map["insert_artist"] = insertArtistInput.text
        if (insertTextCheck.isSelected)     map["insert"]        = insertTextInput.text
        if (appendTextCheck.isSelected)     map["append"]        = appendTextInput.text

        if (titleCapCheck.isSelected) {
            map["cap_name"] = when (titleCapCombo.selectedIndex) {
                0 -> "words"
                1 -> "lower"
                2 -> "upper"
                else -> ""
            }
        }

        if (numberCheck.isSelected) {
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
