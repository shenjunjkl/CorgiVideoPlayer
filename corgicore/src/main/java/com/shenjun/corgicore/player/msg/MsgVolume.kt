package com.shenjun.corgicore.player.msg

import com.shenjun.corgicore.player.IVideoPlayer
import com.shenjun.corgicore.player.PlayerState
import com.shenjun.corgicore.player.PlayerStateMachine

/**
 * Created by shenjun on 2019-10-22.
 */
class MsgVolume(private val volumeLeft: Float, private val volumeRight: Float) : IPlayerMsg {

    override fun transferState(fromState: PlayerState, player: IVideoPlayer?, machine: PlayerStateMachine): PlayerState {
        when (fromState) {
            PlayerState.IDLE -> {
            }
            else -> {
                player?.setVolume(volumeLeft, volumeRight)
            }
        }
        return fromState
    }
}