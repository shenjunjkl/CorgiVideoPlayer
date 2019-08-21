package com.shenjun.corgicore.player.msg

import android.graphics.SurfaceTexture
import com.shenjun.corgicore.player.IVideoPlayer
import com.shenjun.corgicore.player.PlayerState
import com.shenjun.corgicore.player.PlayerStateMachine

/**
 * Created by shenjun on 2019-08-22.
 */
class MsgUpdateSurface(private val surfaceTexture: SurfaceTexture): IPlayerMsg {

    override fun transferState(fromState: PlayerState, player: IVideoPlayer?, machine: PlayerStateMachine): PlayerState {
        when (fromState) {
            PlayerState.IDLE -> {
            }
            else -> {
                player?.updateSurface(surfaceTexture)
            }
        }
        return fromState
    }
}