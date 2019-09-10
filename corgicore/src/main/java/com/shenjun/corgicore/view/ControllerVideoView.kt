package com.shenjun.corgicore.view

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import com.shenjun.corgicore.view.controller.AbstractVideoController
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun setController(controller: AbstractVideoController) {
        val key = controller.key()
        removeController(key)
        val view = controller.createView(context, this)
        addView(view)
        controller.eventCallback = this
        mControllersMap[key] = controller to view
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
