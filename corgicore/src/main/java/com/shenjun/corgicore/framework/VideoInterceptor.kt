package com.shenjun.corgicore.framework

/**
 * Created by shenjun on 2019-08-17.
 */
abstract class VideoInterceptor {

    abstract fun doIntercept(event: String, videoBridge: VideoBridge<*>): Boolean
}