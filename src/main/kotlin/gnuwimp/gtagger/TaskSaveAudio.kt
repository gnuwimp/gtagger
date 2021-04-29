/*
 * Copyright 2016 - 2021 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.gtagger

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
            throw Exception(Labels.ERROR_ABORTING_SAVING)
        }
        else {
            track.save()
            progress = 1
        }
    }
}
