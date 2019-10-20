/*
 * Copyright 2016 - 2019 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.gtagger.data

import gnuwimp.core.util.Task
import gnuwimp.gtagger.resource.Text
import java.io.File

/**
 * Thread task that will be running in background reading audio tags
 */
class AudioReadTask(val file: File, fileName: String) : Task(max = 1) {
    var track: Track? = null
        private set

    init {
        message = fileName
    }

    /**
     * Read tags from file
     */
    override fun run() {
        checkPause()

        if (abort)
            throw Exception(Text.ERROR_ABORTING_READING)
        else {
            track   = Track(file)
            progress = 1
        }
    }
}
