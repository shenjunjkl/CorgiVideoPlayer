package com.shenjun.corgicore.data

import android.os.Bundle

/**
 * Created by shenjun on 2018/11/22.
 */
data class VideoInfo(
    var url: String = "",
    var headers: Map<String, String> = mutableMapOf(),
    var brief: Int = BRIEF_LOADING,
    var errorCode: Int = ERROR_OK,
    var errorMsg: String = "",
    var extra: Bundle = Bundle(),
    var obj: Any? = null
) {

    companion object {
        const val BRIEF_LOADING = 1
        const val BRIEF_CONFIG = 2
        const val BRIEF_INFO = 3
        const val BRIEF_SOURCE = 4

        const val ERROR_OK = 0
        const val ERROR_LOAD_FAILED = 1
        const val ERROR_RETRIEVE_SOURCE_FAILED = 2
        const val ERROR_SOURCE_INVALID = 3
    }
}