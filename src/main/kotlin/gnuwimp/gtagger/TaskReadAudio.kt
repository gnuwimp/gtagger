/*
 * Copyright 2016 - 2021 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.gtagger

import gnuwimp.util.Task
import java.io.File

/**
 * Thread task that will be running in background reading audio tags.
 */
class TaskReadAudio(val file: File, fileName: String) : Task(max = 1) {
    var track: Track? = null
        private set

    init {
        message = fileName
    }

    //--------------------------------------------------------------------------
    override fun run() {
        if (abort == true) {
            throw Exception(Labels.ERROR_ABORTING_READING)
        }
        else {
            track    = Track(file)
            progress = 1
        }
    }
}
