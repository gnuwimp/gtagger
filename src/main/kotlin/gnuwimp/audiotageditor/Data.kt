/*
 * Copyright 2021 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audiotageditor

import gnuwimp.swing.ImageFileDialog
import gnuwimp.swing.Swing
import gnuwimp.swing.TaskDialog
import gnuwimp.swing.toImageIcon
import gnuwimp.util.*
import java.io.File
import java.util.logging.LogManager
import java.util.prefs.Preferences
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.JOptionPane

/**
 * True if file is an audio file
 * Only extension is checked
 */
val File.isAudio: Boolean
    get() {
        val lower = extension.lowercase()

        return if (isFile == false) {
            false
        }
        else {
            lower == "mp3" || lower == "flac" || lower == "m4a" || lower == "m4b" || lower == "ogg" || lower == "wav" || lower == "wma"
        }
    }

/**
 * Data object that loads and saves audio tracks.
 * And also can sort and keep track of loaded data.
 */
object Data {
    enum class Sort {
        ALBUM,
        ALBUM_ARTIST,
        ARTIST,
        BITRATE,
        FILE,
        FORMAT,
        GENRE,
        SIZE,
        TIME,
        TITLE,
        SELECTED,
        TRACK,
        EXTENSION,
    }

    /**
     * Callback function for messages.
     */
    var messageFunc: (String) -> Unit = { _: String -> }

    /**
     * Track list that are used by all views.
     */
    var tracks: List<Track> = listOf()

    /**
     * Current directory path.
     */
    var path = ""

    /**
     * List of listeners.
     */
    private var listeners: MutableList<TrackListener> = mutableListOf()

    /**
     * Count selected tracks.
     */
    val countSelected: Int
        get() {
            return tracks.count { track ->
                track.isSelected
            }
        }

    /**
     * Return true if any track has been changed.
     */
    val isAnyChanged: Boolean
        get() {
            return tracks.any { track ->
                track.isChanged
            }
        }

    /**
     * Return true if any of the selected tracks has been changed.
     */
    val isAnyChangedAndSelected: Boolean
        get() {
            return tracks.any { track ->
                track.isSelected && track.isChanged
            }
        }

    /**
     * Get or set selected track number in array.
     * If track is selected send [TrackEvent.ITEM_SELECTED]
     */
    var selectedRow: Int = -1
        set(value) {
            if (value != selectedRow) {
                field = value
                sendUpdate(TrackEvent.ITEM_SELECTED)
            }
            field = value
        }

    /**
     * Get selected track or null.
     */
    val selectedTrack: Track?
        get() {
            return if (selectedRow >= 0 && selectedRow < tracks.size)
                tracks[selectedRow]
            else
                null
        }

    /**
     * Number of tracks loaded.
     */
    val size: Int
        get() {
            return tracks.size
        }

    /**
     * Add data change listener.
     */
    fun addListener(listener: TrackListener) {
        listeners.add(listener)
    }

    /**
     * Copy tags for all Track objects to internal array to string hash.
     * Even for unselected tracks.
     */
    fun copyTagsFromAudio() {
        tracks.forEach(Track::copyTagsFromAudio)
    }

    /**
     * Delete selected tracks.
     * [TrackEvent.LIST_UPDATED] is sent if tracks is deleted.
     */
    fun deleteTracks() {
        val list = mutableListOf<Track>()

        tracks.forEach {
            if (it.isSelected == false || it.delete() == false) {
                list.add(it)
            }
        }

        if (list.size != tracks.size) {
            tracks      = list
            selectedRow = -1
            sendUpdate(TrackEvent.LIST_UPDATED)
        }
    }

    /**
     * Filter tracks on album tags.
     * All found tracks are selected.
     * Not found are deselected.
     * [TrackEvent.ITEM_DIRTY] is sent.
     */
    fun filterOnAlbums(key: String) {
        tracks.forEach { track ->
            track.isSelected =
                track.artist.indexOf(string = key, ignoreCase = true) >= 0 ||
                track.album.indexOf(string = key, ignoreCase = true) >= 0 ||
                track.albumArtist.indexOf(string = key, ignoreCase = true) >= 0 ||
                track.genre.indexOf(string = key, ignoreCase = true) >= 0
        }

        sendUpdate(TrackEvent.ITEM_DIRTY)
    }

    /**
     * Filter tracks on file names.
     * All found tracks are selected.
     * Not found are deselected.
     * [TrackEvent.ITEM_DIRTY] is sent.
     */
    fun filterOnFiles(key: String) {
        tracks.forEach { track ->
            track.isSelected = track.fileNameWithExtension.indexOf(string = key, ignoreCase = true) >= 0
        }

        sendUpdate(TrackEvent.ITEM_DIRTY)
    }

