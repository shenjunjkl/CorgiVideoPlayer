package com.shenjun.corgicore.view.listener

/**
 * Created by shenjun on 2019-10-24.
 */
interface SeekStateListener {

    fun onSeekStart(startTimeMs: Long)

    fun onSeeking(startTimeMs: Long, curTimeMs: Long)

    fun onSeekEnd(timeMs: Long)
}