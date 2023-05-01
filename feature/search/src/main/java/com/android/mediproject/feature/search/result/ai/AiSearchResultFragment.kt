package com.android.mediproject.feature.search.result.ai

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.search.databinding.FragmentAiSearchResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AiSearchResultFragment :
    BaseFragment<FragmentAiSearchResultBinding, AiSearchResultViewModel>(FragmentAiSearchResultBinding::inflate) {

    override val fragmentViewModel: AiSearchResultViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}