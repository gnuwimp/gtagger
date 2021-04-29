/*
 * Copyright 2016 - 2021 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.gtagger

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
