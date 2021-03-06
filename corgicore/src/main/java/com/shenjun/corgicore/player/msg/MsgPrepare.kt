package com.shenjun.corgicore.player.msg

import com.shenjun.corgicore.data.VideoInfo
import com.shenjun.corgicore.player.IVideoPlayer
import com.shenjun.corgicore.player.PlayerState
import com.shenjun.corgicore.player.PlayerStateMachine

/**
 * Created by shenjun on 2019-08-15.
 */
class MsgPrepare(private val info: VideoInfo) : IPlayerMsg {

    override fun transferState(fromState: PlayerState, player: IVideoPlayer?, machine: PlayerStateMachine): PlayerState {
        when (fromState) {
            PlayerState.INIT -> {
                player?.prepare(info)
            }
            else -> return fromState
        }
        return PlayerState.PREPARING
    }
}