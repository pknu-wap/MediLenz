package com.android.mediproject.core.common.util

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
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.properties.Delegates

@Singleton
@SuppressLint("InternalInsetResource", "DiscouragedApi")
class SystemBarColorAnalyzer @Inject constructor() {
    private val waitLock = Mutex()
    private var waiting: Job? = null
    private val coroutineScope = MainScope() + CoroutineName("SystemBarColorAnalyzer")
    private val onChangedFragmentFlow = MutableSharedFlow<Unit>(onBufferOverflow = BufferOverflow.SUSPEND, replay = 1, extraBufferCapacity = 5)

    private var decorView by Delegates.notNull<View>()
    private var window by Delegates.notNull<Window>()
    private val resource = Resources.getSystem()
    private val statusBarHeight = resource.getDimensionPixelSize(resource.getIdentifier("status_bar_height", "dimen", "android"))
    private val navigationBarHeight = resource.getDimensionPixelSize(resource.getIdentifier("navigation_bar_height", "dimen", "android"))

    private val criteriaColor = 140
    private val avgRange = 3 to 3
    private var systemBarController: SystemBarController? = null

    private val delayTime = 70L

    private val _statusBarColor = MutableSharedFlow<SystemBarStyler.SystemBarColor>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST, replay = 1,
        extraBufferCapacity = 5,
    )
    val statusBarColor = _statusBarColor.asSharedFlow()

    private val _navigationBarColor = MutableSharedFlow<SystemBarStyler.SystemBarColor>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST, replay = 1,
        extraBufferCapacity = 5,
    )
    val navigationBarColor = _navigationBarColor.asSharedFlow()

    init {
        coroutineScope.launch(Dispatchers.Default) {
            onChangedFragmentFlow.collect {
                val convertJob = launch(start = CoroutineStart.LAZY) {
                    val colors = startConvert()
                    withContext(Dispatchers.Main) {
                        systemBarController?.setStyle(colors.first, colors.second)
                    }
                }

                decorView.doOnPreDraw {
                    convertJob.start()
                }
                convertJob.join()
            }
        }
    }

    private suspend fun startConvert(): Pair<SystemBarStyler.SystemBarColor, SystemBarStyler.SystemBarColor> {
        val statusBarBitmap = Bitmap.createBitmap(decorView.width, statusBarHeight, Bitmap.Config.ARGB_8888)
        val navigationBarBitmap = Bitmap.createBitmap(decorView.width, navigationBarHeight, Bitmap.Config.ARGB_8888)

        pixelCopy(Rect(0, 0, decorView.width, statusBarHeight), statusBarBitmap)
        pixelCopy(Rect(0, decorView.height - navigationBarHeight, decorView.width, decorView.height), navigationBarBitmap)

        val statusBarColor = statusBarBitmap[10, 10].toColor()
        val navigationBarColor = navigationBarBitmap[10, 10].toColor()

        statusBarBitmap.recycle()
        navigationBarBitmap.recycle()

        _statusBarColor.emit(statusBarColor)
        _navigationBarColor.emit(navigationBarColor)

        return statusBarColor to navigationBarColor
    }

    private fun avg(bitmap: Bitmap): Int {
        var sum = 0
        for (i in 0 until avgRange.first) {
            for (j in 0 until avgRange.second) {
                sum += bitmap[i, j].toGrayScale()
            }
        }
        return sum / (avgRange.first * avgRange.second)
    }

    fun init(activity: Activity, systemBarController: SystemBarController, lifecycle: Lifecycle) {
        decorView = activity.window.decorView
        this.systemBarController = systemBarController
        window = activity.window
        lifecycle.addObserver(
            object : DefaultLifecycleObserver {

                override fun onStart(owner: LifecycleOwner) {
                    super.onStart(owner)
                    convert()
                }

                override fun onDestroy(owner: LifecycleOwner) {
                    super.onDestroy(owner)
                    coroutineScope.cancel()
                }
            },
        )
    }


    private fun Int.toGrayScale(): Int = kotlin.run {
        val r = Color.red(this)
        val g = Color.green(this)
        val b = Color.blue(this)
        val a = Color.alpha(this)

        if (a == 0) -1
        else (0.2989 * r + 0.5870 * g + 0.1140 * b).toInt()
    }

    private fun Int.toColor() = toGrayScale().let { gray ->
        if (gray == 0 || gray == -1) SystemBarStyler.SystemBarColor.WHITE
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
        coroutineScope.launch {
            waitLock.withLock {
                if (waiting?.isActive == true) waiting?.cancel()
                waiting = launch(Dispatchers.Default) {
                    delay(delayTime)
                    onChangedFragmentFlow.emit(Unit)
                }
            }

        }
    }

}
