package com.shenjun.corgivideoplayer.corgi.fullscreen

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.shenjun.corgicore.framework.VideoBridge
import com.shenjun.corgicore.framework.VideoConfig
import com.shenjun.corgiextension.tools.fullScreen
import com.shenjun.corgicore.view.ControllerVideoView
import com.shenjun.corgiextension.compat.registerLifecycle
import com.shenjun.corgiextension.player.ijk.IjkPlayerProvider
import com.shenjun.corgivideoplayer.corgi.CorgiRepo
import com.shenjun.corgivideoplayer.corgi.fullscreen.controller.CorgiFullscreenBottomController

/**
 * Created by shenjun on 2018/11/23.
 */
class CorgiFullscreenActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        fullScreen()
        super.onCreate(savedInstanceState)
        val view = ControllerVideoView(this)
        view.setBackgroundColor(Color.BLACK)
        view.setController(CorgiFullscreenBottomController())

        setContentView(view)
        val repo = CorgiRepo()
        val config = VideoConfig()
        config.playerProvider = IjkPlayerProvider()
        val bridge = VideoBridge(repo, view, config)
        bridge.startPlay()
        bridge.registerLifecycle(this)
    }
}