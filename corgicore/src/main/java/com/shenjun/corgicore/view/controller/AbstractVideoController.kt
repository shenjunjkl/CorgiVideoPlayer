package com.shenjun.corgicore.view.controller

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.shenjun.corgicore.constant.ControllerConst
import com.shenjun.corgicore.framework.VideoConfig
import com.shenjun.corgicore.tools.name

/**
 * Created by shenjun on 2019-08-28.
 */
abstract class AbstractVideoController {

    var eventCallback: EventCallback? = null
    private var videoConfigRetriever: VideoConfigRetriever? = null

    abstract fun createView(ctx: Context, parent: ViewGroup): View

    fun key() = name()

    fun show(controllerKey: String = key(), hideAll: Boolean = false) {
        eventCallback?.setVisibility(true, controllerKey, Bundle().apply {
            putBoolean(ControllerConst.KEY_HIDE_ALL_OTHER_CONTROLLERS, hideAll)
        })
    }

    fun hide(controllerKey: String = key(), showMainController: Boolean = false) {
        eventCallback?.setVisibility(false, controllerKey, Bundle().apply {
            putBoolean(ControllerConst.KEY_SHOW_MAIN_CONTROLLERS, showMainController)
        })
    }

    open fun onViewCreated(view: View) {}

    open fun onControllerReplaced() {}

    open fun onShowView(view: View) {
        view.visibility = View.VISIBLE
    }

    open fun onHideView(view: View) {
        view.visibility = View.GONE
    }

    /**
     * if a controller is a main controller, it will show itself when user tap the screen
     * and hide itself when tapped again
     */
    open fun isMainController() = false

    fun setVideoConfigRetriever(retriever: VideoConfigRetriever?) {
        videoConfigRetriever = retriever
    }

    protected fun getVideoConfig() = videoConfigRetriever?.getVideoConfig() ?: VideoConfig()


    interface EventCallback {
        fun onControllerEvent(event: Int, extra: Bundle)
    }

    interface VideoConfigRetriever {
        fun getVideoConfig(): VideoConfig
    }

    fun EventCallback?.setPlayPause(isPlay: Boolean) {
        //todo
    }

    fun EventCallback.reversePlayState() {
        onControllerEvent(ControllerConst.REVERSE_PLAY_STATE, Bundle())
    }

    fun EventCallback.seekStart(timeMs: Long) {
        onControllerEvent(ControllerConst.SEEK_START, Bundle().apply {
            putLong(ControllerConst.KEY_TIME_MS, timeMs)
        })
    }

    fun EventCallback.seeking(timeMs: Long) {
        onControllerEvent(ControllerConst.SEEKING, Bundle().apply {
            putLong(ControllerConst.KEY_TIME_MS, timeMs)
        })
    }

    fun EventCallback.seekEnd(timeMs: Long) {
        onControllerEvent(ControllerConst.SEEK_END, Bundle().apply {
            putLong(ControllerConst.KEY_TIME_MS, timeMs)
        })
    }

    private fun EventCallback.setVisibility(isShow: Boolean, controllerKey: String, extraBundle: Bundle = Bundle()) {
        onControllerEvent(ControllerConst.VISIBILITY, Bundle().apply {
            putBoolean(ControllerConst.KEY_SHOW, isShow)
            putString(ControllerConst.KEY_CONTROLLER_KEY, controllerKey)
            putAll(extraBundle)
        })
    }

    fun EventCallback.replay() {
        onControllerEvent(ControllerConst.REPLAY, Bundle())
    }

    fun EventCallback.finish() {
        onControllerEvent(ControllerConst.FINISH, Bundle())
    }

    fun EventCallback.retry() {
        onControllerEvent(ControllerConst.RETRY, Bundle())
    }

    fun EventCallback.refreshFillMode() {
        onControllerEvent(ControllerConst.REFRESH_FILL_MODE, Bundle())
    }
}