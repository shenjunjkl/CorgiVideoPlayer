package com.shenjun.corgicore.player.msg

import com.shenjun.corgicore.player.IVideoPlayer
import com.shenjun.corgicore.player.PlayerState
import com.shenjun.corgicore.player.PlayerStateMachine
import com.shenjun.corgicore.tools.name

/**
 * Created by shenjun on 2019/2/9.
 */
interface IPlayerMsg {

    fun what() = name().hashCode()

    fun transferState(fromState: PlayerState, player: IVideoPlayer?, machine: PlayerStateMachine): PlayerState
}