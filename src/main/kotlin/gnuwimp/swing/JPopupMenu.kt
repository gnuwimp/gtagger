/*
 * Copyright 2016 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.swing

import java.awt.event.ActionListener
import javax.swing.JMenuItem
import javax.swing.JPopupMenu

/**
 * Add menu items to popup menu
 */
fun JPopupMenu.addItems(labels: List<String>, listener: ActionListener) {
    labels.forEach {
        val item = JMenuItem(it)
        item.addActionListener(listener)
        this.add(item)
    }
}
