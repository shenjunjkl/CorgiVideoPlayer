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

    fun getProgress(): Long

    fun getDuration(): Long

    fun setVolume(volumeLeft: Float, volumeRight: Float)

    fun setMute(mute: Boolean)

    fun isMute(): Boolean

    interface IPlayerCallback {

        fun getContext(): Context

        fun onPlayerCreated()

        fun onPlayerPrepareStart()

        fun onPlayerPrepared()

        fun onPlayerBufferingStart()

        fun onPlayerBufferingEnd()

        fun onPlayerError(errorCode: Int, msg: String)

        fun onPlayerPlayPauseStateChanged(isPlaying: Boolean)

        fun onPlayerComplete()

        fun onPlayerSizeChanged(width: Int, height: Int)

        /**
         * called when video buffering size is updated, percent ranges from 0f to 1f
         */
        fun onBufferingUpdate(percent: Float)
    }

    interface IPlayerProvider {
        fun createVideoPlayer(): IVideoPlayer
    }
}