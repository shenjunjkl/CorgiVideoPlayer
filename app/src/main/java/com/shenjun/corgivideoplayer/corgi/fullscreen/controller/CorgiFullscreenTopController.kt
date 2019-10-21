package com.shenjun.corgivideoplayer.corgi.fullscreen.controller

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.shenjun.corgicore.data.VideoInfo
import com.shenjun.corgicore.view.controller.AbstractVideoController
import com.shenjun.corgicore.view.listener.VideoInfoListener
import com.shenjun.corgivideoplayer.R
import com.shenjun.corgivideoplayer.dp2px

/**
 * Created by shenjun on 2019-10-22.
 */
class CorgiFullscreenTopController: AbstractVideoController(), VideoInfoListener {

    private lateinit var mTitleTV: TextView

    override fun createView(ctx: Context, parent: ViewGroup): View {
        val view = View.inflate(ctx, R.layout.corgi_fullscreen_top_controller, null)
        view.layoutParams = layoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ctx.dp2px(60), Gravity.TOP)
        return view
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        view.findViewById<View>(R.id.close_iv).setOnClickListener {
            val a = view.context as Activity?
            a?.onBackPressed()
        }
        mTitleTV = view.findViewById(R.id.video_title_tv)
    }

    override fun onVideoInfoUpdated(info: VideoInfo) {
        if (info.brief == VideoInfo.BRIEF_MEDIA_INFO) {
            mTitleTV.text = info.title
        }
    }
}