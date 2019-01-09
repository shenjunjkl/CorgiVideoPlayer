package com.shenjun.corgicore.framework

import android.graphics.SurfaceTexture
import com.shenjun.corgicore.callback.VideoViewCallback
import com.shenjun.corgicore.log.logW
import com.shenjun.corgicore.view.ControllerVideoView

/**
 * Created by shenjun on 2018/11/21.
 */
open class VideoBridge<out P : AbstractVideoRepo>(
    private val repo: P,
    private val videoView: ControllerVideoView,
    val videoConfig: VideoConfig = VideoConfig()
) : VideoViewCallback {

    init {
        initVideoBridge()
    }

    private fun initVideoBridge() {
        videoView.setVideoViewCallback(this)
    }

    override fun onViewSizeChanged(width: Int, height: Int) {
        logW("onViewSurfaceAvailable: width = $width, height = $height")
    }

    override fun onViewSurfaceAvailable(surfaceTexture: SurfaceTexture) {
        logW("onViewSurfaceAvailable: $surfaceTexture")
    }

    override fun onViewSurfaceDestroyed() {
        logW("onViewSurfaceDestroyed")
    }
}