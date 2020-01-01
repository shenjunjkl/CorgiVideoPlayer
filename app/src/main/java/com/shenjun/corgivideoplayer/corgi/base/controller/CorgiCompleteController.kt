package com.shenjun.corgivideoplayer.corgi.base.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shenjun.corgicore.view.controller.AbstractVideoController
import com.shenjun.corgivideoplayer.R

/**
 * Created by shenjun on 2019-10-24.
 */
class CorgiCompleteController : AbstractVideoController() {

    override fun createView(ctx: Context, parent: ViewGroup): View {
        return LayoutInflater.from(ctx).inflate(R.layout.corgi_complete_controller, parent, false)
    }

}