package com.android.mediproject.feature.medicine.basicinfo.item

import android.os.Bundle
import android.view.View
import com.android.mediproject.core.model.util.XMLParsedResult
import com.android.mediproject.feature.medicine.databinding.FragmentDosageInfoItemBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import repeatOnStarted
import java.lang.ref.WeakReference

@AndroidEntryPoint
class DosageInfoItemFragment : BaseMedicineInfoItemFragment<FragmentDosageInfoItemBinding, XMLParsedResult>(
    FragmentDosageInfoItemBinding::inflate
) {

    companion object : MedicineInfoItemFragmentFactory by BaseMedicineInfoItemFragment.Companion

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.repeatOnStarted {
            collectingData.collectLatest {
                if (it.articleList.isNotEmpty()) {
                    val stringBuilder = WeakReference<StringBuilder>(StringBuilder())

                    stringBuilder.get()?.let { builder ->
                        it.articleList.forEachIndexed { _, article ->
                            builder.append(article.title)
                            article.contentList.forEachIndexed { index, content ->
                                builder.append(content)
                                if (index != article.contentList.size - 1) builder.append("\n")
                            }
                            builder.append("\n")
                        }

                        binding.contentsTextView.text = builder.toString()
                        builder.clear()
                    }
                }

            }
        }

    }

}