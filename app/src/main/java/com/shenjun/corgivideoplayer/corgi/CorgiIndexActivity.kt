package com.shenjun.corgivideoplayer.corgi

import android.app.Activity
import com.shenjun.corgivideoplayer.common.BaseIndexActivity
import com.shenjun.corgivideoplayer.corgi.fullscreen.CorgiFullscreenActivity
import kotlin.reflect.KClass

/**
 * Created by shenjun on 2018/11/23.
 */
class CorgiIndexActivity : BaseIndexActivity() {

    override fun provideIndexMap(): Map<String, KClass<out Activity>> {
        return mapOf<String, KClass<out Activity>>(
            "fullscreen" to CorgiFullscreenActivity::class
        )
    }
}