package com.shenjun.corgicore.player.msg

import com.shenjun.corgicore.framework.VideoConfig
import com.shenjun.corgicore.player.IVideoPlayer
import com.shenjun.corgicore.player.PlayerState
import com.shenjun.corgicore.player.PlayerStateMachine

/**
 * Created by shenjun on 2019-08-08.
 */
class MsgInit(private val config: VideoConfig, private val callback: IVideoPlayer.IPlayerCallback) : IPlayerMsg {

    override fun transferState(fromState: PlayerState, player: IVideoPlayer?, machine: PlayerStateMachine): PlayerState {
        when (fromState) {
            PlayerState.IDLE -> {
            }
            PlayerState.INIT, PlayerState.PREPARING, PlayerState.PREPARED,
            PlayerState.PLAYING, PlayerState.COMPLETE -> {
                player?.release()
            }
        }
        val p = config.createVideoPlayer()
        machine.setVideoPlayerInstance(p)
        p.create()
        p.registerCallback(callback)
        return PlayerState.INIT
    }
}