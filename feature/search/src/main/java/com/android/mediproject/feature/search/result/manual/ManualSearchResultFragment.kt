package com.android.mediproject.feature.search.result.manual

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.search.databinding.FragmentManualSearchResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManualSearchResultFragment :
    BaseFragment<FragmentManualSearchResultBinding, ManualSearchResultViewModel>(FragmentManualSearchResultBinding::inflate) {

    override val fragmentViewModel: ManualSearchResultViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}