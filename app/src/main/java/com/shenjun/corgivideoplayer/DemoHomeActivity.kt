package com.shenjun.corgivideoplayer

import android.app.Activity
import com.shenjun.corgivideoplayer.common.BaseIndexActivity
import com.shenjun.corgivideoplayer.corgi.CorgiIndexActivity
import kotlin.reflect.KClass

/**
 * Created by shenjun on 2018/11/20.
 */
class DemoHomeActivity : BaseIndexActivity() {

    override fun provideIndexMap(): Map<String, KClass<out Activity>> {
        return mapOf<String, KClass<out Activity>>(
            "Corgi" to CorgiIndexActivity::class
        )
    }
}