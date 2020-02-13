package com.shenjun.corgicore.sys

import android.graphics.SurfaceTexture
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.view.Surface
import com.shenjun.corgicore.constant.VideoErrorConst
import com.shenjun.corgicore.data.VideoInfo
import com.shenjun.corgicore.log.logD
import com.shenjun.corgicore.player.IVideoPlayer

/**
 * Created by shenjun on 2019-08-14.
 */
class SystemVideoPlayer : IVideoPlayer {

    private var mPlayer: MediaPlayer? = null
    private val mEventMapper = MediaPlayerEventMapper()
    private var mCallback: IVideoPlayer.IPlayerCallback? = null
    private var mSurface: Surface? = null
    private var mSurfaceTexture: SurfaceTexture? = null

    private var mVolumeLeft = 1f
    private var mVolumeRight = 1f
    private var mPlayerMute = false

    override fun create() {
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setEventMapper(mEventMapper)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val builder = AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer.setAudioAttributes(builder.build())
        } else {
            @Suppress("DEPRECATION")
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }
        mediaPlayer.setScreenOnWhilePlaying(true)
        mPlayer = mediaPlayer
        mCallback?.onPlayerCreated()
    }

    override fun prepare(info: VideoInfo) {
        mCallback?.onPlayerPrepareStart()
        mPlayer?.apply {
            setPlayDataSource(info.url, info.headers)
            prepareAsync()
        }
    }

    override fun isPlaying(): Boolean {
        return mPlayer?.isPlaying ?: false
    }

    override fun start() {
        mPlayer?.start()
        mCallback?.onPlayerPlayPauseStateChanged(true)
    }

    override fun pause() {
        mPlayer?.pause()
        mCallback?.onPlayerPlayPauseStateChanged(false)
    }

    override fun seekTo(timeMs: Long) {
        mPlayer?.seekTo(timeMs.toInt())
    }

    override fun release() {
        mPlayer?.apply {
            setEventMapper(null)
            stop()
            reset()
            release()
        }
    }

    override fun updateSurface(surfaceTexture: SurfaceTexture) {
        if (mSurfaceTexture != surfaceTexture) {
            mSurfaceTexture = surfaceTexture
            mSurface = Surface(mSurfaceTexture)
            mPlayer?.setSurface(mSurface)
        } else if (mSurface == null && mSurfaceTexture != null) {
            mSurface = Surface(mSurfaceTexture)
            mPlayer?.setSurface(mSurface)
        }
    }

    override fun setPlayerCallback(callback: IVideoPlayer.IPlayerCallback) {
        mCallback = callback
    }

    override fun getProgress(): Long {
        return mPlayer?.currentPosition?.toLong() ?: 0
    }

    override fun getDuration(): Long {
        return mPlayer?.duration?.toLong() ?: -1
    }

    override fun setVolume(volumeLeft: Float, volumeRight: Float) {
        mVolumeLeft = volumeLeft
        mVolumeRight = volumeRight
        mPlayer?.setVolume(volumeLeft, volumeRight)
    }

    override fun setMute(mute: Boolean) {
        mPlayerMute = mute
        if (mute) {
            mPlayer?.setVolume(0f, 0f)
        } else {
            mPlayer?.setVolume(mVolumeLeft, mVolumeRight)
        }
    }

    override fun isMute(): Boolean = mPlayerMute

    private fun MediaPlayer.setPlayDataSource(path: String, headers: Map<String, String>? = null) {
        when {
            path.startsWith("assets://") -> {
                val fileName = path.substring("assets://".length, path.length)
                val fd = mCallback?.getContext()?.assets?.openFd(fileName)
                if (fd != null) {
                    setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
                }
            }
            else -> {
                if (headers == null || headers.isEmpty()) {
                    setDataSource(path)
                } else {
                    val ctx = mCallback?.getContext() ?: return
                    val uri = Uri.parse(path)
                    setDataSource(ctx, uri, headers)
                }
            }
        }
    }

    private fun MediaPlayer.setEventMapper(mapper: MediaPlayerEventMapper?) {
        setOnPreparedListener(mapper)
        setOnVideoSizeChangedListener(mapper)
        setOnCompletionListener(mapper)
        setOnErrorListener(mapper)
        setOnBufferingUpdateListener(mapper)
        setOnInfoListener(mapper)
    }

    private inner class MediaPlayerEventMapper : MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener,
        MediaPlayer.OnCompletionListener {
        override fun onPrepared(mp: MediaPlayer?) {
            mCallback?.onPlayerPrepared()
        }

        override fun onVideoSizeChanged(mp: MediaPlayer?, width: Int, height: Int) {
            mCallback?.onPlayerSizeChanged(width, height)
        }

        override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
            mCallback?.onPlayerError(VideoErrorConst.PLAYER_INNER_ERROR, "$what, $extra")
            return true
        }

        override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
            mCallback?.onBufferingUpdate(percent / 100f)
        }

        override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
            logD("onInfo what = $what, extra = $extra")
            return false
        }

        override fun onCompletion(mp: MediaPlayer?) {
            mCallback?.onPlayerComplete()
        }
    }
}