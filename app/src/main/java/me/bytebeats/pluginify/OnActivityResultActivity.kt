package me.bytebeats.pluginify

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import me.bytebeats.pluginify.R

class OnActivityResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_result)
        findViewById<Button>(R.id.on_activity_result).setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtras(Bundle().apply {
                    putString(KEY_HELLO, "I'm OnActivityResultActivity")
                })
            })
            finish()
        }
    }
}