package com.shenjun.corgivideoplayer.corgi.base.controller

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import com.shenjun.corgicore.view.controller.AbstractVideoController
import com.shenjun.corgicore.view.listener.LoadingListener
import com.shenjun.corgivideoplayer.R

/**
 * Created by shenjun on 2019-10-24.
 */
class CorgiLoadingController : AbstractVideoController(), LoadingListener {

    override fun createView(ctx: Context, parent: ViewGroup): View {
        val view = View.inflate(ctx, R.layout.corgi_loading_controller, null)
        view.layoutParams = layoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER)
        return view
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        val iv = view.findViewById<View>(R.id.loading_iv)
        iv.clearAnimation()
        iv.animation = getRotateAnimation()
        hide()
    }

    override fun onLoadingStateChanged(loading: Boolean, buffering: Boolean) {
        if (loading) show() else hide()
    }

    private fun getRotateAnimation(): RotateAnimation {
        val animation = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        animation.duration = 1200
        animation.interpolator = LinearInterpolator()
        animation.repeatCount = Animation.INFINITE
        return animation
    }
}