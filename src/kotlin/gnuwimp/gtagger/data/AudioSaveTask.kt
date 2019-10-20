/*
 * Copyright 2016 - 2019 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.gtagger.data

import gnuwimp.core.util.Task
import gnuwimp.gtagger.resource.Text

/**
 * Thread task that will be running in background saving audio tags
 */
class AudioSaveTask(val track: Track, fileName: String) : Task(max = 1) {
    init {
        message = fileName
    }

    /**
     * Save tags to file
     */
    override fun run() {
        checkPause()

        if (abort)
            throw Exception(Text.ERROR_ABORTING_SAVING)
        else {
            track.save()
            progress = 1
        }
    }
}
