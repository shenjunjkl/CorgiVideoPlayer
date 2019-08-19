package com.shenjun.corgivideoplayer.corgi

import com.shenjun.corgicore.data.VideoInfo
import com.shenjun.corgicore.framework.AbstractVideoRepo
import com.shenjun.corgivideoplayer.DemoVideoUrl

/**
 * Created by shenjun on 2019/1/10.
 */
class CorgiRepo : AbstractVideoRepo() {

    override fun startLoad() {
        mVideoInfo.url = DemoVideoUrl.MP4_4
        mVideoInfo.brief = VideoInfo.BRIEF_SOURCE
        notifyVideoInfoChanged()
    }

    override fun cancel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}