package com.shenjun.corgiextension.compat

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import com.shenjun.corgicore.framework.VideoBridge

/**
 * Created by shenjun on 2019-08-23.
 */


fun VideoBridge<*>.registerLifecycle(owner: LifecycleOwner) {

    owner.lifecycle.addObserver(object : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume() {
            resume()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPause() {
            pause()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            release()
            owner.lifecycle.removeObserver(this)
        }
    })
}
