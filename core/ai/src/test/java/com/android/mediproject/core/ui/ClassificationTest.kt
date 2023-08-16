package com.android.mediproject.core.ui

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import androidx.test.core.app.ApplicationProvider
import com.android.mediproject.core.ai.R
import com.android.mediproject.core.ai.tflite.classification.MedicineClassifier
import com.android.mediproject.core.ai.tflite.classification.MedicineClassifierImpl
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ClassificationTest {

    private val bitmapMap = mutableMapOf<String, Bitmap>()
    private lateinit var classifier: MedicineClassifier
    private lateinit var context: Context
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() = runTest {
        context = ApplicationProvider.getApplicationContext()
        classifier = MedicineClassifierImpl(context, testDispatcher)

        loadBitmap(R.drawable.medi_1970000050)
        loadBitmap(R.drawable.medi_1970000050)
    }

    private fun loadBitmap(@DrawableRes fileName: Int) {
        val drawable = context.resources.getDrawable(fileName, null)
        val bitmap = drawable.toBitmap()
        bitmapMap[drawable.toString()] = bitmap
    }

    @Test
    fun classify() {
        println("Start classification test")
        println(bitmapMap)
    }
}