    /**
     * Filter tracks on track titles.
     * All found tracks are selected.
     * Not found are deselected.
     * [TrackEvent.ITEM_DIRTY] is sent.
     */
    fun filterOnTitles(key: String) {
        tracks.forEach { track ->
            track.isSelected = track.title.indexOf(string = key, ignoreCase = true) >= 0
        }

        sendUpdate(TrackEvent.ITEM_DIRTY)
    }

    /**
     * Get track from array.
     */
    fun getTrack(row: Int): Track? = if (row >= 0 && row < tracks.size) tracks[row] else null

    /**
     * Load image from an image file.
     * The image is scaled down.
     * If it failes then return default image.
     */
    fun loadIconFromFile(file: File?): ImageIcon {
        return try {
            if (file == null || file.isFile == false) {
                throw Exception("")
            }
            else {
                ImageIO.read(file).toImageIcon(Constants.ICON_SIZE.toDouble())
            }
        }
        catch (e: Exception) {
            if (e.message != "") {
                message = "Error: can't read image file => " + e.message
            }

            ImageIcon(Main.noImage)
        }
    }

    /**
     * Get icon from a path.
     */
    fun loadIconFromPath(path: String = ""): ImageIcon = loadIconFromFile(File(path))

    /**
     * Show file dialog and try to set selected image as cover for current selected track and send [TrackEvent.ITEM_IMAGE] if cover is updated.
     */
    fun loadImageForSelectedTrack(pref: Preferences) {
        val track = selectedTrack

        if (track != null) {
            try {
                val dlg = ImageFileDialog(pref.picPath, Main.window)
                val file = dlg.file

                if (file != null && file.isImage == true) {
                    track.cover  = file.canonicalPath
                    pref.picPath = file.parentFile.canonicalPath
                }
            }
            catch (e: Exception) {
                message = e.message ?: ""
            }
            finally {
                if (track.isChanged == true) {
                    sendUpdate(TrackEvent.ITEM_IMAGE)
                }
            }
        }
    }

    /**
     * Load tracks from disk.
     * All exisisting tracks will be replaced.
     * If data are changed ask user about saving changed tracks.
     * Load tracks in thread(s).
     * Add all errors to log.
     * Function will send [TrackEvent.LIST_UPDATED] to all listeners.
     */
    fun loadTracks(newPath: String) {
        val start = System.currentTimeMillis()
        val tasks = mutableListOf<TaskReadAudio>()

        path        = newPath
        message     = Constants.MESSAGE_LOADING_TRACKS.format(path)
        tracks      = listOf()
        selectedRow = -1

        val list = File(path).listFiles()

        if (list != null) {
            try { // Catch very illegal filenames in windows.
                list.sortBy {
                    it.absoluteFile
                }
            }
            catch (e: Exception) {
                JOptionPane.showMessageDialog(null, e, Constants.APP_NAME, JOptionPane.ERROR_MESSAGE)
                return
            }

            list.filter(File::isAudio).forEach {
                tasks.add(TaskReadAudio(file = it, fileName = it.name))
            }
        }

        LogManager.getLogManager().reset()

        if (tasks.isNotEmpty()) {
            val manager = TaskManager(tasks = tasks, threadCount = Constants.THREADS, onError = TaskManager.Execution.CONTINUE, onCancel = TaskManager.Execution.STOP_JOIN)
            val dialog  = TaskDialog(taskManager = manager, type = TaskDialog.Type.PERCENT, title = Constants.DIALOG_TITLE_LOAD, parent = Main.window, height = Swing.defFont.size * 22)

            dialog.enableCancel = true
            dialog.start(messages = Constants.THREADS)

            val stat = Constants.MESSAGE_TIME.format("${ System.currentTimeMillis() - start}")

            message = if (tasks.countError == 0) {
                Constants.MESSAGE_LOADING.format(tasks.tracks.size, path, stat)
            }
            else {
                Constants.ERROR_LOADING.format(tasks.tracks.size, tasks.countError, path, stat)
            }

            tracks = tasks.tracks

            tasks.allErrors().forEach { task ->
                Swing.logMessage = task.error
            }

            Swing.logMessage = message
            sortTracks(Sort.FILE)
        }

        sendUpdate(TrackEvent.LIST_UPDATED)
    }

    /**
     * Send new message, which application can display as it like.
     */
    var message: String = ""
        set(value) {
            field = value
            messageFunc(value)
        }

