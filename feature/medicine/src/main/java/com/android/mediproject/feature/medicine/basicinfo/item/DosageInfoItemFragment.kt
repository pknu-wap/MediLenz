package com.android.mediproject.feature.medicine.basicinfo.item

import android.os.Bundle
import android.view.View
import com.android.mediproject.core.model.util.XMLParsedResult
import com.android.mediproject.feature.medicine.databinding.FragmentDosageInfoItemBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import repeatOnStarted

@AndroidEntryPoint
class DosageInfoItemFragment : BaseMedicineInfoItemFragment<FragmentDosageInfoItemBinding, XMLParsedResult>(
    FragmentDosageInfoItemBinding::inflate
) {

    companion object : MedicineInfoItemFragmentFactory by BaseMedicineInfoItemFragment.Companion

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.repeatOnStarted {
            collectingData.collectLatest {
                it.let {
                    val stringBuilder = StringBuilder()
                    it.articleList.forEach { article ->
                        article.contentList.forEach { content ->
                            stringBuilder.append(content)
                        }
                    }

                    binding.contentsTextView.text = stringBuilder.toString()
                }
            }
        }

    }

}