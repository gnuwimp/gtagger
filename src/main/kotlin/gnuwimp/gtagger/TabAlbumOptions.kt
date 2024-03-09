/*
 * Copyright 2016 - 2021 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.gtagger

import gnuwimp.swing.ComboBox
import gnuwimp.swing.ImageFileDialog
import gnuwimp.swing.LayoutPanel
import gnuwimp.swing.Swing
import java.io.File
import java.util.prefs.Preferences
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JLabel
import javax.swing.JTextField

/**
 * Option panel for changing album tags.
 */
class TabAlbumOptions(private val pref: Preferences) : LayoutPanel(size = Swing.defFont.size / 2) {
    private val albumArtistCheck = JCheckBox(Labels.LABEL_ALBUM_ARTIST)
    private val albumArtistInput = JTextField()
    private val albumCheck       = JCheckBox(Labels.LABEL_ALBUM)
    private val albumInput       = JTextField()
    private val applyButton      = JButton(Labels.LABEL_APPLY_CHANGES)
    private val artistCheck      = JCheckBox(Labels.LABEL_ARTIST)
    private val artistInput      = JTextField()
    private val commentCheck     = JCheckBox(Labels.LABEL_COMMENT)
    private val commentInput     = JTextField()
    private val composerCheck    = JCheckBox(Labels.LABEL_COMPOSER)
    private val composerInput    = JTextField()
    private val copyCheck        = JCheckBox(Labels.LABEL_COPY_ARTIST)
    private val copyCombo        = ComboBox<String>(Labels.OPTIONS_COPY_ARTIST)
    private val coverCheck       = JCheckBox(Labels.LABEL_COVER)
    private val coverIcon        = JLabel()
    private val encoderCheck     = JCheckBox(Labels.LABEL_ENCODER)
    private val encoderInput     = JTextField()
    private val genreCheck       = JCheckBox(Labels.LABEL_GENRE)
    private val genreCombo       = ComboBox<String>(strings = ID3Genre.strings.sorted())
    private val loadCoverButton  = JButton(Labels.LABEL_LOAD_IMAGE)
    private val resetButton      = JButton(Labels.LABEL_RESET)
    private val saveButton       = JButton(Labels.LABEL_SAVE)
    private val trackCheck       = JCheckBox(Labels.LABEL_START_TRACK)
    private val trackInput       = JTextField()
    private val undoButton       = JButton(Labels.LABEL_UNDO)
    private val yearCheck        = JCheckBox(Labels.LABEL_SET_YEAR)
    private val yearInput        = JTextField()
    private var coverFile: File? = null

