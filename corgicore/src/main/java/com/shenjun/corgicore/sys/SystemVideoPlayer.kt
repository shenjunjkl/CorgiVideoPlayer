package com.shenjun.corgicore.sys

import android.graphics.SurfaceTexture
import com.shenjun.corgicore.data.VideoInfo
import com.shenjun.corgicore.player.IVideoPlayer

/**
 * Created by shenjun on 2019-08-14.
 */
class SystemVideoPlayer : IVideoPlayer {
    override fun setVolume(volumeLeft: Float, volumeRight: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setMute(mute: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isMute(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getProgress(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDuration(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPlayerCallback(callback: IVideoPlayer.IPlayerCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun prepare(info: VideoInfo) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isPlaying(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pause() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun seekTo(timeMs: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateSurface(surfaceTexture: SurfaceTexture) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun release() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}