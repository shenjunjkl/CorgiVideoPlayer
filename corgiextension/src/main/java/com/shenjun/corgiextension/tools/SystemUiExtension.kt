package com.shenjun.corgiextension.tools

import android.app.Activity
import android.view.Window
import android.view.WindowManager

/**
 * Created by shenjun on 2019-08-26.
 */

fun Activity.fullScreen() {
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    //todo navigation bar and status bar
}
