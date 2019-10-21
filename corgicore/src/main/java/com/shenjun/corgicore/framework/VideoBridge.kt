package com.shenjun.corgicore.framework

import android.content.Context
import android.graphics.SurfaceTexture
import android.os.Handler
import com.shenjun.corgicore.callback.VideoViewCallback
import com.shenjun.corgicore.constant.InterceptorConst
import com.shenjun.corgicore.constant.PlayerConst
import com.shenjun.corgicore.data.VideoInfo
import com.shenjun.corgicore.log.logD
import com.shenjun.corgicore.log.logE
import com.shenjun.corgicore.player.IVideoPlayer
import com.shenjun.corgicore.player.PlayerStateMachine
import com.shenjun.corgicore.player.msg.*
import com.shenjun.corgicore.view.ControllerVideoView

/**
 * Created by shenjun on 2018/11/21.
 */
open class VideoBridge<out P : AbstractVideoRepo>(
    private val repo: P,
    private val videoView: ControllerVideoView,
    private val videoConfig: VideoConfig = VideoConfig()
) : VideoViewCallback, IVideoPlayer.IPlayerCallback, AbstractVideoRepo.RepoCallback {


    private val mStateMachine = PlayerStateMachine()
    private val mInterceptors = mutableListOf<VideoInterceptor>()
    private val mStateRecorder = VideoStateRecorder()

    private val mHandler = Handler()
    private val mUpdateTimeThread = object : Runnable {
        override fun run() {
            if (canUpdateTime()) {
                getProgressAndUpdateView()
            }
            mHandler.postDelayed(this, progressUpdateIntervalMs())
        }
    }

    init {
        initVideoBridge()
    }

    private fun initVideoBridge() {
        videoView.videoViewCallback = this
        repo.setRepoCallback(this)
    }

    fun startPlay() {
        mStateMachine.post(MsgInit(videoConfig, this))
    }

    override fun onViewSizeChanged(width: Int, height: Int) {
        logD("onViewSizeChanged: width = $width, height = $height")
    }

    override fun onViewSurfaceAvailable(surfaceTexture: SurfaceTexture) {
        logD("onViewSurfaceAvailable: $surfaceTexture")
        mStateMachine.post(MsgUpdateSurface(surfaceTexture), true)
        mStateMachine.post(MsgStart(PlayerConst.PRIORITY_SOURCE_AVAILABLE), true)
    }

    override fun onViewSurfaceDestroyed() {
        logD("onViewSurfaceDestroyed")
    }

    override fun onOperateReversePlayState() {
        val msg = if (mStateMachine.isPlaying()) {
            MsgPause(PlayerConst.PRIORITY_USER)
        } else {
            MsgStart(PlayerConst.PRIORITY_USER)
        }
        mStateMachine.post(msg)
    }

    override fun onOperateSeekStart(startTimeMs: Long) {
        mStateRecorder.isMuteBeforeSeeking = mStateMachine.isMute()
        if (videoConfig.muteDuringSeeking) {
            mStateMachine.post(MsgMute(true))
        }
    }

    override fun onOperateSeeking(startTimeMs: Long, curTimeMs: Long) {
        if (videoConfig.updateFrameDuringSeeking) {
            mStateMachine.postNewest(MsgSeek(curTimeMs))
        }
    }

    override fun onOperateSeekEnd(timeMs: Long) {
        mStateMachine.postNewest(MsgSeek(timeMs))
        if (videoConfig.muteDuringSeeking) {
            mStateMachine.post(MsgMute(mStateRecorder.isMuteBeforeSeeking))
        }
    }

    override fun getContext(): Context {
        return videoView.context
    }

    override fun onPlayerCreated() {
        mInterceptors.forEach {
            if (it.doIntercept(InterceptorConst.PLAYER_CREATE, this)) {
                return
            }
        }
        repo.startLoad()
    }

    override fun onPlayerPrepareStart() {
        // todo
    }

    override fun onPlayerPrepared() {
        videoView.setDuration(mStateMachine.getDuration())
        mStateMachine.post(MsgVolume(videoConfig.originalVolumeLeft, videoConfig.originalVolumeRight))
        mStateMachine.post(MsgPrepared())
        updateTimeThread(true)
    }

    override fun onPlayerError(errorCode: Int, msg: String) {
        logE("player error $errorCode, msg = $msg")
    }

    override fun onPlayerPlayPauseStateChanged(isPlaying: Boolean) {
        videoView.setPlayPauseState(isPlaying)
    }

    override fun onBufferingUpdate(percent: Float) {
        videoView.setBufferingPercent(percent)
    }

    override fun onDataReceived(info: VideoInfo) {
        when (info.brief) {
            VideoInfo.BRIEF_LOADING -> handleVideoLoading(info)
            VideoInfo.BRIEF_CONFIG -> handleVideoConfigReceived(info)
            VideoInfo.BRIEF_MEDIA_INFO -> handleVideoMediaInfoReceived(info)
            VideoInfo.BRIEF_SOURCE -> handleVideoSourceReceived(info)
        }
        videoView.updateVideoInfo(info)
    }

    protected fun handleVideoLoading(info: VideoInfo) {}

    protected fun handleVideoConfigReceived(info: VideoInfo) {}

    protected fun handleVideoMediaInfoReceived(info: VideoInfo) {}

    protected fun handleVideoSourceReceived(info: VideoInfo) {
        mInterceptors.forEach {
            if (it.doIntercept(InterceptorConst.VIDEO_SOURCE_RECEIVED, this)) {
                return
            }
        }
        mStateMachine.post(MsgPrepare(info))
    }

    fun pause() {
        mStateMachine.post(MsgPause(PlayerConst.PRIORITY_BACKGROUND))
    }

    fun resume() {
        mStateMachine.post(MsgStart(PlayerConst.PRIORITY_BACKGROUND))
    }

    fun release() {
        mStateMachine.post(MsgRelease())
    }

    fun getCurrentVideoInfo() = repo.getVideoInfo()


    protected fun progressUpdateIntervalMs() = 100L

    private fun canUpdateTime(): Boolean {
        return true
    }

    protected fun getProgressAndUpdateView() {
        val progress = mStateMachine.getProgress()
        videoView.setCurrentProgress(progress)
    }

    private fun updateTimeThread(isWorking: Boolean) {
        mHandler.removeCallbacks(mUpdateTimeThread)
        if (isWorking) {
            mHandler.postDelayed(mUpdateTimeThread, progressUpdateIntervalMs())
        }
    }
}