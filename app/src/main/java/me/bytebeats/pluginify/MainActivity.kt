package me.bytebeats.pluginify

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE = 0x001
        val permissions = arrayOf(Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE)
    }

    private val greetings by lazy { findViewById<TextView>(R.id.greetings) }

    private val activityResultApiLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.i("AAA", "StartActivityForResult")
                greetings.text = result?.data?.getStringExtra(KEY_HELLO)
            }
        }

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionResults ->
            if (permissionResults.values.any { false }) {
                Toast.makeText(this, "Not all permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "All permission granted", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            activityResultApiLauncher.launch(Intent(this, ActivityResultAPIActivity::class.java))
        }
        if (allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, permissions, 0x001)
            requestPermissions(permissions, 0x001)
            requestPermissionsLauncher.launch(permissions)
        }
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0x0001) {
            var isAllGranted = true
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false
                    break
                }
            }
            if (isAllGranted) {
                Toast.makeText(this, "All permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Not all permission granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivityForResult(Intent(this, OnActivityResultActivity::class.java), REQUEST_CODE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        Log.i("AAA", "onStart before")
        super.onStart()
        Log.i("AAA", "onStart after")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("AAA", "onRestart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("AAA", "onResume")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            greetings.text = data?.getStringExtra(KEY_HELLO)
            Log.i("AAA", "onActivityResult")
        }
    }
}