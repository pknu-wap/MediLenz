package com.android.mediproject.feature.penalties.recentpenaltylist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.penalties.databinding.FragmentRecentPenaltyListBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 * 최근 행정 처분 목록 프래그먼트
 *
 * Material3 Chip으로 회사명/품목명 보여주고, ViewModel로 관리
 */
@AndroidEntryPoint
class RecentPenaltyListFragment :
    BaseFragment<FragmentRecentPenaltyListBinding, RecentPenaltyListViewModel>(FragmentRecentPenaltyListBinding::inflate) {

    override val fragmentViewModel: RecentPenaltyListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}