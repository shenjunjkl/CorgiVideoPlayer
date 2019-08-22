package com.shenjun.corgiextension.player.ijk

import android.content.res.AssetFileDescriptor
import tv.danmaku.ijk.media.player.misc.IMediaDataSource
import kotlin.math.min

/**
 * Created by shenjun on 2019-08-23.
 */
class IjkAssetsDataSource(private val afd: AssetFileDescriptor) : IMediaDataSource {

    private val mMediaBytes: ByteArray = afd.createInputStream().readBytes()

    override fun readAt(position: Long, buffer: ByteArray, offset: Int, size: Int): Int {
        val length = when {
            position + 1 >= mMediaBytes.size -> -1
            position + size < mMediaBytes.size -> size
            else -> min((mMediaBytes.size - position).toInt(), buffer.size) - 1
        }
        if (length > 0) {
            System.arraycopy(mMediaBytes, position.toInt(), buffer, offset, length)
        }
        return length
    }

    override fun getSize(): Long = afd.length

    override fun close() = afd.close()
}