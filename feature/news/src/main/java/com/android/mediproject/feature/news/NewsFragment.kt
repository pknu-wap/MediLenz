package com.android.mediproject.feature.news

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.fragment.app.viewModels
import com.android.mediproject.core.common.util.SystemBarStyler
import com.android.mediproject.core.common.util.navArgs
import com.android.mediproject.core.model.navargs.RecallDisposalArgs
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.news.databinding.FragmentNewsBinding
import com.android.mediproject.feature.news.theme.mediColorScheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewsFragment : BaseFragment<FragmentNewsBinding, NewsViewModel>(FragmentNewsBinding::inflate) {

    override val fragmentViewModel: NewsViewModel by viewModels()

    private val recallDisposalArgs by navArgs<RecallDisposalArgs>()

    @Inject lateinit var systemBarStyler: SystemBarStyler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            systemBarStyler.changeMode(listOf(SystemBarStyler.ChangeView(newsBar, SystemBarStyler.SpacingType.PADDING)))
            composeView.setContent {
                MaterialTheme(colorScheme = mediColorScheme) {
                    Surface(modifier = Modifier.fillMaxSize(), color = mediColorScheme.background) {
                        NewsNavHost(arguments = recallDisposalArgs)
                    }
                }
            }
        }

    }

}
