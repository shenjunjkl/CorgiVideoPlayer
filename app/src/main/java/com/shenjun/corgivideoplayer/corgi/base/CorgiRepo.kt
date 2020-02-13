package com.shenjun.corgivideoplayer.corgi.base

import com.shenjun.corgicore.data.VideoInfo
import com.shenjun.corgicore.framework.AbstractVideoRepo
import com.shenjun.corgivideoplayer.DemoVideoUrl

/**
 * Created by shenjun on 2019/1/10.
 */
class CorgiRepo : AbstractVideoRepo() {

    override fun startLoad() {
        // loading
        notifyVideoInfoChanged()
        // prepare config
        mVideoInfo.obj = "test config 1"
        mVideoInfo.brief = VideoInfo.BRIEF_CONFIG
        notifyVideoInfoChanged()
        // prepare info
        mVideoInfo.title = "corgi test video"
        mVideoInfo.brief = VideoInfo.BRIEF_MEDIA_INFO
        notifyVideoInfoChanged()
        // can play now
        mVideoInfo.url = DemoVideoUrl.MP4_5
        mVideoInfo.brief = VideoInfo.BRIEF_SOURCE
        notifyVideoInfoChanged()
    }

    override fun cancel() {}
}