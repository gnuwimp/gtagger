/*
 * Copyright 2016 - 2019 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.gtagger.data

import gnuwimp.core.extension.numOrMinus
import gnuwimp.core.extension.numOrZero
import gnuwimp.core.swing.Platform
import gnuwimp.core.swing.extension.scale
import gnuwimp.core.util.TimeFormat
import gnuwimp.core.util.format
import gnuwimp.gtagger.resource.Text
import org.jaudiotagger.audio.AudioFile
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.images.StandardArtwork
import java.io.File
import javax.swing.ImageIcon

/**
 * Return loaded tracks from task list
 */
val List<AudioReadTask>.tracks: List<Track>
    get() {
        val list = mutableListOf<Track>()

        this.forEach { audioReadTask ->
            val track = audioReadTask.track

            if (track != null) {
                list.add(track)
            }
        }

        return list
    }

/**
 * Track object contains medata data about audio tracks
 * It can load and save meta data to the audio file
 * It will use JAudioTagger library for loading and saving
 * JAudioTagger library is only used in this file
 */
class Track(file: File) {
    private var selected: Boolean = true
    private var error: Boolean = false
    private var changed: Boolean = false
    private var cleared: Boolean = false
    private var audio: AudioFile? = null
    private val test: MutableMap<String, String> = mutableMapOf()

    /**
     * Album string
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
     * Album artist string
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
     * Artist string
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
     * Bitrate in raw number
     */
    val bitrate: String
        get() = test["bitrate"] ?: ""

    /**
     * Bitrate info string
     */
    val bitrateInfo: String
        get() = test["bitrate_info"] ?: ""

    /**
     * Comment string
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
     * Composer string
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
     * Cover string
     * If string is "cover" then it means that the cover is the embedded image in audio track
     * If string is empty then it does not have any image
     * Otherwise is should be pointing to an image file
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
     * Loaded image from audio track or from external image file or the default image
     */
    val coverIcon: ImageIcon
        get() {
            return if (cover == "cover") {
                try {
                    ImageIcon(audio?.tag?.firstArtwork?.binaryData).scale(Text.ICON_SIZE.toDouble())
                }
                catch (e: Exception) {
                    Data.message = e.message ?: ""
                    Data.loadIconFromPath()
                }
            }
            else
                Data.loadIconFromPath(cover)
        }

    /**
     * Encoder string
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
     * File extension
     * Only captilization can be changed, not the extension itself
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
     * Audio file name
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
     * File name with extension
     */
    val fileNameWithExtension: String
        get() = "$fileName.$fileExt"

    /**
     * Filesize in bytes
     */
    val fileSize: String
        get() = test["file_size"] ?: ""

    /**
     * Filesize info string with and as KB
     */
    val fileSizeInfo: String
        get() = test["file_size_info"] ?: ""

    /**
     * Audio format info
     */
    val formatInfo: String
        get() = test["format"] ?: ""

    /**
     * Audio genre string
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
     * Compare meta data from file with track string hash data
     * Return true if any data has been changed
     */
    private val hasChangedCompareWithTags: Boolean
        get() {
            var res = false

            try {
                val tag = audio?.tag

                if (tag != null) {
                    if (album != tag.getFirst(FieldKey.ALBUM))
                        res = true

                    if (albumArtist != tag.getFirst(FieldKey.ALBUM_ARTIST))
                        res = true

                    if (artist != tag.getFirst(FieldKey.ARTIST))
                        res = true

                    if (comment != tag.getFirst(FieldKey.COMMENT))
                        res = true

                    if (composer != tag.getFirst(FieldKey.COMPOSER))
                        res = true

                    if (cover != "cover")
                        res = true

                    if (encoder != tag.getFirst(FieldKey.ENCODER))
                        res = true

                    if (genre != tag.getFirst(FieldKey.GENRE))
                        res = true

                    if (title != tag.getFirst(FieldKey.TITLE))
                        res = true

                    if (track != tag.getFirst(FieldKey.TRACK))
                        res = true

                    if (year != tag.getFirst(FieldKey.YEAR))
                        res = true

                }
            }
            catch (e: Exception) {
            }
            finally {
                return res
            }
        }

    /**
     * True if track saving/loading data has failed
     */
    val hasError: Boolean
        get() = error

    /**
     * True if any data in string hash been changed
     */
    val isChanged: Boolean
        get() = changed

    /**
     * Select or unselect track
     * For table views and for updating only selected audio tracks
     */
    var isSelected: Boolean
        get() = selected

        set(value) {
            selected = value
        }

    /**
     * Time in milliseconds
     */
    val time: String
        get() = test["time"] ?: ""

    /**
     * Time as formatted string "MM:SS"
     */
    val timeInfo: String
        get() = test["time_info"] ?: ""

    /**
     * Title string
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
     * Track number
     * Only track values from 1 - 99999 can be set
     */
    var track: String
        get() = test["track"] ?: ""

        set(value) {
            if (test["track"] != value && value.numOrZero > 0 && value.numOrZero <= 99999) {
                test["track"] = "${value.numOrMinus}"
                changed = true
            }
        }

    /**
     * Track number with zeros before number so sorting as string will work
     */
    val trackWithZeros: String
        get() = String.format("%05d", track.numOrZero)

    /**
     * Year string
     * Only year values from 1900 - 9999 can be set
     */
    var year: String
        get() = test["year"] ?: ""

        set(value) {
            if (test["year"] != value && value.numOrMinus >= 1900 && value.numOrZero <= 9999) {
                test["year"] = "${value.numOrMinus}"
                changed = true
            }
        }

