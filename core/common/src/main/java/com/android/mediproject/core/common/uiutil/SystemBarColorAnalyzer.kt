package com.android.mediproject.core.common.uiutil

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import android.view.Window
import androidx.core.graphics.get
import androidx.core.view.doOnPreDraw
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.properties.Delegates

@SuppressLint("InternalInsetResource", "DiscouragedApi")
object SystemBarColorAnalyzer {

    private val onChangedFragment = Channel<Unit>(onBufferOverflow = BufferOverflow.SUSPEND, capacity = Channel.BUFFERED)

    private var decorView by Delegates.notNull<View>()
    private var window by Delegates.notNull<Window>()
    private val resource = Resources.getSystem()
    private val statusBarHeight = resource.getDimensionPixelSize(resource.getIdentifier("status_bar_height", "dimen", "android"))
    private val navigationBarHeight = resource.getDimensionPixelSize(resource.getIdentifier("navigation_bar_height", "dimen", "android"))
    private var windowHeight by Delegates.notNull<Int>()

    private const val criteriaColor = 140

    private fun Int.toGrayScale(): Int {
        val r = Color.red(this)
        val g = Color.green(this)
        val b = Color.blue(this)

        return (0.2989 * r + 0.5870 * g + 0.1140 * b).toInt()
    }

    private var systemBarController: SystemBarController? = null

    fun init(activity: Activity, systemBarController: SystemBarController) {
        decorView = activity.window.decorView
        this.systemBarController = systemBarController
        window = activity.window
        windowHeight = decorView.height
    }

    private val job = MainScope().launch {
        onChangedFragment.consumeAsFlow().collect {
            if (decorView.width == 0 || decorView.height == 0) return@collect
            decorView.doOnPreDraw {
                launch(Dispatchers.Default) {
                    val statusBarBitmap = Bitmap.createBitmap(decorView.width, statusBarHeight, Bitmap.Config.ARGB_8888)
                    val navigationBarBitmap = Bitmap.createBitmap(decorView.width, navigationBarHeight, Bitmap.Config.ARGB_8888)

                    suspendCancellableCoroutine { continuation ->
                        PixelCopy.request(
                            window, Rect(0, 0, decorView.width, statusBarHeight), statusBarBitmap,
                            {
                                continuation.resume(Unit)
                            },
                            Handler(Looper.getMainLooper()),
                        )
                    }

                    suspendCancellableCoroutine { continuation ->
                        PixelCopy.request(
                            window, Rect(0, windowHeight - navigationBarHeight, decorView.width, windowHeight), navigationBarBitmap,
                            {
                                continuation.resume(Unit)
                            },
                            Handler(Looper.getMainLooper()),
                        )
                    }

                    println("whiteCriteria: $criteriaColor")
                    println("statusBarBitmap[0, 0]: ${statusBarBitmap[0, 0].toGrayScale()}")
                    println("navigationBarBitmap[0, 0]: ${navigationBarBitmap[0, 0].toGrayScale()}")

                    withContext(Dispatchers.Main) {
                        systemBarController?.setStyle(
                            if (statusBarBitmap[0, 0].toGrayScale() <= criteriaColor) SystemBarStyler.StatusBarColor.BLACK else SystemBarStyler
                                .StatusBarColor.WHITE,
                            if (navigationBarBitmap[0, 0].toGrayScale() == 0) SystemBarStyler.NavigationBarColor.BLACK else SystemBarStyler
                                .NavigationBarColor.WHITE,
                        )
                        statusBarBitmap.recycle()
                        navigationBarBitmap.recycle()
                    }

                }
            }
        }
    }

    fun convert() {
        onChangedFragment.trySend(Unit)
        println("convert")
    }

    fun release() {
        job.cancel()
    }
}
