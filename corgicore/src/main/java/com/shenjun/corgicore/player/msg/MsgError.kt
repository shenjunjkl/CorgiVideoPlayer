package com.shenjun.corgicore.player.msg

import com.shenjun.corgicore.player.IVideoPlayer
import com.shenjun.corgicore.player.PlayerState
import com.shenjun.corgicore.player.PlayerStateMachine

/**
 * Created by shenjun on 2019-08-15.
 */
class MsgError(private val errorCode: Int, private val msg: String) : IPlayerMsg {

    override fun transferState(fromState: PlayerState, player: IVideoPlayer?, machine: PlayerStateMachine): PlayerState {
        player?.release()
        return PlayerState.IDLE
    }
}