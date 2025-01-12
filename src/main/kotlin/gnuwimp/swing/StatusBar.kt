/*
 * Copyright 2016 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.swing

import java.awt.Color
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.BevelBorder

/**
 * A simple status bar widget that colors text to red if "error" is in string
 */
class StatusBar : JPanel() {
    private val _label = JLabel()

    init {
        border = BorderFactory.createEtchedBorder(BevelBorder.LOWERED)
        add(_label)
        _label.font = Font(Font.MONOSPACED, Font.PLAIN, font.size)
    }

    /**
     * Set or get message string
     */
    var message: String
        get() = _label.text

        set(value) {
            _label.text = " $value"

            if (value.indexOf("Error") == 0 || value.indexOf("error") == 0) {
                _label.foreground = Color(255, 0, 0)
            }
            else {
                _label.foreground = parent.foreground
            }
        }
}
