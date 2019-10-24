package com.shenjun.corgicore.constant

/**
 * Created by shenjun on 2019-09-09.
 */
object ControllerConst {

    const val REVERSE_PLAY_STATE = 1
    const val SEEK_START = 2
    const val SEEKING = 3
    const val SEEK_END = 4
    /**
     * change controller's visibility
     * params: [KEY_SHOW], [KEY_CONTROLLER_KEY]
     */
    const val VISIBILITY = 5







    /**
     * long, time in millisecond
     */
    const val KEY_TIME_MS = "corgi_timeMs"
    /**
     * boolean, mark showing or hiding controller
     */
    const val KEY_SHOW = "corgi_show"
    /**
     * String, to find a specific controller
     */
    const val KEY_CONTROLLER_KEY = "corgi_controller_key"
}