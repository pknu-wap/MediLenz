package com.android.mediproject.core.ui.base.view

import android.content.Context
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.common.util.dpToPx
import com.android.mediproject.core.ui.R
import com.google.android.material.progressindicator.CircularProgressIndicator

class LoadStateView(context: Context, onRetry: () -> Unit) : ConstraintLayout(context), LoadStateListener {
    init {


        val horizontalPadding = 24.dpToPx()
        val verticalPadding = 12.dpToPx()

        setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)

        CircularProgressIndicator(context).also { indicator ->
            indicator.id = android.R.id.progress
            indicator.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also {
                it.topToTop = LayoutParams.PARENT_ID
                it.startToStart = LayoutParams.PARENT_ID
                it.endToEnd = LayoutParams.PARENT_ID
            }
            addView(indicator)
        }

        TextView(context).also { textView ->
            textView.id = android.R.id.text1
            textView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also {
                it.topToBottom = android.R.id.progress
                it.startToStart = LayoutParams.PARENT_ID
                it.endToEnd = LayoutParams.PARENT_ID
                it.setMargins(0, 16.dpToPx(), 0, 0)
            }

            addView(textView)
        }

        Button(context).also { btn ->
            btn.id = android.R.id.button1
            btn.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also {
                it.topToBottom = android.R.id.text1
                it.startToStart = LayoutParams.PARENT_ID
                it.endToEnd = LayoutParams.PARENT_ID
                it.setMargins(0, 8.dpToPx(), 0, 0)
            }
            btn.text = context.getString(R.string.retryText)
            setOnClickListener {
                onRetry()
            }
            addView(btn)
        }
    }


    override fun setLoadingState(state: LoadingState) {
        when (state) {
            is LoadingState.Loading -> {
                findViewById<CircularProgressIndicator>(android.R.id.progress).visibility = VISIBLE
                findViewById<TextView>(android.R.id.text1).text = ""
                findViewById<Button>(android.R.id.button1).visibility = GONE
            }

            is LoadingState.NotLoading -> {
                findViewById<CircularProgressIndicator>(android.R.id.progress).visibility = GONE
                findViewById<TextView>(android.R.id.text1).text = ""
                findViewById<Button>(android.R.id.button1).visibility = GONE
            }

            is LoadingState.Error -> {
                findViewById<CircularProgressIndicator>(android.R.id.progress).visibility = GONE
                findViewById<TextView>(android.R.id.text1).text = state.message
                findViewById<Button>(android.R.id.button1).visibility = VISIBLE
            }
        }
    }

}

sealed class LoadingState {
    object Loading : LoadingState()
    object NotLoading : LoadingState()
    data class Error(val message: String) : LoadingState()
}

class LoadStateViewHolder(context: Context, onRetry: () -> Unit) : RecyclerView.ViewHolder(LoadStateView(context, onRetry)),
    LoadStateListener {
    override fun setLoadingState(state: LoadingState) {
        (itemView as LoadStateView).setLoadingState(state)
    }
}

interface LoadStateListener {
    fun setLoadingState(state: LoadingState)
}