    init {
        val lw = 20
        var yp = 1

        add(artistCheck, x = 1, y = yp, w = lw, h = 4)
        add(artistInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(albumCheck, x = 1, y = yp, w = lw, h = 4)
        add(albumInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(albumArtistCheck, x = 1, y = yp, w = lw, h = 4)
        add(albumArtistInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(copyCheck, x = 1, y = yp, w = lw, h = 4)
        add(copyCombo, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(genreCheck, x = 1, y = yp, w = lw, h = 4)
        add(genreCombo, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(yearCheck, x = 1, y = yp, w = lw, h = 4)
        add(yearInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(trackCheck, x = 1, y = yp, w = lw, h = 4)
        add(trackInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(commentCheck, x = 1, y = yp, w = lw, h = 4)
        add(commentInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(composerCheck, x = 1, y = yp, w = lw, h = 4)
        add(composerInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(encoderCheck, x = 1, y = yp, w = lw, h = 4)
        add(encoderInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(coverCheck, x = 1, y = yp, w = lw, h = 4)
        add(coverIcon, x = lw + 2, y = yp, w = -1, h = (Labels.ICON_SIZE / (Swing.defFont.size / 2)) + 4)

        yp = -25
        add(loadCoverButton, x = 1, y = yp, w = -1, h = 4)

        yp += 5
        add(applyButton, x = 1, y = yp, w = -1, h = 4)

        yp += 5
        add(undoButton, x = 1, y = yp, w = -1, h = 4)

        yp += 5
        add(resetButton, x = 1, y = yp, w = -1, h = 4)

        yp += 5
        add(saveButton, x = 1, y = yp, w = -1, h = 4)

        albumArtistCheck.toolTipText  = Labels.TOOL_ALBUM_ARTIST
        albumCheck.toolTipText        = Labels.TOOL_ALBUM
        applyButton.toolTipText       = Labels.TOOL_APPLY_CHANGES
        artistCheck.toolTipText       = Labels.TOOL_ARTIST
        commentCheck.toolTipText      = Labels.TOOL_COMMENT
        composerCheck.toolTipText     = Labels.TOOL_COMPOSER
        copyCheck.toolTipText         = Labels.TOOL_COPY_ARTIST
        coverCheck.toolTipText        = Labels.TOOL_COVER
        coverIcon.horizontalAlignment = JLabel.LEFT
        encoderCheck.toolTipText      = Labels.TOOL_ENCODER
        genreCheck.toolTipText        = Labels.TOOL_GENRE
        genreCombo.isEditable         = true
        loadCoverButton.toolTipText   = Labels.TOOL_LOAD_IMAGE
        resetButton.toolTipText       = Labels.TOOL_RESET
        saveButton.toolTipText        = Labels.TOOL_SAVE
        trackCheck.toolTipText        = Labels.TOOL_START_TRACK
        undoButton.toolTipText        = Labels.TOOL_UNDO
        yearCheck.toolTipText         = Labels.TOOL_SET_YEAR

        reset()

        //----------------------------------------------------------------------
        // Load cover image for icon
        loadCoverButton.addActionListener {
            val dlg  = ImageFileDialog(pref.picPath, Main.window)
            val file = dlg.file

            coverFile = null

            try {
                if (file != null && file.isFile) {
                    coverIcon.icon = Data.loadIconFromFile(file)
                    coverFile      = file

                    pref.picPath = file.parentFile.canonicalPath
                }
            }
            catch (e: Exception) {
                coverIcon.icon = Data.loadIconFromPath()
                Data.message   = e.message ?: ""
            }
            finally {
                coverIcon.repaint()
            }
        }

        //----------------------------------------------------------------------
        // Update tags but do NOT save changes
        applyButton.addActionListener {
            Data.renameAlbums(values())
        }

        //----------------------------------------------------------------------
        // Reset options to default values and undo changes
        resetButton.addActionListener {
            reset()
            Data.copyTagsFromAudio()
            Data.sendUpdate(TrackEvent.LIST_UPDATED)
        }

        //----------------------------------------------------------------------
        // Save all changed tracks
        saveButton.addActionListener {
            Data.saveTracks()
        }

        //----------------------------------------------------------------------
        // Reset all changes to the tracks that has not been saved
        undoButton.addActionListener {
            Data.copyTagsFromAudio()
            Data.sendUpdate(TrackEvent.LIST_UPDATED)
        }

        Data.addListener(object : TrackListener {
            //------------------------------------------------------------------
            // Listener callback for data changes
            override fun update(event: TrackEvent) {
                if (event == TrackEvent.ITEM_DIRTY) {
                    saveButton.isEnabled = Data.isAnyChangedAndSelected
                }
                else if (event == TrackEvent.LIST_UPDATED) {
                    applyButton.isEnabled = Data.tracks.isNotEmpty()
                    undoButton.isEnabled  = Data.tracks.isNotEmpty()
                    saveButton.isEnabled  = Data.isAnyChangedAndSelected
                }
            }
        })
    }

    //--------------------------------------------------------------------------
    // Reset album options to default values including resetting cover icon.
    private fun reset() {
        artistCheck.isSelected      = false
        artistInput.text            = ""
        albumCheck.isSelected       = false
        albumInput.text             = ""
        albumArtistCheck.isSelected = false
        albumArtistInput.text       = ""
        copyCheck.isSelected        = false
        copyCombo.selectedIndex     = 0
        genreCheck.isSelected       = false
        genreCombo.selectedIndex    = 0
        yearCheck.isSelected        = false
        yearInput.text              = ""
        trackCheck.isSelected       = false
        trackInput.text             = Labels.DEF_START_TRACK
        commentCheck.isSelected     = false
        commentInput.text           = ""
        composerCheck.isSelected    = false
        composerInput.text          = ""
        encoderCheck.isSelected     = false
        encoderInput.text           = ""
        coverCheck.isSelected       = false
        coverIcon.icon              = Data.loadIconFromPath()
    }

    //--------------------------------------------------------------------------
    // Return a string map with options set.
    private fun values(): Map<String, String> {
        val map = mutableMapOf<String, String>()

        if (artistCheck.isSelected == true)      map["artist"]       = artistInput.text
        if (albumCheck.isSelected == true)       map["album"]        = albumInput.text
        if (albumArtistCheck.isSelected == true) map["album_artist"] = albumArtistInput.text
        if (genreCheck.isSelected == true)       map["genre"]        = genreCombo.text
        if (yearCheck.isSelected == true)        map["year"]         = yearInput.text
        if (trackCheck.isSelected == true)       map["track"]        = trackInput.text
        if (commentCheck.isSelected == true)     map["comment"]      = commentInput.text
        if (composerCheck.isSelected == true)    map["composer"]     = composerInput.text
        if (encoderCheck.isSelected == true)     map["encoder"]      = encoderInput.text
        if (coverCheck.isSelected == true)       map["cover"]        = coverFile?.canonicalPath ?: ""

        if (copyCheck.isSelected == true) {
            map["copy_artist"] = when (copyCombo.selectedIndex) {
                0 -> "artist_to_albumartist"
                1 -> "album_to_albumartist"
                2 -> "artist+album_to_albumartist"
                3 -> "albumartist_to_artist"
                4 -> "albumartist_to_album"
                else -> ""
            }
        }

        return map
    }
}
