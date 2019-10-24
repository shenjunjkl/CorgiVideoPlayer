package com.shenjun.corgicore.view.controller

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.shenjun.corgicore.constant.ControllerConst
import com.shenjun.corgicore.tools.name

/**
 * Created by shenjun on 2019-08-28.
 */
abstract class AbstractVideoController {

    var eventCallback: EventCallback? = null

    abstract fun createView(ctx: Context, parent: ViewGroup): View

    fun key() = name()

    fun show() {
        eventCallback?.setVisibility(true, key())
    }

    fun hide() {
        eventCallback?.setVisibility(false, key())
    }

    open fun onViewCreated(view: View) {}

    open fun onControllerReplaced() {}

    open fun onShowView(view: View) {
        view.visibility = View.VISIBLE
    }

    open fun onHideView(view: View) {
        view.visibility = View.GONE
    }

    interface EventCallback {
        fun onControllerEvent(event: Int, extra: Bundle)
    }

    fun layoutParams(width: Int, height: Int, gravity: Int): FrameLayout.LayoutParams {
        val lp = FrameLayout.LayoutParams(width, height)
        lp.gravity = gravity
        return lp
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

    fun EventCallback.setVisibility(isShow: Boolean, controllerKey: String) {
        onControllerEvent(ControllerConst.VISIBILITY, Bundle().apply {
            putBoolean(ControllerConst.KEY_SHOW, isShow)
            putString(ControllerConst.KEY_CONTROLLER_KEY, controllerKey)
        })
    }
}