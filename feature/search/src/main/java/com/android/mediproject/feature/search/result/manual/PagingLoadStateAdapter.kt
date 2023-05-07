package com.android.mediproject.feature.search.result.manual

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.ui.databinding.LoadStateItemBinding

class PagingLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<LoadStateViewHolder>() {


    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) = holder.bind(loadState)

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder = LoadStateViewHolder(
        LoadStateItemBinding.inflate(
            LayoutInflater.from(parent.context), null, false
        ), retry
    )
}

class LoadStateViewHolder(private val binding: LoadStateItemBinding, private val retry: () -> Unit) : RecyclerView.ViewHolder(
    binding.root
) {

    init {
        binding.retryButton.setOnClickListener {
            retry.invoke()
        }
    }

    fun bind(loadState: LoadState) {
        binding.apply {
            if (loadState is LoadState.Error)
                errorTextView.text = loadState.error.localizedMessage

            progressBar.isVisible = loadState is LoadState.Loading
            retryButton.isVisible = loadState is LoadState.Error
            errorTextView.isVisible = loadState is LoadState.Error
        }
    }
}