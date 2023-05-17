package com.android.mediproject.feature.medicine.precautions.item.precautions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.util.XMLParsedResult
import com.android.mediproject.feature.medicine.databinding.ItemViewPrecautionsBinding
import java.lang.ref.WeakReference

class PrecautionsListAdapter : ListAdapter<XMLParsedResult.Article, ViewHolder>(Diff) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemViewPrecautionsBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class ViewHolder(private val binding: ItemViewPrecautionsBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(article: XMLParsedResult.Article) {
        binding.apply {
            this.precaution = article

            if (article.contentList.isEmpty()) {
                binding.contentsTextView.visibility = View.GONE
            } else {
                binding.contentsTextView.visibility = View.VISIBLE
                val stringBuilder = WeakReference<StringBuilder>(StringBuilder())

                stringBuilder.get()?.let { builder ->
                    article.contentList.forEachIndexed { index, content ->
                        builder.append(content)
                        if (index != article.contentList.size - 1) builder.append("\n")
                    }

                    this.contentsTextView.text = builder.toString()
                    builder.clear()
                }
            }

            executePendingBindings()
        }
    }
}

object Diff : DiffUtil.ItemCallback<XMLParsedResult.Article>() {
    override fun areItemsTheSame(oldItem: XMLParsedResult.Article, newItem: XMLParsedResult.Article): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: XMLParsedResult.Article, newItem: XMLParsedResult.Article): Boolean {
        return oldItem == newItem
    }
}