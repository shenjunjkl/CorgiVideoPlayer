package com.shenjun.corgivideoplayer.corgi.fullscreen.controller

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.*
import com.shenjun.corgicore.tools.time2MediaLength
import com.shenjun.corgicore.view.controller.AbstractVideoController
import com.shenjun.corgicore.view.listener.ProgressListener
import com.shenjun.corgivideoplayer.R
import com.shenjun.corgivideoplayer.dp2px

/**
 * Created by shenjun on 2019-08-28.
 */
class CorgiFullscreenBottomController : AbstractVideoController(), ProgressListener {

    private lateinit var mPauseIV: ImageView
    private lateinit var mProgressTV: TextView
    private lateinit var mSeekBar: SeekBar
    private lateinit var mDurationTV: TextView
    private lateinit var mMenuIV: ImageView
    private lateinit var mFullscreenIV: ImageView

    private var mSavedDuration = -1L

    override fun createView(ctx: Context, parent: ViewGroup): View {
        val view = View.inflate(ctx, R.layout.corgi_fullscreen_bottom_controller, null)
        mPauseIV = view.findViewById(R.id.play_pause_iv)
        mProgressTV = view.findViewById(R.id.progress_tv)
        mSeekBar = view.findViewById(R.id.progress_seek_bar)
        mDurationTV = view.findViewById(R.id.duration_tv)
        mMenuIV = view.findViewById(R.id.menu_iv)
        mFullscreenIV = view.findViewById(R.id.full_screen_iv)
        view.layoutParams = layoutParams(MATCH_PARENT, ctx.dp2px(60), Gravity.BOTTOM)
        return view
    }

    override fun onProgressUpdate(timeMs: Long) {
        if (timeMs < 0) return
        mProgressTV.text = time2MediaLength(timeMs)
        if (mSavedDuration <= 0) return
        mSeekBar.progress = (timeMs * mSeekBar.max / mSavedDuration).toInt()
    }

    override fun onBufferProgressUpdate(percent: Float) {
        mSeekBar.secondaryProgress = (percent * mSeekBar.max).toInt()
    }

    override fun onDurationUpdate(durationMs: Long) {
        if (durationMs <= 0) return
        mSavedDuration = durationMs
        mDurationTV.text = time2MediaLength(durationMs)
    }
}