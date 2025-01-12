/*
 * Copyright 2021 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audiotageditor

import gnuwimp.util.Task

/**
 * Thread task that will be running in background saving audio tags.
 */
class TaskSaveAudio(val track: Track, fileName: String) : Task(max = 1) {
    init {
        message = fileName
    }

    //--------------------------------------------------------------------------
    override fun run() {
        if (abort == true) {
            throw Exception(Constants.ERROR_ABORTING_SAVING)
        }
        else {
            track.save(updateTagsWithUserData = true)
            progress = 1
        }
    }
}
