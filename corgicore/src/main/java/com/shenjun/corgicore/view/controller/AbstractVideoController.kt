package com.shenjun.corgicore.view.controller

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.shenjun.corgicore.tools.name

/**
 * Created by shenjun on 2019-08-28.
 */
abstract class AbstractVideoController {

    var eventCallback: EventCallback? = null

    abstract fun createView(ctx: Context, parent: ViewGroup): View

    fun key() = name()

    fun onControllerReplaced() {}

    interface EventCallback {
        fun onControllerEvent(event: Int, extra: Bundle)
    }

    fun layoutParams(width: Int, height: Int, gravity: Int): FrameLayout.LayoutParams {
        val lp = FrameLayout.LayoutParams(width, height)
        lp.gravity = gravity
        return lp
    }
}