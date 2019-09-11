package com.shenjun.corgicore.view

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import com.shenjun.corgicore.constant.ControllerConst
import com.shenjun.corgicore.view.controller.AbstractVideoController
import com.shenjun.corgicore.view.listener.PlayPauseListener
import com.shenjun.corgicore.view.listener.ProgressListener

/**
 * Created by shenjun on 2018/11/22.
 */
open class ControllerVideoView(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : TextureVideoView(context, attrs, defStyleAttr), AbstractVideoController.EventCallback {

    private val mControllersMap: MutableMap<String, Pair<AbstractVideoController, View>> = mutableMapOf()

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

        }
    }

    fun setController(controller: AbstractVideoController) {
        val key = controller.key()
        removeController(key)
        val view = controller.createView(context, this)
        addView(view)
        controller.eventCallback = this
        mControllersMap[key] = controller to view
        controller.onViewCreated(view)
    }

    fun setCurrentProgress(timeMs: Long) {
        findAllControllerImpl<ProgressListener> {
            it.onProgressUpdate(timeMs)
        }
    }

    fun setDuration(timeMs: Long) {
        findAllControllerImpl<ProgressListener> {
            it.onDurationUpdate(timeMs)
        }
    }

    fun setBufferingPercent(percent: Float) {
        findAllControllerImpl<ProgressListener> {
            it.onBufferProgressUpdate(percent)
        }
    }

    fun setPlayPauseState(isPlaying: Boolean) {
        findAllControllerImpl<PlayPauseListener> {
            it.onPlayPauseStateChanged(isPlaying)
        }
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

    private inline fun <reified T> findAllControllerImpl(operate: (T) -> Unit) {
        mControllersMap.values.forEach {
            val controller = it.first
            if (controller is T) {
                operate(controller)
            }
        }
    }
}
