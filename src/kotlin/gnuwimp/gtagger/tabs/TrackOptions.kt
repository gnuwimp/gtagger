/*
 * Copyright 2016 - 2019 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.gtagger.tabs

import gnuwimp.core.swing.ComboBoxCallback
import gnuwimp.core.swing.LayoutPanel
import gnuwimp.core.swing.Platform
import gnuwimp.core.swing.TextFieldCallback
import gnuwimp.core.util.ID3Genre
import gnuwimp.gtagger.Main
import gnuwimp.gtagger.TrackEvent
import gnuwimp.gtagger.TrackListener
import gnuwimp.gtagger.data.Data
import gnuwimp.gtagger.data.Track
import gnuwimp.gtagger.resource.Text
import java.util.prefs.Preferences
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JOptionPane

/**
 * Create a panel with input widgets for all track properties
 */
class TrackOptions(private val pref: Preferences) : LayoutPanel(size = Platform.defFont.size / 2) {
    private val albumLabel        = JLabel(Text.LABEL_ALBUM)
    private val albumArtistLabel  = JLabel(Text.LABEL_ALBUM_ARTIST)
    private val albumArtistInput  = TextFieldCallback(callback = { val track = Data.selectedTrack; if (track != null && it != track.artist) { track.albumArtist = it ; Data.sendUpdate(TrackEvent.ITEM_DIRTY) } })
    private val albumInput        = TextFieldCallback(callback = { val track = Data.selectedTrack; if (track != null && it != track.album) { track.album = it ; Data.sendUpdate(TrackEvent.ITEM_DIRTY) } })
    private val artistLabel       = JLabel(Text.LABEL_ARTIST)
    private val artistInput       = TextFieldCallback(callback = { val track = Data.selectedTrack; if (track != null && it != track.artist) { track.artist = it ; Data.sendUpdate(TrackEvent.ITEM_DIRTY) } })
    private val clearButton       = JButton(Text.LABEL_DELETE_TAGS)
    private val commentLabel      = JLabel(Text.LABEL_COMMENT)
    private val commentInput      = TextFieldCallback(callback = { val track = Data.selectedTrack; if (track != null && it != track.comment) { track.comment = it ; Data.sendUpdate(TrackEvent.ITEM_DIRTY) } })
    private val composerLabel     = JLabel(Text.LABEL_COMPOSER)
    private val composerInput     = TextFieldCallback(callback = { val track = Data.selectedTrack; if (track != null && it != track.composer) { track.composer = it ; Data.sendUpdate(TrackEvent.ITEM_DIRTY) } })
    private val coverLabel        = JLabel(Text.LABEL_COVER)
    private val coverIcon         = JLabel()
    private val deleteFileButton  = JButton(Text.LABEL_DELETE_TRACK)
    private val encoderLabel      = JLabel(Text.LABEL_ENCODER)
    private val encoderInput      = TextFieldCallback(callback = { val track = Data.selectedTrack; if (track != null && it != track.encoder) { track.encoder = it ; Data.sendUpdate(TrackEvent.ITEM_DIRTY) } })
    private val filenameLabel     = JLabel(Text.LABEL_FILENAME)
    private val filenameInput     = TextFieldCallback(callback = { val track = Data.selectedTrack; if (track != null && it != track.fileName) { track.fileName = it ; Data.sendUpdate(TrackEvent.ITEM_DIRTY) } })
    private val genreLabel        = JLabel(Text.LABEL_GENRE)
    private val genreCombo        = ComboBoxCallback<String>(strings = ID3Genre.strings.sorted(), callback = { val track = Data.selectedTrack; if (track != null && it != track.genre) { track.genre = it ; Data.sendUpdate(TrackEvent.ITEM_DIRTY) } })
    private val loadCoverButton   = JButton(Text.LABEL_LOAD_IMAGE)
    private val removeCoverButton = JButton(Text.LABEL_REMOVE_COVER)
    private val saveButton        = JButton(Text.LABEL_SAVE)
    private val titleLabel        = JLabel(Text.LABEL_TITLE)
    private val titleInput        = TextFieldCallback(callback = { val track = Data.selectedTrack; if (track != null && it != track.title) { track.title = it ; Data.sendUpdate(TrackEvent.ITEM_DIRTY) } })
    private val trackLabel        = JLabel(Text.LABEL_TRACK)
    private val trackInput        = TextFieldCallback(callback = { val track = Data.selectedTrack; if (track != null && it != track.track) { track.track = it ; Data.sendUpdate(TrackEvent.ITEM_DIRTY) } })
    private val yearLabel         = JLabel(Text.LABEL_YEAR)
    private val yearInput         = TextFieldCallback(callback = { val track = Data.selectedTrack; if (track != null && it != track.year) { track.year = it ; Data.sendUpdate(TrackEvent.ITEM_DIRTY) } })
    private val activateWidgets: List<JComponent> = listOf(artistInput, albumInput, albumArtistInput, titleInput, yearInput, genreCombo, trackInput, commentInput, composerInput, encoderInput, filenameInput, loadCoverButton)

