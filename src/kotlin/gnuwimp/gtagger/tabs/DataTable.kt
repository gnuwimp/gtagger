/*
 * Copyright 2016 - 2019 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.gtagger.tabs

import gnuwimp.core.swing.ColorTable
import gnuwimp.gtagger.data.Data
import java.awt.Color

/**
 * Base table for all tables
 * Adds background color for changed tracks
 * And for tracks that has failed to save
 */
class DataTable : ColorTable() {
    private val dirtyColor = Color(255, 230, 230)
    private val errorColor = Color(255, 0, 0)

    /**
     * Get background color
     */
    override fun getRowBgColor(row: Int, column: Int): Color {
        val track = Data.getTrack(row)

        return when {
            track == null -> background
            track.hasError -> errorColor
            track.isChanged -> dirtyColor
            else -> super.getRowBgColor(row, column)
        }
    }
}
