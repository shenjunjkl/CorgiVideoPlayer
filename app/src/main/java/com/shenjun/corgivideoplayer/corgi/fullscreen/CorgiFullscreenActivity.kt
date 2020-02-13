package com.shenjun.corgivideoplayer.corgi.fullscreen

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.shenjun.corgicore.framework.VideoBridge
import com.shenjun.corgicore.framework.VideoConfig
import com.shenjun.corgicore.view.ControllerVideoView
import com.shenjun.corgiextension.compat.registerLifecycle
import com.shenjun.corgiextension.player.ijk.IjkPlayerProvider
import com.shenjun.corgiextension.tools.fullScreen
import com.shenjun.corgivideoplayer.corgi.base.CorgiRepo
import com.shenjun.corgivideoplayer.corgi.base.controller.*
import com.shenjun.corgivideoplayer.corgi.fullscreen.controller.CorgiFullscreenBottomController
import com.shenjun.corgivideoplayer.corgi.fullscreen.controller.CorgiFullscreenTopController

/**
 * Created by shenjun on 2018/11/23.
 */
class CorgiFullscreenActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        fullScreen()
        super.onCreate(savedInstanceState)

        val view = ControllerVideoView(this)
        view.setBackgroundColor(Color.BLACK)
        view.addController(CorgiFullscreenBottomController())
        view.addController(CorgiFullscreenTopController())
        view.addController(CorgiSeekStateController(), "state")
        view.addController(CorgiLoadingController(), "state")
        view.addController(CorgiCompleteController(), "state")
        view.addController(CorgiErrorController(), "state")
        view.addController(CorgiSettingsController())

        setContentView(view)

        val repo = CorgiRepo()
        val config = VideoConfig()
        config.playerProvider = IjkPlayerProvider()

        val bridge = VideoBridge(repo, view, config)
        bridge.startPlay()
        bridge.registerLifecycle(this)
    }
}