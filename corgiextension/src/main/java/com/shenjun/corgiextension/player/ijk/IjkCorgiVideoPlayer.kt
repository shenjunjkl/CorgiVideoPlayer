package com.shenjun.corgiextension.player.ijk

import android.graphics.SurfaceTexture
import android.media.AudioManager
import android.text.TextUtils
import android.view.Surface
import com.shenjun.corgicore.constant.VideoErrorConst
import com.shenjun.corgicore.data.VideoInfo
import com.shenjun.corgicore.log.logW
import com.shenjun.corgicore.player.IVideoPlayer
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import kotlin.concurrent.thread

/**
 * Created by shenjun on 2019-08-08.
 */
class IjkCorgiVideoPlayer : IVideoPlayer {

    private var mPlayer: IjkMediaPlayer? = null
    private val mEventMapper = IjkPlayerEventMapper()
    private var mCallback: IVideoPlayer.IPlayerCallback? = null
    private var mSurface: Surface? = null
    private var mSurfaceTexture: SurfaceTexture? = null

    private var mVolumeLeft = 1f
    private var mVolumeRight = 1f
    private var mPlayerMute = false

    override fun create() {
        val ijkPlayer = IjkMediaPlayer()
        ijkPlayer.setEventMapper(mEventMapper)
        ijkPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        ijkPlayer.setScreenOnWhilePlaying(true)
        mPlayer = ijkPlayer
        mCallback?.onPlayerCreated()
    }

    override fun prepare(info: VideoInfo) {
        mCallback?.onPlayerPrepareStart()
        try {
            mPlayer?.apply {
                setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "safe", 0)
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "soundtouch", 1)

                val headers = info.headers
                val ua = headers["User-Agent"]
                if (!TextUtils.isEmpty(ua)) {
                    setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "user_agent", ua)
                }
                setPlayDataSource(info.url, headers)
                prepareAsync()
            }
        } catch (e: Exception) {
            logW("ijkPlayer prepare failed, exception = $e")
            mCallback?.onPlayerError(VideoErrorConst.PLAYER_PREPARE_FAILED, e.toString())
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
        mPlayer?.seekTo(timeMs)
    }

    override fun release() {
        mPlayer?.let {
            it.setEventMapper(null)
            it.stop()
            thread {
                try {
                    it.reset()
                    it.release()
                } catch (e: Exception) {
                    logW("ijkPlayer release error, exception = $e")
                }
            }
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
        return mPlayer?.currentPosition ?: 0
    }

    override fun getDuration(): Long {
        return mPlayer?.duration ?: -1
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

    private fun IjkMediaPlayer.setPlayDataSource(path: String, headers: Map<String, String>? = null) {
        when {
            path.startsWith("assets://") -> {
                val fileName = path.substring("assets://".length, path.length)
                val fd = mCallback?.getContext()?.assets?.openFd(fileName)
                if (fd != null) {
                    setDataSource(IjkAssetsDataSource(fd))
                }
            }
            else -> {
                if (headers == null || headers.isEmpty()) {
                    dataSource = path
                } else {
                    setDataSource(path, headers)
                }
            }
        }
    }

    private fun IjkMediaPlayer.setEventMapper(mapper: IjkPlayerEventMapper?) {
        setOnPreparedListener(mapper)
        setOnVideoSizeChangedListener(mapper)
        setOnCompletionListener(mapper)
        setOnErrorListener(mapper)
        setOnBufferingUpdateListener(mapper)
        setOnInfoListener(mapper)
    }

    private inner class IjkPlayerEventMapper : IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnInfoListener {
        override fun onBufferingUpdate(player: IMediaPlayer?, percent: Int) {
            // percent range 0-100
            mCallback?.onBufferingUpdate(percent / 100f)
        }

        override fun onPrepared(player: IMediaPlayer?) {
            mCallback?.onPlayerPrepared()
        }

        override fun onCompletion(player: IMediaPlayer?) {
        }

        override fun onError(player: IMediaPlayer?, what: Int, extra: Int): Boolean {
            mCallback?.onPlayerError(VideoErrorConst.PLAYER_INNER_ERROR, "$what, $extra")
            return false
        }

        override fun onVideoSizeChanged(player: IMediaPlayer?, width: Int, height: Int, sarNum: Int, sarDen: Int) {
        }

        override fun onInfo(player: IMediaPlayer?, what: Int, extra: Int): Boolean {
            when (what) {
                IMediaPlayer.MEDIA_INFO_BUFFERING_START -> mCallback?.onPlayerBufferingStart()
                IMediaPlayer.MEDIA_INFO_BUFFERING_END -> mCallback?.onPlayerBufferingEnd()
            }
            return false
        }
    }
}