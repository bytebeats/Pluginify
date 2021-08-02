package me.bytebeats.pluginify

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ActivityResultAPIActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_a_p_i)
        findViewById<Button>(R.id.activity_result_contract).setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtras(Bundle().apply {
                    putString(KEY_HELLO, "I'm ResultAPIActivity")
                })
            })
            finish()
        }
    }
}