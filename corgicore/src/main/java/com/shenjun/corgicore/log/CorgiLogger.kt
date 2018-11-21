package com.shenjun.corgicore.log

import android.util.Log
import com.shenjun.corgicore.BuildConfig

/**
 * Created by shenjun on 2018/11/21.
 * log for CorgiVideoPlayer
 */
class CorgiLogger {

    companion object {

        var DEBUG: Boolean = BuildConfig.DEBUG

        private const val TAG = "CorgiVideoPlayer"

        fun v(msg: String) = printLog(Log.VERBOSE, msg)

        fun d(msg: String) = printLog(Log.DEBUG, msg)

        fun i(msg: String) = printLog(Log.INFO, msg)

        fun w(msg: String) = printLog(Log.WARN, msg)

        fun e(msg: String) = printLog(Log.ERROR, msg)

        private fun printLog(type: Int, msg: String) {
            if (DEBUG) {
                when (type) {
                    Log.VERBOSE -> Log.v(TAG, msg)
                    Log.DEBUG -> Log.d(TAG, msg)
                    Log.INFO -> Log.i(TAG, msg)
                    Log.WARN -> Log.w(TAG, msg)
                    Log.ERROR -> Log.e(TAG, msg)
                }
            }
        }
    }
}