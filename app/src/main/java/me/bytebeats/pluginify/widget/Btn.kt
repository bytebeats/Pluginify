package me.bytebeats.pluginify.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatButton

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/1/21 11:50
 * @Version 1.0
 * @Description TO-DO
 */

class Btn @JvmOverloads constructor(context: Context, attrSet: AttributeSet? = null, defStyleRes: Int = 0) :
        AppCompatButton(context, attrSet, defStyleRes) {
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Log.i("AAA", "Btn: dispatchTouchEvent: ${MotionEvent.actionToString(event?.action ?: 0)}")
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.i("AAA", "Btn: onTouchEvent: ${MotionEvent.actionToString(event?.action ?: 0)}")
        return super.onTouchEvent(event)
    }
}