package com.android.mediproject.feature.news

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.android.mediproject.core.model.local.navargs.RecallDisposalArgs
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.news.databinding.FragmentNewsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment :
    BaseFragment<FragmentNewsBinding, NewsViewModel>(FragmentNewsBinding::inflate) {

    override val fragmentViewModel: NewsViewModel by viewModels()

    private val recallDisposalArgs: RecallDisposalArgs by lazy {
        try {
            navArgs<RecallDisposalArgs>().value
        } catch (e: Exception) {
            RecallDisposalArgs("")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            composeView.setContent {
                NewsNavHost(arguments = recallDisposalArgs)
            }
        }
    }
}