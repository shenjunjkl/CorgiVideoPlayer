package com.shenjun.corgicore.player.msg

import com.shenjun.corgicore.player.IVideoPlayer
import com.shenjun.corgicore.player.PlayerState
import com.shenjun.corgicore.player.PlayerStateMachine

class MsgSeek(private val timeMs: Long) : IPlayerMsg {

    override fun transferState(fromState: PlayerState, player: IVideoPlayer?, machine: PlayerStateMachine): PlayerState {
        if (fromState == PlayerState.PLAYING) {
            player?.seekTo(timeMs)
        }
        return fromState
    }
}