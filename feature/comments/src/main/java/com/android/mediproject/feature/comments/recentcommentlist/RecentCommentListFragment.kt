package com.android.mediproject.feature.comments.recentcommentlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.comments.databinding.FragmentRecentCommentListBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecentCommentListFragment :
    BaseFragment<FragmentRecentCommentListBinding, RecentCommentListViewModel>(FragmentRecentCommentListBinding::inflate) {
    enum class ResultKey {
        RESULT_KEY, WORD
    }

    override val fragmentViewModel: RecentCommentListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
        }
    }
}
