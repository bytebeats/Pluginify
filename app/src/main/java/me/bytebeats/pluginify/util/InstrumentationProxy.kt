package me.bytebeats.pluginify.util

import android.app.Activity
import android.app.Instrumentation
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Bundle
import android.os.Environment
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import kotlin.Exception

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/1/15 12:06
 * @Version 1.0
 * @Description TO-DO
 */

internal class StubActivity : AppCompatActivity()

internal class IntentProxy(var base: Intent?) : Intent() {
    init {
        component = ComponentName(base?.component?.packageName ?: "", StubActivity::class.java.name)
    }
}

internal class PluginResources(hostRes: Resources?) : Resources(AssetManager::class.java.newInstance().apply {
    val pluginFile = File(Environment.getExternalStorageDirectory(), "plugin-with-resource.apk")
    val addAssetPath = javaClass.getMethod("addAssetPath", String::class.java)
    addAssetPath.isAccessible = true
    addAssetPath.invoke(this, pluginFile.path)
}, hostRes?.displayMetrics, hostRes?.configuration) {

}


internal class InstrumentationProxy(val base: Instrumentation) : Instrumentation() {
    var mIntentProxy: IntentProxy? = null
    override fun newActivity(cl: ClassLoader?, className: String?, intent: Intent?): Activity? {
        return (mIntentProxy?.base ?: intent)?.let {
            mIntentProxy = null
            base.newActivity(DynamicLoader.dexClassLoader, className, it)
        }
    }

    fun execStartActivity(
        who: Context, contextThread: IBinder, token: IBinder, target: Activity,
        intent: Intent, requestCode: Int, options: Bundle
    ): ActivityResult? {
        mIntentProxy = IntentProxy(intent)
        return try {
            val execStartActivity = base::class.java.getDeclaredMethod(
                "execStartActivity",
                Context::class.java,
                IBinder::class.java,
                IBinder::class.java,
                Activity::class.java,
                Intent::class.java,
                Int::class.java,
                Bundle::class.java
            )
            execStartActivity.invoke(
                mIntentProxy,
                who,
                contextThread,
                token,
                target,
                intent,
                requestCode,
                options
            ) as ActivityResult
        } catch (e: Exception) {
            null
        }
    }

    override fun callActivityOnCreate(activity: Activity?, icicle: Bundle?) {
        if (!isActivityFromPlugin(activity)) {
            base.callActivityOnCreate(activity, icicle)
            return
        }
        val pluginRes = PluginResources(activity?.resources)
        try {
            activity?.apply {
                val mPackageInfo = baseContext::class.java.getDeclaredField("mPackageInfo")
                mPackageInfo.isAccessible = true
                val mClassLoader = mPackageInfo::class.java.getDeclaredField("mClassLoader")
                mClassLoader.isAccessible = true
//                mClassLoader.invo
            }
        } catch (e: Exception) {

        }
    }

    private fun isActivityFromPlugin(activity: Activity?): Boolean {
        return activity?.javaClass?.canonicalName?.startsWith("me.bytebeats.pluginify.demo") ?: false
    }
}