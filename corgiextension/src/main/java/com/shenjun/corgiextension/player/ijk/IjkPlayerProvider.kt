package com.shenjun.corgiextension.player.ijk

import com.shenjun.corgicore.player.IVideoPlayer

/**
 * Created by shenjun on 2019-08-14.
 */
class IjkPlayerProvider : IVideoPlayer.IPlayerProvider {
    override fun createVideoPlayer(): IVideoPlayer = IjkCorgiVideoPlayer()
}