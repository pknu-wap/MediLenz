package com.android.mediproject.core.common.bindingadapter

import android.graphics.Bitmap
import android.text.Spannable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import androidx.databinding.BindingAdapter
import com.android.mediproject.core.common.R
import com.android.mediproject.core.common.constant.MedicationType
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class GlideAppModule : AppGlideModule()

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("medicationTypeText")
    fun setStatusText(textView: TextView, type: MedicationType) {
        val arr = textView.context.resources.getStringArray(R.array.medicationTypes)
        textView.text = arr[type.ordinal]
    }

    @BindingAdapter("img")
    @JvmStatic
    fun loadImage(imageView: ImageView, img: String) {
        GlideApp.with(imageView.context).load(img).centerInside().skipMemoryCache(false).into(imageView)
    }

    @BindingAdapter("img")
    @JvmStatic
    fun loadImage(imageView: ImageView, img: Bitmap) {
        GlideApp.with(imageView.context).load(img).centerInside().skipMemoryCache(false).into(imageView)
    }


    @BindingAdapter("asyncText")
    @JvmStatic
    fun setAsyncText(textView: TextView, text: Spannable) {
        val precomputedText = PrecomputedTextCompat.create(text, TextViewCompat.getTextMetricsParams(textView))
        TextViewCompat.setPrecomputedText(textView, precomputedText)
    }
}