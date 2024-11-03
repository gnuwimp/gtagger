/*
 * Copyright 2021 - 2024 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audiotageditor

import gnuwimp.swing.Swing
import gnuwimp.swing.scale
import gnuwimp.util.TimeFormat
import gnuwimp.util.format
import gnuwimp.util.numOrZero
import org.jaudiotagger.audio.AudioFile
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.Tag
import org.jaudiotagger.tag.images.Artwork
import org.jaudiotagger.tag.images.StandardArtwork
import java.io.File
import javax.swing.ImageIcon

/**
 * Return loaded tracks from task list.
 */
val List<TaskReadAudio>.tracks: List<Track>
    get() {
        val list = mutableListOf<Track>()

        this.forEach { task ->
            val track = task.track

            if (track != null) {
                list.add(track)
            }
        }

        return list
    }

/**
 * Get cover image with no exception.
 */
fun Tag.getCover(): String {
    return try {
        if (firstArtwork != null) "cover" else ""
    }
    catch (e: Exception) {
        Swing.logMessage = e.message.toString()
        ""
    }
}

/**
 * Get tag value with no exception.
 */
fun Tag.getValue(field: FieldKey): String {
    return try {
        getFirst(field)
    }
    catch (e: Exception) {
        Swing.logMessage = e.message.toString()
        ""
    }
}

/**
 * Set cover image with no exception.
 */
fun Tag.setCover(cover: String): Int {
    return try {
        if (cover == "") {
            deleteArtworkField()
        }
        else if (cover != "cover") {
            deleteArtworkField()
            addField(StandardArtwork.createArtworkFromFile(File(cover)))
        }

        0
    }
    catch (e: Exception) {
        Swing.logMessage = e.message.toString()
        1
    }
}

/**
 * Set cover image with no exception.
 */
fun Tag.setCover(art: Artwork): Int {
    return try {
        deleteArtworkField()
        addField(art)
        0
    }
    catch (e: Exception) {
        Swing.logMessage = e.message.toString()
        1
    }
}

/**
 * Set tag value with no exception.
 */
fun Tag.setValue(field: FieldKey, value: String): Int {
    return try {
        setField(field, value)
        0
    }
    catch (e: Exception) {
        Swing.logMessage = e.message.toString()
        1
    }
}

//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------

/**
 * Data events for all classes that are interested to receive data changes.
 * All events that are sent to all classes using TrackListener interface.
 */
enum class TrackEvent {
    ITEM_DIRTY,
    ITEM_IMAGE,
    ITEM_SELECTED,
    LIST_UPDATED,
}

//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------

/**
 * Interface for track data events
 * Inherit to receive events
 */
interface TrackListener {
    fun update(event: TrackEvent) {}
}

//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------

/**
 * Track object contains medata data about audio tracks.
 * It can load and save metadata to the audio file.
 * It will use JAudioTagger library for loading and saving.
 * JAudioTagger library is only used in this file.
 */
class Track(file: File) {
    companion object {
        var ERRORS = 0
    }

    private var audio: AudioFile? = null
    private var selected          = true
    private var error             = false
    private var changed           = false
    private var cleared           = false
    private val test              = mutableMapOf<String, String>()
    private val prop              = mutableMapOf<String, String>()

    /**
     * Album string.
     */
    var album: String
        get() = test["album"] ?: ""

        set(value) {
            if (test["album"] != value) {
                test["album"] = value
                changed = true
            }
        }

    /**
     * Album artist string.
     */
    var albumArtist: String
        get() = test["album_artist"] ?: ""

        set(value) {
            if (test["album_artist"] != value) {
                test["album_artist"] = value
                changed = true
            }
        }

    /**
     * Artist string.
     */
    var artist: String
        get() = test["artist"] ?: ""

        set(value) {
            if (test["artist"] != value) {
                test["artist"] = value
                changed = true
            }
        }

