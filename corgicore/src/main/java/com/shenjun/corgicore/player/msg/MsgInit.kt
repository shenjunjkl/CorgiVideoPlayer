package com.shenjun.corgicore.player.msg

import com.shenjun.corgicore.player.IVideoPlayer
import com.shenjun.corgicore.player.PlayerState
import com.shenjun.corgicore.player.PlayerStateMachine

/**
 * Created by shenjun on 2019-08-08.
 */
class MsgInit : IPlayerMsg {


    override fun transferState(fromState: PlayerState, player: IVideoPlayer, machine: PlayerStateMachine): PlayerState {



        return PlayerState.INIT
    }
}