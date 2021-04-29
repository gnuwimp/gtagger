/*
 * Copyright 2016 - 2021 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.gtagger

/**
 * All text strings for the application
 */
object Labels {
    const val APP_NAME                    = "gTagger"
    const val APP_ABOUT: String           = "<html><h2>gTagger 1.03</h2>" +
        "<font color=\"blue\">" +
        "Copyright 2016 - 2021 gnuwimp@gmail.com.<br>" +
        "Released under the GNU General Public License v3.0.<br>" +
        "See https://github.com/gnuwimp/gtagger.<br>" +
        "</font>" +
        "<br>" +
        "gTagger is an audio file tag editor written in Kotlin.<br>" +
        "Use gTagger with caution and at your own risk.<br>" +
        "<br>" +
        "Following third party software library is used:<br>" +
        "JAudioTagger - http://www.jthink.net/jaudiotagger<br>" +
        "</html>"

    const val DIALOG_ABOUT                = "About gTagger"
    const val DIALOG_CLEAR                = "Clear Tags"
    const val DIALOG_CLEAR_FAILED         = "Clear Tags Failed"
    const val DIALOG_DELETE               = "Delete File"
    const val DIALOG_FILTER               = "Filter Tracks"
    const val DIALOG_LOG                  = "Log"
    const val DIALOG_SAVE                 = "Save Changes"
    const val DIALOG_SAVE_FAILED          = "Save Failed"
    const val DIALOG_TITLE_LOAD           = "Loading Tracks"
    const val DIALOG_TITLE_SAVE           = "Saving Tracks"

    const val LABEL_ABOUT                 = "About"
    const val LABEL_ALBUM                 = "Album"
    const val LABEL_ALBUM_ARTIST          = "Album Artist"
    const val LABEL_APPEND_TEXT           = "Append Text"
    const val LABEL_APPLY_CHANGES         = "Apply Changes"
    const val LABEL_ARTIST                = "Artist"
    const val LABEL_BITRATE               = "Bitrate"
    const val LABEL_COMMENT               = "Comment"
    const val LABEL_COMPOSER              = "Composer"
    const val LABEL_COPY_ARTIST           = "Copy Tags"
    const val LABEL_COVER                 = "Cover"
    const val LABEL_DELETE_TAGS           = "Delete Tags"
    const val LABEL_DELETE_TAGS2          = "Delete Tags..."
    const val LABEL_DELETE_TRACK          = "Delete Track..."
    const val LABEL_ENCODER               = "Encoder"
    const val LABEL_EXTENSION             = "Extension"
    const val LABEL_FILE                  = "File"
    const val LABEL_FILENAME              = "Filename"
    const val LABEL_FILTER                = "Select..."
    const val LABEL_FORMAT                = "Format"
    const val LABEL_GENRE                 = "Genre"
    const val LABEL_INSERT_ALBUM          = "Insert Album"
    const val LABEL_INSERT_ARTIST         = "Insert Artist"
    const val LABEL_INSERT_TEXT           = "Insert Text"
    const val LABEL_LOAD_IMAGE            = "Load Cover Image..."
    const val LABEL_MOVE_DOWN             = "Move Down"
    const val LABEL_MOVE_UP               = "Move Up"
    const val LABEL_NUMBER_SEP            = "Number Separator"
    const val LABEL_QUIT                  = "Quit"
    const val LABEL_RELOAD_DIR            = "Reload Directory"
    const val LABEL_REMOVE_COVER          = "Remove Cover Image"
    const val LABEL_REMOVE_ILLEG          = "Remove Illegal Characters"
    const val LABEL_REMOVE_LEAD           = "Remove Leading"
    const val LABEL_REMOVE_TEXT           = "Remove Text"
    const val LABEL_REMOVE_TRAIL          = "Remove Trailing"
    const val LABEL_REPLACE_TEXT          = "Replace Text"
    const val LABEL_RESET                 = "Reset Options"
    const val LABEL_SAVE                  = "Save Changes"
    const val LABEL_SELECT                = "Select"
    const val LABEL_SELECT_ALL            = "Select All"
    const val LABEL_SELECT_NONE           = "Unselect All"
    const val LABEL_SET_FILENAME          = "Set Filename"
    const val LABEL_SET_TITLE             = "Set Title"
    const val LABEL_SET_YEAR              = "Set Year"
    const val LABEL_SHOW_LOG              = "Show Log..."
    const val LABEL_SIZE                  = "Size"
    const val LABEL_START_TRACK           = "Start Track"
    const val LABEL_TAB_ALBUM             = "Rename Album Tags"
    const val LABEL_TAB_FILE              = "Rename File Names"
    const val LABEL_TAB_TITLE             = "Rename Title Tags"
    const val LABEL_TAB_TRACK             = "Edit Tracks"
    const val LABEL_TIME                  = "Time"
    const val LABEL_TITLE                 = "Title"
    const val LABEL_TRACK                 = "Track"
    const val LABEL_UNDO                  = "Undo Changes"
    const val LABEL_USE_FILENAME          = "Use Filename"
    const val LABEL_USE_TITLE             = "Use Title"
    const val LABEL_YEAR                  = "Year"