    /**
     * Bitrate in raw number.
     */
    val bitrate: String
        get() = prop["bitrate"] ?: ""

    /**
     * Bitrate info string
     */
    val bitrateInfo: String
        get() = prop["bitrate_info"] ?: ""

    /**
     * Comment string.
     */
    var comment: String
        get() = test["comment"] ?: ""

        set(value) {
            if (test["comment"] != value) {
                test["comment"] = value
                changed = true
            }
        }

    /**
     * Composer string.
     */
    var composer: String
        get() = test["composer"] ?: ""

        set(value) {
            if (test["composer"] != value) {
                test["composer"] = value
                changed = true
            }
        }

    /**
     * Cover string.
     * If string is "cover" then it means that the cover is the embedded image in audio track.
     * If string is empty then it does not have any image.
     * Otherwise, it should be pointing to an image file.
     */
    var cover: String
        get() = test["cover"] ?: ""

        set(value) {
            if (test["cover"] != value) {
                test["cover"] = value
                changed = true
            }
        }

    /**
     * Loaded image from audio track or from external image file or the default image.
     */
    val coverIcon: ImageIcon
        get() {
            return if (cover == "cover") {
                try {
                    ImageIcon(audio?.tag?.firstArtwork?.binaryData).scale(Constants.ICON_SIZE.toDouble())
                }
                catch (e: Exception) {
                    Data.message = e.message ?: ""
                    Data.loadIconFromPath()
                }
            }
            else {
                Data.loadIconFromPath(cover)
            }
        }

    /**
     * Encoder string.
     */
    var encoder: String
        get() = test["encoder"] ?: ""

        set(value) {
            if (test["encoder"] != value) {
                test["encoder"] = value
                changed = true
            }
        }

    /**
     * File extension.
     * Only captilization can be changed, not the extension itself.
     */
    var fileExt: String
        get() = test["file_ext"] ?: ""

        set(value) {
            if (value != test["file_ext"]) {
                test["file_ext"] = value
                changed = true
            }
        }

    /**
     * Audio file name.
     */
    var fileName: String
        get() = test["file_name"] ?: ""

        set(value) {
            if (value != test["file_name"]) {
                test["file_name"] = value
                changed = true
            }
        }

    /**
     * File name with extension.
     */
    val fileNameWithExtension: String
        get() = "$fileName.$fileExt"

    /**
     * Filesize in bytes.
     */
    val fileSize: String
        get() = prop["file_size"] ?: ""

    /**
     * Filesize info string with and as KB.
     */
    val fileSizeInfo: String
        get() = prop["file_size_info"] ?: ""

    /**
     * Audio format info.
     */
    val formatInfo: String
        get() = prop["format"] ?: ""

    /**
     * Audio genre string.
     */
    var genre: String
        get() = test["genre"] ?: ""

        set(value) {
            if (test["genre"] != value) {
                test["genre"] = value
                changed = true
            }
        }

    /**
     * Compare metadata from file with track string hash data.
     * Return true if any data has been changed.
     */
    private val hasChangedCompareWithTags: Boolean
        get() {
            val tag = audio?.tag

            if (tag != null) {
                if (album != tag.getValue(FieldKey.ALBUM)) {
                    return true
                }
                else if (albumArtist != tag.getValue(FieldKey.ALBUM_ARTIST)) {
                    return true
                }
                else if (artist != tag.getValue(FieldKey.ARTIST)) {
                    return true
                }
                else if (comment != tag.getValue(FieldKey.COMMENT)) {
                    return true
                }
                else if (composer != tag.getValue(FieldKey.COMPOSER)) {
                    return true
                }
                else if (cover != "cover") {
                    return true
                }
                else if (encoder != tag.getValue(FieldKey.ENCODER)) {
                    return true
                }
                else if (genre != tag.getValue(FieldKey.GENRE)) {
                    return true
                }
                else if (title != tag.getValue(FieldKey.TITLE)) {
                    return true
                }
                else if (track != tag.getValue(FieldKey.TRACK)) {
                    return true
                }
                else if (year != tag.getValue(FieldKey.YEAR)) {
                    return true
                }
            }

            return false
        }

