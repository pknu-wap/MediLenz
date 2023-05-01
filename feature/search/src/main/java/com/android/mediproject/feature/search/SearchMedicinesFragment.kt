package com.android.mediproject.feature.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.search.databinding.FragmentSearchMedicinesHostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchMedicinesFragment :
    BaseFragment<FragmentSearchMedicinesHostBinding, SearchMedicinesViewModel>(FragmentSearchMedicinesHostBinding::inflate) {

    override val fragmentViewModel: SearchMedicinesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // contents_fragment_container_view 에 최근 검색 목록과 검색 결과 목록 화면 두 개를 띄운다.
    }
}