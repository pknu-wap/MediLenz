package com.android.mediproject.feature.news

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.common.uiutil.SystemBarStyler
import com.android.mediproject.core.common.util.navArgs
import com.android.mediproject.core.model.local.navargs.RecallDisposalArgs
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.news.databinding.FragmentNewsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewsFragment : BaseFragment<FragmentNewsBinding, NewsViewModel>(FragmentNewsBinding::inflate) {

    override val fragmentViewModel: NewsViewModel by viewModels()

    private val recallDisposalArgs by navArgs<RecallDisposalArgs>()

    @Inject lateinit var systemBarStyler: SystemBarStyler

    override fun onAttach(context: Context) {
        super.onAttach(context)
        systemBarStyler.setStyle(SystemBarStyler.StatusBarColor.BLACK, SystemBarStyler.NavigationBarColor.BLACK)
    }

    override fun onDetach() {
        super.onDetach()
        systemBarStyler.defaultStyle()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            systemBarStyler.changeMode(listOf(SystemBarStyler.ChangeView(newsBar, SystemBarStyler.SpacingType.PADDING)))
            composeView.setContent {
                NewsNavHost(arguments = recallDisposalArgs)
            }
        }

    }

}