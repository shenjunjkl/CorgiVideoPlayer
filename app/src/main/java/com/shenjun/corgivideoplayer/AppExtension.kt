package com.shenjun.corgivideoplayer

import android.content.Context
import android.util.TypedValue
import android.widget.SeekBar


fun Context.dp2px(dp: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(), resources.displayMetrics
    ).toInt()
}

fun SeekBar.setProgressForVideo(timeMs: Long, durationMs: Long) {
    progress = if (durationMs > 0) (timeMs * max / durationMs).toInt() else 0
}

fun SeekBar.getTimeMs(durationMs: Long): Long {
    if (max == 0 || durationMs <= 0) return 0
    return (durationMs * (progress * 1f / max)).toLong()
}