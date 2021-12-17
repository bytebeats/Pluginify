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
import android.os.UserHandle
import androidx.appcompat.app.AppCompatActivity
import java.io.File

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/1/15 12:06
 * @Version 1.0
 * @Description TO-DO
 */

internal const val EXTRA_IS_PLUGIN_ACTIVITY = "is_plugin_activity"
internal const val EXTRA_PLUGIN_PKG = "plugin_pkg"
internal const val EXTRA_PLUGIN_CLASS = "plugin_class"

internal class StandardStubActivity : AppCompatActivity()
internal class SingleTopStubActivity : AppCompatActivity()
internal class SingleTaskStubActivity : AppCompatActivity()
internal class SingleInstanceStubActivity : AppCompatActivity()

internal class PluginResources(hostRes: Resources?) : Resources(AssetManager::class.java.newInstance().apply {
    val pluginFile = File(Environment.getExternalStorageDirectory(), "plugin-with-resource.apk")
    val addAssetPath = javaClass.getMethod("addAssetPath", String::class.java)
    addAssetPath.isAccessible = true
    addAssetPath.invoke(this, pluginFile.path)
}, hostRes?.displayMetrics, hostRes?.configuration)

internal class InstrumentationProxy(val mBase: Instrumentation) : Instrumentation() {
    override fun newActivity(cl: ClassLoader?, className: String?, intent: Intent?): Activity? {
        return mBase.newActivity(DynamicLoader.dexClassLoader, className, intent)
    }

    fun execStartActivity(
        who: Context?, contextThread: IBinder?, token: IBinder?, target: Activity?,
        intent: Intent?, requestCode: Int, options: Bundle?
    ): ActivityResult? {
        injectIntent(intent, target?.localClassName)
        return try {
            val execStartActivity = mBase::class.java.getDeclaredMethod(
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

    fun execStartActivity(
        who: Context?, contextThread: IBinder?, token: IBinder?, resultWho: String?,
        intent: Intent?, requestCode: Int, options: Bundle?, user: UserHandle?
    ): ActivityResult? {
        injectIntent(intent, resultWho)
        return try {
            val execStartActivity = mBase::class.java.getDeclaredMethod(
                "execStartActivity",
                Context::class.java,
                IBinder::class.java,
                IBinder::class.java,
                String::class.java,
                Intent::class.java,
                Int::class.java,
                Bundle::class.java,
                UserHandle::class.java
            )
            execStartActivity.invoke(
                who,
                contextThread,
                token,
                resultWho,
                intent,
                requestCode,
                options,
                user
            ) as ActivityResult
        } catch (e: Exception) {
            null
        }
    }

    fun execStartActivity(
        who: Context?, contextThread: IBinder?, token: IBinder?, target: String?,
        intent: Intent?, requestCode: Int, options: Bundle?
    ): ActivityResult? {
        injectIntent(intent, target)
        return try {
            val execStartActivity = mBase::class.java.getDeclaredMethod(
                "execStartActivity",
                Context::class.java,
                IBinder::class.java,
                IBinder::class.java,
                String::class.java,
                Intent::class.java,
                Int::class.java,
                Bundle::class.java
            )
            execStartActivity.invoke(
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
            mBase.callActivityOnCreate(activity, icicle)
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

    private fun injectIntent(intent: Intent?, target: String?) {
        if (isActivityFromPlugin(target)) {
            val pkg = intent?.component?.packageName
            val klazz = intent?.component?.className
            val launchMode = intent?.flags
            val stubActivity = if (launchMode == Intent.FLAG_ACTIVITY_NEW_TASK) {
                SingleInstanceStubActivity::class.java
            } else if (launchMode == Intent.FLAG_ACTIVITY_SINGLE_TOP) {
                SingleTopStubActivity::class.java
            } else if (launchMode == Intent.FLAG_ACTIVITY_CLEAR_TOP) {
                SingleTaskStubActivity::class.java
            } else {
                StandardStubActivity::class.java
            }
            intent?.component = ComponentName(DynamicLoader.mContext, stubActivity)
            intent?.putExtra(EXTRA_IS_PLUGIN_ACTIVITY, true)
            intent?.putExtra(EXTRA_PLUGIN_PKG, pkg)
            intent?.putExtra(EXTRA_PLUGIN_CLASS, klazz)
        }
    }

    private fun uninjectIntent(intent: Intent?) {
        if (intent?.getBooleanExtra(EXTRA_IS_PLUGIN_ACTIVITY, false) == true) {
            val pkg = intent.getStringExtra(EXTRA_PLUGIN_PKG).orEmpty()
            val klazz = intent.getStringExtra(EXTRA_PLUGIN_PKG).orEmpty()
            intent.component = ComponentName(pkg, klazz)
        }
    }

    private fun isActivityFromPlugin(activity: String?): Boolean {
        return activity?.startsWith("me.bytebeats.pluginify.demo") ?: false
    }

    private fun isActivityFromPlugin(activity: Activity?): Boolean {
        return activity?.javaClass?.canonicalName?.startsWith("me.bytebeats.pluginify.demo") ?: false
    }
}