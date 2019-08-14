package com.shenjun.corgicore.sys

import com.shenjun.corgicore.player.IVideoPlayer

/**
 * Created by shenjun on 2019-08-14.
 */
class SystemPlayerProvider : IVideoPlayer.IPlayerProvider {
    override fun createVideoPlayer(): IVideoPlayer = SystemVideoPlayer()
}