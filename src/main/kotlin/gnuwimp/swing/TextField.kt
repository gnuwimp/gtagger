/*
 * Copyright 2016 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.swing

import java.awt.event.KeyEvent
import javax.swing.JTextField

/**
 * A text field with callback for data changes from user.
 */
class TextField(value: String = "", val callback: ((String) -> Unit)? = null) : JTextField(value) {
    init {
        addKeyListener(object : java.awt.event.KeyListener {
            override fun keyTyped(e: KeyEvent?) {}

            override fun keyPressed(e: KeyEvent?) {}

            override fun keyReleased(e: KeyEvent?) {
                callback?.invoke(text)
            }
        })
    }
}
