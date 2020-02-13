package com.shenjun.corgicore.tools

import com.shenjun.corgicore.framework.VideoConfig
import com.shenjun.corgicore.log.logW

/**
 * Created by shenjun on 2018/11/23.
 */
class TextureSizeCalculator(private val onSizeUpdate: ((outputWidth: Int, outputHeight: Int) -> Unit)) {

    private var fillMode = -1
    private var fillModeNum = 0
    private var fillModeDen = 0

    private var viewWidth = 0
    private var viewHeight = 0
    private var playerWidth = 0
    private var playerHeight = 0

    fun updateViewSize(width: Int, height: Int) {
        viewWidth = width
        viewHeight = height
        refreshSize()
    }

    fun updatePlayerSize(width: Int, height: Int) {
        playerWidth = width
        playerHeight = height
        refreshSize()
    }

    fun updateFillMode(mode: Int, num: Int, den: Int) {
        fillMode = mode
        fillModeNum = num
        fillModeDen = den
        refreshSize()
    }

    private fun refreshSize() {
        if (viewWidth <= 0 || viewHeight <= 0 || playerWidth <= 0 || playerHeight <= 0 || fillMode < 0) {
            return
        }
        if (fillMode == VideoConfig.FILL_MODE_CENTER_RATIO && (fillModeNum <= 0 || fillModeDen <= 0)) {
            logW("fill mode ratio incorrect, force to 16:9")
            fillModeNum = 16
            fillModeDen = 9
        }
        when (fillMode) {
            VideoConfig.FILL_MODE_FIT_CENTER -> {
                if (viewHeight * playerWidth < viewWidth * playerHeight) {
                    val outputWidth = viewHeight * playerWidth / playerHeight
                    onSizeUpdate.invoke(outputWidth, viewHeight)
                } else {
                    val outputHeight = viewWidth * playerHeight / playerWidth
                    onSizeUpdate.invoke(viewWidth, outputHeight)
                }
            }
            VideoConfig.FILL_MODE_CENTER_CROP -> {
                if (viewHeight * playerWidth < viewWidth * playerHeight) {
                    val outputHeight = viewWidth * playerHeight / playerWidth
                    onSizeUpdate.invoke(viewWidth, outputHeight)
                } else {
                    val outputWidth = viewHeight * playerWidth / playerHeight
                    onSizeUpdate.invoke(outputWidth, viewHeight)
                }
            }
            VideoConfig.FILL_MODE_FIT_XY -> {
                onSizeUpdate.invoke(viewWidth, viewHeight)
            }
            VideoConfig.FILL_MODE_CENTER_RATIO -> {
                if (viewHeight * fillModeNum < viewWidth * fillModeDen) {
                    val outputWidth = viewHeight * fillModeNum / fillModeDen
                    onSizeUpdate.invoke(outputWidth, viewHeight)
                } else {
                    val outputHeight = viewWidth * fillModeDen / fillModeNum
                    onSizeUpdate.invoke(viewWidth, outputHeight)
                }
            }
        }
    }
}