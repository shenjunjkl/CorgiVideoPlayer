package com.shenjun.corgivideoplayer

import android.app.Activity
import com.shenjun.corgivideoplayer.common.BaseIndexActivity
import com.shenjun.corgivideoplayer.corgi.CorgiIndexActivity

/**
 * Created by shenjun on 2018/11/20.
 */
class DemoHomeActivity : BaseIndexActivity() {

    override fun provideIndexMap(): Map<String, Class<out Activity>> {
        return mapOf<String, Class<out Activity>>(
            "Corgi" to CorgiIndexActivity::class.java
        )
    }
}