    /**
     * True if track saving/loading data has failed.
     */
    val hasError: Boolean
        get() = error

    /**
     * True if any data in string hash been changed.
     */
    val isChanged: Boolean
        get() = changed

    /**
     * Select or unselect track.
     * For table views and for updating only selected audio tracks.
     */
    var isSelected: Boolean
        get() = selected

        set(value) {
            selected = value
        }

    /**
     * Time in milliseconds.
     */
    val time: String
        get() = prop["time"] ?: ""

    /**
     * Time as formatted string "MM:SS".
     */
    val timeInfo: String
        get() = prop["time_info"] ?: ""

    /**
     * Title string.
     */
    var title: String
        get() = test["title"] ?: ""

        set(value) {
            if (test["title"] != value) {
                test["title"] = value
                changed = true
            }
        }

    /**
     *
     * Track number.
     * Only track values from 1 to 99999 can be set.
     */
    var track: String
        get() = test["track"] ?: ""

        set(value) {
            if (test["track"] != value && value.numOrZero in 1..99999) {
                test["track"] = "${value.numOrZero}"
                changed = true
            }
        }

    /**
     * Track number with zeros before number so sorting as string will work.
     */
    val trackWithZeros: String
        get() = String.format("%05d", track.numOrZero)

    /**
     * Year string.
     * Only year values from 1 to 9999 can be set.
     */
    var year: String
        get() = test["year"] ?: ""

        set(value) {
            if (test["year"] != value && value.numOrZero in 1..9999) {
                test["year"] = "${value.numOrZero}"
                changed = true
            }
        }

    init {
        load(file)
        copyTagsFromAudio()
    }

    /**
     * Delete all meta data tags.
     */
    fun clear() {
        try {
            audio?.tag = audio?.createDefaultTag()
            audio?.tag?.setValue(FieldKey.ENCODER, "") // jaudiotagger string suddenly appeared.
            changed = true
            save(updateTagsWithUserData = false)
        }
        catch (e: Exception) {
            throw e
        }
    }

    /**
     * Copy metadata from audio track into internal hash.
     * Changed and error flag is cleared even if it failes to read metadata.
     * And if it failes the hash data might be empty.
     */
    fun copyTagsFromAudio() {
        test.clear()
        prop.clear()

        if (cleared == true || error == true) {
            load(audio?.file)
        }

        val tag    = audio?.tag
        val file   = audio?.file
        val header = audio?.audioHeader

        changed = false
        error   = false

        if (tag != null && file != null && header != null) {
            prop["samplerate"]      = header.sampleRate
            prop["samplerate_info"] = "${header.sampleRate} Hz"
            prop["bitrate"]         = String.format("%04d", header.bitRateAsNumber)
            prop["bitrate_info"]    = "${header.bitRate} Kb/s"
            prop["file_size"]       = String.format("%08d", file.length())
            prop["file_size_info"]  = "%.2f MB".format(file.length().toDouble() / 1_000_000.0)
            prop["format"]          = header.format.lowercase()
            prop["time"]            = String.format("%08d", header.trackLength)
            prop["time_info"]       = if (header.trackLength * 1000L >= 3600000) TimeFormat.LONG_TIME.format(header.trackLength * 1000L, "UTC") else TimeFormat.LONG_MINSEC.format(header.trackLength * 1000L, "UTC")

            fileExt     = file.extension
            fileName    = file.nameWithoutExtension
            album       = tag.getValue(FieldKey.ALBUM)
            albumArtist = tag.getValue(FieldKey.ALBUM_ARTIST)
            artist      = tag.getValue(FieldKey.ARTIST)
            comment     = tag.getValue(FieldKey.COMMENT)
            composer    = tag.getValue(FieldKey.COMPOSER)
            cover       = tag.getCover()
            encoder     = tag.getValue(FieldKey.ENCODER)
            genre       = tag.getValue(FieldKey.GENRE)
            title       = tag.getValue(FieldKey.TITLE)
            track       = tag.getValue(FieldKey.TRACK)
            year        = tag.getValue(FieldKey.YEAR)
            changed     = false

        }
        else {
            throw Exception(Constants.ERROR_LOADING_TRACK.format(audio?.file?.name ?: "?"))
        }
    }

