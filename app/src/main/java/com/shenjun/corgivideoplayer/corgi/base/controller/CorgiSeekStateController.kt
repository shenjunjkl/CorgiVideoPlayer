package com.shenjun.corgivideoplayer.corgi.base.controller

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import com.shenjun.corgicore.tools.time2MediaLength
import com.shenjun.corgicore.view.controller.AbstractVideoController
import com.shenjun.corgicore.view.listener.ProgressListener
import com.shenjun.corgicore.view.listener.SeekStateListener
import com.shenjun.corgivideoplayer.R

/**
 * Created by shenjun on 2019-10-24.
 */
class CorgiSeekStateController : AbstractVideoController(), ProgressListener, SeekStateListener {

    private lateinit var mStateIV: ImageView
    private lateinit var mHintTV: TextView

    private var mSavedDuration = -1L

    override fun createView(ctx: Context, parent: ViewGroup): View {
        val view = View.inflate(ctx, R.layout.corgi_seek_state_controller, null)
        view.layoutParams = layoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER)
        return view
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        mStateIV = view.findViewById(R.id.ff_rew_iv)
        mHintTV = view.findViewById(R.id.progress_and_duration_tv)
        hide()
    }

    override fun onDurationUpdate(durationMs: Long) {
        mSavedDuration = durationMs
    }

    override fun onSeekStart(startTimeMs: Long) {
        show()
    }

    override fun onSeeking(startTimeMs: Long, curTimeMs: Long) {
        if (startTimeMs < curTimeMs) {
            mStateIV.setImageResource(android.R.drawable.ic_media_ff)
        } else {
            mStateIV.setImageResource(android.R.drawable.ic_media_rew)
        }
        val text = "${time2MediaLength(curTimeMs)}/${time2MediaLength(mSavedDuration)}"
        mHintTV.text = text
    }

    override fun onSeekEnd(timeMs: Long) {
        hide()
    }
}