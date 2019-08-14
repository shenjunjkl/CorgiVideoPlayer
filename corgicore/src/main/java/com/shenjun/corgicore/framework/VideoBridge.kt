package com.shenjun.corgicore.framework

import android.graphics.SurfaceTexture
import com.shenjun.corgicore.callback.VideoViewCallback
import com.shenjun.corgicore.log.logD
import com.shenjun.corgicore.player.IVideoPlayer
import com.shenjun.corgicore.player.PlayerStateMachine
import com.shenjun.corgicore.player.msg.MsgInit
import com.shenjun.corgicore.view.ControllerVideoView

/**
 * Created by shenjun on 2018/11/21.
 */
open class VideoBridge<out P : AbstractVideoRepo>(
    private val repo: P,
    private val videoView: ControllerVideoView,
    private val videoConfig: VideoConfig = VideoConfig()
) : VideoViewCallback, IVideoPlayer.IPlayerCallback {

    private val mStateMachine = PlayerStateMachine()

    init {
        initVideoBridge()
    }

    private fun initVideoBridge() {
        videoView.setVideoViewCallback(this)
    }

    fun startPlay() {
        mStateMachine.post(MsgInit(videoConfig, this))
    }

    override fun onViewSizeChanged(width: Int, height: Int) {
        logD("onViewSurfaceAvailable: width = $width, height = $height")
    }

    override fun onViewSurfaceAvailable(surfaceTexture: SurfaceTexture) {
        logD("onViewSurfaceAvailable: $surfaceTexture")
    }

    override fun onViewSurfaceDestroyed() {
        logD("onViewSurfaceDestroyed")
    }
}