    init {
        load(file)
        copyTagsFromAudio()
    }

    /**
     * Delete all meta data tags
     */
    fun clear() {
        try {
            audio?.tag = audio?.createDefaultTag()
            copyTagsFromAudio()
        }
        catch (e: Exception) {
            throw e
        }
        finally {
            changed = true
            cleared = true
        }
    }

    /**
     * Copy meta data from audio track into internal hash
     * Changed and error flag is cleared even if it failes to read meta data
     * And if it failes the hash data might be empty
     */
    fun copyTagsFromAudio() {
        test.clear()

        if (cleared || error)
            load(audio?.file)

        val tag = audio?.tag
        val file = audio?.file
        val header = audio?.audioHeader

        changed = false
        error   = false

        if (tag != null && file != null && header != null) {
            test["samplerate"]      = header.sampleRate
            test["samplerate_info"] = "${header.sampleRate} Hz"
            test["bitrate"]         = String.format("%04d", header.bitRateAsNumber)
            test["bitrate_info"]    = "${header.bitRate} Kb/s"
            test["file_size"]       = String.format("%08d", file.length())
            test["file_size_info"]  = "${file.length() / 1000} kB"
            test["format"]          = header.format
            test["time"]            = String.format("%08d", header.trackLength)
            test["time_info"]       = if (header.trackLength * 1000L >= 3600000)
                                          TimeFormat.LONG_TIME.format(header.trackLength * 1000L, "UTC")
                                      else
                                          TimeFormat.LONG_MINSEC.format(header.trackLength * 1000L, "UTC")

            fileExt     = file.extension
            fileName    = file.nameWithoutExtension

            album       = tag.getFirst(FieldKey.ALBUM)
            albumArtist = tag.getFirst(FieldKey.ALBUM_ARTIST)
            artist      = tag.getFirst(FieldKey.ARTIST)
            comment     = tag.getFirst(FieldKey.COMMENT)
            composer    = tag.getFirst(FieldKey.COMPOSER)
            cover       = if (tag.firstArtwork != null) "cover" else ""
            encoder     = tag.getFirst(FieldKey.ENCODER)
            genre       = tag.getFirst(FieldKey.GENRE)
            title       = tag.getFirst(FieldKey.TITLE)
            track       = tag.getFirst(FieldKey.TRACK)
            year        = tag.getFirst(FieldKey.YEAR)

            changed    = false

        }
        else {
            throw Exception(Text.ERROR_LOADING_TRACK.format(audio?.file?.name ?: "?"))
        }
    }

    /**
     * Copy tags from string hash to audio tag object
     */
    private fun copyTagsToAudio(): String {
        try {
            val tag = audio?.tag

            if (tag != null) {
                tag.setField(FieldKey.ALBUM, album)
                tag.setField(FieldKey.ALBUM_ARTIST, albumArtist)
                tag.setField(FieldKey.ARTIST, artist)
                tag.setField(FieldKey.COMMENT, comment)
                tag.setField(FieldKey.COMPOSER, composer)
                tag.setField(FieldKey.ENCODER, encoder)
                tag.setField(FieldKey.GENRE, genre)
                tag.setField(FieldKey.TITLE, title)
                tag.setField(FieldKey.TRACK, track)

                if (year.numOrMinus >= 0)
                    tag.setField(FieldKey.YEAR, year)

                if (cover.isBlank())
                    tag.deleteArtworkField()
                else if (cover != "cover") {
                    tag.deleteArtworkField()
                    tag.addField(StandardArtwork.createArtworkFromFile(File(cover)))
                }

                return ""
            }
            else
                throw Exception(Text.ERROR_TAG_OBJECT)
        }
        catch (e: Exception) {
            Platform.logMessage = e.message ?: "Unknown exception"
            return e.message ?: "Unknown exception"
        }
    }

    /**
     * Delete audio track
     */
    fun delete(): Boolean {
        return try {
            audio?.file?.delete() ?: false
        }
        catch (e: Exception) {
            Data.message = e.message ?: ""
            false
        }
    }

    /**
     * Load meta data from audio file
     */
    private fun load(file: File?) {
        if (file != null) {
            Platform.logMessage = Text.MESSAGE_LOADING_TRACK.format(file.name)

            audio = AudioFileIO.read(file)

            if (audio?.tag == null) {
                audio?.tag = audio?.createDefaultTag()
            }
        }

        error   = false
        cleared = false
    }

    /**
     * Save meta data or change filename or both
     */
    fun save() {
        error = true

        val dirty = hasChangedCompareWithTags
        val message = copyTagsToAudio()

        if (message == "") {
            val audio = audio
            val file  = this.audio?.file

            if (audio != null && file != null) {
                if (dirty) {
                    Platform.logMessage = Text.MESSAGE_SAVING_TRACK.format(file.name)
                    audio.commit()
                }

                if (test["file_name"] != this.audio?.file?.nameWithoutExtension) {
                    val dest = File(file.parent + File.separator + test["file_name"] + "." + file.extension)

                    Platform.logMessage = Text.MESSAGE_RENAMING_TRACK.format(file.name, dest.name)

                    if (!file.renameTo(dest))
                        throw Exception(Text.ERROR_RENAME)

                    audio.file = dest
                }

                error   = false
                cleared = false

                copyTagsFromAudio()
            }
            else
                throw Exception("Track.save: internal problem")
        }
        else  {
            throw Exception(message)
        }
    }

    /**
     * Debug string
     */
    override fun toString() = "$track - $artist - $album - $title - $genre - '$cover' - '$fileName'"

}
