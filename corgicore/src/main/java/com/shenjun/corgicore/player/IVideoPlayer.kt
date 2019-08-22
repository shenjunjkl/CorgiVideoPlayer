package com.shenjun.corgicore.player

import android.content.Context
import android.graphics.SurfaceTexture
import com.shenjun.corgicore.data.VideoInfo

/**
 * Created by shenjun on 2018/11/22.
 */
interface IVideoPlayer {

    fun create()

    fun prepare(info: VideoInfo)

    fun isPlaying(): Boolean

    fun start()

    fun pause()

    fun seekTo(timeMs: Long)

    fun release()

    fun updateSurface(surfaceTexture: SurfaceTexture)

    fun setPlayerCallback(callback: IPlayerCallback)

    interface IPlayerCallback {

        fun getContext(): Context

        fun onPlayerCreated()

        fun onPlayerPrepareStart()

        fun onPlayerPrepared()

        fun onPlayerError(errorCode: Int, msg: String)
    }

    interface IPlayerProvider {
        fun createVideoPlayer(): IVideoPlayer
    }
}