    /**
     * Copy tags from string hash to audio tag object.
     * Create new tag to delete all unused tags (for AudiTagEditor) then copy from widgets.
     * Image has to be extracted first, so it can be reattached.
     */
    private fun copyUserDataToTag() {
        val art = audio?.tag?.firstArtwork
        audio?.tag = audio?.createDefaultTag()
        val tag = audio?.tag

        if (tag != null) {
            ERRORS += tag.setValue(FieldKey.ALBUM, album)
            ERRORS += tag.setValue(FieldKey.ALBUM_ARTIST, albumArtist)
            ERRORS += tag.setValue(FieldKey.ARTIST, artist)
            ERRORS += tag.setValue(FieldKey.COMMENT, comment)
            ERRORS += tag.setValue(FieldKey.COMPOSER, composer)
            ERRORS += tag.setValue(FieldKey.ENCODER, encoder)
            ERRORS += tag.setValue(FieldKey.GENRE, genre)
            ERRORS += tag.setValue(FieldKey.TITLE, title)

            if (track.numOrZero in 1..9999) {
                ERRORS += tag.setValue(FieldKey.TRACK, track)
            }

            if (year.numOrZero in 1..9999) {
                ERRORS += tag.setValue(FieldKey.YEAR, year)
            }

            ERRORS += if (cover == "cover" && art != null) {
                tag.setCover(art)
            }
            else {
                tag.setCover(cover)
            }
        }
    }

    /**
     * Delete audio track.
     */
    fun delete(): Boolean {
        return try {
            audio?.file?.delete() == true
        }
        catch (e: Exception) {
            Data.message = e.message ?: ""
            false
        }
    }

    /**
     * Load meta data from audio file.
     */
    private fun load(file: File?) {
        if (file != null) {
            Swing.logMessage = Constants.MESSAGE_LOADING_TRACK.format(file.name)

            audio = AudioFileIO.read(file)

            if (audio?.tag == null) {
                audio?.tag = audio?.createDefaultTag()
            }
        }

        error   = false
        cleared = false
    }

    /**
     * Save metadata or change filename or both.
     */
    fun save(updateTagsWithUserData: Boolean) {
        error = true

        val dirty = if (updateTagsWithUserData == false) hasChangedCompareWithTags else true

        if (updateTagsWithUserData == true) {
            copyUserDataToTag()
        }

        val audio = audio
        val file  = this.audio?.file

        if (audio != null && file != null) {
            if (dirty == true) {
                Swing.logMessage = Constants.MESSAGE_SAVING_TRACK.format(file.name)
                audio.commit()
            }

            if (test["file_name"] != this.audio?.file?.nameWithoutExtension) {
                val dest = File(file.parent + File.separator + test["file_name"] + "." + file.extension)

                Swing.logMessage = Constants.MESSAGE_RENAMING_TRACK.format(file.name, dest.name)

                if (file.renameTo(dest) == false) {
                    throw Exception(Constants.ERROR_RENAME)
                }

                audio.file = dest
            }

            error   = false
            cleared = false

            copyTagsFromAudio()
        }
        else {
            throw Exception("Track.save: internal problem")
        }
    }

    /**
     * Debug string.
     */
    override fun toString() = "$track - $artist - $album - $title - $genre - '$cover' - '$fileName'"

}
