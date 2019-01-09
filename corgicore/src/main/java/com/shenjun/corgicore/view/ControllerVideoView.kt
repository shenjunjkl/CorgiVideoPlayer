package com.shenjun.corgicore.view

import android.content.Context
import android.util.AttributeSet

/**
 * Created by shenjun on 2018/11/22.
 */
open class ControllerVideoView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextureVideoView(context, attrs, defStyleAttr) {

    init {
        initControllerView()
    }

    private fun initControllerView() {

    }
}
