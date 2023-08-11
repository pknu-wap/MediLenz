package com.android.mediproject.core.common.util

import android.annotation.SuppressLint
import android.app.Activity
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
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.properties.Delegates

@Singleton
@SuppressLint("InternalInsetResource", "DiscouragedApi")
class ViewColorChanger @Inject constructor() {
    private val waitLock = Mutex()
    private var waiting: Job? = null
    private val coroutineScope = MainScope() + CoroutineName("ViewColorChanger")
    private val onChangedViewFlow = MutableSharedFlow<ViewValue>(onBufferOverflow = BufferOverflow.SUSPEND, replay = 1, extraBufferCapacity = 5)

    private var decorView by Delegates.notNull<View>()
    private var window by Delegates.notNull<Window>()

    private val criteriaColor = 140
    private val delayTime = 100L

    enum class ColorType {
        A, B
    }

    init {
        coroutineScope.launch(Dispatchers.Default) {
            onChangedViewFlow.collect {
                it.view.get()?.let { view ->
                    val convertJob = launch(start = CoroutineStart.LAZY) {
                        val color = startConvert(view)
                        withContext(Dispatchers.Main) {
                            it.block(color)
                        }
                    }

                    decorView.doOnPreDraw {
                        convertJob.start()
                    }
                    convertJob.join()
                }
            }
        }
    }

    private suspend fun startConvert(view: View): ColorType {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)

        val location = IntArray(2)
        view.getLocationInWindow(location)
        pixelCopy(Rect(location[0], location[1], location[0] + view.width, location[1] + view.height), bitmap)

        val color = bitmap[0, 0].toColor()

        bitmap.recycle()
        return color
    }

    fun init(activity: Activity) {
        decorView = activity.window.decorView
        window = activity.window
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
        if (gray == 0 || gray == -1) ColorType.A
        else if (gray <= criteriaColor) ColorType.A
        else ColorType.B
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

    fun convert(viewValue: ViewValue) {
        coroutineScope.launch {
            waitLock.withLock {
                if (waiting?.isActive == true) waiting?.cancel()
                waiting = launch(Dispatchers.Default) {
                    delay(delayTime)
                    onChangedViewFlow.emit(viewValue)
                }
            }

        }
    }

    class ViewValue(view: View, val block: (ColorType) -> Unit) {
        val view = WeakReference(view)
    }
}
