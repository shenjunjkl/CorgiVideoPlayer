package com.shenjun.corgicore.player

/**
 * Created by shenjun on 2018/11/22.
 */
interface IVideoPlayer {

    fun create()

    fun release()

    fun registerCallback(callback: IPlayerCallback)

    fun unregisterCallback(callback: IPlayerCallback)

    interface IPlayerCallback {

    }

    interface IPlayerProvider {
        fun createVideoPlayer(): IVideoPlayer
    }
}