package com.shenjun.corgicore.player.msg

import com.shenjun.corgicore.player.IVideoPlayer
import com.shenjun.corgicore.player.PlayerState
import com.shenjun.corgicore.player.PlayerStateMachine

/**
 * Created by shenjun on 2019-08-21.
 */
class MsgPrepared: IPlayerMsg {

    override fun transferState(fromState: PlayerState, player: IVideoPlayer?, machine: PlayerStateMachine): PlayerState {
        when (fromState) {
            PlayerState.PREPARING -> {
                if (machine.startAfterPrepared) {
                    return MsgStart().transferState(PlayerState.PREPARED, player, machine)
                }
            }
            else -> return fromState
        }
        return PlayerState.PREPARED
    }
}