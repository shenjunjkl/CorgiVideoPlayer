package com.shenjun.corgicore.view.listener

/**
 * Created by shenjun on 2020-02-12.
 */
interface ErrorListener {

    fun onError(errorCode: Int, msg: String)
}