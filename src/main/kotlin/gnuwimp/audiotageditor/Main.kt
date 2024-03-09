/*
 * Copyright © 2021 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audiotageditor

import gnuwimp.swing.Swing
import java.awt.Font
import java.awt.Image
import java.awt.Toolkit
import javax.swing.JOptionPane
import javax.swing.SwingUtilities
import kotlin.system.exitProcess

//------------------------------------------------------------------------------
object Main {
    val noImage: Image
    val icon: Image
    val window: MainWindow

    init {
        try {
            Swing.setup(theme = "nimbus", appName = Constants.APP_NAME, aboutText = Constants.APP_ABOUT, quitLambda = { quit() })

            noImage       = "gnuwimp/audiotageditor/cover.png".loadImageFromResource()
            icon          = "gnuwimp/audiotageditor/AudioTagEditor.png".loadImageFromResource()
            Swing.bigFont = Font(Font.SANS_SERIF, Font.PLAIN, 24)
            Swing.defFont = Font(Font.SANS_SERIF, Font.PLAIN, 12)
            window        = MainWindow()
        }
        catch(e: Exception) {
            e.printStackTrace()
            JOptionPane.showMessageDialog(null, e, Constants.APP_NAME, JOptionPane.ERROR_MESSAGE)
            exitProcess(status = 1)
        }
    }

    //--------------------------------------------------------------------------
    private fun String.loadImageFromResource(): Image {
        val classLoader = Main::class.java.classLoader
        val pathShell   = classLoader.getResource(this)

        return Toolkit.getDefaultToolkit().getImage(pathShell)
    }

    //--------------------------------------------------------------------------
    private fun quit() {
        window.quit()
    }

    //--------------------------------------------------------------------------
    @JvmStatic fun main(args: Array<String>) {
        try {
            SwingUtilities.invokeLater {
                window.prefLoad()
                window.isVisible = true
            }
        }
        catch(e: Exception) {
            e.printStackTrace()
            JOptionPane.showMessageDialog(null, e, Constants.APP_NAME, JOptionPane.ERROR_MESSAGE)
        }
    }
}
