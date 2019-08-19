package com.shenjun.corgicore.framework

import com.shenjun.corgicore.data.VideoInfo

/**
 * Created by shenjun on 2018/11/22.
 */
abstract class AbstractVideoRepo {

    private var mCallback: RepoCallback? = null
    protected var mVideoInfo = createVideoInfo()

    abstract fun startLoad()

    abstract fun cancel()

    protected fun createVideoInfo() = VideoInfo()

    fun setRepoCallback(callback: RepoCallback) {
        mCallback = callback
    }

    fun getVideoInfo() = mVideoInfo

    protected fun notifyVideoInfoChanged() {
        mCallback?.onDataReceived(mVideoInfo)
    }

    interface RepoCallback {
        fun onDataReceived(info: VideoInfo)
    }
}