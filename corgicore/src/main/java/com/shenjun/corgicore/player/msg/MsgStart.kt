package com.shenjun.corgicore.player.msg

import com.shenjun.corgicore.constant.PlayerConst
import com.shenjun.corgicore.player.IVideoPlayer
import com.shenjun.corgicore.player.PlayerState
import com.shenjun.corgicore.player.PlayerStateMachine

/**
 * Created by shenjun on 2019-08-22.
 */
class MsgStart(private val priority: Int): IPlayerMsg {

    override fun transferState(fromState: PlayerState, player: IVideoPlayer?, machine: PlayerStateMachine): PlayerState {
        if (priority < machine.pausePriority) {
            return fromState
        }
        machine.pausePriority = PlayerConst.PRIORITY_NONE
        when (fromState) {
            PlayerState.INIT, PlayerState.PREPARING -> {
                machine.startAfterPrepared = true
                return fromState
            }
            PlayerState.PREPARED -> {
                machine.startAfterPrepared = true
                player?.start()
            }
            PlayerState.PLAYING -> {
                if (player?.isPlaying() == false) {
                    player.start()
                }
            }
            PlayerState.COMPLETE -> {
                player?.seekTo(0)
                player?.start()
            }
            else -> return fromState
        }
        return PlayerState.PLAYING
    }
}