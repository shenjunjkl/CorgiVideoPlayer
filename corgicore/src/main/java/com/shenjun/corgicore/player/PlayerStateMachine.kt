package com.shenjun.corgicore.player

import android.os.Handler
import android.os.Message
import com.shenjun.corgicore.constant.PlayerConst
import com.shenjun.corgicore.log.logD
import com.shenjun.corgicore.player.msg.IPlayerMsg
import com.shenjun.corgicore.tools.name
import java.lang.ref.WeakReference

/**
 * Created by shenjun on 2018/11/22.
 */
class PlayerStateMachine {

    private var mCurrentState = PlayerState.IDLE
    private var mVideoPlayerImpl: IVideoPlayer? = null
    private val mHandler = MediaMsgHandler(this)

    var startAfterPrepared = false
    var pausePriority = PlayerConst.PRIORITY_NONE

    fun post(playerMsg: IPlayerMsg, removeOld: Boolean = false) {
        if (removeOld) {
            remove(playerMsg)
        }
        val message = Message()
        message.what = playerMsg.what()
        message.obj = playerMsg
        mHandler.sendMessage(message)
    }

    fun remove(playerMsg: IPlayerMsg) {
        mHandler.removeMessages(playerMsg.what())
    }

    fun postNewest(playerMsg: IPlayerMsg) {
        remove(playerMsg)
        post(playerMsg)
    }

    fun setVideoPlayerInstance(player: IVideoPlayer?) {
        mVideoPlayerImpl = player
    }

    fun getProgress(): Long = mVideoPlayerImpl?.getProgress() ?: 0

    fun getDuration(): Long = mVideoPlayerImpl?.getDuration() ?: -1

    fun isPlaying(): Boolean = mVideoPlayerImpl?.isPlaying() ?: false

    fun isMute(): Boolean = mVideoPlayerImpl?.isMute() ?: false

    private class MediaMsgHandler(stateMachine: PlayerStateMachine) : Handler() {

        private val mRef = WeakReference(stateMachine)

        override fun handleMessage(msg: Message?) {
            val obj = msg?.obj as? IPlayerMsg ?: return
            mRef.get()?.apply {
                val oldState = mCurrentState
                mCurrentState = obj.transferState(mCurrentState, mVideoPlayerImpl, this)
                logD("handle msg = ${obj.name()}, state $oldState -> $mCurrentState")
            }
        }
    }
}