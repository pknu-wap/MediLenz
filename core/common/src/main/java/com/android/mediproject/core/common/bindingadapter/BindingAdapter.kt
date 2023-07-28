package com.android.mediproject.core.common.bindingadapter

import android.graphics.Bitmap
import android.text.Spanned
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import androidx.databinding.BindingAdapter
import com.android.mediproject.core.common.R
import com.android.mediproject.core.model.constants.MedicationType
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class GlideAppModule : AppGlideModule()

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("medicationTypeText")
    fun setMedicationTypeText(textView: TextView, type: MedicationType) {
        val arr = textView.context.resources.getStringArray(R.array.medicationTypes)
        textView.text = arr[type.ordinal]
    }

    @BindingAdapter(value = ["img", "caching"], requireAll = false)
    @JvmStatic
    fun loadImage(imageView: ImageView, img: String, caching: Boolean = true) {
        if (img.isEmpty()) {
            imageView.setBackgroundResource(R.drawable.baseline_hide_image_24)
            return
        }
        GlideApp.with(imageView.context).load(img).centerInside().skipMemoryCache(caching).into(imageView)
    }

    @BindingAdapter("img")
    @JvmStatic
    fun loadImage(imageView: ImageView, img: Bitmap) {
        GlideApp.with(imageView.context).load(img).centerInside().skipMemoryCache(false).into(imageView)
    }


    /**
     * TextView에 비동기적으로 텍스트를 설정합니다.
     */
    @BindingAdapter("asyncText")
    @JvmStatic
    fun setAsyncText(textView: TextView, text: Spanned?) {
        if (text != null) {
            val precomputedText = PrecomputedTextCompat.create(text, TextViewCompat.getTextMetricsParams(textView))
            TextViewCompat.setPrecomputedText(textView, precomputedText)
        }
    }

    /**
     * EditText의 텍스트를 전달받은 ViewModel의 onClickedSendButton 함수에 전달하고, EditText의 텍스트를 지웁니다.
     *
     * @param view 클릭 이벤트를 받을 View
     * @param editText 텍스트를 전달받을 EditText
     * @param iSendText 텍스트를 전달받을 ViewModel
     */
    @BindingAdapter("onClickSend", "inputText")
    @JvmStatic
    fun setOnClick(view: View, iSendText: ISendText, editText: EditText) {
        view.setOnClickListener {
            iSendText.onClickedSendButton(editText.text)
            editText.text.clear()
        }
    }

    /**
     * ImageView에 이미지를 설정합니다.
     * 이미지가 없을 경우 TextView에 메시지를 표시합니다.
     * @param imageView 이미지를 설정할 ImageView
     * @param img 이미지의 URL
     * @param textView 이미지가 없을 경우 표시할 TextView
     */
    @BindingAdapter("img", "messageView", "message")
    @JvmStatic
    fun setImage(imageView: ImageView, img: String, textView: TextView, message: String) {
        textView.text = message
        textView.isVisible = img.isEmpty()

        if (img.isNotEmpty()) GlideApp.with(imageView.context).load(img).centerInside().skipMemoryCache(false).into(imageView)
    }
}
