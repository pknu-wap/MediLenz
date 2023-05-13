package com.android.mediproject.core.common.bindingadapter

import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.android.mediproject.core.common.R
import com.android.mediproject.core.common.constant.MedicationType
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class GlideAppModule : AppGlideModule()

object BitmapBindingAdapter {
    @BindingAdapter("bitmap")
    @JvmStatic
    fun loadBitmapImage(imageView: ImageView, bitmap: Bitmap) {
        GlideApp.with(imageView.context).load(bitmap).skipMemoryCache(false).into(imageView)
    }
}

object ImgUrlBindingAdapter {
    @BindingAdapter("imgUrl")
    @JvmStatic
    fun loadUrlImage(imageView: ImageView, imgUrl: String) {
        GlideApp.with(imageView.context).load(imgUrl).skipMemoryCache(false).into(imageView)
    }
}

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("medicationTypeText")
    fun setStatusText(textView: TextView, type: MedicationType) {
        val arr = textView.context.resources.getStringArray(R.array.medicationTypes)
        textView.text = arr[type.ordinal]
    }
}