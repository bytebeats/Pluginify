package me.bytebeats.pluginify

import android.app.Application
import android.net.Uri
import android.os.Environment
import me.bytebeats.pluginify.util.DynamicLoader
import java.io.File

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/1/11 14:05
 * @Version 1.0
 * @Description TO-DO
 */

class PluginApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val pluginApk = File(Environment.getExternalStorageDirectory(), "test-apk-with-res.apk")
        DynamicLoader.loadApk(this, Uri.fromFile(pluginApk))
    }
}