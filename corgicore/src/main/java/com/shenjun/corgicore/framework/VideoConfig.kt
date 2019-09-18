package com.shenjun.corgicore.framework

import com.shenjun.corgicore.player.IVideoPlayer
import com.shenjun.corgicore.sys.SystemPlayerProvider

/**
 * Created by shenjun on 2018/11/22.
 */
class VideoConfig : IVideoPlayer.IPlayerProvider {

    var playerProvider: IVideoPlayer.IPlayerProvider? = null

    //todo handle true case
    var doSeekToWhileSeeking = false


    override fun createVideoPlayer(): IVideoPlayer {
        val p = playerProvider ?: SystemPlayerProvider().apply { playerProvider = this }
        return p.createVideoPlayer()
    }

}