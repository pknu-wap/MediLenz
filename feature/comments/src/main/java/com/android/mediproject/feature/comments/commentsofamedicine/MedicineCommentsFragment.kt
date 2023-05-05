package com.android.mediproject.feature.comments.commentsofamedicine

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.mediproject.core.ui.base.BaseFragment
import com.android.mediproject.feature.comments.databinding.FragmentMedicineCommentsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MedicineCommentsFragment :
    BaseFragment<FragmentMedicineCommentsBinding, MedicineCommentsViewModel>(FragmentMedicineCommentsBinding::inflate) {

    override val fragmentViewModel: MedicineCommentsViewModel by viewModels()
    

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val commentsAdapter = CommentsAdapter()

        binding.apply {

        }

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }
}