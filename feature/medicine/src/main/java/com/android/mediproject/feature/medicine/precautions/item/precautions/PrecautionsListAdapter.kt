package com.android.mediproject.feature.medicine.precautions.item.precautions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.mediproject.core.model.util.XMLParsedResult
import com.android.mediproject.feature.medicine.databinding.ItemViewPrecautionsBinding

class PrecautionsListAdapter : ListAdapter<XMLParsedResult.Article, ViewHolder>(Diff) {
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

            val stringBuilder = StringBuilder()
            article.contentList.forEachIndexed { index, content ->
                stringBuilder.append(content)
                if (index != article.contentList.size - 1) stringBuilder.append("\n")
            }

            this.contentsTextView.text = stringBuilder.toString()
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