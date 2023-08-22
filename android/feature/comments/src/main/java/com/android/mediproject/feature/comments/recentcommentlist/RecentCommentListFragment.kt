package com.android.mediproject.feature.comments.recentcommentlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.comments.databinding.FragmentRecentCommentListBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 * 최근 댓글 목록 프래그먼트
 *
 * Material3 Chip으로 의약품 명 보여주고, ViewModel로 관리
 */
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
        initHeader()
    }


    /**
     * 헤더 초기화
     *
     * 확장 버튼 리스너, 더 보기 버튼 리스너
     */
    private fun initHeader() {
        binding.headerView.setOnExpandClickListener {}

        binding.headerView.setOnMoreClickListener {}
    }
}