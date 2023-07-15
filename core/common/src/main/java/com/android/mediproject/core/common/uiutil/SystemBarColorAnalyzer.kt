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
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.properties.Delegates

@SuppressLint("InternalInsetResource", "DiscouragedApi")
object SystemBarColorAnalyzer {
    private val onChangedFragmentFlow = MutableSharedFlow<Unit>(onBufferOverflow = BufferOverflow.DROP_OLDEST, replay = 1, extraBufferCapacity = 4)

    private var decorView by Delegates.notNull<View>()
    private var window by Delegates.notNull<Window>()
    private val resource = Resources.getSystem()
    private val statusBarHeight = resource.getDimensionPixelSize(resource.getIdentifier("status_bar_height", "dimen", "android"))
    private val navigationBarHeight = resource.getDimensionPixelSize(resource.getIdentifier("navigation_bar_height", "dimen", "android"))

    private const val criteriaColor = 100
    private var systemBarController: SystemBarController? = null

    private val job = MainScope().launch {
        onChangedFragmentFlow.collect {
            decorView.doOnPreDraw {
                launch(Dispatchers.Default) {
                    val statusBarBitmap = Bitmap.createBitmap(decorView.width, statusBarHeight, Bitmap.Config.ARGB_8888)
                    val navigationBarBitmap = Bitmap.createBitmap(decorView.width, navigationBarHeight, Bitmap.Config.ARGB_8888)

                    val statusBarCopyResult = async {
                        pixelCopy(Rect(0, 0, decorView.width, statusBarHeight), statusBarBitmap)
                    }

                    val navBarCopyResult = async {
                        pixelCopy(Rect(0, decorView.height - navigationBarHeight, decorView.width, decorView.height), navigationBarBitmap)
                    }

                    if (!statusBarCopyResult.await() || !navBarCopyResult.await()) return@launch

                    val statusBarColor = statusBarBitmap[10, 10].toColor()
                    val navigationBarColor = navigationBarBitmap[10, 10].toColor()

                    statusBarBitmap.recycle()
                    navigationBarBitmap.recycle()

                    withContext(Dispatchers.Main) {
                        systemBarController?.setStyle(
                            statusBarColor,
                            navigationBarColor,
                        )
                    }

                }
            }
        }
    }

    fun init(activity: Activity, systemBarController: SystemBarController, lifecycle: Lifecycle) {
        decorView = activity.window.decorView
        this.systemBarController = systemBarController
        window = activity.window
        lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onCreate(owner: LifecycleOwner) {
                    super.onCreate(owner)
                }

                override fun onStart(owner: LifecycleOwner) {
                    super.onStart(owner)
                    convert()
                }

                override fun onDestroy(owner: LifecycleOwner) {
                    super.onDestroy(owner)
                    release()
                }
            },
        )
    }


    private fun Int.toGrayScale(): Int = (0.2989 * Color.red(this) + 0.5870 * Color.green(this) + 0.1140 * Color.blue(this)).toInt()

    private fun Int.toColor() = toGrayScale().let { gray ->
        if (gray == 0) SystemBarStyler.SystemBarColor.BLACK
        else if (gray <= criteriaColor) SystemBarStyler.SystemBarColor.WHITE
        else SystemBarStyler.SystemBarColor.BLACK
    }

    private suspend fun pixelCopy(rect: Rect, bitmap: Bitmap) = suspendCancellableCoroutine { cancellableContinuation ->
        PixelCopy.request(
            window, rect, bitmap,
            {
                cancellableContinuation.resume(it == PixelCopy.SUCCESS)
            },
            Handler(Looper.getMainLooper()),
        )
    }

    fun convert() {
        onChangedFragmentFlow.tryEmit(Unit)
    }

    private fun release() {
        job.cancel()
    }
}
