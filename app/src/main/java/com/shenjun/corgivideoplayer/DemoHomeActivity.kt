package com.shenjun.corgivideoplayer

import android.app.Activity
import android.os.Bundle
import com.shenjun.corgicore.log.CorgiLogger

/**
 * Created by shenjun on 2018/11/20.
 */
class DemoHomeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CorgiLogger.v("hello")
    }
}