package com.shenjun.corgicore.player.msg

import com.shenjun.corgicore.player.IVideoPlayer
import com.shenjun.corgicore.player.PlayerState
import com.shenjun.corgicore.player.PlayerStateMachine

/**
 * Created by shenjun on 2019-08-23.
 */
class MsgRelease: IPlayerMsg {

    override fun transferState(fromState: PlayerState, player: IVideoPlayer?, machine: PlayerStateMachine): PlayerState {
        if (fromState == PlayerState.IDLE) return fromState
        player?.release()
        machine.setVideoPlayerInstance(null)
        return PlayerState.IDLE
    }
}