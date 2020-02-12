package com.shenjun.corgicore.view

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.view.Gravity
import android.view.TextureView
import android.view.ViewGroup
import android.widget.FrameLayout
import com.shenjun.corgicore.callback.VideoViewCallback

/**
 * Created by shenjun on 2018/12/31.
 *
 * a base video view provides surface texture.
 */
open class TextureVideoView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    TextureView.SurfaceTextureListener {

    private val mTextureView = TextureView(context)
    private var mSurface: SurfaceTexture? = null
    var videoViewCallback: VideoViewCallback? = null
        set(value) {
            field = value
            mSurface?.let { value?.onViewSurfaceAvailable(it) }
        }

    init {
        initTextureView()
    }

    fun getSurfaceTexture() = mSurface

    private fun initTextureView() {
        val lp = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        lp.gravity = Gravity.CENTER
        addView(mTextureView, lp)
        mTextureView.keepScreenOn = true
        mTextureView.surfaceTextureListener = this
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        surface?.let {
            mSurface = it
            videoViewCallback?.onViewSurfaceAvailable(surface)
        }
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {}

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        mSurface = null
        videoViewCallback?.onViewSurfaceDestroyed()
        return false
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {}

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        videoViewCallback?.onViewSizeChanged(w, h)
    }
}