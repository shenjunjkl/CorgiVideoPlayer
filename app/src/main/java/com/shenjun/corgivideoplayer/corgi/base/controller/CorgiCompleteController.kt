package com.shenjun.corgivideoplayer.corgi.base.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shenjun.corgicore.view.controller.AbstractVideoController
import com.shenjun.corgicore.view.listener.CompleteListener
import com.shenjun.corgivideoplayer.R

/**
 * Created by shenjun on 2019-10-24.
 */
class CorgiCompleteController : AbstractVideoController(), CompleteListener {

    override fun createView(ctx: Context, parent: ViewGroup): View {
        return LayoutInflater.from(ctx).inflate(R.layout.corgi_complete_controller, parent, false)
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        view.setOnClickListener { }
        val replayLL = view.findViewById<View>(R.id.replay_ll)
        replayLL.setOnClickListener {
            eventCallback?.replay()
        }
        val finishLL = view.findViewById<View>(R.id.finish_ll)
        finishLL.setOnClickListener {
            eventCallback?.finish()
        }
    }

    override fun onVideoComplete() {
        show(true)
    }
}