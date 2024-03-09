/*
 * Copyright 2016 - 2021 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.gtagger

import java.io.File
import java.util.prefs.Preferences

/**
 * Split view pos for album tab
 */
var Preferences.albumSplit: Int
    get() = getInt("split_album", 200)

    set(value) {
        putInt("split_album", value)
    }

/**
 * Split view pos for directories
 */
var Preferences.dirSplit: Int
    get() = getInt("split_dir", 200)

    set(value) {
        putInt("split_dir", value)
    }

/**
 * Split view pos for file tab
 */
var Preferences.fileSplit: Int
    get() = getInt("split_file", 200)

    set(value) {
        putInt("split_file", value)
    }

/**
 * Last used path
 */
var Preferences.lastPath: String
    get() = get("path_last", File(System.getProperty("user.home")).canonicalPath)

    set(value) {
        put("path_last", value)
    }

/**
 * Last used image path
 */
var Preferences.picPath: String
    get() = get("path_pic", File(System.getProperty("user.home")).canonicalPath)

    set(value) {
        put("path_pic", value)
    }

/**
 * Split view pos for title tab
 */
var Preferences.titleSplit: Int
    get() = getInt("split_title", 200)

    set(value) {
        putInt("split_title", value)
    }

/**
 * Split view pos for track tab
 */
var Preferences.trackSplit: Int
    get() = getInt("split_track", 200)

    set(value) {
        putInt("split_track", value)
    }

/**
 * Window height
 */
var Preferences.winHeight: Int
    get() = getInt("win_height", 600)

    set(value) {
        putInt("win_height", value)
    }

/**
 * Window maximized?
 */
var Preferences.winMax: Boolean
    get() = getBoolean("win_max", false)

    set(value) {
        putBoolean("win_max", value)
    }

/**
 * Window width
 */
var Preferences.winWidth: Int
    get() = getInt("win_width", 800)

    set(value) {
        putInt("win_width", value)
    }

/**
 * Window x pos
 */
var Preferences.winX: Int
    get() = getInt("win_x", 50)

    set(value) {
        putInt("win_x", value)
    }

/**
 * Window y pos
 */
var Preferences.winY: Int
    get() = getInt("win_y", 50)

    set(value) {
        putInt("win_y", value)
    }

