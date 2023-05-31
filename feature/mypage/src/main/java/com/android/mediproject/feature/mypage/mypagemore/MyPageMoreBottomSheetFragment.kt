package com.android.mediproject.feature.mypage.mypagemore

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.mediproject.feature.mypage.MyPageViewModel
import com.android.mediproject.feature.mypage.R
import com.android.mediproject.feature.mypage.databinding.FragmentMyPageMoreBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.ViewModelLifecycle

@AndroidEntryPoint
class MyPageMoreBottomSheetFragment(private val context : Context) : BottomSheetDialogFragment() {

    private var _binding : FragmentMyPageMoreBottomSheetBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPageMoreBottomSheetBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}