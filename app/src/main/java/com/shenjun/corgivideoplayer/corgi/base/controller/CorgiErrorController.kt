package com.shenjun.corgivideoplayer.corgi.base.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shenjun.corgicore.view.controller.AbstractVideoController
import com.shenjun.corgicore.view.listener.ErrorListener
import com.shenjun.corgivideoplayer.R

/**
 * Created by shenjun on 2019-10-24.
 */
class CorgiErrorController : AbstractVideoController(), ErrorListener {
    override fun createView(ctx: Context, parent: ViewGroup): View {
        return LayoutInflater.from(ctx).inflate(R.layout.corgi_error_controller, parent, false)
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        view.setOnClickListener { }
        view.findViewById<View>(R.id.retry_ll).setOnClickListener {
            eventCallback?.retry()
        }
    }

    override fun onError(errorCode: Int, msg: String) {
        show(true)
    }
}