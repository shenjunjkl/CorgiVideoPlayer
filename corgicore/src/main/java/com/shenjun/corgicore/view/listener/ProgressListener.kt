package com.shenjun.corgicore.view.listener

/**
 * Created by shenjun on 2019-08-30.
 */
interface ProgressListener {

    fun onProgressUpdate(timeMs: Long) {}

    fun onBufferProgressUpdate(percent: Float) {}

    fun onDurationUpdate(durationMs: Long) {}
}