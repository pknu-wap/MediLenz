package com.android.mediproject.feature.mypage.mypagemore

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.android.mediproject.feature.mypage.MyPageViewModel
import com.android.mediproject.feature.mypage.R
import com.android.mediproject.feature.mypage.databinding.FragmentMyPageMoreBottomSheetBinding
import com.android.mediproject.feature.mypage.mypagemore.MyPageMoreBottomSheetViewModel.Companion.CHANGE_NICKNAME
import com.android.mediproject.feature.mypage.mypagemore.MyPageMoreBottomSheetViewModel.Companion.CHANGE_PASSWORD
import com.android.mediproject.feature.mypage.mypagemore.MyPageMoreBottomSheetViewModel.Companion.WITHDRAWAL
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.ViewModelLifecycle
import repeatOnStarted

@AndroidEntryPoint
class MyPageMoreBottomSheetFragment(private val context: Context) : BottomSheetDialogFragment() {

    private var _binding: FragmentMyPageMoreBottomSheetBinding? = null
    val binding get() = _binding!!
    private val fragmentViewModel: MyPageMoreBottomSheetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPageMoreBottomSheetBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = fragmentViewModel.apply {
                viewLifecycleOwner.apply {
                    repeatOnStarted { bottomsheetFlag.collect { handleFlag(it) } }
                    repeatOnStarted { eventFlow.collect { handleEvent(it) } }
                }
            }
        }
    }

    private fun handleEvent(event: MyPageMoreBottomSheetViewModel.MyPageMoreBottomSheetEvent) =
        when (event) {
            is MyPageMoreBottomSheetViewModel.MyPageMoreBottomSheetEvent.Confirm -> {}
        }

    private fun handleFlag(flag: Int) = when (flag) {
        CHANGE_NICKNAME -> {}
        CHANGE_PASSWORD -> {}
        WITHDRAWAL -> {}
        else -> Unit
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}