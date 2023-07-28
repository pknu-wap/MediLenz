package com.android.mediproject.feature.medicine.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.android.mediproject.core.common.util.setArguments
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.model.navargs.MedicineBasicInfoArgs
import com.android.mediproject.feature.medicine.databinding.FragmentCommentsHostBinding
import com.android.mediproject.feature.medicine.main.MedicineInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HostCommentsFragment : Fragment() {

    private var _binding: FragmentCommentsHostBinding? = null

    private val binding get() = _binding!!

    private val viewModel: MedicineInfoViewModel by viewModels(
        {
            requireParentFragment()
        },
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCommentsHostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment = binding.commentsFragmentContainerView.getFragment() as NavHostFragment
        val navInflater = navHostFragment.navController.navInflater
        val graph = navInflater.inflate(com.android.mediproject.feature.comments.R.navigation.medicine_comments_nav)

        val args = (viewModel.medicineDetails.value as UiState.Success).data
        graph.findNode(com.android.mediproject.feature.comments.R.id.medicineCommentsFragment)
            ?.setArguments(MedicineBasicInfoArgs(args.itemSequence.toLong(), args.medicineIdInServer))

        navHostFragment.navController.graph = graph
        viewModel.scrollToBottom()
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