    /**
     * Move current selected track down in list (up in array) and send [TrackEvent.ITEM_SELECTED].
     */
    fun moveRowDown() {
        if (selectedRow >= 0 && selectedRow < tracks.size - 1) {
            val list  = tracks.toMutableList()
            val index = selectedRow

            list.swap(index, index + 1)
            tracks = list
            selectedRow = index + 1

            sendUpdate(TrackEvent.ITEM_SELECTED)
        }
    }

    /**
     * Move current selected track up in list (down in array) and send [TrackEvent.ITEM_SELECTED].
     */
    fun moveRowUp() {
        if (selectedRow > 0) {
            val list  = tracks.toMutableList()
            val index = selectedRow

            list.swap(index, index - 1)
            tracks = list
            selectedRow = index - 1

            sendUpdate(TrackEvent.ITEM_SELECTED)
        }
    }

    /**
     * Delete image for current selected track and send [TrackEvent.ITEM_IMAGE].
     */
    fun removeImage() {
        val track = selectedTrack

        if (track != null) {
            track.cover = ""
            sendUpdate(TrackEvent.ITEM_IMAGE)
        }
    }

    /**
     * Remove all tags for current selected tracks and send [TrackEvent.ITEM_DIRTY] and [TrackEvent.ITEM_SELECTED].
     */
    fun removeTagsForAll() {
        var failed = false
        Track.ERRORS = 0

        tracks.filter {
            it.isSelected
        }.forEach { track ->
            try {
                track.clear()
            }
            catch (e: Exception) {
                Swing.logMessage = Constants.ERROR_DELETE_TAGS.format(track.fileNameWithExtension)
                Swing.logMessage = e.message.toString()
                failed = true
            }
        }

        sendUpdate(TrackEvent.ITEM_DIRTY)
        sendUpdate(TrackEvent.ITEM_SELECTED)

        if (failed == true) {
            JOptionPane.showMessageDialog(Main.window, Constants.ERROR_DELETE_TAGS2_HTML.format(selectedTrack?.fileNameWithExtension), Constants.DIALOG_CLEAR_FAILED, JOptionPane.ERROR_MESSAGE)
        }
        else if (Track.ERRORS > 0) {
            JOptionPane.showMessageDialog(Main.window, Constants.ERROR_DELETE_TAGS1_HTML.format(selectedTrack?.fileNameWithExtension), Constants.DIALOG_CLEAR_FAILED, JOptionPane.ERROR_MESSAGE)
        }
    }

    /**
     * Rename all selected tracks on various album fields and send [TrackEvent.LIST_UPDATED].
     */
    fun renameAlbums(options: Map<String, String>) {
        copyTagsFromAudio()

        if (options.isNotEmpty() == true) {
            var num = 0

            tracks.filter { track ->
                track.isSelected
            }.forEach { track ->
                track.renameAlbum(options, num)
                num++
            }
        }

        sendUpdate(TrackEvent.LIST_UPDATED)
    }

    /**
     * Rename all selected tracks on file names and send [TrackEvent.LIST_UPDATED].
     */
    fun renameFiles(options: MutableMap<String, String>) {
        copyTagsFromAudio()

        if (options.isNotEmpty() == true) {
            tracks.filter { track ->
                track.isSelected
            }.forEach { track ->
                track.renameFile(options)
            }
        }

        sendUpdate(TrackEvent.LIST_UPDATED)
    }

    /**
     * Rename all selected tracks on track title and send [TrackEvent.LIST_UPDATED].
     */
    fun renameTitles(options: Map<String, String>) {
        copyTagsFromAudio()

        if (options.isNotEmpty() == true) {
            tracks.filter { track ->
                track.isSelected
            }.forEach { track ->
                track.renameTitle(options)
            }
        }

        sendUpdate(TrackEvent.LIST_UPDATED)
    }

