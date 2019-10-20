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
 * Options panel for file renames
 */
class FileOptions : LayoutPanel(size = Platform.defFont.size / 2) {
    private val appendTextCheck     = JCheckBox(Text.LABEL_APPEND_TEXT)
    private val appendTextInput     = JTextField()
    private val applyButton         = JButton(Text.LABEL_APPLY_CHANGES)
    private val extCapCheck         = JCheckBox(Text.LABEL_EXTENSION)
    private val extCapCombo         = ComboBoxCallback<String>(Text.OPTIONS_CAP_EXT)
    private val insertAlbumCheck    = JCheckBox(Text.LABEL_INSERT_ALBUM)
    private val insertAlbumInput    = JTextField()
    private val insertArtistCheck   = JCheckBox(Text.LABEL_INSERT_ARTIST)
    private val insertArtistInput   = JTextField()
    private val insertTextCheck     = JCheckBox(Text.LABEL_INSERT_TEXT)
    private val insertTextInput     = JTextField()
    private val nameCapCheck        = JCheckBox(Text.LABEL_FILENAME)
    private val nameCapCombo        = ComboBoxCallback<String>(Text.OPTIONS_CAP_NAME)
    private val numberCheck         = JCheckBox(Text.LABEL_TRACK)
    private val numberCombo         = ComboBoxCallback<String>(Text.OPTIONS_NUMBER)
    private val numberSep           = JLabel(Text.LABEL_NUMBER_SEP)
    private val numberSepInput      = JTextField()
    private val removeIllegalCheck  = JCheckBox(Text.LABEL_REMOVE_ILLEG)
    private val removeLeadingCheck  = JCheckBox(Text.LABEL_REMOVE_LEAD)
    private val removeTextCheck     = JCheckBox(Text.LABEL_REMOVE_TEXT)
    private val removeTextInput     = JTextField()
    private val removeTrailingCheck = JCheckBox(Text.LABEL_REMOVE_TRAIL)
    private val replaceTextCheck    = JCheckBox(Text.LABEL_REPLACE_TEXT)
    private val replaceTextInput    = JTextField()
    private val resetButton         = JButton(Text.LABEL_RESET)
    private val saveButton          = JButton(Text.LABEL_SAVE)
    private val setFileNameCheck    = JCheckBox(Text.LABEL_SET_FILENAME)
    private val setFileNameInput    = JTextField()
    private val undoButton          = JButton(Text.LABEL_UNDO)
    private val useTitleCheck       = JCheckBox(Text.LABEL_USE_TITLE)

    init {
        val lw = 24
        var yp = 1

        add(useTitleCheck, x = 1, y = yp, w = lw, h = 4)

        yp += 5
        add(removeLeadingCheck, x = 1, y = yp, w = lw, h = 4)

        yp += 5
        add(removeTrailingCheck, x = 1, y = yp, w = lw, h = 4)

        yp += 5
        add(setFileNameCheck, x = 1, y = yp, w = lw, h = 4)
        add(setFileNameInput, x = lw + 2, y = yp, w = -1, h = 4)

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
        add(nameCapCheck, x = 1, y = yp, w = lw, h = 4)
        add(nameCapCombo, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(extCapCheck, x = 1, y = yp, w = lw, h = 4)
        add(extCapCombo, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(numberCheck, x = 1, y = yp, w = lw, h = 4)
        add(numberCombo, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(numberSep, x = 1, y = yp, w = lw, h = 4)
        add(numberSepInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(removeIllegalCheck, x = 1, y = yp, w = -1, h = 4)

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
        extCapCheck.toolTipText         = Text.TOOL_EXT_CAP
        insertAlbumCheck.toolTipText    = Text.TOOL_INSERT_ALBUM
        insertArtistCheck.toolTipText   = Text.TOOL_INSERT_ARTIST
        insertTextCheck.toolTipText     = Text.TOOL_INSERT_TEXT
        nameCapCheck.toolTipText        = Text.TOOL_NAME_CAP
        numberCheck.toolTipText         = Text.TOOL_NUMBER
        numberSep.toolTipText           = Text.TOOL_NUMBER_SEP
        removeIllegalCheck.toolTipText  = Text.TOOL_REMOVE_ILLEG
        removeLeadingCheck.toolTipText  = Text.TOOL_REMOVE_LEAD
        removeTextCheck.toolTipText     = Text.TOOL_REMOVE_TEXT
        removeTrailingCheck.toolTipText = Text.TOOL_REMOVE_TRAIL
        replaceTextCheck.toolTipText    = Text.TOOL_REPLACE_TEXT
        resetButton.toolTipText         = Text.TOOL_RESET
        saveButton.toolTipText          = Text.TOOL_SAVE
        setFileNameCheck.toolTipText    = Text.TOOL_SET_FILENAME
        undoButton.toolTipText          = Text.TOOL_UNDO
        useTitleCheck.toolTipText       = Text.TOOL_USE_TITLE

        reset()

        // Update tags but do NOT save changes
        applyButton.addActionListener {
            Data.renameFiles(values())
        }

        // Reset options to default values
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
                    TrackEvent.ITEM_SELECTED -> Unit
                    TrackEvent.ITEM_IMAGE -> Unit
                }
            }
        })
    }

    /**
     * Reset album options to default values
     */
    private fun reset() {
        useTitleCheck.isSelected       = false
        removeLeadingCheck.isSelected  = false
        removeTrailingCheck.isSelected = false
        setFileNameCheck.isSelected    = false
        setFileNameInput.text          = ""
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
        nameCapCheck.isSelected        = false
        nameCapCombo.selectedIndex     = 0
        extCapCheck.isSelected         = false
        extCapCombo.selectedIndex      = 0
        numberCheck.isSelected         = false
        numberCombo.selectedIndex      = 0
        numberSepInput.text            = Text.DEF_NUMBER_SEP
        removeIllegalCheck.isSelected  = true
    }

    /**
     * Return a string map with options set
     */
    private fun values(): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()

        if (useTitleCheck.isSelected)       map["title"]         = "true"
        if (removeLeadingCheck.isSelected)  map["leading"]       = "true"
        if (removeTrailingCheck.isSelected) map["trailing"]      = "true"
        if (setFileNameCheck.isSelected)    map["set"]           = setFileNameInput.text
        if (removeTextCheck.isSelected)     map["remove"]        = removeTextInput.text
        if (replaceTextCheck.isSelected)    map["replace"]       = replaceTextInput.text
        if (insertAlbumCheck.isSelected)    map["insert_album"]  = insertAlbumInput.text
        if (insertArtistCheck.isSelected)   map["insert_artist"] = insertArtistInput.text
        if (insertTextCheck.isSelected)     map["insert"]        = insertTextInput.text
        if (appendTextCheck.isSelected)     map["append"]        = appendTextInput.text
        if (removeIllegalCheck.isSelected)  map["illegal"]       = "true"

        if (nameCapCheck.isSelected) {
            map["cap_name"] = when (nameCapCombo.selectedIndex) {
                0 -> "words"
                1 -> "lower"
                2 -> "upper"
                else -> ""
            }
        }

        if (extCapCheck.isSelected) {
            map["cap_ext"] = when (extCapCombo.selectedIndex) {
                0 -> "lower"
                1 -> "upper"
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
