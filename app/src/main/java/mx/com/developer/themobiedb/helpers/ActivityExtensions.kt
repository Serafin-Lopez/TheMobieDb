package mx.com.developer.themobiedb.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent

/**
 * Extension for simpler launching of Activities
 */
inline fun <reified T : Any> Activity.launchActivity(noinline init: Intent.() -> Unit = {}) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent)
}

inline fun <reified T : Any> newIntent(context: Context) = Intent(context, T::class.java)