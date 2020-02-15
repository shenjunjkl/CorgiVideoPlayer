package com.shenjun.corgicore.tools

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import kotlin.math.abs

/**
 * Created by shenjun on 2020-02-13.
 */
class GestureHelper(ctx: Context, private val callback: GestureEventCallback) {

    private companion object {
        const val SCROLL_IDLE = 0
        const val SCROLL_HORIZONTAL = 1
        const val SCROLL_VERTICAL = 2
    }

    private val mWrapper = VideoGestureWrapper()
    private val mGestureDetector: GestureDetector = GestureDetector(ctx, mWrapper)

    var verticalScrollEnabled = true
    var horizontalScrollEnabled = true
    var doubleTapEnabled = true
    var singleTapEnabled = true

    fun handleTouchEvent(ev: MotionEvent) {
        mGestureDetector.onTouchEvent(ev)
        mWrapper.handleUpEvent(ev)
    }

    interface GestureEventCallback {
        fun onSingleTap()
        fun onDoubleTap()
        fun onHorizontalScroll(ev: MotionEvent, delta: Float)
        fun onVerticalScroll(ev: MotionEvent, delta: Float)
        fun onScrollFinish(isVertical: Boolean)
    }

    private inner class VideoGestureWrapper : GestureDetector.SimpleOnGestureListener() {

        private var lastScrollAction = SCROLL_IDLE

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            if (singleTapEnabled) {
                callback.onSingleTap()
            }
            return false
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            if (doubleTapEnabled) {
                callback.onDoubleTap()
            }
            return false
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            e1 ?: return false
            e2 ?: return false
            val deltaX = e2.x - e1.x
            val deltaY = e2.y - e1.y

            when (lastScrollAction) {
                SCROLL_HORIZONTAL -> callback.onHorizontalScroll(e2, deltaY)
                SCROLL_VERTICAL -> callback.onVerticalScroll(e2, deltaX)
                SCROLL_IDLE -> if (abs(deltaX) > abs(deltaY)) {
                    if (verticalScrollEnabled) {
                        lastScrollAction = SCROLL_VERTICAL
                        callback.onVerticalScroll(e2, deltaX)
                    }
                } else {
                    if (horizontalScrollEnabled) {
                        lastScrollAction = SCROLL_HORIZONTAL
                        callback.onHorizontalScroll(e2, deltaY)
                    }
                }
            }
            return false
        }

        fun handleUpEvent(ev: MotionEvent) {
            if (verticalScrollEnabled || horizontalScrollEnabled) {
                when (ev.action) {
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_OUTSIDE -> {
                        if (lastScrollAction != SCROLL_IDLE) {
                            callback.onScrollFinish(lastScrollAction == SCROLL_VERTICAL)
                            lastScrollAction = SCROLL_IDLE
                        }
                    }
                }
            }
        }
    }
}