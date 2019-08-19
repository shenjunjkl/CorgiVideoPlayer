package com.shenjun.corgiextension.player.ijk

import android.media.AudioManager
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

    override fun create() {
        val ijkPlayer = IjkMediaPlayer()
        ijkPlayer.setEventMapper(mEventMapper)
        ijkPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        ijkPlayer.setScreenOnWhilePlaying(true)
        mPlayer = ijkPlayer
        mCallbacks.forEach { it.onPlayerCreated() }
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
        override fun onBufferingUpdate(p0: IMediaPlayer?, p1: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onPrepared(p0: IMediaPlayer?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onCompletion(p0: IMediaPlayer?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onError(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onVideoSizeChanged(p0: IMediaPlayer?, p1: Int, p2: Int, p3: Int, p4: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onInfo(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}