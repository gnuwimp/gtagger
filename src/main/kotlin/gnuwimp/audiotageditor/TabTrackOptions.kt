/*
 * Copyright 2021 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audiotageditor

import gnuwimp.swing.ComboBox
import gnuwimp.swing.LayoutPanel
import gnuwimp.swing.Swing
import gnuwimp.swing.TextField
import java.util.prefs.Preferences
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JLabel

/**
 * Create a panel with input widgets for all track properties.
 */
class TabTrackOptions(private val pref: Preferences) : LayoutPanel(size = Swing.defFont.size / 2 + 1) {
    private val albumLabel        = JLabel(Constants.LABEL_ALBUM)
    private val albumArtistLabel  = JLabel(Constants.LABEL_ALBUM_ARTIST)
    private val albumArtistInput  = TextField(callback = { val track = Data.selectedTrack; if (track != null && it != track.artist) { track.albumArtist = it ; Data.sendUpdate(TrackEvent.ITEM_DIRTY) } })
    private val albumInput        = TextField(callback = { val track = Data.selectedTrack; if (track != null && it != track.album) { track.album = it ; Data.sendUpdate(TrackEvent.ITEM_DIRTY) } })
    private val artistLabel       = JLabel(Constants.LABEL_ARTIST)
    private val artistInput       = TextField(callback = { val track = Data.selectedTrack; if (track != null && it != track.artist) { track.artist = it ; Data.sendUpdate(TrackEvent.ITEM_DIRTY) } })
    private val commentLabel      = JLabel(Constants.LABEL_COMMENT)
    private val commentInput      = TextField(callback = { val track = Data.selectedTrack; if (track != null && it != track.comment) { track.comment = it ; Data.sendUpdate(TrackEvent.ITEM_DIRTY) } })
    private val composerLabel     = JLabel(Constants.LABEL_COMPOSER)
    private val composerInput     = TextField(callback = { val track = Data.selectedTrack; if (track != null && it != track.composer) { track.composer = it ; Data.sendUpdate(TrackEvent.ITEM_DIRTY) } })
    private val coverLabel        = JLabel(Constants.LABEL_COVER)
    private val coverIcon         = JLabel()
    private val encoderLabel      = JLabel(Constants.LABEL_ENCODER)
    private val encoderInput      = TextField(callback = { val track = Data.selectedTrack; if (track != null && it != track.encoder) { track.encoder = it ; Data.sendUpdate(TrackEvent.ITEM_DIRTY) } })
    private val filenameLabel     = JLabel(Constants.LABEL_FILENAME)
    private val filenameInput     = TextField(callback = { val track = Data.selectedTrack; if (track != null && it != track.fileName) { track.fileName = it ; Data.sendUpdate(TrackEvent.ITEM_DIRTY) } })
    private val genreLabel        = JLabel(Constants.LABEL_GENRE)
    private val genreCombo        = ComboBox<String>(strings = ID3Genre.strings.sorted(), callback = { val track = Data.selectedTrack; if (track != null && it != track.genre) { track.genre = it ; Data.sendUpdate(TrackEvent.ITEM_DIRTY) } })
    private val loadCoverButton   = JButton(Constants.LABEL_LOAD_IMAGE)
    private val removeCoverButton = JButton(Constants.LABEL_REMOVE_COVER)
    private val saveButton        = JButton(Constants.LABEL_SAVE)
    private val titleLabel        = JLabel(Constants.LABEL_TITLE)
    private val titleInput        = TextField(callback = { val track = Data.selectedTrack; if (track != null && it != track.title) { track.title = it ; Data.sendUpdate(TrackEvent.ITEM_DIRTY) } })
    private val trackLabel        = JLabel(Constants.LABEL_TRACK)
    private val trackInput        = TextField(callback = { val track = Data.selectedTrack; if (track != null && it != track.track) { track.track = it ; Data.sendUpdate(TrackEvent.ITEM_DIRTY) } })
    private val yearLabel         = JLabel(Constants.LABEL_YEAR)
    private val yearInput         = TextField(callback = { val track = Data.selectedTrack; if (track != null && it != track.year) { track.year = it ; Data.sendUpdate(TrackEvent.ITEM_DIRTY) } })
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
        add(coverIcon, x = lw + 2, y = yp, w = -1, h = (Constants.ICON_SIZE / (Swing.defFont.size / 2)) + 4)

        yp = -15
        add(loadCoverButton, x = 1, y = yp, w = -1, h = 4)

        yp += 5
        add(removeCoverButton, x = 1, y = yp, w = -1, h = 4)

        yp += 5
        add(saveButton, x = 1, y = yp, w = -1, h = 4)

        coverIcon.horizontalAlignment = JLabel.LEFT
        genreCombo.isEditable         = true
        loadCoverButton.toolTipText   = Constants.TOOL_LOAD_IMAGE
        removeCoverButton.toolTipText = Constants.TOOL_DELETE_COVER
        saveButton.toolTipText        = Constants.TOOL_SAVE

        setIcon(null)
        setButtons(false)

        //----------------------------------------------------------------------
        loadCoverButton.addActionListener {
            Data.loadImageForSelectedTrack(pref)
        }

        //----------------------------------------------------------------------
        removeCoverButton.addActionListener {
            Data.removeImage()
        }

        //----------------------------------------------------------------------
        saveButton.addActionListener {
            Data.saveTracks()
        }

        //----------------------------------------------------------------------
        Data.addListener(object : TrackListener {
            override fun update(event: TrackEvent) {
                val track = Data.selectedTrack

                when (event) {
                    TrackEvent.ITEM_DIRTY -> {
                        setButtons(track != null)
                    }
                    TrackEvent.ITEM_IMAGE -> {
                        setIcon(track)
                    }
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

    //----------------------------------------------------------------------
    fun setButtons(track: Boolean) {
        saveButton.isEnabled      = Data.isAnyChangedAndSelected
        loadCoverButton.isEnabled = track == true

        activateWidgets.forEach { widget ->
            widget.isEnabled = track
        }
    }

    //----------------------------------------------------------------------
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
