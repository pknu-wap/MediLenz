package com.android.mediproject.feature.search.recentsearchlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.search.databinding.FragmentRecentSearchListBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 * 최근 검색 목록 프래그먼트
 *
 * Material3 Chip이용하고, ViewModel로 관리
 */
@AndroidEntryPoint
class RecentSearchListFragment :
    BaseFragment<FragmentRecentSearchListBinding, RecentSearchListViewModel>(FragmentRecentSearchListBinding::inflate) {

    override val fragmentViewModel: RecentSearchListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}