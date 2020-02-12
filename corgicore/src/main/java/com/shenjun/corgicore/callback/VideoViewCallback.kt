package com.shenjun.corgicore.callback

import android.graphics.SurfaceTexture

/**
 * Created by shenjun on 2018/11/23.
 */
interface VideoViewCallback {

    fun onViewSizeChanged(width: Int, height: Int)

    fun onViewSurfaceAvailable(surfaceTexture: SurfaceTexture)

    fun onViewSurfaceDestroyed()

    fun onOperateReversePlayState()

    fun onOperateSeekStart(startTimeMs: Long)

    fun onOperateSeeking(startTimeMs: Long, curTimeMs: Long)

    fun onOperateSeekEnd(timeMs: Long)

    fun onOperateReplay()

    fun onOperateRetry()

    fun onOperateControllerVisibilityEvent(isShow: Boolean, key: String)

}