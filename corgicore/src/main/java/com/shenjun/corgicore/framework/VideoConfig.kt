package com.shenjun.corgicore.framework

import com.shenjun.corgicore.player.IVideoPlayer
import com.shenjun.corgicore.sys.SystemPlayerProvider

/**
 * Created by shenjun on 2018/11/22.
 */
class VideoConfig : IVideoPlayer.IPlayerProvider {

    var playerProvider: IVideoPlayer.IPlayerProvider? = null

    var updateFrameDuringSeeking = false
    var muteDuringSeeking = false

    var originalVolumeLeft = 1f
    var originalVolumeRight = 1f

    var loopVideo = false

    var fillMode = FILL_MODE_FIT_CENTER
    var fillModeNum = 0
    var fillModeDen = 0

    var autoHideMainControllerIntervalMs = 3000L

    companion object {
        const val FILL_MODE_FIT_CENTER = 0
        const val FILL_MODE_CENTER_CROP = 1
        const val FILL_MODE_FIT_XY = 2
        const val FILL_MODE_CENTER_RATIO = 3
    }

    override fun createVideoPlayer(): IVideoPlayer {
        val p = playerProvider ?: SystemPlayerProvider().apply { playerProvider = this }
        return p.createVideoPlayer()
    }

}