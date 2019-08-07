package com.shenjun.corgicore.player

import android.os.Handler
import android.os.Message
import com.shenjun.corgicore.player.msg.IPlayerMsg
import java.lang.ref.WeakReference

/**
 * Created by shenjun on 2018/11/22.
 */
class PlayerStateMachine {

    private var mCurrentState = PlayerState.IDLE

    private var mVideoPlayerImpl: IVideoPlayer? = null

    private val mHandler = MediaMsgHandler(this)

    fun post(playerMsg: IPlayerMsg) {
        val message = Message()
        message.what = playerMsg.what()
        message.obj = playerMsg
        mHandler.sendMessage(message)
    }

    fun setVideoPlayerInstance(player: IVideoPlayer) {
        mVideoPlayerImpl = player
    }

    private class MediaMsgHandler(
        stateMachine: PlayerStateMachine
    ) : Handler() {

        private val mRef = WeakReference(stateMachine)

        override fun handleMessage(msg: Message?) {
            val obj = msg?.obj as? IPlayerMsg ?: return
            mRef.get()?.apply {
                mVideoPlayerImpl?.let { p ->
                    mCurrentState = obj.transferState(mCurrentState, p, this)
                }
            }
        }
    }
}