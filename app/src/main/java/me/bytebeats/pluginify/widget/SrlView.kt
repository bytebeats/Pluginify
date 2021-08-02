package me.bytebeats.pluginify.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ScrollView

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/1/21 11:54
 * @Version 1.0
 * @Description TO-DO
 */

class SrlView @JvmOverloads constructor(context: Context, attrSet: AttributeSet? = null, defStyleRes: Int = 0) :
        ScrollView(context, attrSet, defStyleRes) {
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.i("AAA", "SrlView: onInterceptTouchEvent: ${MotionEvent.actionToString(ev?.action ?: 0)}")
        return super.onInterceptTouchEvent(ev)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.i("AAA", "SrlView: dispatchTouchEvent: ${MotionEvent.actionToString(ev?.action ?: 0)}")
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        Log.i("AAA", "SrlView: onTouchEvent: ${MotionEvent.actionToString(ev?.action ?: 0)}")
        return super.onTouchEvent(ev)
    }
}