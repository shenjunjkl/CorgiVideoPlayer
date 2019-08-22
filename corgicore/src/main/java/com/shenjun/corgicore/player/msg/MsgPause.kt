package com.shenjun.corgicore.player.msg

import com.shenjun.corgicore.player.IVideoPlayer
import com.shenjun.corgicore.player.PlayerState
import com.shenjun.corgicore.player.PlayerStateMachine

/**
 * Created by shenjun on 2019-08-22.
 */
class MsgPause: IPlayerMsg {
    override fun transferState(fromState: PlayerState, player: IVideoPlayer?, machine: PlayerStateMachine): PlayerState {
        when (fromState) {
            PlayerState.INIT, PlayerState.PREPARING, PlayerState.PREPARED -> {
                machine.startAfterPrepared = false
            }
            PlayerState.PLAYING -> {
                machine.startAfterPrepared = false
                player?.pause()
            }
            else -> {
            }
        }
        return fromState
    }
}