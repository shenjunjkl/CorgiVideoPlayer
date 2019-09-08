package com.shenjun.corgicore.tools

import java.util.*

/**
 * Created by shenjun on 2019-08-30.
 */
fun Any.name(): String = javaClass.simpleName

fun time2MediaLength(time: Long): String {
    var hour: Long = 0
    var minute: Long = 0
    var second: Long = time / 1000

    if (second > 60) {
        minute = second / 60
        second %= 60
    }
    if (minute > 60) {
        hour = minute / 60
        minute %= 60
    }

    return if (hour == 0L) {
        String.format(Locale.ENGLISH, "%02d:%02d", minute, second)
    } else {
        String.format(Locale.ENGLISH, "%02d:%02d:%02d", hour, minute, second)
    }
}