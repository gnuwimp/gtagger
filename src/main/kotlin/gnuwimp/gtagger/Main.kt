/*
 * Copyright 2016 - 2021 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.gtagger

import gnuwimp.swing.Swing
import java.awt.Font
import java.awt.Image
import java.awt.Toolkit
import javax.swing.JOptionPane
import javax.swing.SwingUtilities
import kotlin.system.exitProcess

//------------------------------------------------------------------------------
object Main {
    val noImage:  Image
    val icon: Image
    val window: MainWindow

    init {
        try {
            Swing.setup(nativeLook = false, appName = Labels.APP_NAME, aboutText = Labels.APP_ABOUT, quitLambda = { quit() })

            noImage       = "gnuwimp/gtagger/cover.png".loadImageFromResource()
            icon          = "gnuwimp/gtagger/gtagger.png".loadImageFromResource()
            Swing.bigFont = Font(Font.SANS_SERIF, Font.PLAIN, 24)
            Swing.defFont = Font(Font.SANS_SERIF, Font.PLAIN, 12)
            window        = MainWindow()
        }
        catch(e: Exception) {
            e.printStackTrace()
            JOptionPane.showMessageDialog(null, e, Labels.APP_NAME, JOptionPane.ERROR_MESSAGE)
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
                window.isVisible = true
                window.prefLoad()
            }
        }
        catch(e: Exception) {
            e.printStackTrace()
            JOptionPane.showMessageDialog(null, e, Labels.APP_NAME, JOptionPane.ERROR_MESSAGE)
        }
    }
}
