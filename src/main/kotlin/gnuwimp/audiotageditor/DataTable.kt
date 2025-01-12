/*
 * Copyright 2021 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audiotageditor

import gnuwimp.swing.Table
import java.awt.Color

//------------------------------------------------------------------------------
class DataTable : Table() {
    private val dirtyColor = Color(255, 230, 230)
    private val errorColor = Color(255, 0, 0)

    //--------------------------------------------------------------------------
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