    const val DEF_INSERT_ALBUM            = " - "
    const val DEF_INSERT_ARTIST           = " - "
    const val DEF_NUMBER_SEP              = " "
    const val DEF_START_TRACK             = "1"

    const val ERROR_ABORTING_READING      = "Aborting reading tracks"
    const val ERROR_ABORTING_SAVING       = "Aborting saving tracks"
    const val ERROR_DELETE_TAGS           = "Error! delete tags on file %s failed"
    const val ERROR_DELETE_TAGS1_HTML     = "<html><h3>Error! </h3>delete tags on file %s failed</html>"
    const val ERROR_DELETE_TAGS2_HTML     = "<html><h3>Error! </h3>delete tags on file(s) failed</html>"
    const val ERROR_LOADING               = "Error! loaded %d track(s) and %d track(s) failed or skipped from %s %s"
    const val ERROR_LOADING_TRACK         = "Error! can't read file %s"
    const val ERROR_RENAME                = "Error! can't rename track"
    const val ERROR_SAVE_HTML             = "<html><h3>Error! </h3>Saving tracks failed, check the log what track(s) failed!</html>"
    const val ERROR_SAVING                = "Error! Saved %d track(s) and %d track(s) failed %s"
    const val ERROR_TAG_OBJECT            = "Error! tag object is missing"

    const val MESSAGE_ASK_CLEAR_HTML      = "<html><h3>Are you sure </h3>that you want to clear all tags from all selected tracks?</html>"
    const val MESSAGE_ASK_DELETE_HTML     = "<html><h3>Are you sure </h3>that you want to delete this track?</html>"
    const val MESSAGE_ASK_FILTER_ALBUM    = "Enter text to search for in artist, album and genre fields"
    const val MESSAGE_ASK_FILTER_FILE     = "Enter text to search for in file names"
    const val MESSAGE_ASK_FILTER_TITLE    = "Enter text to search for in titles"
    const val MESSAGE_ASK_SAVE_HTML       = "<html><h3>You have modified tracks, </h3>do you want to save changes?</html>"
    const val MESSAGE_LOADING             = "Loaded %d track(s) from %s %s"
    const val MESSAGE_LOADING_TRACK       = "Loading file %s"
    const val MESSAGE_LOADING_TRACKS      = "Loading tracks from %s"
    const val MESSAGE_RENAMING_TRACK      = "Renaming file %s to %s"
    const val MESSAGE_SAVING              = "Saved %d track(s) %s"
    const val MESSAGE_SAVING_TRACK        = "Saving file %s"
    const val MESSAGE_TIME                = "in %s ms"

