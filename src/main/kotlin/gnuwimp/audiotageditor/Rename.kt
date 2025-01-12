/*
 * Copyright 2021 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audiotageditor

import gnuwimp.util.*

/**
 * Create a string with formatted number.
 */
fun String.number(formatType: String, seperator: String, number: Int) : String = when (formatType) {
    "insert_1" -> String.format("%d%s%s", number, seperator, this)
    "insert_2" -> String.format("%02d%s%s", number, seperator, this)
    "insert_3" -> String.format("%03d%s%s", number, seperator, this)
    "insert_4" -> String.format("%04d%s%s", number, seperator, this)
    "append_1" -> String.format("%s%s%d", this, seperator, number)
    "append_2" -> String.format("%s%s%02d", this, seperator, number)
    "append_3" -> String.format("%s%s%03d", this, seperator, number)
    "append_4" -> String.format("%s%s%04d", this, seperator, number)
    else -> throw Exception("String.number: internal error")
}

/**
 * Rename album tags.
 */
fun Track.renameAlbum(options: Map<String, String>, index: Int) {
    this.artist      = options.getOrElse(key = "artist") { this.artist }
    this.album       = options.getOrElse(key = "album") { this.album }
    this.albumArtist = options.getOrElse(key = "album_artist") { this.albumArtist }
    this.genre       = options.getOrElse(key = "genre") { this.genre }
    this.year        = options.getOrElse(key = "year") { this.year }
    this.comment     = options.getOrElse(key = "comment") { this.comment }
    this.composer    = options.getOrElse(key = "composer") { this.composer }
    this.encoder     = options.getOrElse(key = "encoder") { this.encoder }
    this.cover       = options.getOrElse(key = "cover") { this.cover }

    when (options["copy_artist"]) {
        null -> Unit
        "artist_to_albumartist" ->       this.albumArtist = this.artist
        "album_to_albumartist" ->        this.albumArtist = this.album
        "artist+album_to_albumartist" -> this.albumArtist = this.artist + " - " + this.album
        "albumartist_to_artist" ->       this.artist      = this.albumArtist
        "albumartist_to_album" ->        this.album       = this.albumArtist
        else -> Unit
    }

    val num = options["track"]?.numOrZero ?: 0

    if (num > 0) {
        this.track = "${num + index}"
    }
}

/**
 * Rename file name.
 */
fun Track.renameFile(options: Map<String, String>) {
    var name = fileName
    var ext  = fileExt

    if (options["title"] != null) {
        name = title
    }

    if (options["leading"] != null) {
        name = name.removeLeadingNoneLetters
    }

    if (options["trailing"] != null) {
        name = name.removeTrailingNoneLetters
    }

    name = options.getOrElse(key = "set") { name }

    val remove  = options["remove"]
    val replace = options["replace"]

    if (remove != null && replace == null) {
        name = name.replace(Regex(remove), "")
    }
    else if (remove != null && replace != null) {
        name = name.replace(Regex(remove), replace)
    }

    val insertAlbum = options["insert_album"]

    if (insertAlbum != null) {
        name = album + insertAlbum + name
    }

    val insertArtist = options["insert_artist"]

    if (insertArtist != null) {
        name = artist + insertArtist + name
    }

    val insert = options["insert"]

    if (insert != null) {
        name = insert + name
    }

    val append = options["append"]

    if (append != null) {
        name += append
    }

    val capName = options["cap_name"]

    if (capName.isNullOrBlank() == false) {
        name = name.capWords(capName)
    }

    val capExt = options["cap_ext"]

    if (capExt.isNullOrBlank() == false) {
        ext = ext.capWords(capExt)
    }

    val number = options["number"]
    val numberSep = options["number_sep"]

    if (number.isNullOrBlank() == false && numberSep != null) {
        name = name.number(number, numberSep, track.numOrZero.toInt())
    }

    if (options["illegal"] != null) {
        name = name.removeIllegalFileChar
    }

    if (name.isNotEmpty() == true) {
        fileName = name
    }

    fileExt = ext
}

/**
 * Rename title tag.
 */
fun Track.renameTitle(options: Map<String, String>) {
    var title = this.title

    if (options["filename"] != null) {
        title = fileName
    }

    if (options["leading"] != null) {
        title = title.removeLeadingNoneLetters
    }

    if (options["trailing"] != null) {
        title = title.removeTrailingNoneLetters
    }

    title = options.getOrElse(key = "set") { title }

    val remove  = options["remove"]
    val replace = options["replace"]

    if (remove != null && replace == null) {
        title = title.replace(Regex(remove), "")
    }
    else if (remove != null && replace != null) {
        title = title.replace(Regex(remove), replace)
    }

    val insertAlbum = options["insert_album"]

    if (insertAlbum != null) {
        title = album + insertAlbum + title
    }

    val insertArtist = options["insert_artist"]

    if (insertArtist != null) {
        title = artist + insertArtist + title
    }

    val insert = options["insert"]

    if (insert != null) {
        title = insert + title
    }

    val append = options["append"]

    if (append != null) {
        title += append
    }

    val capName = options["cap_name"]

    if (capName.isNullOrBlank() == false) {
        title = title.capWords(capName)
    }

    val number    = options["number"]
    val numberSep = options["number_sep"]

    if (number.isNullOrBlank() == false && numberSep != null) {
        title = title.number(number, numberSep, track.numOrZero.toInt())
    }

    this.title = title
}
