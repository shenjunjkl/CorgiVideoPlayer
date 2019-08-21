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
    private var mCallbacks = mutableSetOf<IVideoPlayer.IPlayerCallback>()
    private var mSurface: Surface? = null
    private var mSurfaceTexture: SurfaceTexture? = null

    override fun create() {
        val ijkPlayer = IjkMediaPlayer()
        ijkPlayer.setEventMapper(mEventMapper)
        ijkPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        ijkPlayer.setScreenOnWhilePlaying(true)
        mPlayer = ijkPlayer
        mCallbacks.forEach { it.onPlayerCreated() }
    }

    override fun prepare(info: VideoInfo) {
        mCallbacks.forEach { it.onPlayerPrepareStart() }
        try {
            mPlayer?.apply {
                setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "safe", 0)
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "soundtouch", 1)

                val headers = info.headers
                val ua = headers["User-Agent"]
                if (!TextUtils.isEmpty(ua)) {
                    setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "user_agent", ua)
                }
                if (headers.isEmpty()) {
                    dataSource = info.url
                } else {
                    setDataSource(info.url, headers)
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            logW("ijkPlayer prepare failed, exception = $e")
            mCallbacks.forEach { it.onPlayerError(VideoErrorConst.PLAYER_PREPARE_FAILED, e.toString()) }
        }
    }

    override fun isPlaying(): Boolean {
        return mPlayer?.isPlaying ?: false
    }

    override fun start() {
        mPlayer?.start()
    }

    override fun pause() {
        mPlayer?.pause()
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

    override fun registerCallback(callback: IVideoPlayer.IPlayerCallback) {
        mCallbacks.add(callback)
    }

    override fun unregisterCallback(callback: IVideoPlayer.IPlayerCallback) {
        mCallbacks.remove(callback)
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
        override fun onBufferingUpdate(player: IMediaPlayer?, p1: Int) {
        }

        override fun onPrepared(player: IMediaPlayer?) {
            mCallbacks.forEach { it.onPlayerPrepared() }
        }

        override fun onCompletion(player: IMediaPlayer?) {
        }

        override fun onError(player: IMediaPlayer?, what: Int, extra: Int): Boolean {
            mCallbacks.forEach { it.onPlayerError(VideoErrorConst.PLAYER_INNER_ERROR, "$what, $extra") }
            return false
        }

        override fun onVideoSizeChanged(player: IMediaPlayer?, width: Int, height: Int, sarNum: Int, sarDen: Int) {
        }

        override fun onInfo(player: IMediaPlayer?, p1: Int, p2: Int): Boolean {
            return false
        }
    }
}