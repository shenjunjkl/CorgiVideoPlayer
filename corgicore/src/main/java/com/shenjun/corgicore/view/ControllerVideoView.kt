package com.shenjun.corgicore.view

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import com.shenjun.corgicore.constant.ControllerConst
import com.shenjun.corgicore.data.VideoInfo
import com.shenjun.corgicore.log.logD
import com.shenjun.corgicore.log.logW
import com.shenjun.corgicore.view.controller.AbstractVideoController
import com.shenjun.corgicore.view.listener.PlayPauseListener
import com.shenjun.corgicore.view.listener.ProgressListener
import com.shenjun.corgicore.view.listener.SeekStateListener
import com.shenjun.corgicore.view.listener.VideoInfoListener

/**
 * Created by shenjun on 2018/11/22.
 */
open class ControllerVideoView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextureVideoView(context, attrs, defStyleAttr), AbstractVideoController.EventCallback {

    private val mControllersMap: MutableMap<String, Pair<AbstractVideoController, View>> = mutableMapOf()
    /**
     * if two controller is in list under the same key, they cannot coexist and only the latest one will show
     */
    private val mControllerGroupMap: MutableMap<String, MutableSet<AbstractVideoController>> = mutableMapOf()

    private var seekStartTimeMs = 0L

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
                        // check controllers in group first
                        hideAllOtherControllerInSameGroup(controller)
                        controller.onShowView(view)
                    } else {
                        controller.onHideView(view)
                    }
                }
            }
        }
    }

    fun addController(controller: AbstractVideoController) {
        val key = controller.key()
        removeController(key)
        val view = controller.createView(context, this)
        addView(view)
        controller.eventCallback = this
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
        }
        val view = pair?.second
        view?.let {
            removeView(it)
        }
    }

    private fun hideAllOtherControllerInSameGroup(controller: AbstractVideoController) {
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
