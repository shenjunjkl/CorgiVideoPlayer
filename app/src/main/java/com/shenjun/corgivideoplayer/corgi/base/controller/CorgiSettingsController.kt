package com.shenjun.corgivideoplayer.corgi.base.controller

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.shenjun.corgicore.framework.VideoConfig
import com.shenjun.corgicore.view.controller.AbstractVideoController
import com.shenjun.corgivideoplayer.R

/**
 * Created by shenjun on 2020-02-13.
 */
class CorgiSettingsController : AbstractVideoController(), View.OnClickListener {

    private val fillModeViewIds = listOf(R.id.fill_mode_0, R.id.fill_mode_1, R.id.fill_mode_2, R.id.fill_mode_3_1, R.id.fill_mode_3_2)
    private val fillModeTVs = mutableListOf<TextView>()

    override fun createView(ctx: Context, parent: ViewGroup): View {
        return LayoutInflater.from(ctx).inflate(R.layout.corgi_settings_controller, parent, false)
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        hide()
        fillModeTVs.clear()
        fillModeViewIds.forEach {
            val tv = view.findViewById<TextView>(it)
            fillModeTVs.add(tv)
            tv.setOnClickListener(this)
        }
    }

    override fun onShowView(view: View) {
        super.onShowView(view)
        val config = getVideoConfig()
        updateView(config)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fill_mode_0 -> updateFillModeConfig(VideoConfig.FILL_MODE_FIT_CENTER)
            R.id.fill_mode_1 -> updateFillModeConfig(VideoConfig.FILL_MODE_CENTER_CROP)
            R.id.fill_mode_2 -> updateFillModeConfig(VideoConfig.FILL_MODE_FIT_XY)
            R.id.fill_mode_3_1 -> updateFillModeConfig(VideoConfig.FILL_MODE_CENTER_RATIO, 16, 9)
            R.id.fill_mode_3_2 -> updateFillModeConfig(VideoConfig.FILL_MODE_CENTER_RATIO, 4, 3)
        }
    }

    private fun updateFillModeConfig(mode: Int, num: Int = 0, den: Int = 0) {
        val config = getVideoConfig().apply {
            fillMode = mode
            if (num > 0) {
                fillModeNum = num
            }
            if (den > 0) {
                fillModeDen = den
            }
        }
        updateView(config)
        eventCallback?.refreshFillMode()
    }

    private fun updateView(config: VideoConfig) {
        val highlightId = when (config.fillMode) {
            VideoConfig.FILL_MODE_FIT_CENTER -> R.id.fill_mode_0
            VideoConfig.FILL_MODE_CENTER_CROP -> R.id.fill_mode_1
            VideoConfig.FILL_MODE_FIT_XY -> R.id.fill_mode_2
            VideoConfig.FILL_MODE_CENTER_RATIO -> if (config.fillModeNum == 16 && config.fillModeDen == 9) {
                R.id.fill_mode_3_1
            } else if (config.fillModeNum == 4 && config.fillModeDen == 3) {
                R.id.fill_mode_3_2
            } else 0
            else -> 0
        }
        fillModeTVs.forEach {
            val color = Color.parseColor(if (it.id == highlightId) "#80FFFF00" else "#80000000")
            it.setBackgroundColor(color)
        }
    }

}