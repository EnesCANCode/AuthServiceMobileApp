package dev.enes.authapp

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

/**
 * Android platform implementasyonu.
 */
actual fun getPlatformName(): String = "Android ${android.os.Build.VERSION.SDK_INT}"

/**
 * Android için OkHttp engine sağlar.
 */
fun createAndroidHttpEngine(): HttpClientEngine = OkHttp.create()
