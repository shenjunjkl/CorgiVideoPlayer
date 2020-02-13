package com.shenjun.corgicore.view

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import com.shenjun.corgicore.constant.ControllerConst
import com.shenjun.corgicore.data.VideoInfo
import com.shenjun.corgicore.framework.VideoConfig
import com.shenjun.corgicore.log.logD
import com.shenjun.corgicore.tools.getActivity
import com.shenjun.corgicore.view.controller.AbstractVideoController
import com.shenjun.corgicore.view.listener.*

/**
 * Created by shenjun on 2018/11/22.
 */
open class ControllerVideoView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextureVideoView(context, attrs, defStyleAttr), AbstractVideoController.EventCallback, AbstractVideoController.VideoConfigRetriever {

    private val mControllersMap: MutableMap<String, Pair<AbstractVideoController, View>> = mutableMapOf()
    /**
     * if two controller in list are under the same key, they cannot coexist and only the latest one will show
     */
    private val mControllerGroupMap: MutableMap<String, MutableSet<AbstractVideoController>> = mutableMapOf()

    private var seekStartTimeMs = 0L
    private var mConfig: VideoConfig? = null

    init {
        initControllerView()
    }

    private fun initControllerView() {

    }

    override fun onControllerEvent(event: Int, extra: Bundle) {
        when (event) {
            ControllerConst.REVERSE_PLAY_STATE -> {
                videoViewCallback?.onOperateReversePlayState()
            }
            ControllerConst.SEEK_START -> {
                //todo do not hide controller
                seekStartTimeMs = extra.getLong(ControllerConst.KEY_TIME_MS)
                videoViewCallback?.onOperateSeekStart(seekStartTimeMs)
                findAllControllerImpl<SeekStateListener> { it.onSeekStart(seekStartTimeMs) }
            }
            ControllerConst.SEEKING -> {
                //todo do not hide controller
                val time = extra.getLong(ControllerConst.KEY_TIME_MS)
                videoViewCallback?.onOperateSeeking(seekStartTimeMs, time)
                findAllControllerImpl<SeekStateListener> { it.onSeeking(seekStartTimeMs, time) }
            }
            ControllerConst.SEEK_END -> {
                //todo hide controller count down start
                val time = extra.getLong(ControllerConst.KEY_TIME_MS)
                videoViewCallback?.onOperateSeekEnd(time)
                findAllControllerImpl<SeekStateListener> { it.onSeekEnd(time) }
            }
            ControllerConst.VISIBILITY -> {
                val isShow = extra.getBoolean(ControllerConst.KEY_SHOW, false)
                val key = extra.getString(ControllerConst.KEY_CONTROLLER_KEY, "")
                val pair = mControllersMap[key]
                if (pair == null) {
                    mControllersMap.remove(key)
                } else {
                    val controller = pair.first
                    val view = pair.second
                    if (isShow) {
                        val hideAllOtherControllers = extra.getBoolean(ControllerConst.KEY_HIDE_ALL_OTHER_CONTROLLERS, false)
                        if (hideAllOtherControllers) {
                            hideAllOtherControllers(controller)
                        } else {
                            // check controllers in group
                            hideAllOtherControllersInSameGroup(controller)
                        }
                        controller.onShowView(view)
                    } else {
                        val showMainControllers = extra.getBoolean(ControllerConst.KEY_SHOW_MAIN_CONTROLLERS, false)
                        if (showMainControllers) {
                            showMainControllers()
                        }
                        controller.onHideView(view)
                    }
                    videoViewCallback?.onOperateControllerVisibilityEvent(isShow, key)
                }
            }
            ControllerConst.REPLAY -> {
                videoViewCallback?.onOperateReplay()
            }
            ControllerConst.FINISH -> {
                getActivity(context)?.finish()
            }
            ControllerConst.RETRY -> {
                videoViewCallback?.onOperateRetry()
            }
            ControllerConst.REFRESH_FILL_MODE -> {
                videoViewCallback?.onOperateRefreshFillMode()
            }
        }
    }

    fun attachVideoConfig(videoConfig: VideoConfig) {
        mConfig = videoConfig
    }

    override fun getVideoConfig(): VideoConfig {
        return mConfig ?: VideoConfig()
    }

    fun addController(controller: AbstractVideoController) {
        val key = controller.key()
        removeController(key)
        val view = controller.createView(context, this)
        addView(view)
        controller.eventCallback = this
        controller.setVideoConfigRetriever(this)
        mControllersMap[key] = controller to view
        controller.onViewCreated(view)
    }

    fun addController(controller: AbstractVideoController, groupKey: String) {
        val set = mControllerGroupMap[groupKey]
        if (set != null) {
            set.add(controller)
        } else {
            mControllerGroupMap[groupKey] = mutableSetOf(controller)
        }
        addController(controller)
    }

    fun setCurrentProgress(timeMs: Long) {
        findAllControllerImpl<ProgressListener> { it.onProgressUpdate(timeMs) }
    }

    fun setDuration(timeMs: Long) {
        findAllControllerImpl<ProgressListener> { it.onDurationUpdate(timeMs) }
    }

    fun setBufferingPercent(percent: Float) {
        findAllControllerImpl<ProgressListener> { it.onBufferProgressUpdate(percent) }
    }

    fun setPlayPauseState(isPlaying: Boolean) {
        findAllControllerImpl<PlayPauseListener> { it.onPlayPauseStateChanged(isPlaying) }
    }

    fun setLoadingState(isLoading: Boolean, isBuffering: Boolean) {
        findAllControllerImpl<LoadingListener> { it.onLoadingStateChanged(isLoading, isBuffering) }
    }

    fun setErrorState(errorCode: Int, msg: String) {
        findAllControllerImpl<ErrorListener> { it.onError(errorCode, msg) }
    }

    fun setCompleteState() {
        findAllControllerImpl<CompleteListener> { it.onVideoComplete() }
    }

    fun showMainControllers() {
        mControllersMap.values.forEach {
            if (it.first.isMainController()) {
                it.first.onShowView(it.second)
            }
        }
    }

    fun hideMainControllers() {
        mControllersMap.values.forEach {
            if (it.first.isMainController()) {
                it.first.onHideView(it.second)
            }
        }
    }

    fun updateVideoInfo(info: VideoInfo) {
        logD("video info updated: $info")
        findAllControllerImpl<VideoInfoListener> { it.onVideoInfoUpdated(info) }
    }

    private fun removeController(key: String) {
        val pair = mControllersMap[key]
        val controller = pair?.first
        controller?.let {
            it.onControllerReplaced()
            it.eventCallback = null
            it.setVideoConfigRetriever(null)
        }
        val view = pair?.second
        view?.let {
            removeView(it)
        }
    }

    private fun hideAllOtherControllers(controller: AbstractVideoController) {
        mControllersMap.values.forEach {
            val c = it.first
            if (c != controller) {
                c.onHideView(it.second)
            }
        }
    }

    private fun hideAllOtherControllersInSameGroup(controller: AbstractVideoController) {
        mControllerGroupMap.values.forEach {
            if (it.contains(controller)) {
                it.forEach { c ->
                    if (c != controller) {
                        val v = mControllersMap[c.key()]?.second
                        if (v != null) {
                            c.onHideView(v)
                        }
                    }
                }
                return
            }
        }
    }

    private inline fun <reified T> findAllControllerImpl(operate: (T) -> Unit) {
        mControllersMap.values.forEach {
            val controller = it.first
            if (controller is T) {
                operate(controller)
            }
        }
    }
}
