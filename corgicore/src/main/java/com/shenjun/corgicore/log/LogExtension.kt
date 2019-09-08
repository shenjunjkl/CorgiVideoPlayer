package com.shenjun.corgicore.log

import android.util.Log
import com.shenjun.corgicore.BuildConfig
import com.shenjun.corgicore.tools.name

/**
 * Created by shenjun on 2019/1/10.
 */
class CorgiLog {
    companion object {
        var DEBUG: Boolean = BuildConfig.DEBUG
    }
}

fun Any.logD(msg: String) {
    if (CorgiLog.DEBUG) {
        Log.d(createTag(), msg)
    }
}

fun Any.logW(msg: String) {
    if (CorgiLog.DEBUG) {
        Log.w(createTag(), msg)
    }
}

fun Any.logE(msg: String) {
    if (CorgiLog.DEBUG) {
        Log.e(createTag(), msg)
    }
}

private fun Any.createTag() = "Corgi#${name()}"