    init {
        val lw = 16
        var yp = 1

        add(artistLabel, x = 1, y = yp, w = lw, h = 4)
        add(artistInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(albumLabel, x = 1, y = yp, w = lw, h = 4)
        add(albumInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(albumArtistLabel, x = 1, y = yp, w = lw, h = 4)
        add(albumArtistInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(titleLabel, x = 1, y = yp, w = lw, h = 4)
        add(titleInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(yearLabel, x = 1, y = yp, w = lw, h = 4)
        add(yearInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(genreLabel, x = 1, y = yp, w = lw, h = 4)
        add(genreCombo, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(trackLabel, x = 1, y = yp, w = lw, h = 4)
        add(trackInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(commentLabel, x = 1, y = yp, w = lw, h = 4)
        add(commentInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(composerLabel, x = 1, y = yp, w = lw, h = 4)
        add(composerInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(encoderLabel, x = 1, y = yp, w = lw, h = 4)
        add(encoderInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(filenameLabel, x = 1, y = yp, w = lw, h = 4)
        add(filenameInput, x = lw + 2, y = yp, w = -1, h = 4)

        yp += 5
        add(coverLabel, x = 1, y = yp, w = lw, h = 4)
        add(coverIcon, x = lw + 2, y = yp, w = -1, h = (Text.ICON_SIZE / (Platform.defFont.size / 2)) + 4)

        yp = -25
        add(loadCoverButton, x = 1, y = yp, w = -1, h = 4)

        yp += 5
        add(removeCoverButton, x = 1, y = yp, w = -1, h = 4)

        yp += 5
        add(clearButton, x = 1, y = yp, w = -1, h = 4)

        yp += 5
        add(deleteFileButton, x = 1, y = yp, w = -1, h = 4)

        yp += 5
        add(saveButton, x = 1, y = yp, w = -1, h = 4)

        clearButton.toolTipText       = Text.TOOL_DELETE_TAGS1
        coverIcon.horizontalAlignment = JLabel.LEFT
        deleteFileButton.toolTipText  = Text.TOOL_DELETE_TRACK
        genreCombo.isEditable         = true
        loadCoverButton.toolTipText   = Text.TOOL_LOAD_IMAGE
        removeCoverButton.toolTipText = Text.TOOL_DELETE_COVER
        saveButton.toolTipText        = Text.TOOL_SAVE

        setIcon(null)
        setButtons(false)

         // Remove all tags from track
        clearButton.addActionListener {
            Data.removeTags()
        }

        // Delete track file but ask first
        deleteFileButton.addActionListener {
            if (JOptionPane.showConfirmDialog(Main.window, Text.MESSAGE_ASK_DELETE_HTML, Text.DIALOG_DELETE, JOptionPane.YES_NO_OPTION) == 0)
                Data.deleteTrack()
        }

        // Load cover image from disk
        loadCoverButton.addActionListener {
            Data.loadImageForSelectedTrack(pref)
        }

        // Delete cover image from track
        removeCoverButton.addActionListener {
            Data.removeImage()
        }

        // Save track
        saveButton.addActionListener {
            Data.saveTracks()
        }

        // Listener callback for data changes
        Data.addListener(object : TrackListener {
            override fun update(event: TrackEvent) {
                val track = Data.selectedTrack

                when (event) {
                    TrackEvent.ITEM_DIRTY -> setButtons(track != null)
                    TrackEvent.ITEM_IMAGE -> setIcon(track)
                    TrackEvent.ITEM_SELECTED -> {
                        setButtons(track != null)
                        setFields(track)
                        setIcon(track)
                    }
                    TrackEvent.LIST_UPDATED -> {
                        setButtons(track != null)
                        setFields(track)
                        setIcon(track)
                    }
                }
            }
        })
    }

    /**
     * Turn buttons on or off
     */
    fun setButtons(track: Boolean) {
        saveButton.isEnabled = Data.isAnyChangedAndSelected

        if (track) {
            clearButton.isEnabled      = true
            deleteFileButton.isEnabled = true
            loadCoverButton.isEnabled  = true
        }
        else {
            clearButton.isEnabled      = false
            deleteFileButton.isEnabled = false
            loadCoverButton.isEnabled  = false
        }

        activateWidgets.forEach { it.isEnabled = track }
    }

    /**
     * Copy data from track to input widgets
     */
    fun setFields(track: Track?) {
        if (track != null) {
            artistInput.text      = track.artist
            albumInput.text       = track.album
            albumArtistInput.text = track.albumArtist
            titleInput.text       = track.title
            yearInput.text        = track.year
            genreCombo.text       = track.genre
            trackInput.text       = track.track
            commentInput.text     = track.comment
            composerInput.text    = track.composer
            encoderInput.text     = track.encoder
            filenameInput.text    = track.fileName
        }
        else {
            artistInput.text      = ""
            albumInput.text       = ""
            albumArtistInput.text = ""
            titleInput.text       = ""
            yearInput.text        = ""
            genreCombo.text       = ""
            trackInput.text       = ""
            commentInput.text     = ""
            composerInput.text    = ""
            encoderInput.text     = ""
            filenameInput.text    = ""
        }
    }

    /**
     * Copy image from track to icon and enable buttons
     */
    fun setIcon(track: Track?) {
        saveButton.isEnabled = Data.isAnyChangedAndSelected

        if (track != null) {
            removeCoverButton.isEnabled = track.cover != ""
            coverIcon.icon              = track.coverIcon
        }
        else {
            removeCoverButton.isEnabled = false
            coverIcon.icon              = Data.loadIconFromPath()
        }

        coverIcon.repaint()
    }
}
