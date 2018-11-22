package com.shenjun.corgivideoplayer.common

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import com.shenjun.corgivideoplayer.R

/**
 * Created by shenjun on 2018/11/23.
 */
@SuppressLint("Registered")
abstract class BaseIndexActivity : Activity() {

    abstract fun provideIndexMap() : Map<String, Class<out Activity>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scrollView = ScrollView(this)
        scrollView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        scrollView.addView(linearLayout)
        setContentView(scrollView)

        val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.topMargin = resources.getDimension(R.dimen.px20).toInt()
        lp.gravity = Gravity.CENTER_HORIZONTAL

        for ((str, clz) in provideIndexMap()) {
            val btn = Button(this)
            with(btn) {
                text = str
                gravity = Gravity.CENTER
                setTextColor(Color.BLACK)
                layoutParams = lp
                setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.px26))
                isAllCaps = false
                setOnClickListener { jumpToActivity(clz) }
            }
            linearLayout.addView(btn)
        }
    }

    private fun jumpToActivity(actClass: Class<out Activity>) {
        val intent = Intent()
        intent.setClass(this, actClass)
        startActivity(intent)
    }
}