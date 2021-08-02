package me.bytebeats.pluginify.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/1/21 17:17
 * @Version 1.0
 * @Description TO-DO
 */

class FrmLayout @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attributeSet, defStyleAttr) {
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.i("AAA", "FrmLayout: onInterceptTouchEvent: ${MotionEvent.actionToString(ev?.action ?: 0)}")
        return super.onInterceptTouchEvent(ev)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.i("AAA", "FrmLayout: dispatchTouchEvent: ${MotionEvent.actionToString(ev?.action ?: 0)}")
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.i("AAA", "FrmLayout: onTouchEvent: ${MotionEvent.actionToString(event?.action ?: 0)}")
        return super.onTouchEvent(event)
    }
}