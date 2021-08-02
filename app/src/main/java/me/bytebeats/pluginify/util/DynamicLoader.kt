package me.bytebeats.pluginify.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.net.Uri
import dalvik.system.DexClassLoader
import kotlin.jvm.internal.Reflection

object DynamicLoader {
    internal lateinit var dexClassLoader: DexClassLoader
    internal lateinit var instrumentation: Instrumentation

    fun loadApk(context: Context, pluginUri: Uri) {
        dexClassLoader = DexClassLoader(pluginUri.path, context.cacheDir.path, null, context.classLoader)
    }

    @SuppressLint("PrivateApi", "DiscouragedPrivateApi")
    fun replaceInstrumentation() {
        val clazz = Class.forName("android.app.ActivityThread")
        val method = clazz.getDeclaredMethod("currentActivityThread")
        method.isAccessible = true
        val instance = method.invoke(null)

        val field = clazz.getDeclaredField("mInstrumentation")
        field.isAccessible = true
        val instrumentation = field.get(instance) as Instrumentation

        if (instrumentation !is InstrumentationProxy) {
            val proxy = InstrumentationProxy(instrumentation)
            field.set(instance, proxy)
        }
    }
}