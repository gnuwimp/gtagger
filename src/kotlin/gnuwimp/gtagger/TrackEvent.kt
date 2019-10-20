/*
 * Copyright 2016 - 2019 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.gtagger

/**
 * Data events for all classes that are interested to receive data changes
 * All events are sent to classes using TrackListener interface
 */
enum class TrackEvent {
    ITEM_DIRTY,
    ITEM_IMAGE,
    ITEM_SELECTED,
    LIST_UPDATED,
}

/**
 * Interface for track data events
 * Inherit to receive events
 */
interface TrackListener {
    fun update(event: TrackEvent) {}
}
