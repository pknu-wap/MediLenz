package com.android.mediproject.core.common.bindingadapter

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BitmapBindingAdapter {
    @BindingAdapter("app:bitmap")
    @JvmStatic
    fun loadImage(imageView: ImageView, bitmap: Bitmap) {
        Glide.with(imageView.context).load(bitmap).skipMemoryCache(false).into(imageView)
    }
}

object ImgUrlBindingAdapter {
    @BindingAdapter("app:imgUrl")
    @JvmStatic
    fun loadImage(imageView: ImageView, imgUrl: String) {
        Glide.with(imageView.context).load(imgUrl).skipMemoryCache(false).into(imageView)
    }
}