    const val TOOL_ABOUT                  = "Show about dialog"
    const val TOOL_ALBUM                  = "Select to change album text for all selected tracks."
    const val TOOL_ALBUM_ARTIST           = "Set album artist for all selected tracks."
    const val TOOL_APPEND_TEXT            = "Enter text to append AFTER filename for all selected tracks."
    const val TOOL_APPLY_CHANGES          = "Apply changes to all selected track but do NOT save them."
    const val TOOL_ARTIST                 = "Select to change artist text for all selected tracks."
    const val TOOL_COMMENT                = "Select to change comment text for all selected tracks."
    const val TOOL_COMPOSER               = "Select to change composer text for all selected tracks."
    const val TOOL_COPY_ARTIST            = "Copy artist text to album artist or vice versa."
    const val TOOL_COVER                  = "<html>Select to change cover image for all selected tracks.<br>Load image first or use default image to remove covers.<html>"
    const val TOOL_DELETE_COVER           = "Delete cover image for selected track."
    const val TOOL_DELETE_TAGS1           = "Remove all tags for selected track"
    const val TOOL_DELETE_TAGS2           = "Clear all tags from all tracks"
    const val TOOL_DELETE_TRACK           = "Delete selected track from disk!"
    const val TOOL_ENCODER                = "Select to change encoder for all selected tracks."
    const val TOOL_EXT_CAP                = "Change file extension capitalisation"
    const val TOOL_GENRE                  = "Select to change genre text for all selected tracks."
    const val TOOL_INSERT_ALBUM           = "<html>Insert album BEFORE text.<br>Enter optional separator between album and text.<br>Default separator is ' - '</html>."
    const val TOOL_INSERT_ARTIST          = "<html>Insert artist BEFORE text.<br>Enter optional separator between artist and text.<br>Default separator is ' - '</html>."
    const val TOOL_INSERT_TEXT            = "Enter text to insert BEFORE text."
    const val TOOL_LOAD_IMAGE             = "Load cover image from disk"
    const val TOOL_MOVE_DOWN              = "Move selected track down in the track list"
    const val TOOL_MOVE_UP                = "Move selected track up in the track list"
    const val TOOL_NAME_CAP               = "Change filename capitalisation"
    const val TOOL_NUMBER                 = "Insert or append track number before filename."
    const val TOOL_NUMBER_SEP             = "<html>Separator string to insert or append between track number and text.<br>Default separator is space.</html>"
    const val TOOL_QUIT                   = "Quit gTagger"
    const val TOOL_RELOAD                 = "Reload all tracks from surrent directory"
    const val TOOL_REMOVE_ILLEG           = "Remove characters such as : and / and a few more that might cause problem on various OS."
    const val TOOL_REMOVE_LEAD            = "Remove all leading none letters from the text until first letter is found."
    const val TOOL_REMOVE_TEXT            = "<html>Enter text to remove from the text.<br>Try regular expressions such as <b>'^\\d\\d'</b> which removes the two first numbers.</html>."
    const val TOOL_REMOVE_TRAIL           = "Remove all trailing none letters from the filename."
    const val TOOL_REPLACE_TEXT           = "Enter text to replace removed text with."
    const val TOOL_RESET                  = "Reset all options."
    const val TOOL_SAVE                   = "<html>Save all SELECTED tracks that have been MODIFIED.<br>Changes can't be undone!</html>"
    const val TOOL_SELECT_ALBUM           = "Select tracks by searching artist, album and genre."
    const val TOOL_SELECT_ALL             = "Select all loaded tracks."
    const val TOOL_SELECT_FILE            = "Select tracks by searching file names."
    const val TOOL_SELECT_NONE            = "Unselect all loaded tracks."
    const val TOOL_SELECT_TITLE           = "Select tracks by searching titles."
    const val TOOL_SET_FILENAME           = "Enter text to use as the filename."
    const val TOOL_SET_TITLE              = "Set title to input text."
    const val TOOL_SET_YEAR               = "Select to change year for all selected tracks."
    const val TOOL_SHOW_LOG               = "Show log window"
    const val TOOL_START_TRACK            = "<html>Select to change track number for all selected tracks.<br>Set start track number.</html>"
    const val TOOL_TABLE_HEAD             = "<html>Click table header to sort rows.<br>Ctrl-click to reverse sort order.</html>"
    const val TOOL_TAB_ALBUM              = "Update album tags for all tracks."
    const val TOOL_TAB_FILE               = "Update file names for all tracks."
    const val TOOL_TAB_TITLE              = "Update title tags for all tracks."
    const val TOOL_TAB_TRACK              = "Edit single tracks."
    const val TOOL_UNDO                   = "Undo all changes made to the tracks."
    const val TOOL_USE_FILENAME           = "Use filename as title."
    const val TOOL_USE_TITLE              = "If title exist, use it as the filename."

    val OPTIONS_CAP_EXT: List<String>     = listOf("All Extension Lowercase", "All Extension Uppercase")
    val OPTIONS_CAP_NAME: List<String>    = listOf("Capitalize All Words", "All Letters Lowercase", "All letters Uppercase")
    val OPTIONS_CAP_TITLE: List<String>   = listOf("Capitalize All Words", "All Letters Lowercase", "All Letters Uppercase")
    val OPTIONS_COPY_ARTIST: List<String> = listOf("Copy Artist To 'Album Artist'", "Copy Album To 'Album Artist'", "Copy Artist + Album To 'Album Artist'", "Copy 'Album Artist' To Artist", "Copy 'Album Artist' To Album")
    val OPTIONS_NUMBER: List<String>      = listOf("Insert Track Number", "Insert Track Number - 2 Decimals", "Insert Track Number - 3 Decimals", "Insert Track Number - 4 Decimals", "Append Track Number", "Append Track Number - 2 Decimals", "Append Track Number - 3 Decimals", "Append Track Number - 4 Decimals")

    const val YES                         = 0
    const val NO                          = 1
    const val CANCEL                      = 2
    const val ICON_SIZE                   = 180
}