    /**
     * Check if files must be saved but ask first.
     */
    fun saveChangedAskFirst(displayErrorDialogOnSave: Boolean, abortOnFailedSave: Boolean): Boolean {
        if (isAnyChangedAndSelected &&
            JOptionPane.showConfirmDialog(Main.window, Constants.MESSAGE_ASK_SAVE_HTML, Constants.DIALOG_SAVE, JOptionPane.YES_NO_OPTION) == Constants.YES && saveTracks() == false) {

            if (displayErrorDialogOnSave == true) {
                JOptionPane.showMessageDialog(Main.window, Constants.ERROR_SAVE_HTML, Constants.DIALOG_SAVE_FAILED, JOptionPane.ERROR_MESSAGE )
            }

            if (abortOnFailedSave == true) {
                return false
            }
        }

        return true
    }
    /**
     * Display progress dialog and save all changed tracks and return true if all was saved.
     */
    fun saveTracks(): Boolean {
        val start = System.currentTimeMillis()
        val tasks = mutableListOf<TaskSaveAudio>()

        Track.ERRORS = 0

        tracks.filter { track ->
            track.isChanged && track.isSelected
        }.forEach { track ->
            tasks.add(TaskSaveAudio(track = track, fileName = track.fileName))
        }

        if (tasks.isNotEmpty() == true) {
            val manager = TaskManager(tasks = tasks, threadCount = Constants.THREADS, onError = TaskManager.Execution.CONTINUE, onCancel = TaskManager.Execution.STOP_JOIN)
            val dialog  = TaskDialog(taskManager = manager, type = TaskDialog.Type.PERCENT, title = Constants.DIALOG_TITLE_SAVE, parent = Main.window, height = Swing.defFont.size * 22)

            dialog.enableCancel = true
            dialog.start(messages = Constants.THREADS)

            val stat = Constants.MESSAGE_TIME.format("${System.currentTimeMillis() - start}")
            val row  = selectedRow

            message = if (tasks.countOk == 0) {
                Constants.ERROR_SAVING3.format(tasks.size)
            }
            else if (Track.ERRORS > 0) {
                Constants.ERROR_SAVING2.format(tasks.countOk)
            }
            else if (tasks.countError > 0) {
                Constants.ERROR_SAVING1.format(tasks.countOk, tasks.countError, stat)
            }
            else {
                Constants.MESSAGE_SAVING.format(tasks.size, stat)
            }

            selectedRow = row

            tasks.allErrors().forEach { task ->
                Swing.logMessage = task.error
            }

            Swing.logMessage = message
            sendUpdate(TrackEvent.ITEM_DIRTY)

            return tasks.countError == 0
        }

        return true
    }

    /**
     * Send update to all receivers.
     */
    fun sendUpdate(event: TrackEvent) {
        listeners.forEach { trackListener ->
            trackListener.update(event)
        }
    }

    /**
     * Select or unselect tracks and send [TrackEvent.ITEM_DIRTY].
     */
    fun selectAll(selected: Boolean) {
        tracks.forEach { track ->
            track.isSelected = selected
        }

        sendUpdate(TrackEvent.ITEM_DIRTY)
    }

    /**
     * Sort tracks.
     */
    fun sortTracks(sortTracksOn: Sort, descending: Boolean = false) {
        val list = tracks.sortedWith { track1, track2 ->
            when (sortTracksOn) {
                Sort.ALBUM -> (track1.album + track1.artist + track1.trackWithZeros).compareTo((track2.album + track2.artist + track2.trackWithZeros))
                Sort.ALBUM_ARTIST -> (track1.albumArtist + track1.album + track1.trackWithZeros).compareTo((track2.albumArtist + track2.album + track2.trackWithZeros))
                Sort.ARTIST -> (track1.artist + track1.album + track1.trackWithZeros).compareTo(track2.artist + track2.album + track2.trackWithZeros)
                Sort.BITRATE -> (track1.bitrate + track1.artist + track1.album + track1.trackWithZeros).compareTo(track2.bitrate + track2.artist + track2.album + track2.trackWithZeros)
                Sort.FILE -> track1.fileNameWithExtension.compareTo(track2.fileNameWithExtension)
                Sort.FORMAT -> (track1.formatInfo + track1.artist + track1.album + track1.trackWithZeros).compareTo(track2.formatInfo + track2.artist + track2.album + track2.trackWithZeros)
                Sort.GENRE -> (track1.genre + track1.artist + track1.album + track1.trackWithZeros).compareTo(track2.genre + track2.artist + track2.album + track2.trackWithZeros)
                Sort.SELECTED -> track1.isSelected.compareTo(track2.isSelected)
                Sort.SIZE -> (track1.fileSize + track1.artist + track1.album + track1.trackWithZeros).compareTo(track2.fileSize + track2.artist + track2.album + track2.trackWithZeros)
                Sort.TIME -> (track1.time + track1.artist + track1.album + track1.trackWithZeros).compareTo(track2.time + track2.artist + track2.album + track2.trackWithZeros)
                Sort.TITLE -> (track1.title + track1.artist + track1.album + track1.trackWithZeros).compareTo(track2.title + track2.artist + track2.album + track2.trackWithZeros)
                Sort.TRACK -> (track1.trackWithZeros + track1.artist + track1.album + track1.trackWithZeros).compareTo(track2.trackWithZeros + track2.artist + track2.album + track2.trackWithZeros)
                Sort.EXTENSION -> (track1.fileExt + track1.fileName).compareTo(track2.fileExt + track2.fileName)
            }
        }.toMutableList()

        if (descending == true) {
            list.reverse()
        }

        tracks = list
    }
}
