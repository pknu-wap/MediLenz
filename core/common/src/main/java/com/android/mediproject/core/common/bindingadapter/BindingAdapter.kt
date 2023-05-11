package com.android.mediproject.core.common.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BitmapBindingAdapter {
    @BindingAdapter("app:byteArray")
    @JvmStatic
    fun loadImage(imageView: ImageView, byteArray: ByteArray) {
        Glide.with(imageView.context).load(byteArray).skipMemoryCache(false).into(imageView)
    }
}

object ImgUrlBindingAdapter {
    @BindingAdapter("app:imgUrl")
    @JvmStatic
    fun loadImage(imageView: ImageView, imgUrl: String) {
        Glide.with(imageView.context).load(imgUrl).skipMemoryCache(false).into(imageView)
    }
}