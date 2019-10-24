package com.shenjun.corgicore.view.listener

/**
 * Created by shenjun on 2019-10-25.
 */
interface LoadingListener {

    fun onLoadingStateChanged(loading: Boolean, buffering: Boolean)
}