/*
 * Copyright 2016 - 2019 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.gtagger

import gnuwimp.core.swing.Platform
import gnuwimp.gtagger.resource.Text
import java.awt.Image
import java.awt.Toolkit
import javax.swing.JOptionPane
import javax.swing.SwingUtilities
import kotlin.system.exitProcess

/**
 * Application starts here
 */
object Main {
    val noImage:  Image
    val icon: Image
    val window: MainWindow

    init {
        try {
            Platform.setup(nativeLook = false, appName = Text.APP_NAME, aboutText = Text.APP_ABOUT, quitHandler = { quit() })

            noImage = loadImageFromResource(path = "gnuwimp/gtagger/resource/noimage.png")
            icon = loadImageFromResource(path = "gnuwimp/gtagger/resource/icon.png")
            window = MainWindow()
        }
        catch(e: Exception) {
            e.printStackTrace()
            JOptionPane.showMessageDialog(null, e, Text.APP_NAME, JOptionPane.ERROR_MESSAGE)
            exitProcess(status = 1)
        }
    }

    /**
     * Load icons from jar file
     */
    private fun loadImageFromResource(path: String): Image {
        val classLoader = Main::class.java.classLoader
        val pathShell = classLoader.getResource(path)
        return Toolkit.getDefaultToolkit().getImage(pathShell)
    }

    /**
     * Quit for macOS
     */
    private fun quit() {
        window.quit()
    }

    /**
     * Create window and restore previous state
     */
    @JvmStatic fun main(args: Array<String>) {
        try {
            SwingUtilities.invokeLater {
                window.isVisible = true
                window.windowRestore()
            }
        }
        catch(e: Exception) {
            e.printStackTrace()
            JOptionPane.showMessageDialog(null, e, Text.APP_NAME, JOptionPane.ERROR_MESSAGE)
        }
    }
}
